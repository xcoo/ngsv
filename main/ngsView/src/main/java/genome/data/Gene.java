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

package genome.data;

import genome.data.type.GeneOrientaion;
import genome.data.type.GeneType;

/**
 * Gene for connecting exons and introns
 * 
 * @author K. Nishimura
 *
 */
public class Gene {
	
	public static final String KNOWN_GENE_TYPE_NAME ="knownGene";
	
	private long id;
	private long chrId;
	private long start;
	private long end;
	private GeneOrientaion orientation;
	private String group;
	private GeneType type;
	private int order;
	private String name;

	public Gene(long id, long chrId, long start, long end, GeneOrientaion orientation, GeneType type, String group, String name){
		this.id = id;
	    this.chrId = chrId;
		this.group = group;
		this.start = start;
		this.end = end;
		this.orientation = orientation;
		this.type = type;
		this.order = 0;
		this.name = name;
	}
	
	@Override
	public boolean equals(Object obj){
		if( obj instanceof Gene ) {
			return false;
		}
		
		Gene other = (Gene)obj;
		
		if( other.getID() == this.id && other.getGroup().equalsIgnoreCase(this.group) ){
			return true;
		}else{
			return false;
		}
	}
	
	@Override
	public int hashCode(){
		return (this.id + ":" + this.chrId + ":" + this.group).hashCode();
	}
	
	public long getID() {
		return id;
	}
	
	public void setID(long id) {
		this.id = id;
	}
	
	public long getChrId() {
		return chrId;
	}
	
	public void setChrId(long chrId) {
		this.chrId = chrId;
	}
	
	public long getStart() {
		return start;
	}
	
	public void setStart(long start) {
		this.start = start;
	}
	
	public long getEnd() {
		return end;
	}
	
	public void setEnd(long end) {
		this.end = end;
	}
	
	public GeneOrientaion getOrientation() {
		return orientation;
	}
	
	public void setOrientation(GeneOrientaion orientation) {
		this.orientation = orientation;
	}
	
	public String getGroup() { // TODO rename
		return group;
	}
	
	public void setGroup(String group) {
		this.group = group;
	}
	
	public GeneType getType() {
		return type;
	}
	
	public void setType(GeneType type) {
		this.type = type;
	}
	
	public long getLength() {
		return Math.abs(this.end - this.start);
	}

	public int getOrder() {
		return order;
	}
	
	public void setOrder(int order) {
		this.order = order;
	}
	
	public long getLeft() {
		if( this.start < this.end ) {
			return this.start;
		} else {
			return this.end;
		}
	}
	
	public long getRight() {
		if( this.start > this.end ) {
			return this.start;
		} else {
			return this.end;
		}
	}
	
	public boolean checkOverlap(Gene other) {
		
		double left = this.getLeft(), right = this.getRight();
		double otherLeft = other.getLeft(), otherRight = other.getRight();
		
		if(left >= otherLeft && left <= otherRight){
			return true;
		}else if(right >= otherLeft && right <= otherRight){
			return true;
		}else if(left < otherLeft && right > otherRight){
			return true;
		}else{
			return false;
		}
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
