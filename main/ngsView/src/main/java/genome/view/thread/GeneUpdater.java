package genome.view.thread;

import genome.data.Chromosome;
import genome.data.Exon;
import genome.data.Gene;
import genome.db.GeneLoaderResult;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.element.ExonElement;
import genome.view.element.GeneElement;
import genome.view.group.GeneGroup;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;


public class GeneUpdater {

    static Logger logger = LoggerFactory.getLogger(GeneUpdater.class);
    
    private final SQLLoader sqlLoader;
    private final Text annotationText;
    private final Mouse mouse;

    private Chromosome chromosome;
    private long start, end;
    private GeneGroup geneGroup;
    private double scale;
    
    private GeneUpdateThread currentThread; 
    
    private class GeneUpdateThread extends Thread {
        
        boolean stopFlag = false;
        
        @Override
        public void run() {
            // Load refGene.
            // -----------------------------------------------------------------
            GeneLoaderResult geneLoaderResult = sqlLoader.loadGene(chromosome, start, end);
            List<Gene> geneList = geneLoaderResult.getGenes();
            List<Exon> exonList = geneLoaderResult.getExons();
            
            if (stopFlag) return;
            
            logger.info("Loaded " + geneList.size() + " Genes");
            logger.info("Loaded " + exonList.size() + " Exons");

            // Setup GeneGroup.
            // -----------------------------------------------------------------
            geneGroup.setup(geneList, exonList);
            
            if (stopFlag) return;

            for (GeneElement e : geneGroup.getGeneElementList()) {
                e.setScale(scale);
                e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));
                
                if (stopFlag) return;
            }
        
            for (ExonElement e : geneGroup.getExonElementList()) {
                e.setScale(scale);
                e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));
                
                if (stopFlag) return;
            }

            logger.debug("Finished updating gene.");
        }
    }
    
    public GeneUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        this.sqlLoader      = sqlLoader;
        this.annotationText = annotationText;
        this.mouse          = mouse;
    }
    
    public void start(Chromosome chromosome, long start, long end,
                      GeneGroup geneGroup, double scale) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.geneGroup = geneGroup;
        this.scale = scale;
        
        if (currentThread != null && currentThread.isAlive()) {
            stop();
            
//            try {
//                currentThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        
        currentThread = new GeneUpdateThread();
        
        currentThread.start();
    }
    
    public void stop() {
        if (currentThread != null) {
            currentThread.stopFlag = true;
        }
    }
}
