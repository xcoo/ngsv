/*
 *   ngsv
 *   http://github.com/xcoo/ngsv
 *   Copyright (C) 2013, Xcoo, Inc.
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
import genome.data.Cnv;
import genome.data.CnvFragment;
import genome.db.SQLLoader;
import genome.view.AnnotationMouseOverCallback;
import genome.view.element.CnvElement;
import genome.view.group.CnvFragmentGroup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;

/**
 * Cnv update manager.
 *
 * @author T. Takeuchi
 */
public class CnvUpdater {

    static Logger logger = LoggerFactory.getLogger(CnvUpdater.class);

    private final SQLLoader sqlLoader;
    private final Text annotationText;
    private final Mouse mouse;

    private Cnv cnv;
    private Chromosome chromosome;
    private long start, end;
    private CnvFragmentGroup cnvFragmentGroup;
    private double scale;

    private CnvUpdateThread currentThread;

    private class CnvUpdateThread extends Thread {

        boolean stopFlag = false;

        @Override
        public void run() {
            logger.debug("Loading Cnv from DB...");
            CnvFragment[] cfs = sqlLoader.loadCnvFragment(cnv.getCnvId(), chromosome, start, end);
            if (stopFlag) return;
            cnvFragmentGroup.setup(cfs);
            for (CnvElement e : cnvFragmentGroup.getCnvElementList()) {
                e.setScale(scale);
                e.addMouseEventCallback(new AnnotationMouseOverCallback(e.getName(),
                    annotationText, mouse));
                if (stopFlag) return;
            }
            logger.debug("Finished updating Cnv");
        }
    }

    public CnvUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        this.sqlLoader = sqlLoader;
        this.annotationText = annotationText;
        this.mouse = mouse;
    }

    public void start(Cnv cnv, Chromosome chromosome, long start, long end, CnvFragmentGroup cnvFragmentGroup, double scale) {
        this.cnv = cnv;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.cnvFragmentGroup = cnvFragmentGroup;
        this.scale = scale;

        if (currentThread != null && currentThread.isAlive()) stop();
        currentThread = new CnvUpdateThread();
        currentThread.start();
    }

    public void stop() {
        if (currentThread != null) {
            currentThread.stopFlag = true;
        }
    }
}
