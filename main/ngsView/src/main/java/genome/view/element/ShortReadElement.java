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

import genome.data.ShortRead;
import casmi.graphics.color.Color;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Polygon;

public class ShortReadElement extends Polygon implements ScalableElement {
	
	private static final double HEIGHT = 10;
	private static final double DEFAULT_HEAD_LENGTH = 6;
	
	// when short read is shorter than SHORT_READ_RECT_HEAD
	private static final double HEAD_PERCENT = 0.2;

	private static final Color STRAND_PLUS_COLOR = new RGBColor(242.0/255.0, 135.0/255.0, 5.0/255.0);
	private static final Color STRAND_MINUS_COLOR = new RGBColor(242.0/255.0, 206.0/255.0, 60.0/255.0);
	
	private double initialBaseX;
	private double baseX;

	private String name;
	private String sequence;

	private double scale;
	private double initialScale;

	public ShortReadElement (ShortRead sr, double scale) {
	    super();
	    
        this.scale = scale;
        this.initialScale = this.scale;

        this.name = sr.getName();
		this.sequence = sr.getSequence();

		this.baseX = sr.getRefStart() + sr.getRefLength() / 2.0;
		this.initialBaseX = this.baseX;
		
		makePolygon(sr);
	}

	private void makePolygon(ShortRead sr) { // TODO refactoring
		
		// Strand of the query sequence (0 forward, 1 reverse) 
		double headLength = DEFAULT_HEAD_LENGTH;
		double length = sr.getRefLength();
		
		if( length < headLength ){
			headLength = length * HEAD_PERCENT;
		}
		
		if(sr.getSamFlag().isSamFlagStrand()){
			this.vertex(length, HEIGHT / 2.0 );
			this.vertex(headLength, HEIGHT / 2.0 );
			this.vertex(0.0, 0.0);
			this.vertex(headLength, - HEIGHT / 2.0 );
			this.vertex(length, - HEIGHT / 2.0 );
			
	        this.setFillColor(STRAND_MINUS_COLOR); 
		}else{
			this.vertex(0, HEIGHT / 2.0 );
			this.vertex(length - headLength, HEIGHT / 2.0 );
			this.vertex(length, 0.0);
			this.vertex(length - headLength, - HEIGHT / 2.0 );
			this.vertex(0, - HEIGHT / 2.0 );
			
	        this.setFillColor(STRAND_PLUS_COLOR); 
		}
		
		this.setStroke(false);
	}
	
	public String getName() {
		return name;
	}

	public double getInitialBaseX() {
		return initialBaseX;
	}

	public void setInitialBaseX(double initialBaseX) {
		this.initialBaseX = initialBaseX;
	}

	public double getBaseX() {
		return baseX;
	}

	public void setBaseX(double baseX) {
		this.baseX = baseX;
	}
	
	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
		this.setScaleX(this.scale/this.initialScale);
		
		this.baseX = this.initialBaseX * scale;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public double getBaseY() {
		return 0;
	}
}
