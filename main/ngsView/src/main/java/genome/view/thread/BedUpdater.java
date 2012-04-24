package genome.view.thread;

import genome.data.Bed;
import genome.data.BedFragment;
import genome.data.Chromosome;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.element.BedFragmentElement;
import genome.view.group.BedFragmentGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;


public class BedUpdater {

    static Logger logger = LoggerFactory.getLogger(BedUpdater.class);
    
    private final SQLLoader sqlLoader;
    private final Text annotationText;
    private final Mouse mouse;
    
    private Bed bed;
    private Chromosome chromosome;
    private long start, end;
    private BedFragmentGroup bedFragmentGroup;
    private double scale;
    
    private BedUpdateThread currentThread;
    
    private class BedUpdateThread extends Thread {
        
        boolean stopFlag = false;
        
        @Override
        public void run() {
            // Load BedFragment.
            // ---------------------------------------------------------------------
            // BedFragment[] bfs = sqlLoader.loadBedFragment(bedFragmentGroup.getBed().getBedId(), chromosome);
            BedFragment[] bfs = sqlLoader.loadBedFragment(bedFragmentGroup.getBed().getBedId(), chromosome, start, end);

            if (bfs == null || bfs.length == 0) return;

            if (stopFlag) return;
            
            bed.setBedFragments(bfs);

            logger.info("Loaded " + bfs.length + " BedFragments");

            // Setup ShortReadGroup.
            // ---------------------------------------------------------------------
            bedFragmentGroup.setup(bfs);
            
            if (stopFlag) return;

            for (BedFragmentElement e : bedFragmentGroup.getBedFragmentElementList()) {
                e.setScale(scale);
                e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));
                
                if (stopFlag) return;
            }

            logger.debug("Finished updating bed.");
        }
    }
    
    public BedUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        this.sqlLoader = sqlLoader;
        this.annotationText = annotationText;
        this.mouse = mouse;
    }
    
    public void start(Bed bed, Chromosome chromosome, long start, long end,
                      BedFragmentGroup bedFragmentGroup, double scale) {
        this.bed = bed;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.bedFragmentGroup = bedFragmentGroup;
        this.scale = scale;
        
        if (currentThread != null && currentThread.isAlive()) {
            stop();
            
//            try {
//                currentThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        
        currentThread = new BedUpdateThread();
        
        currentThread.start();
    }
    
    public void stop() {
        if (currentThread != null) {
            currentThread.stopFlag = true;
        }
    }

}
