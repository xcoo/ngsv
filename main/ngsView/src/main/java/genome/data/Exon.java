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
 * Exon in Gene
 * 
 * @author K. Nishimura
 *
 */
public class Exon {

	private long chrId;
	private long start;
	private long end;
	private long id;
	private GeneOrientaion orientation;
	private String ref;
	private String group;
	private GeneType type;
	private int order;
	private String name;

	public Exon(long id, long chrId, long start, long end, GeneOrientaion orientation, GeneType type, String group, String name){
		this.id = id;
		this.chrId = chrId;
		this.start = start;
		this.end = end;
		this.orientation = orientation;
		this.group = group;
		this.type = type;
		this.order = 0;
		this.name = name;
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

	public long getLength() {
		return Math.abs(this.end - this.start);
	}

	public long getID() {
		return id;
	}

	public void setID(long featureid) {
		this.id = featureid;
	}

	public GeneOrientaion getOrientation() {
		return orientation;
	}

	public void setOrientation(GeneOrientaion orientation) {
		this.orientation = orientation;
	}

	public String getRef() {
		return ref;
	}

	public void setRef(String ref) {
		this.ref = ref;
	}

	public String getGroup() {
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

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
