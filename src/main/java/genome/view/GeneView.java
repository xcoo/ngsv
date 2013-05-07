/*
 *   ngsv
 *   https://github.com/xcoo/ngsv
 *   Copyright (C) 2012, Xcoo, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package genome.view;

import genome.config.Config;
import genome.config.Default;
import genome.data.Bed;
import genome.data.BedFragment;
import genome.data.Chromosome;
import genome.data.CytoBand;
import genome.data.Sam;
import genome.data.SamHistogram;
import genome.data.ViewScale;
import genome.db.SQLLoader;
import genome.net.DataSelectionListener;
import genome.net.Selection;
import genome.net.WebSocket;
import genome.view.SamSelectionDialogBox.SamSelectionDialongBoxListener;
import genome.view.element.BedFragmentElement;
import genome.view.element.CytobandElement;
import genome.view.element.ExonElement;
import genome.view.element.GeneElement;
import genome.view.element.HistogramBinElement;
import genome.view.element.RulerElement;
import genome.view.group.BedFragmentGroup;
import genome.view.group.CytobandGroup;
import genome.view.group.GeneGroup;
import genome.view.group.HistogramBinGroup;
import genome.view.group.RulerGroup;
import genome.view.thread.BedUpdater;
import genome.view.thread.GeneUpdater;
import genome.view.thread.HistogramUpdater;
import genome.view.ui.Indicator;
import genome.view.ui.ScaleController;
import genome.view.ui.ScaleControllerCallback;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Applet;
import casmi.AppletRunner;
import casmi.KeyEvent;
import casmi.MouseButton;
import casmi.MouseEvent;
import casmi.Trackball;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.GrayColor;
import casmi.graphics.element.Line;
import casmi.graphics.element.Text;
import casmi.graphics.font.Font;
import casmi.graphics.font.FontStyle;
import casmi.graphics.object.GraphicsObject;
import casmi.parser.JSON;

/**
 * Class for Gene View
 *
 * @author K. Nishimura
 *
 */
public class GeneView extends Applet implements SamSelectionDialongBoxListener, DataSelectionListener {

    private static final long serialVersionUID = 1L;

    static Logger logger = LoggerFactory.getLogger(GeneView.class);
    static String[] args;

    private static final String CONFIG_INI_PATH = "./config/config.ini";
    private static final String DEFAULT_INI_PATH = "./config/default.ini";

    private static final double MIN_SCALE = 0.000004;
    private static final double MAX_SCALE = 2.0;
    private static final double WHEEL_SCALE_FACTOR = 0.007;

    private static final double SCROLL_SPEED_EPS = 0.01;
    private static final double MOUSE_SCROLL_SPEED_FACTOR = 15.0;
    private static final double SCROLL_SPEED_DAMPING_FACTOR = 0.8;
    private static final double SCROLL_POWER_FACTOR = 0.8;

    private static final double INITIAL_SCALE = 1.0;
    private double scale = INITIAL_SCALE;

    private double scroll      = 0.0;
    private double scrollSpeed = 0.0;

    private long leftValue = 0, rightValue = 0;

    private long loadBinSize = 0;
    private long dispBinSize = 0;
    private long start = 0, end = 0;

    // Graphics objects
    private GraphicsObject baseObject = new GraphicsObject();

    private GeneGroup               geneGroup;
    private List<HistogramBinGroup> histogramBinGroupList = new ArrayList<HistogramBinGroup>();
    private CytobandGroup           cytobandGroup;
    private List<BedFragmentGroup>  bedFragmentGroupList  = new ArrayList<BedFragmentGroup>();

    private RulerGroup rulerGroup;

    private Text annotationText;

    private ScaleController scaleController;
    private Indicator indicator;

    // ref genes
    private ViewScale viewScale;

    // sam, histogram, bed, and chromosome data
    private Sam[]     samFiles;
    private List<Sam> selectedSamList = new ArrayList<Sam>();

    private Bed[]     bedFiles;
    private List<Bed> selectedBedList = new ArrayList<Bed>();

    private Chromosome[] chromosomes;
    private Chromosome   selectedChromosome;

    private CytoBand[] cytobands;

    private SQLLoader sqlLoader;

    // dialog box
    private SamSelectionDialogBox dbox;

    private boolean isFinishedInitialSelection;

    // trackball
    private Trackball trackball;
    private int prvMouseX = 0, prvMouseY = 0;

    private HistogramUpdater histogramUpdater;
    private BedUpdater       bedUpdater;
    private GeneUpdater      geneUpdater;

    private WebSocket webSocket;

    @Override
    public void setup() {
        // Load configuration
        // -----------------------------------------
        Config conf = Config.getInstance();
        if (args.length > 0)
            conf.load(args[0]);
        else
            conf.load(CONFIG_INI_PATH);

        Default dflt = Default.getInstance();
        dflt.load(DEFAULT_INI_PATH);

        // Initialize global settings
        // -----------------------------------------
        setFPS(dflt.getFPS());
        setSize(dflt.getWindowWidth(), dflt.getWindowHeight());
        setBackGroundColor(ColorSet.BLACK);


        // load data
        // -----------------------------------------

        // load mysql database
        try {
            sqlLoader = new SQLLoader();
        } catch (Exception e) {
            logger.error("Failed to connect to database.");
            showAlert("Error", "Could not connect to database");
            System.exit(-1);
        }

        // load sam files
        samFiles = sqlLoader.loadSamFiles();

        // load bed files
        bedFiles = sqlLoader.loadBedFiles();

        // load chromosome
        chromosomes = sqlLoader.loadChromosome();

        logger.info("Read " + samFiles.length + " sam files.");
        logger.info("Read " + bedFiles.length + " bed files.");

        // load cytoband
        cytobands = sqlLoader.loadCytoBand();
        Map<String, Long> chrLengthMap = calcCytoBandLength(cytobands, chromosomes);

        // WebSocket
        try {
            webSocket = new WebSocket(this);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        webSocket.connect();

        // set dialog box for sam
        dbox = new SamSelectionDialogBox(samFiles, bedFiles, chrLengthMap, sqlLoader, this);
    }

    public void initViews(String chrName, long start, long end) {
        // initialize view scale (region)
        viewScale = new ViewScale(chrName, start, end);

        // load data for view related to viewScale.getChr()
        reloadViewData(viewScale.getChr(), viewScale.getStart(), viewScale.getEnd());

        trackball = new Trackball(getWidth(), getHeight());

        Font f = new Font("Sans-Serif", FontStyle.PLAIN, 12.0);
        annotationText = new Text("", f);
        annotationText.setStrokeColor(new GrayColor(0.75));

        scaleController = new ScaleController(MIN_SCALE, MAX_SCALE, scale, getMouse());
        scaleController.setPosition(getWidth() - 30, getHeight() / 2);
        scaleController.addCallback(new ScaleControllerCallback() {

            @Override
            public void run(double value) {
                scale = value;
            }
        });

        indicator = new Indicator(getWidth() - 40, getHeight() - 40);

        histogramUpdater = new HistogramUpdater(sqlLoader, annotationText, getMouse());
        bedUpdater       = new BedUpdater(sqlLoader, annotationText, getMouse());
        geneUpdater      = new GeneUpdater(sqlLoader, annotationText, getMouse());

        rebuildViewData(viewScale.getChr(), viewScale.getStart(), viewScale.getEnd());

        addObject(baseObject);
        addObject(annotationText);
        addObject(scaleController);
        addObject(indicator);
    }

    public Map<String, Long> calcCytoBandLength(CytoBand[] cytoBands, Chromosome[] chromosomes) {
        Map<String, Long> map = new HashMap<String, Long>();

        for (CytoBand b : cytoBands) {
            String chrName = findChromosome(b.getChrId(), chromosomes).getChromosome();
            b.setChrName(chrName);
            Long curr = map.get(b.getChrId());

            if (curr != null) {
                if (curr < b.getEnd()) {
                    map.put(chrName, b.getEnd());
                }
            } else {
                map.put(chrName, b.getEnd());
            }
        }

        return map;
    }

    private Chromosome findChromosome(long chrId, Chromosome[] chromosomes) {
        for (Chromosome c : chromosomes) {
            if (chrId == c.getChrId()) {
                return c;
            }
        }
        return null;
    }

    private Chromosome findChromosome(String chr, Chromosome[] chromosomes) {
        for(Chromosome c : chromosomes) {
            if (c.getChromosome().equalsIgnoreCase(chr) ||
                c.getChromosome().equalsIgnoreCase("chr" + chr)) {
                return c;
            }
        }
        return null;
    }

    private void reloadViewData(String chr, long start, long end) {
        selectedSamList.clear();
        selectedBedList.clear();

        selectedChromosome = findChromosome(chr, chromosomes);
        if (selectedChromosome == null) {
            return;
        }

        // load histogram
        for (Sam sam : samFiles) {
            if (sam.isSelected()) {
                // Load histograms
                SamHistogram[] shs = sqlLoader.loadSamHistogram(sam.getSamId());
                sam.setSamHistograms(shs);

                logger.info("Loaded " + shs.length + " SamHistograms.");

                selectedSamList.add(sam);
            }
        }

        for (Bed bed : bedFiles) {
            if (bed.isSelected()) {
                BedFragment[] bedFragments = sqlLoader.loadBedFragment(bed.getBedId(), selectedChromosome);
                bed.setBedFragments(bedFragments);
                selectedBedList.add(bed);
            }
        }
    }

    private void rebuildViewData(String chr, long start, long end) {

        baseObject.clear();

        histogramBinGroupList.clear();
        bedFragmentGroupList.clear();

        Chromosome c = findChromosome(chr, chromosomes);
        if (c == null) {
            return;
        }

        scroll = - (start + end) / 2.0;
        scale = INITIAL_SCALE;

        setupRuler(start, end);

        for (Sam sam : selectedSamList) {
            setupHistogramBin(sam);
        }

        int i = 0;
        for (HistogramBinGroup g : histogramBinGroupList) {
            g.setY(g.getY() + (HistogramBinElement.MAX_HEIGHT + 10.0) * i++);
        }

        for (Bed bed : selectedBedList) {
            setupBedFragment(bed);
        }

        setupCytoband(c.getChromosome());
        setupGene();
    }

    private void setupRuler(long start, long end) {
        rulerGroup = new RulerGroup(start, end, scale);
        rulerGroup.getMainLine().set(viewScale.getStart(), 0, viewScale.getEnd(), 0);
        baseObject.add(rulerGroup);
    }

    private void setupHistogramBin(Sam sam) {

        HistogramBinGroup g = new HistogramBinGroup(sam, scale, getMouse());
        g.setY(Default.getInstance().getHistogramPosY());

        histogramBinGroupList.add(g);
        baseObject.add(g);

    }

    private void setupBedFragment(Bed bed) {

        BedFragmentGroup g = new BedFragmentGroup(bed, scale, getMouse());
        g.setY(Default.getInstance().getBedPosY());

        bedFragmentGroupList.add(g);
        baseObject.add(g);

    }

    private void setupCytoband(String chr) {
        cytobandGroup = new CytobandGroup(cytobands, chr,
            Default.getInstance().getCytobandHeight(), scale, getMouse());
        cytobandGroup.setY(Default.getInstance().getCytobandPosY());

        for (CytobandElement e : cytobandGroup.getCytobandElementList()) {
            e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, getMouse()));
        }

        baseObject.add(cytobandGroup);
    }

    private void setupGene() {
        geneGroup = new GeneGroup(scale, getMouse());
        geneGroup.setY(Default.getInstance().getRulerPosY());
        baseObject.add(geneGroup);
    }

    @Override
    public void update() {
        if (!isFinishedInitialSelection)
            return;

        // Update horizontal scroll.
        updateScroll();

        leftValue  = (long)(-scroll - 550.0 / scale);
        rightValue = (long)(-scroll + 550.0 / scale);

        // Update data.
        updateData();

        // Update graphics objects.
        updateElements();
    }

    private void updateScroll() {
        scroll += scrollSpeed / getFPS() / Math.pow(scale, SCROLL_POWER_FACTOR);
        scrollSpeed *= SCROLL_SPEED_DAMPING_FACTOR;
        if (Math.abs(scrollSpeed) < SCROLL_SPEED_EPS) {
            scrollSpeed = 0.0;
        }
    }

    private void updateData() {
        // Select appropriate binSize.
        long newDispBinSize = 100;
        long newLoadBinSize = 100;

        double base = 0.2;
        if (scale < base * 0.0001) {
            newDispBinSize = 10000000;
            newLoadBinSize = 1000000;
        } else if (scale < base * 0.0002) {
            newDispBinSize = 1000000;
            newLoadBinSize = 1000000;
        } else if (scale < base * 0.001) {
            newDispBinSize = 100000;
            newLoadBinSize = 10000;
        } else if (scale < base * 0.1 * 0.1 * 0.125) {
            newDispBinSize = 80000;
            newLoadBinSize = 10000;
        } else if (scale < base * 0.1 * 0.1 * 0.17) {
            newDispBinSize = 60000;
            newLoadBinSize = 10000;
        } else if (scale < base * 0.1 * 0.1 * 0.25) {
            newDispBinSize = 40000;
            newLoadBinSize = 10000;
        } else if (scale < base * 0.1 * 0.1 * 0.5) {
            newDispBinSize = 20000;
            newLoadBinSize = 10000;
        } else if (scale < base * 0.1 * 0.1) {
            newDispBinSize = 10000;
            newLoadBinSize = 10000;
        } else if (scale < base * 0.1 * 0.125) {
            newDispBinSize = 8000;
            newLoadBinSize = 100;
        } else if (scale < base * 0.1 * 0.17) {
            newDispBinSize = 6000;
            newLoadBinSize = 100;
        } else if (scale < base * 0.1 * 0.25) {
            newDispBinSize = 4000;
            newLoadBinSize = 100;
        } else if (scale < base * 0.1 * 0.5) {
            newDispBinSize = 2000;
            newLoadBinSize = 100;
        } else if (scale < base * 0.1) {
            newDispBinSize = 1000;
            newLoadBinSize = 100;
        } else if (scale < base * 0.125) {
            newDispBinSize = 800;
            newLoadBinSize = 100;
        } else if (scale < base * 0.17) {
            newDispBinSize = 600;
            newLoadBinSize = 100;
        } else if (scale < base * 0.25) {
            newDispBinSize = 400;
            newLoadBinSize = 100;
        } else if (scale < base * 0.5) {
            newDispBinSize = 200;
            newLoadBinSize = 100;
        }

        boolean showRefGene = true;
        if (scale < 0.0015) {
            showRefGene = false;
            geneGroup.setVisible(false);
        } else {
            geneGroup.setVisible(true);
        }

        // Calculate appropriate range.
        long newStart = leftValue  - (long)(700.0 / scale);
        long newEnd   = rightValue + (long)(700.0 / scale);

        if (leftValue < start + (long)(500.0 / scale) || end - (long)(500.0 / scale) < rightValue) {
            // Update histogram data.
            updateHistogram(newDispBinSize, newLoadBinSize, newStart, newEnd, true);

            // Update bed data
            updateBed(newStart, newEnd);

            // Update refGene.
            if (showRefGene) {
                updateRefGene(newStart, newEnd);
            }

            loadBinSize = newLoadBinSize;
            dispBinSize = newDispBinSize;
            start = newStart;
            end   = newEnd;
        } else if (newLoadBinSize != loadBinSize) {
            updateHistogram(newDispBinSize, newLoadBinSize, newStart, newEnd, true);

            loadBinSize = newLoadBinSize;
            dispBinSize = newDispBinSize;
            start = newStart;
            end   = newEnd;
        } else if (newDispBinSize != dispBinSize) {
            updateHistogram(newDispBinSize, newLoadBinSize, newStart, newEnd, false);

            loadBinSize = newLoadBinSize;
            dispBinSize = newDispBinSize;
            start = newStart;
            end   = newEnd;
        }
    }

    private void updateHistogram(long newBinSize, long newLoadBinSize, long newStart, long newEnd, boolean loadDB) {

        for (Sam sam : selectedSamList) {
            SamHistogram  sh = getLoadingSamHistogram(sam.getSamHistograms(), newLoadBinSize);

            if (sh != null) {
                for (HistogramBinGroup hbg : histogramBinGroupList) {
                    if (hbg.getSam().getSamId() == sam.getSamId()) {

                        // Update HistogramBin data and elements in background.
                        histogramUpdater.start(sh, selectedChromosome, newBinSize, newStart, newEnd, loadDB, hbg, scale);
                        break;
                    }
                }
            }
        }
    }

    private SamHistogram getLoadingSamHistogram(SamHistogram[] samHistograms, long newLoadBinSize) {
        for (SamHistogram sh : samHistograms) {
            if (sh.getBinSize() == newLoadBinSize) {
                return sh;
            }
        }

        return null;
    }

    private void updateBed(long newStart, long newEnd) {
        for (Bed bed : selectedBedList) {
            for (BedFragmentGroup bfg : bedFragmentGroupList) {
                if (bed.getBedId() == bfg.getBed().getBedId()) {
                    bedUpdater.start(bed, selectedChromosome, newStart, newEnd, bfg, scale);
                    break;
                }
            }
        }
    }

    private void updateRefGene(long newStart, long newEnd) {
        geneUpdater.start(selectedChromosome, newStart, newEnd, geneGroup, scale);
    }

    private void updateElements() {

        // Horizontal offset of scroll
        double offset = scroll * scale + getWidth() / 2.0;

        // Update ruler
        updateRuler(offset);

        // Update other groups.
        // ---------------------------------------------------------------------
        for (HistogramBinGroup g : histogramBinGroupList) {
            for (HistogramBinElement e : g.getHistogramBinElementList()) {
                e.setScale(scale);
                e.setPosition(offset + e.getBaseX(), e.getBaseY(), 0);
            }
        }

        for (BedFragmentGroup g : bedFragmentGroupList) {
            for (BedFragmentElement e : g.getBedFragmentElementList()) {
                e.setScale(scale);
                e.setX(offset + e.getBaseX());
            }
        }

        for (CytobandElement e : cytobandGroup.getCytobandElementList()) {
            e.setScale(scale);
            e.setX(offset + e.getBaseX());
        }

        for (ExonElement e : geneGroup.getExonElementList()) {
            e.setScale(scale);
            e.setPosition(offset + e.getBaseX(), e.getBaseY(), 0);
        }

        for (GeneElement e : geneGroup.getGeneElementList()) {
            e.setScale(scale);
            e.setPosition(offset + e.getBaseX(), e.getBaseY(), 0);
        }
    }

    private void updateRuler(double offset) {
        long start = leftValue, end = rightValue;
        if (start < 0) start = 0;
        if (end < 0) end = 0;
        rulerGroup.setStart(start);
        rulerGroup.setEnd(end);

        Line mainLine = rulerGroup.getMainLine();
        mainLine.setScale(scale);
        mainLine.setPosition(offset + (viewScale.getStart() + viewScale.getEnd()) / 2.0 * scale,
            Default.getInstance().getRulerPosY(),
            0);

        long step = 100;
        if (scale < 0.000005) {
            step = 100000000;
        } else if (scale < 0.00005) {
            step = 10000000;
        } else if (scale < 0.0005) {
            step = 1000000;
        } else if (scale < 0.005) {
            step = 100000;
        } else if (scale < 0.05) {
            step = 10000;
        } else if (scale < 0.5) {
            step = 1000;
        }

        int idx = 0;
        for (long x = start - start % step; x < end; x += step) {
            if (rulerGroup.getRulerElementList().size() <= idx) break;

            RulerElement e = rulerGroup.getRulerElementList().get(idx);

            e.setInitialBaseX(x);
            e.getText().setText(Long.toString(x));

            e.setScale(scale);
            e.getLine().setPosition(offset + e.getBaseX(), Default.getInstance().getRulerPosY(), 0);
            e.getText().setPosition(offset + e.getBaseX() + 3, Default.getInstance().getRulerPosY() + 3, 0);

            e.getLine().setVisible(true);
            e.getText().setVisible(true);

            idx++;
        }

        for (int i = idx; i < rulerGroup.getRulerElementList().size(); i++) {
            RulerElement e = rulerGroup.getRulerElementList().get(i);
            e.getLine().setVisible(false);
            e.getText().setVisible(false);
        }
    }

    @Override
    public void keyEvent(KeyEvent e) {
        if (e == KeyEvent.PRESSED) {
            int keycode = getKeyCode();
            char key = getKey();

            // ESC to quit the application.
            if (keycode == 27) {
                sqlLoader.close();
                System.exit(0);
            }

            if (key == 'c'){
                dbox.setVisible(true);
            } else if (key == 'r') {
                trackball.reset();
                trackball.rotate(baseObject);
            }
        }
    }

    @Override
    public void mouseEvent(MouseEvent e, MouseButton b) {
        switch (e) {
        case PRESSED:
            prvMouseX = getMouseX();
            prvMouseY = getMouseY();
            break;

        case DRAGGED:
            switch (b) {
            case LEFT:
            {
                if (getKeyCode() == java.awt.event.KeyEvent.VK_SPACE) {
                    int mouseX = getMouseX();
                    int mouseY = getMouseY();

                    trackball.update(mouseX, mouseY, prvMouseX, prvMouseY);

                    trackball.rotate(baseObject, getWidth() / 2.0, getHeight() / 2.0, 0.0);

                    prvMouseX = mouseX;
                    prvMouseY = mouseY;
                } else {
                    double diffX = getMouseX() - getPreMouseX();
                    scrollSpeed += diffX * MOUSE_SCROLL_SPEED_FACTOR;
                }

                break;
            }

            case RIGHT:
            {
                int mouseX = getMouseX();
                int mouseY = getMouseY();

                trackball.update(mouseX, mouseY, prvMouseX, prvMouseY);

                trackball.rotate(baseObject, getWidth() / 2.0, getHeight() / 2.0, 0.0);

                prvMouseX = mouseX;
                prvMouseY = mouseY;
                break;
            }

            default:
                break;
            }

            break;

        case WHEEL_ROTATED:
        {
            double diff = getMouseWheelRotation();
            double scaleDiff = Math.abs(diff) * scale * WHEEL_SCALE_FACTOR;
            scale += 0 < diff ? scaleDiff : -scaleDiff;
            if (scale < MIN_SCALE) scale = MIN_SCALE;
            else if (scale > MAX_SCALE) scale = MAX_SCALE;
            scaleController.setValue(scale);
            break;
        }

        default:
            break;
        }
    }

    @Override
    public void finishedSamSelection() {
        logger.debug("Finished selecting sam data.");

        dbox.setVisible(false);

        if (!isFinishedInitialSelection) {
            initViews(dbox.getChrName(), dbox.getStart(), dbox.getEnd());
            webSocket.connect();
            isFinishedInitialSelection = true;
        } else {
            // initialize view scale (region)
            viewScale.setViewScale(dbox.getChrName(), dbox.getStart(), dbox.getEnd());

            // load data for view related to viewScale.getChr()
            reloadViewData(viewScale.getChr(), viewScale.getStart(), viewScale.getEnd());

            synchronized (this.getListener()) {
                rebuildViewData(viewScale.getChr(), viewScale.getStart(), viewScale.getEnd());
            }
        }
    }

    @Override
    public void finishedDataSelection(String jsonText) {
        Selection s = new JSON().decode(jsonText, Selection.class);

        for (Sam sam : samFiles) {
            sam.setSelected(s.hasBam(sam.getSamId()));
        }

        for (Bed bed : bedFiles) {
            bed.setSelected(s.hasBed(bed.getBedId()));
        }

        if (!isFinishedInitialSelection) {
            initViews(s.chromosome.name, s.chromosome.start, s.chromosome.end);
            isFinishedInitialSelection = true;
        } else {
            // initialize view scale (region)
            viewScale.setViewScale(s.chromosome.name, s.chromosome.start, s.chromosome.end);

            // load data for view related to viewScale.getChr()
            reloadViewData(s.chromosome.name, s.chromosome.start, s.chromosome.end);

            synchronized (this.getListener()) {
                rebuildViewData(s.chromosome.name, s.chromosome.start, s.chromosome.end);
            }
        }
    }

    @Override
    public void exit() {
        webSocket.close();
    }

    public static void main(String[] args) {
        logger.info("Start NGSV");

        GeneView.args = args;
        AppletRunner.run("genome.view.GeneView", "NGSV: Next-Generation DNA Sequencing Viewer");
    }
}
