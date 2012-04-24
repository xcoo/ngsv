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

@Tablename("bed")
public class Bed extends Entity {
	@PrimaryKey
	private long bedId;

	@Fieldname("fileName")
	private String fileName;

	@Fieldname("createdDate")
	private long createdDate;

	@Ignore
    private String trackName;

	@Ignore
    private String description;

	@Ignore
    private int visibility;
	
	@Ignore
	private int itemRgb;

	@Ignore
	private boolean selected;
	
	@Ignore
	private BedFragment[] bedFragments;
	
	public long getBedId() {
		return bedId;
	}

	public void setBedId(long bedId) {
		this.bedId = bedId;
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

	public String getTrackName() {
		return trackName;
	}

	public void setTrackName(String trackName) {
		this.trackName = trackName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getVisibility() {
		return visibility;
	}

	public void setVisibility(int visibility) {
		this.visibility = visibility;
	}

	public int getItemRgb() {
		return itemRgb;
	}

	public void setItemRgb(int itemRgb) {
		this.itemRgb = itemRgb;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}
    
    public BedFragment[] getBedFragments() {
        return bedFragments;
    }
    
    public void setBedFragments(BedFragment[] bedFragments) {
        this.bedFragments = bedFragments;
    }

}
