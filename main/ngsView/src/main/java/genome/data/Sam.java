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

package genome.data;

import casmi.sql.Entity;
import casmi.sql.annotation.Fieldname;
import casmi.sql.annotation.Ignore;
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("sam")
public class Sam extends Entity {
    
	@PrimaryKey
	@Fieldname("samId")
	private long samId;

	@Fieldname("fileName")
	private String fileName;

	@Fieldname("createdDate")
	private long createdDate;

	@Ignore
    private String header;
    
    @Ignore
    private String length;
    
    @Ignore
    private String mapped;

	@Fieldname("numberOfChromosomes")
	private int numChromosome;

	@Fieldname("chromosomes")
	private String chromosome;
	
	@Ignore
	private boolean selected;

	@Ignore
	private ShortRead[] shortReads;
	
	@Ignore
	private SamHistogram[] samHistograms;

	public ShortRead[] getShortReads() {
		return shortReads;
	}

	public void setShortReads(ShortRead[] shortReads) {
		this.shortReads = shortReads;
	}
	
    public SamHistogram[] getSamHistograms() {
        return samHistograms;
    }
    
    public void setSamHistograms(SamHistogram[] samHistograms) {
        this.samHistograms = samHistograms;
    }

    public long getSamId() {
		return samId;
	}

	public void setSamId(long samId) {
		this.samId = samId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getMapped() {
		return mapped;
	}

	public void setMapped(String mapped) {
		this.mapped = mapped;
	}

	public int getNumChromosome() {
		return numChromosome;
	}

	public void setNumChromosome(int numChr) {
		this.numChromosome = numChr;
	}

	public String getChromosome() {
		return chromosome;
	}

	public void setChromosome(String chromosome) {
		this.chromosome = chromosome;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
}
