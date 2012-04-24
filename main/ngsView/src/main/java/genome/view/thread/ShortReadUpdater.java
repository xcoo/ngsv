package genome.view.thread;

import genome.data.Chromosome;
import genome.data.Sam;
import genome.data.ShortRead;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.element.ShortReadElement;
import genome.view.group.ShortReadGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;


public class ShortReadUpdater {

    static Logger logger = LoggerFactory.getLogger(ShortReadUpdater.class);
    
    private final SQLLoader sqlLoader;
    private final Text annotationText;
    private final Mouse mouse;
    
    private Sam sam;
    private Chromosome chromosome;
    private long start, end;
    private ShortReadGroup shortReadGroup;
    private double scale;
    
    private ShortReadUpdateThread currentThread;
    
    private class ShortReadUpdateThread extends Thread {
        
        boolean stopFlag = false;
        
        @Override
        public void run() {
            // Load ShortRead.
            // ---------------------------------------------------------------------
            ShortRead[] srs = sqlLoader.loadShortRead(sam.getSamId(), chromosome, start, end);

            if (srs == null || srs.length == 0) return;
            
            if (stopFlag) return;

            sam.setShortReads(srs);

            logger.info("Loaded " + srs.length + " ShortReads");

            // Setup ShortReadGroup.
            // ---------------------------------------------------------------------
            shortReadGroup.setup(srs);
            
            if (stopFlag) return;

            for (ShortReadElement e : shortReadGroup.getShortReadElementList()) {
                e.setScale(scale);
                e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));
                
                if (stopFlag) return;
            }

            logger.debug("Finished updating shortread.");
        }
    }
    
    public ShortReadUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        this.sqlLoader = sqlLoader;
        this.annotationText = annotationText;
        this.mouse = mouse;
    }
    
    public void start(Sam sam, Chromosome chromosome, long start, long end,
                      ShortReadGroup shortReadGroup, double scale) {
        this.sam = sam;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.shortReadGroup = shortReadGroup;
        this.scale = scale;
        
        if (currentThread != null && currentThread.isAlive()) {
            stop();

//            try {
//                currentThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        
        currentThread = new ShortReadUpdateThread();
        
        currentThread.start();
    }
    
    public void stop() {
        if (currentThread != null) {
            currentThread.stopFlag = true;
        }
    }
    
}
