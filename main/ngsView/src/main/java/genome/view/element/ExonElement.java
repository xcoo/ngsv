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

import genome.data.Exon;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Rect;

/**
 * element for drawing Exon
 * 
 * @author K. Nishimura
 *
 */
public class ExonElement extends Rect implements ScalableElement {
	
	private static final double HEIGHT = 12;

	private String name;
	
	private double scale;
	private double initialScale;
	
	private double baseX;
	private double baseY;
	private double initialBaseX;
//	private double initialBaseY;
	
	private static final Color KNOWN_COLOR;
	private static final Color REFERENCE_COLOR;
	
	// number of gene order (limited to MAX_GENE_ORDER_NUM layers)
	private static final int GENE_ELEMENT_ORDER_MAX_NUM = 3;
	private static final double GENE_ELEMENT_ORDER_STEP = 16;
	
	static {
		KNOWN_COLOR = new RGBColor(134/255.0, 186/255.0, 104/255.0);
		REFERENCE_COLOR = new RGBColor(134/255.0, 186/255.0, 204/255.0);
	}
	
	public ExonElement(Exon e, double scale) {
		super(scale * e.getLength(), HEIGHT);
		
		this.scale = scale;
		this.initialScale = this.scale;
		
		this.name = e.getName();

		this.baseX = (e.getEnd() + e.getStart()) / 2.0;
		
		double order = e.getOrder();
		
		if (order > GENE_ELEMENT_ORDER_MAX_NUM) {
			order = GENE_ELEMENT_ORDER_MAX_NUM;
		}
		
		this.baseY = GENE_ELEMENT_ORDER_STEP * order;

		this.initialBaseX = this.baseX;
		
    	this.setStroke(false);
    	
    	switch( e.getType() ) {
		case KNOWN:
			this.setFillColor(KNOWN_COLOR);
			break;
		case REFERENCE_SEQUENCE:
			this.setFillColor(REFERENCE_COLOR);
			break;
		case OTHER:
			this.setFillColor(ColorSet.BLACK);
			break;
		}
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

	public void setScale(double scale) {
		this.scale = scale;
		this.setScaleX(this.scale/this.initialScale);
		
		this.baseX = this.initialBaseX * scale;
	}
}
