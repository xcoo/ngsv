/*
 *   ngs View
 *   http://casmi.github.com/
 *   Copyright (C) 2011-2012, Xcoo, Inc.
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

import genome.data.BedFragment;
import casmi.graphics.color.Color;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Rect;

public class BedFragmentElement extends Rect implements ScalableElement {
	
	private String name;
	private double scale;
	private double initialScale;
	
	private final double initialBaseX;
	private double baseX;
	
	private static final double HEIGHT = 12;
	
    private static final Color DEFAULT_COLOR = new RGBColor(171.0/255.0, 39.0/255.0, 74.0/255.0);


	public BedFragmentElement (BedFragment bf, double scale) {
		super( bf.getChrStart() + (bf.getChrEnd() - bf.getChrStart())/ 2.0, 0.0,
				scale * (bf.getChrEnd() - bf.getChrStart()), HEIGHT);
		
		this.baseX = bf.getChrStart() + (bf.getChrEnd() - bf.getChrStart())/ 2.0;

		this.initialBaseX = this.baseX;
		
		this.setStroke(false);
		this.setFillColor(DEFAULT_COLOR);    	

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

	public double getBaseX() {
		return baseX;
	}

	public void setScale(double scale) {
		this.scale = scale;
		this.setScaleX(this.scale/this.initialScale);
		
		this.baseX = this.initialBaseX * scale;
	}

	public double getBaseY() {
		return 0;
	}
}
