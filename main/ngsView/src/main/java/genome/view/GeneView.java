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
import genome.data.Bed;
import genome.data.BedFragment;
import genome.data.Chromosome;
import genome.data.CytoBand;
import genome.data.Sam;
import genome.data.SamHistogram;
import genome.data.ViewScale;
import genome.db.SQLLoader;
import genome.view.SamSelectionDialogBox.SamSelectionDialongBoxListener;
import genome.view.element.BedFragmentElement;
import genome.view.element.CytobandElement;
import genome.view.element.ExonElement;
import genome.view.element.GeneElement;
import genome.view.element.HistogramBinElement;
import genome.view.element.RulerElement;
import genome.view.element.ShortReadElement;
import genome.view.group.BedFragmentGroup;
import genome.view.group.CytobandGroup;
import genome.view.group.GeneGroup;
import genome.view.group.HistogramBinGroup;
import genome.view.group.Indicator;
import genome.view.group.RulerGroup;
import genome.view.group.ShortReadGroup;
import genome.view.thread.BedUpdater;
import genome.view.thread.GeneUpdater;
import genome.view.thread.HistogramUpdater;
import genome.view.thread.ShortReadUpdater;

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

/**
 * Class for Gene View
 * 
 * @author K. Nishimura
 * 
 */
public class GeneView extends Applet implements SamSelectionDialongBoxListener {

    private static final long serialVersionUID = 1L;

    static Logger logger = LoggerFactory.getLogger(GeneView.class);
    static String[] args;
    
    private static final String DEFAULT_CONFIG_PATH = "./config/config.properties";
    
    // position of elements
    // origin is left-bottom corner
    private static final double CYTOBAND_POS_Y      = 100.0;
    private static final double RULER_POS_Y         = 200.0;
    private static final double BED_FRAGMENT_POS_Y  = 700.0;
    private static final double SHORTREAD_POS_Y     = 400.0;
    private static final double HISTOGRAM_BIN_POS_Y = 500.0;

    private static final double TEXT_OFFSET_POS_X =  15.0;
    private static final double TEXT_OFFSET_POS_Y = -30.0;
    
    private static final double MIN_SCALE = 0.000004;
    private static final double WHEEL_SCALE_FACTOR = 0.007;

    // parameter for cytoband
    private static final double CHROMOSOME_HEIGHT = 20;

    private static final double SCROLL_SPEED_EPS = 0.01;
    private static final double MOUSE_SCROLL_SPEED_FACTOR = 15.0;
    private static final double SCROLL_SPEED_DAMPING_FACTOR = 0.8;
    private static final double SCROLL_POWER_FACTOR = 0.8;

    private static final double FPS = 20.0;

    private static final double INITIAL_SCALE = 1.0;
    private double scale = INITIAL_SCALE;

    private double scroll      = 0.0;
    private double scrollSpeed = 0.0;
    
    private long leftValue = 0, rightValue = 0;
    
    private long binSize = 0;
    private long start = 0, end = 0;

    // Graphics objects
    private GraphicsObject baseObject = new GraphicsObject();

    private GeneGroup               geneGroup;
    private List<ShortReadGroup>    shortReadGroupList    = new ArrayList<ShortReadGroup>();
    private List<HistogramBinGroup> histogramBinGroupList = new ArrayList<HistogramBinGroup>();
    private CytobandGroup           cytobandGroup;
    private List<BedFragmentGroup>  bedFragmentGroupList  = new ArrayList<BedFragmentGroup>();

    private RulerGroup rulerGroup;

    private Text annotationText;
    
    private Indicator indicator;
 
    private static final Font EXPLANATION_F;
    static {
        EXPLANATION_F = new Font("Sans-Serif", FontStyle.PLAIN, 14.0);
    }
    
    // ref genes
    private ViewScale viewScale;

    // sam, shortRead, histogram, bed, and chromosome data
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
    
    private ShortReadUpdater shortReadUpdater;
    private HistogramUpdater histogramUpdater;
    private BedUpdater       bedUpdater;
    private GeneUpdater      geneUpdater;

    @Override
    public void setup() {
        
        // initialize global settings
        setFPS(FPS);
        setSize(1024, 768);
        setBackGroundColor(ColorSet.BLACK);
        
        // Load configuration
        if (args.length > 0)
            Config.getInstance().load(args[0]);
        else
            Config.getInstance().load(DEFAULT_CONFIG_PATH);
        
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
        
        // set dialog box for sam
        dbox = new SamSelectionDialogBox(samFiles, bedFiles, chrLengthMap, sqlLoader, this);
    }
    
    public void initViews() {        
        // initialize view scale (region)
        viewScale = new ViewScale(dbox.getChrName(), dbox.getStart(), dbox.getEnd());

        // load data for view related to viewScale.getChr()
        reloadViewData(viewScale.getChr(), viewScale.getStart(), viewScale.getEnd());
        
        trackball = new Trackball(getWidth(), getHeight());
        
        Font f = new Font("Sans-Serif", FontStyle.PLAIN, 12.0);
        annotationText = new Text("", f);
        annotationText.setStrokeColor(new GrayColor(0.75));

        indicator = new Indicator(getWidth() - 40, getHeight() - 40);
        
        shortReadUpdater = new ShortReadUpdater(sqlLoader, annotationText, getMouse());
        histogramUpdater = new HistogramUpdater(sqlLoader, annotationText, getMouse());
        bedUpdater       = new BedUpdater(sqlLoader, annotationText, getMouse());
        geneUpdater      = new GeneUpdater(sqlLoader, annotationText, getMouse());
        
        rebuildViewData(viewScale.getChr(), viewScale.getStart(), viewScale.getEnd());

        addObject(baseObject);
        setupExplanationText();
        addObject(annotationText);
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

        // load short reads and histogram
        for (Sam sam : samFiles) {
            if (sam.isSelected()) {
                // load short read for the selected sam files
                sam.setShortReads(sqlLoader.loadShortRead(sam.getSamId(), selectedChromosome, start, end));

                logger.info("load " + sam.getFileName() + " " + sam.getShortReads().length + " reads ");

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
        
        shortReadGroupList.clear();
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
            setupShortRead(sam); 
        }
        
        int i = 0;
        for (ShortReadGroup g : shortReadGroupList) {
            g.setY(g.getY() + 30.0 * i++);
        }

        for (Sam sam : selectedSamList) {
            setupHistogramBin(sam); 
        }
        
        i = 0;
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
        baseObject.add(rulerGroup);
    }

    private void setupShortRead(Sam sam) {

        ShortReadGroup g = new ShortReadGroup(sam, scale);
        g.setY(SHORTREAD_POS_Y);
        
        for (ShortReadElement e : g.getShortReadElementList()) {
            e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, getMouse()));
        }
        
        shortReadGroupList.add(g);
        baseObject.add(g);   
    }

    private void setupHistogramBin(Sam sam) {

        HistogramBinGroup g = new HistogramBinGroup(sam, scale);
        g.setY(HISTOGRAM_BIN_POS_Y);
        
        histogramBinGroupList.add(g);
        baseObject.add(g);
        
    }

    private void setupBedFragment(Bed bed) {
        
        BedFragmentGroup g = new BedFragmentGroup(bed, scale);
        g.setY(BED_FRAGMENT_POS_Y);
        
        bedFragmentGroupList.add(g);
        baseObject.add(g);
        
    }

    private void setupCytoband(String chr) {
        
        cytobandGroup = new CytobandGroup(cytobands, chr, CHROMOSOME_HEIGHT, scale);
    
        for (CytobandElement e : cytobandGroup.getCytobandElementList()) {
            e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, getMouse()));            
        }
        
        baseObject.add(cytobandGroup);        
    }
    
    private void setupGene() {
        
        geneGroup = new GeneGroup(scale);
        
        baseObject.add(geneGroup);
        
    }

    private class ExplanationTextData {
        
        private double x;
        private double y;
        private String name;
        
        public ExplanationTextData(double x, double y, String s) {
            this.x = x;
            this.y = y;
            this.name = s;
        }
        
        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public String getName() {
            return name;
        }
    }
    
    private void setupExplanationText() {
        ExplanationTextData[] explanationTextData = {
            new ExplanationTextData(TEXT_OFFSET_POS_X, CYTOBAND_POS_Y      + TEXT_OFFSET_POS_Y, "chromosome"),
            new ExplanationTextData(TEXT_OFFSET_POS_X, BED_FRAGMENT_POS_Y  + TEXT_OFFSET_POS_Y, "bed"),
            new ExplanationTextData(TEXT_OFFSET_POS_X, SHORTREAD_POS_Y     + TEXT_OFFSET_POS_Y, "short read"),
            new ExplanationTextData(TEXT_OFFSET_POS_X, RULER_POS_Y         + TEXT_OFFSET_POS_Y, "known gene"),
            new ExplanationTextData(TEXT_OFFSET_POS_X, HISTOGRAM_BIN_POS_Y + TEXT_OFFSET_POS_Y, "histogram")                
        };
        
        for (ExplanationTextData e: explanationTextData) {
            Text t = new Text(e.getName(), EXPLANATION_F, e.getX(), e.getY());
            t.setStrokeColor(ColorSet.LIGHT_GRAY);
            addObject(t);
        }
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
        scroll += scrollSpeed / FPS / Math.pow(scale, SCROLL_POWER_FACTOR);
        scrollSpeed *= SCROLL_SPEED_DAMPING_FACTOR;
        if (Math.abs(scrollSpeed) < SCROLL_SPEED_EPS) {
            scrollSpeed = 0.0;
        }
    }
    
    private void updateData() {
        // Select appropriate binSize.
        long newBinSize = 1000;
        
        if (scale < 0.0000005) {
            newBinSize = 100000000;
        } else if (scale < 0.000005) {
            newBinSize = 10000000;
        } else if (scale < 0.00005) {
            newBinSize = 1000000;
        } else if (scale < 0.0005) {
            newBinSize = 100000;
        } else if (scale < 0.005) {
            newBinSize = 10000;
        }
        
        // Calculate appropriate range.
        long newStart = leftValue  - (long)(700.0 / scale);
        long newEnd   = rightValue + (long)(700.0 / scale);
        
        if (leftValue < start + (long)(500.0 / scale) || end - (long)(500.0 / scale) < rightValue) {
            // Update shortRead data.
            updateShortRead(newStart, newEnd);

            // Update histogram data.
            updateHistogram(newBinSize, newStart, newEnd);

            // Update bed data
            updateBed(newStart, newEnd);
            
            // Update refGene.
            updateRefGene(newStart, newEnd);
            
            binSize = newBinSize;
            start   = newStart;
            end     = newEnd;
        } else if (newBinSize != binSize) {
            updateHistogram(newBinSize, newStart, newEnd);
            
            binSize = newBinSize;
            start   = newStart;
            end     = newEnd;
        }
    }
    
    private void updateShortRead(long newStart, long newEnd) {
        for (Sam sam : selectedSamList) {
            for (ShortReadGroup srg : shortReadGroupList) {
                if (srg.getSam().getSamId() == sam.getSamId()) {
                    shortReadUpdater.start(sam, selectedChromosome, newStart, newEnd, srg, scale);
                    break;
                }
            }
        }
    }
    
    private void updateHistogram(long newBinSize, long newStart, long newEnd) {

        for (Sam sam : selectedSamList) {
            long nearestBinSize = Long.MAX_VALUE;
            SamHistogram nearestSH = null;
            for (SamHistogram sh : sam.getSamHistograms()) {
                if (Math.abs(sh.getBinSize() - newBinSize) < nearestBinSize) {
                    nearestBinSize = sh.getBinSize();
                    nearestSH = sh;
                }
            }
            if (nearestSH != null) {
                for (HistogramBinGroup hbg : histogramBinGroupList) {
                    if (hbg.getSam().getSamId() == sam.getSamId()) {

                        // Update HistogramBin data and elements in background.
                        histogramUpdater.start(nearestSH, selectedChromosome, newStart, newEnd, hbg, scale);
                        break;
                    }
                }
            }
        }
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
        for (ShortReadGroup g : shortReadGroupList) {
            for (ShortReadElement e : g.getShortReadElementList()) {
                e.setScale(scale);
                e.setPosition(offset + e.getBaseX(), 0.0, 0.0);
            }
        }

        for (HistogramBinGroup g : histogramBinGroupList) {
            for (HistogramBinElement e : g.getHistogramBinElementList()) {
                e.setScale(scale);
                e.setPosition(offset + e.getBaseX(), e.getBaseY(), 0);
            }
        }

        for (BedFragmentGroup g : bedFragmentGroupList) {
            for (BedFragmentElement e : g.getBedFragmentElementList()) {
                e.setScale(scale);
                e.setPosition(offset + e.getBaseX(), BED_FRAGMENT_POS_Y, 0);
            }
        }

        for (CytobandElement e : cytobandGroup.getCytobandElementList()) {
            e.setScale(scale);
            e.setPosition(offset + e.getBaseX(), CYTOBAND_POS_Y, 0);
        }

        for (ExonElement e : geneGroup.getExonElementList()) {
            e.setScale(scale);
            e.setPosition(offset + e.getBaseX(), RULER_POS_Y + e.getBaseY(), 0);
        }

        for (GeneElement e : geneGroup.getGeneElementList()) {
            e.setScale(scale);
            e.setPosition(offset + e.getBaseX(), RULER_POS_Y + e.getBaseY(), 0);
        }
    }

    private void updateRuler(double offset) {
        Line mainLine = rulerGroup.getMainLine();
        long start = leftValue, end = rightValue;
        if (start < 0) start = 0;
        if (end < 0) end = 0;
        mainLine.set(start, 0, end, 0);
        rulerGroup.setStart(start);
        rulerGroup.setEnd(end);
        mainLine.setScale(scale);
        mainLine.setPosition(offset + (rulerGroup.getStart() + rulerGroup.getEnd()) / 2.0 * scale, RULER_POS_Y, 0);

        long step = 1000;
        if (scale < 0.000004) {
            step = 100000000;
        } else if (scale < 0.00004) {
            step = 10000000;
        } else if (scale < 0.0004) {
            step = 1000000;
        } else if (scale < 0.004) {
            step = 100000;
        } else if (scale < 0.04) {
            step = 10000;
        }

        int idx = 0;
        for (long x = start - start % step; x < end; x += step) {
            if (rulerGroup.getRulerElementList().size() <= idx) break;

            RulerElement e = rulerGroup.getRulerElementList().get(idx);

            e.setInitialBaseX(x);
            e.getText().setText(Long.toString(x));

            e.setScale(scale);
            e.getLine().setPosition(offset + e.getBaseX(), RULER_POS_Y, 0);
            e.getText().setPosition(offset + e.getBaseX() + 3, RULER_POS_Y + 3, 0);

            e.getLine().visible();
            e.getText().visible();

            idx++;
        }
        
        for (int i = idx; i < rulerGroup.getRulerElementList().size(); i++) {
            RulerElement e = rulerGroup.getRulerElementList().get(i);
            e.getLine().hidden();
            e.getText().hidden();
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
            break;
        }
            
        default:
            break;
        }
    }

    public void finishedSamSelection() {
        logger.debug("Finished selecting sam data.");
        
        dbox.setVisible(false);
        
        if (!isFinishedInitialSelection) {
            initViews();
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
    
    public static void main(String[] args) {        
        logger.info("Start ngsView (Next Generation Sequencer View).");
        
        GeneView.args = args;
        AppletRunner.run("genome.view.GeneView", "ngs View");        
    }
}
