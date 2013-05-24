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

/**
 * @author T. Takeuchi
 */
class GeneUpdateThread extends UpdateThread {

    static Logger logger = LoggerFactory.getLogger(GeneUpdateThread.class);

    private final GeneUpdater geneUpdater;

    public GeneUpdateThread(GeneUpdater geneUpdater) {
        this.geneUpdater = geneUpdater;
    }

    @Override
    public void run() {
        SQLLoader sqlLoader = geneUpdater.getSqlLoader();
        Text annotationText = geneUpdater.getAnnotationText();
        Mouse mouse = geneUpdater.getMouse();
        Chromosome chromosome = geneUpdater.getChromosome();
        long start = geneUpdater.getStart();
        long end = geneUpdater.getEnd();
        GeneChart geneChart = geneUpdater.getGeneChart();
        double scale = geneUpdater.getScale();

        // Load refGene.
        // -----------------------------------------------------------------
        GeneLoaderResult geneLoaderResult = sqlLoader.loadGene(chromosome, start, end);
        List<Gene> geneList = geneLoaderResult.getGenes();
        List<Exon> exonList = geneLoaderResult.getExons();

        if (isStopFlag()) return;

        logger.info(String.format("Load %d genes", geneList.size()));
        logger.info(String.format("Load %d exons", exonList.size()));

        // Setup GeneGroup.
        // -----------------------------------------------------------------
        geneChart.setup(geneList, exonList);

        if (isStopFlag()) return;

        for (GeneElement e : geneChart.getGeneElementList()) {
            e.setScale(scale);
            e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));

            if (isStopFlag()) return;
        }

        for (ExonElement e : geneChart.getExonElementList()) {
            e.setScale(scale);
            e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));

            if (isStopFlag()) return;
        }

        logger.debug("Finished updating gene.");
    }
}
