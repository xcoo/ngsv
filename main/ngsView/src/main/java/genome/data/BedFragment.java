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

import casmi.sql.Entity;
import casmi.sql.annotation.Fieldname;
import casmi.sql.annotation.Ignore;
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("bedFragment")
public class BedFragment extends Entity {
	@PrimaryKey
	private long bedFragmentId;

	@Fieldname("chrId")
	private long chrId;
	
	@Fieldname("chrStart")
	private long chrStart;

	@Fieldname("chrEnd")
	private long chrEnd;

	@Fieldname("name")
	private String name;

	@Fieldname("score")
	private int score;
	
	@Fieldname("strand")
	private int strand;
	
	@Fieldname("thickStart")
	private long thickStart;

	@Fieldname("thickEnd")
	private long thickEnd;
	
	@Fieldname("itemR")
	private int itemR;
	
	@Fieldname("itemG")
	private int itemG;

	@Fieldname("itemB")
	private int itemB;

	@Ignore
	private long blockCount;
	
	@Ignore
	private String blockSizes;

	@Ignore
	private String blockStarts;
	
	@Fieldname("bedId")
	private long bedId;

	public long getBedFragmentId() {
		return bedFragmentId;
	}

	public void setBedFragmentId(long bedFragmentId) {
		this.bedFragmentId = bedFragmentId;
	}

	public long getChrId() {
		return chrId;
	}

	public void setChrId(long chrId) {
		this.chrId = chrId;
	}

	public long getChrStart() {
		return chrStart;
	}

	public void setChrStart(long chrStart) {
		this.chrStart = chrStart;
	}

	public long getChrEnd() {
		return chrEnd;
	}

	public void setChrEnd(long chrEnd) {
		this.chrEnd = chrEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getScore() {
		return score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public int getStrand() {
		return strand;
	}

	public void setStrand(int strand) {
		this.strand = strand;
	}

	public long getThickStart() {
		return thickStart;
	}

	public void setThickStart(long thickStart) {
		this.thickStart = thickStart;
	}

	public long getThickEnd() {
		return thickEnd;
	}

	public void setThickEnd(long thickEnd) {
		this.thickEnd = thickEnd;
	}

	public int getItemR() {
		return itemR;
	}

	public void setItemR(int itemR) {
		this.itemR = itemR;
	}

	public int getItemG() {
		return itemG;
	}

	public void setItemG(int itemG) {
		this.itemG = itemG;
	}

	public int getItemB() {
		return itemB;
	}

	public void setItemB(int itemB) {
		this.itemB = itemB;
	}

	public long getBlockCount() {
		return blockCount;
	}

	public void setBlockCount(long blockCount) {
		this.blockCount = blockCount;
	}

	public String getBlockSizes() {
		return blockSizes;
	}

	public void setBlockSizes(String blockSizes) {
		this.blockSizes = blockSizes;
	}

	public String getBlockStarts() {
		return blockStarts;
	}

	public void setBlockStarts(String blockStarts) {
		this.blockStarts = blockStarts;
	}

	public long getBedId() {
		return bedId;
	}

	public void setBedId(long bedId) {
		this.bedId = bedId;
	}
}
