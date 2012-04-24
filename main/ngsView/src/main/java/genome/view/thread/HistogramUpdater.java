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
import genome.data.HistogramBin;
import genome.data.SamHistogram;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.element.HistogramBinElement;
import genome.view.group.HistogramBinGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;


public class HistogramUpdater {

    static Logger logger = LoggerFactory.getLogger(HistogramUpdater.class);
    
    private final SQLLoader sqlLoader;
    private final Text annotationText;
    private final Mouse mouse;
    
    private SamHistogram samHistogram;
    private Chromosome chromosome;
    private long start, end;
    private HistogramBinGroup histogramBinGroup;
    private double scale;
    
    private HistogramUpdateThread currentThread;
    
    private class HistogramUpdateThread extends Thread {
        
        boolean stopFlag = false;
        
        @Override
        public void run() {
            // Load HistogramBins.
            // ---------------------------------------------------------------------
            HistogramBin[] hbs = sqlLoader.loadHistgramBin(samHistogram.getSamHistogramId(), chromosome, start, end);

            if (hbs == null || hbs.length == 0) return;
            
            if (stopFlag) return;

            samHistogram.setHistogramBins(hbs);

            // Get max value of histogram.
            // ---------------------------------------------------------------------
            long maxValue = sqlLoader.getMaxHistogram(samHistogram.getSamHistogramId(), chromosome.getChrId());

            logger.info("Loaded " + hbs.length + " HistogramBins: (" + 
                        "samHistogramId: " + samHistogram.getSamHistogramId() + ", " +
                        "binSize: " + samHistogram.getBinSize() + ", " +
                        "Max value: " + maxValue + ")");

            // Setup HistogramBinGroup.
            // ---------------------------------------------------------------------
            histogramBinGroup.setup(samHistogram, hbs, samHistogram.getBinSize(), maxValue);
            
            if (stopFlag) return;

            for (HistogramBinElement e : histogramBinGroup.getHistogramBinElementList()) {
                e.setScale(scale);
                e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));
                
                if (stopFlag) return;
            }

            logger.debug("Finished updating histogram.");
        }
    }
    
    public HistogramUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        this.sqlLoader = sqlLoader;
        this.annotationText = annotationText;
        this.mouse = mouse;
    }
    
    public void start(SamHistogram samHistogram, Chromosome chromosome, long start, long end,
                      HistogramBinGroup histogramBinGroup, double scale) {
        this.samHistogram = samHistogram;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.histogramBinGroup = histogramBinGroup;
        this.scale = scale;
        
        if (currentThread != null && currentThread.isAlive()) {
            stop();
            
//            try {
//                currentThread.join();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }
        
        currentThread = new HistogramUpdateThread();
        
        currentThread.start();
    }
    
    public void stop() {
        if (currentThread != null) {
            currentThread.stopFlag = true;
        }
    }
}
