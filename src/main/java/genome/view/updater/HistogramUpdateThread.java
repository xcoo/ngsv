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

package genome.view.updater;

import genome.data.Chromosome;
import genome.data.HistogramBin;
import genome.data.SamHistogram;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.chart.HistogramChart;
import genome.view.element.HistogramBinElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;

/**
 * @author T. Takeuchi
 */
public class HistogramUpdateThread extends UpdateThread {

    static Logger logger = LoggerFactory.getLogger(HistogramUpdateThread.class);

    private final HistogramUpdater histogramUpdater;

    public HistogramUpdateThread(HistogramUpdater histogramUpdater) {
        this.histogramUpdater = histogramUpdater;
    }

    @Override
    public void run() {
        SQLLoader sqlLoader = histogramUpdater.getSqlLoader();
        Text annotationText = histogramUpdater.getAnnotationText();
        Mouse mouse = histogramUpdater.getMouse();
        SamHistogram samHistogram = histogramUpdater.getSamHistogram();
        Chromosome chromosome = histogramUpdater.getChromosome();
        long binSize = histogramUpdater.getBinSize();
        long start = histogramUpdater.getStart();
        long end = histogramUpdater.getEnd();
        boolean loadDB = histogramUpdater.isLoadDB();
        HistogramChart histogramChart = histogramUpdater.getHistogramChart();
        double scale = histogramUpdater.getScale();
        long maxValue = histogramUpdater.getMaxValue();

        if (loadDB) {
            // Load HistogramBins.
            // ---------------------------------------------------------------------
            logger.debug("Loading HistogramBin from DB...");
            HistogramBin[] hbs = sqlLoader.loadHistgramBin(samHistogram.getSamHistogramId(), chromosome, start, end);

            if (hbs == null || hbs.length == 0) return;

            if (isStopFlag()) return;

            samHistogram.setHistogramBins(hbs);

            // Get max value of histogram.
            // ---------------------------------------------------------------------
            maxValue = sqlLoader.getMaxHistogram(samHistogram.getSamHistogramId(), chromosome.getChrId());

            logger.info(
                String.format("Load %d HistogramBins: (samHistogramId: %d, binSize: %d, maxValue: %d)",
                    hbs.length, samHistogram.getSamHistogramId(), samHistogram.getBinSize(), maxValue));
        }

        if (samHistogram.getHistogramBins() == null || samHistogram.getHistogramBins().length == 0)
            return;

        // Setup HistogramBinGroup.
        // ---------------------------------------------------------------------
        histogramChart.setup(samHistogram.getHistogramBins(), samHistogram.getBinSize(), binSize, maxValue);

        if (isStopFlag()) return;

        for (HistogramBinElement e : histogramChart.getHistogramBinElementList()) {
            e.setScale(scale);
            e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));

            if (isStopFlag()) return;
        }

        logger.debug(String.format("Display binSize: %d", binSize));

        logger.debug("Finished updating histogram.");
    }
}
