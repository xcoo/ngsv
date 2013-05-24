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
import genome.db.SQLLoader;
import genome.view.chart.GeneChart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;

/**
 * @author T. Takeuchi
 */
public class GeneUpdater extends Updater<GeneUpdateThread> {

    static Logger logger = LoggerFactory.getLogger(GeneUpdater.class);

    private Chromosome chromosome;
    private long start, end;
    private GeneChart geneChart;
    private double scale;

    public GeneUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        super(GeneUpdateThread.class, sqlLoader, annotationText, mouse);
    }

    public void setup(Chromosome chromosome, long start, long end,
                      GeneChart geneChart, double scale) {
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.geneChart = geneChart;
        this.scale = scale;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }

    public GeneChart getGeneChart() {
        return geneChart;
    }

    public double getScale() {
        return scale;
    }
}
