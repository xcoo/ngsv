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

import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Line;
import casmi.graphics.element.Text;
import casmi.graphics.font.Font;

public class RulerElement implements ScalableElement {
	
	private double initialBaseX;
	private double baseX;
	
	private double scale;
//	private double initialScale;
	
	private Line l;
	private Text t;
	
	private static Font DEFAULT_FONT;
	
	static { 
		DEFAULT_FONT = new Font("San-Serif");
		DEFAULT_FONT.setSize(10);
	};
	
	// parameter for scale
	private static final double RULER_HEIGHT = 14;
	private static final double HALF_RULER_HEIGHT = RULER_HEIGHT / 2;
	
	public RulerElement(double baseX, double baseY, double scale) {
		this.scale = scale;
//		this.initialScale = this.scale;

		this.initialBaseX = baseX;
		this.baseX = baseX;
		
		this.l = new Line( 0, baseY - HALF_RULER_HEIGHT, 0, baseY + HALF_RULER_HEIGHT);
		this.l.setStrokeColor(ColorSet.WHITE);
		
		this.t = new Text(Long.toString((long)baseX), DEFAULT_FONT, baseX * scale, baseY);
		this.t.setStrokeColor(ColorSet.GRAY);
	}
		
	public Line getLine(){
		return this.l;
	}
	
	public Text getText(){
		return this.t;
	}
	
	public double getBaseX() {
		return baseX;
	}

	public void setBaseX(double baseX) {
		this.baseX = baseX;
	}

    public double getInitialBaseX() {
        return initialBaseX;
    }
    
    public void setInitialBaseX(double initialBaseX) {
        this.initialBaseX = initialBaseX;
    }

    public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
		//this.l.setScaleX(this.scale/this.initialScale);
		this.baseX = this.initialBaseX * scale;
	}

	public double getBaseY() {
		return 0;
	}
}
