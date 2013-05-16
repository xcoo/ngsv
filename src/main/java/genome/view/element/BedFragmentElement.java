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

import genome.config.Default;
import genome.data.BedFragment;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Rect;

public class BedFragmentElement extends Rect implements ScalableElement {

    private String name;
    private double scale;
    private double initialScale;

    private final double initialBaseX;
    private double baseX;

    private static final double HEIGHT = 12;

    public BedFragmentElement(BedFragment bf, double scale) {
        super(bf.getChrStart() + (bf.getChrEnd() - bf.getChrStart()) / 2.0, 0.0, scale
            * (bf.getChrEnd() - bf.getChrStart()), HEIGHT);

        this.baseX = bf.getChrStart() + (bf.getChrEnd() - bf.getChrStart()) / 2.0;

        this.initialBaseX = this.baseX;

        this.setStroke(false);
        this.setFillColor(new RGBColor(Default.getInstance().getBedFillColor()));

        this.scale = scale;
        this.initialScale = this.scale;

        this.name = bf.getName();
    }

    public String getName() {
        return name;
    }

    public double getScale() {
        return scale;
    }

    @Override
    public double getBaseX() {
        return baseX;
    }

    @Override
    public void setScale(double scale) {
        this.scale = scale;
        this.setScaleX(this.scale / this.initialScale);

        this.baseX = this.initialBaseX * scale;
    }

    @Override
    public double getBaseY() {
        return 0.0;
    }
}
