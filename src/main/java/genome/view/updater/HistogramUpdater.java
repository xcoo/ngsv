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
import genome.data.SamHistogram;
import genome.db.SQLLoader;
import genome.view.chart.HistogramChart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.Mouse;
import casmi.graphics.element.Text;

/**
 * Histogram update manager.
 *
 * @author T. Takeuchi
 */
public class HistogramUpdater extends Updater<HistogramUpdateThread> {

    static Logger logger = LoggerFactory.getLogger(HistogramUpdater.class);

    private SamHistogram samHistogram;
    private Chromosome chromosome;
    private long binSize;
    private long start, end;
    private boolean loadDB;
    private HistogramChart histogramChart;
    private double scale;
    private long maxValue;

    public HistogramUpdater(SQLLoader sqlLoader, Text annotationText, Mouse mouse) {
        super(HistogramUpdateThread.class, sqlLoader, annotationText, mouse);
    }

    public void setup(SamHistogram samHistogram, Chromosome chromosome,
                      long binSize, long start, long end, boolean loadDB,
                      HistogramChart histogramChart, double scale) {
        this.samHistogram = samHistogram;
        this.chromosome = chromosome;
        this.binSize = binSize;
        this.start = start;
        this.end = end;
        this.loadDB = loadDB;
        this.histogramChart = histogramChart;
        this.scale = scale;
    }

    public SamHistogram getSamHistogram() {
        return samHistogram;
    }

    public Chromosome getChromosome() {
        return chromosome;
    }

    public long getBinSize() {
        return binSize;
    }

    public long getStart() {
        return start;
    }

    public long getEnd() {
        return end;
    }


    public boolean isLoadDB() {
        return loadDB;
    }

    public HistogramChart getHistogramChart() {
        return histogramChart;
    }

    public double getScale() {
        return scale;
    }

    public long getMaxValue() {
        return maxValue;
    }
}
