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

package genome.view.element;

import casmi.graphics.color.Color;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Box;

/**
 * Cnv element class.
 * 
 * @author T. Takeuchi
 */
public class CnvElement extends Box implements ScalableElement {

    private static final Color DEFAULT_FILL_COLOR   = new RGBColor(255.0 / 255.0, 94.0 / 255.0, 25.0 / 255.0);
	private static final Color DEFAULT_STROKE_COLOR = new RGBColor(255.0 / 255.0, 50.0 / 255.0, 13.0 / 255.0);
    
    private double initialBaseX;
    private double baseX;
    private double baseY;
    
    private double scale;
    private double initialScale;
    
    private String name;
    
    public CnvElement(long copyNumber, long chrStart, long chrEnd, double initialScale) {
        super((chrEnd - chrStart) * initialScale, copyNumber * 10.0, 50.0);
        
        setPosition((chrStart + chrEnd) / 2.0, copyNumber * 10.0);
        setStroke(true);
        setStrokeColor(DEFAULT_STROKE_COLOR);
        setFillColor(DEFAULT_FILL_COLOR);
        
        this.initialScale = initialScale;
        this.scale = initialScale;
        
        baseX = (chrStart + chrEnd) / 2.0;
        initialBaseX = baseX;
        
        baseY = copyNumber * 10.0 / 2.0;
        
        name = String.format("%d (%d - %d)", copyNumber, chrStart, chrEnd);
    }

    @Override
    public double getBaseX() {
        return baseX;
    }

    @Override
    public double getBaseY() {
        return baseY;
    }
    
    public String getName() {
        return name;
    }
    
    public double getScale() {
        return scale;
    }
    
    @Override
    public void setScale(double scale) {
        this.scale = scale;
        setScaleX(scale / initialScale);
        
        baseX = initialBaseX * scale;
    }
}
