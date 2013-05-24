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

import genome.data.Bed;
import genome.data.BedFragment;
import genome.data.Chromosome;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.chart.BedChart;
import genome.view.element.BedFragmentElement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;

/**
 * @author T. Takeuchi
 */
class BedUpdateThread extends UpdateThread {

    static Logger logger = LoggerFactory.getLogger(BedUpdateThread.class);

    private final BedUpdater bedUpdater;

    public BedUpdateThread(BedUpdater bedUpdater) {
        this.bedUpdater = bedUpdater;
    }

    @Override
    public void run() {
        SQLLoader sqlLoader = bedUpdater.getSqlLoader();
        Text annotationText = bedUpdater.getAnnotationText();
        Mouse mouse = bedUpdater.getMouse();
        Bed bed = bedUpdater.getBed();
        Chromosome chromosome = bedUpdater.getChromosome();
        long start = bedUpdater.getStart();
        long end = bedUpdater.getEnd();
        BedChart bedChart = bedUpdater.getBedChart();
        double scale = bedUpdater.getScale();

        // Load BedFragment.
        // ---------------------------------------------------------------------
        BedFragment[] bfs = sqlLoader.loadBedFragment(bedChart.getBed().getBedId(), chromosome, start, end);

        if (bfs == null || bfs.length == 0) return;

        if (isStopFlag()) return;

        bed.setBedFragments(bfs);

        logger.info("Loaded " + bfs.length + " BedFragments");

        // Setup BedFragmentGroup.
        // ---------------------------------------------------------------------
        bedChart.setup(bfs);

        if (isStopFlag()) return;

        for (BedFragmentElement e : bedChart.getBedFragmentElementList()) {
            e.setScale(scale);
            e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(), annotationText, mouse));

            if (isStopFlag()) return;
        }

        logger.debug("Finished updating bed.");
    }
}
