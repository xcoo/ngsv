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
import genome.data.Chromosome;
import genome.db.SQLLoader;
import genome.view.chart.BedChart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;

/**
 * @author T. Takeuchi
 */
public class BedUpdater extends Updater<BedUpdateThread> {

    static Logger logger = LoggerFactory.getLogger(BedUpdater.class);

    private Bed bed;
    private Chromosome chromosome;
    private long start, end;
    private BedChart bedChart;
    private double scale;

    public BedUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        super(BedUpdateThread.class, sqlLoader, annotationText, mouse);
    }

    public void setup(Bed bed, Chromosome chromosome, long start, long end,
                      BedChart bedChart, double scale) {
        this.bed = bed;
        this.chromosome = chromosome;
        this.start = start;
        this.end = end;
        this.bedChart = bedChart;
        this.scale = scale;
    }

    public Bed getBed() {
        return bed;
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

    public BedChart getBedChart() {
        return bedChart;
    }

    public double getScale() {
        return scale;
    }
}
