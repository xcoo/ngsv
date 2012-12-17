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

import genome.data.HistogramBin;
import casmi.graphics.color.Color;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Box;

public class HistogramBinElement extends Box implements ScalableElement {
	
	public static final double MAX_HEIGHT = 100.0;
	
	private static final Color DEFAULT_FILL_COLOR   = new RGBColor(28.0 / 255.0, 97.0  / 255.0, 140.0 / 255.0);
	private static final Color DEFAULT_STROKE_COLOR = new RGBColor(46.0 / 255.0, 120.0 / 255.0, 160.0 / 255.0);

	private double initialBaseX;
	private double baseX;
	private double baseY;
	
	private String name;

	private double scale;
	private double initialScale;
            
	public HistogramBinElement(HistogramBin hb, double initialScale, long binSize, double maxHeight) {
	    this(hb.getValue(), hb.getPosition(), initialScale, binSize, maxHeight);
	}
	
	public HistogramBinElement(long value, long position, double initialScale, long binSize, double maxHeight) {
		super(binSize * initialScale,
		      Math.log((double)value)  / Math.log(maxHeight) * MAX_HEIGHT,
		      50.0);
		
		this.setPosition(position + binSize / 2.0, 
		                 Math.log((double)value) / Math.log(maxHeight) / 2.0 * MAX_HEIGHT);
        this.setStroke(true);
        this.setStrokeColor(DEFAULT_STROKE_COLOR);
        this.setFillColor(DEFAULT_FILL_COLOR);       

        this.initialScale = initialScale;
        this.scale = initialScale;
		
		this.name = value + " (" + position + " - " + (position + binSize) + ")";

		this.baseX = position + binSize / 2.0;
		this.initialBaseX = this.baseX;
		
		this.baseY = Math.log((double)value) / Math.log(maxHeight) / 2.0 * MAX_HEIGHT;	    
	}

	public String getName() {
		return name;
	}

	public double getBaseX() {
		return baseX;
	}

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
