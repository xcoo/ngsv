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

@Tablename("samHistogram")
public class SamHistogram extends Entity {

	@PrimaryKey
	@Fieldname("samId")
	private long samId;

	@Fieldname("binSize")
	private long binSize;

	@Fieldname("createdDate")
	private long createdDate;
	
	@Fieldname("samHistogramId")
	private long samHistogramId;

	@Ignore
	private long maxCount;
	
	@Ignore
	private HistogramBin[] histogramBins;
	
	public long getSamId() {
		return samId;
	}

	public void setSamId(long samId) {
		this.samId = samId;
	}

	public long getBinSize() {
		return binSize;
	}

	public void setBinSize(long binSize) {
		this.binSize = binSize;
	}

	public long getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(long createdDate) {
		this.createdDate = createdDate;
	}

	public long getSamHistogramId() {
		return samHistogramId;
	}

	public void setSamHistogramId(long samHistogramId) {
		this.samHistogramId = samHistogramId;
	}

    public long getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(long maxCount) {
        this.maxCount = maxCount;
    }

    public HistogramBin[] getHistogramBins() {
        return histogramBins;
    }
    
    public void setHistogramBins(HistogramBin[] histogramBins) {
        this.histogramBins = histogramBins;
    }
}
