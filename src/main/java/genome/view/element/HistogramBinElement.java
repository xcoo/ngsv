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

package genome.view.element;

import genome.config.ViewerConfig;
import genome.data.HistogramBin;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Box;

public class HistogramBinElement extends Box implements ScalableElement {

    public static final double MAX_HEIGHT = 100.0;

    private double initialBaseX;
    private double baseX;
    private double baseY;

    private String name;

    private double scale;
    private double initialScale;

    public HistogramBinElement(HistogramBin hb, double initialScale, long binSize, long maxValue) {
        this(hb.getValue(), hb.getPosition(), initialScale, binSize, maxValue);
    }

    public HistogramBinElement(long value, long position, double initialScale, long binSize,
        long maxValue) {
        super(binSize * initialScale, Math.log(value) / Math.log(maxValue) * MAX_HEIGHT, 50.0);

        this.setPosition(position + binSize / 2.0,
            Math.log(value) / Math.log(maxValue) / 2.0 * MAX_HEIGHT);
        this.setStroke(true);
        this.setStrokeColor(new RGBColor(ViewerConfig.getInstance().getHistogramStrokeColor()));
        this.setFillColor(new RGBColor(ViewerConfig.getInstance().getHistogramFillColor()));

        this.initialScale = initialScale;
        this.scale = initialScale;

        this.name = String.format("%d (%d - %d)", value, position, position + binSize);

        this.baseX = position + binSize / 2.0;
        this.initialBaseX = this.baseX;

        this.baseY = Math.log(value) / Math.log(maxValue) / 2.0 * MAX_HEIGHT;
    }

    public String getName() {
        return name;
    }

    @Override
    public double getBaseX() {
        return baseX;
    }

    @Override
    public double getBaseY() {
        return baseY;
    }

    public double getScale() {
        return scale;
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
        this.setScaleX(this.scale / this.initialScale);

        this.baseX = this.initialBaseX * scale;
    }
}
