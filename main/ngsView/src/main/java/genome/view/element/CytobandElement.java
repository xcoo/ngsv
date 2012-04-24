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

import genome.data.CytoBand;
import casmi.graphics.color.Color;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.GrayColor;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Rect;


/**
 * rendering object for cytoband 
 * 
 * @author K. Nishimura
 *
 */

public class CytobandElement extends Rect implements ScalableElement {

	private CytoBand cb;
	
	private double scale;
	private double initialScale;
	
	private final double initialBaseX;
	private double baseX;
	
	private final static Color  GNEG_COLOR, GVAR_COLOR, GPOS100_COLOR,
	                            GPOS75_COLOR, GPOS66_COLOR, GPOS50_COLOR, 
	                            GPOS33_COLOR, GPOS25_COLOR, GPOS0_COLOR,
	                            ACEN_COLOR, STALK_COLOR, GPOS_COLOR;
	static{
	    GPOS100_COLOR = new GrayColor(1.00);
	    GPOS75_COLOR  = new GrayColor(0.25);
        GPOS66_COLOR  = new GrayColor(0.333);
	    GPOS50_COLOR  = new GrayColor(0.50);
        GPOS33_COLOR  = new GrayColor(0.666);
        GPOS25_COLOR  = new GrayColor(0.75);
        GPOS0_COLOR   = new GrayColor(0.00);
        GNEG_COLOR    = new GrayColor(0.9);
        GVAR_COLOR    = new GrayColor(0.85);
        ACEN_COLOR    = new RGBColor(0.851, 0.184, 0.153);
        STALK_COLOR   = new RGBColor(0.439, 0.502, 0.565);
        GPOS_COLOR    = new GrayColor(0.3);
	}
	
	public CytobandElement(CytoBand cb, double scale, double height) {
		
		super(scale * cb.getLength(), height);
		
		this.scale = scale;
		this.initialScale = this.scale;

		this.baseX = cb.getStart() + cb.getLength() / 2.0;
		this.initialBaseX = baseX;
		
		this.cb = cb;
		
    	this.setStrokeColor(ColorSet.WHITE);
    	this.setStrokeWidth(1);
    	this.setStroke(true);
    	
    	String colorName = cb.getColor();
    	
    	if(colorName.equalsIgnoreCase("gpos100")){
    		this.setFillColor(GPOS100_COLOR); 
    	}else if(colorName.equalsIgnoreCase("gpos")){
    		this.setFillColor(GPOS0_COLOR);
    	}else if(colorName.equalsIgnoreCase("gpos75")){
    		this.setFillColor(GPOS75_COLOR);
    	}else if(colorName.equalsIgnoreCase("gpos66")){
    		this.setFillColor(GPOS66_COLOR);
    	}else if(colorName.equalsIgnoreCase("gpos50")){
    		this.setFillColor(GPOS50_COLOR);
    	}else if(colorName.equalsIgnoreCase("gpos33")){
    		this.setFillColor(GPOS33_COLOR);
    	}else if(colorName.equalsIgnoreCase("gpos25")){
    		this.setFillColor(GPOS25_COLOR);
    	}else if(colorName.equalsIgnoreCase("gvar")){
    		this.setFillColor(GVAR_COLOR);
    	}else if(colorName.equalsIgnoreCase("gneg")){
    		this.setFillColor(GNEG_COLOR);
    	}else if(colorName.equalsIgnoreCase("acen")){
    		this.setFillColor(ACEN_COLOR);
    	}else if(colorName.equalsIgnoreCase("stalk")){
    		this.setFillColor(STALK_COLOR);
    	}else{
    		this.setFillColor(GPOS_COLOR);
    	}
	}

    public String getName() {
//    	if( cb.getChromosomeNumber() == 23 ){
//			return "Chr.X " + this.cb.getName();
//		}else if( cb.getChromosomeNumber() == 24 ){
//			return "Chr.Y " + this.cb.getName();
//		}else{
//			return "Chr." + this.cb.getChromosomeNumber() + " " + this.cb.getName();
//		}
    	return "Chr." + this.cb.getChrName() + this.cb.getName();
    }

	public double getScale() {
		return scale;
	}

	public void setScale(double scale) {
		this.scale = scale;
		this.setScaleX(this.scale/this.initialScale);
		
		this.baseX = this.initialBaseX * scale;
	}

	public double getBaseX() {
		return baseX;
	}

	public void setBaseX(double baseX) {
		this.baseX = baseX;
	}

	public double getBaseY() {
		return 0;
	}
}
