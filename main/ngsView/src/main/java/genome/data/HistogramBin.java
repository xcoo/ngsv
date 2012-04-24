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
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("histogramBin")
public class HistogramBin extends Entity {
	
	@PrimaryKey
	@Fieldname("histogramBinId")
	private long histogramBinId;

	@Fieldname("samHistogramId")
	private long samHistogramId;

	@Fieldname("value")
	private long value;

	@Fieldname("chrId")
	private long chrId;

	@Fieldname("position")
	private long position;
	
	public long getHistogramBinId() {
		return histogramBinId;
	}

	public void setHistogramBinId(long histogramBinId) {
		this.histogramBinId = histogramBinId;
	}

	public long getSamHistogramId() {
		return samHistogramId;
	}

	public void setSamHistogramId(long samHistogramId) {
		this.samHistogramId = samHistogramId;
	}

	public long getValue() {
		return value;
	}

	public void setValue(long value) {
		this.value = value;
	}

	public long getChrId() {
		return chrId;
	}

	public void setChrId(long chrId) {
		this.chrId = chrId;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}
}
