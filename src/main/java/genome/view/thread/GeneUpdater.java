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

package genome.view.thread;

import genome.data.Chromosome;
import genome.data.Exon;
import genome.data.Gene;
import genome.db.GeneLoaderResult;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.chart.GeneChart;
import genome.view.element.ExonElement;
import genome.view.element.GeneElement;

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
    private GeneChart geneChart;
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
            
            logger.info(String.format("Load %d genes", geneList.size()));
            logger.info(String.format("Load %d exons", exonList.size()));

            // Setup GeneGroup.
            // -----------------------------------------------------------------
            geneChart.setup(geneList, exonList);
            
            if (stopFlag) return;

            for (GeneElement e : geneChart.getGeneElementList()) {
                e.setScale(scale);
                e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));
                
                if (stopFlag) return;
            }
        
            for (ExonElement e : geneChart.getExonElementList()) {
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
                      GeneChart geneChart, double scale) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.geneChart = geneChart;
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
