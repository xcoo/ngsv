/*
 *   ngsv
 *   http://github.com/xcoo/ngsv
 *   Copyright (C) 2013, Xcoo, Inc.
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

/**
 * CNV O/R mapping class.
 *
 * @author T. Takeuchi
 */
@Tablename("cnv_fragment")
public class CnvFragment extends Entity {

    @PrimaryKey
    @Fieldname("cnv_fragment_id")
    private long cnvFragmentId;

    @Fieldname("cnv_id")
    private long cnvId;

    @Fieldname("chr_id")
    private long chrId;

    @Fieldname("chr_start")
    private long chrStart;

    @Fieldname("chr_end")
    private long chrEnd;

    private String lengths;

    private String state;

    @Fieldname("copy_number")
    private long copyNumber;

    @Fieldname("num_snp")
    private long numSnp;

    @Fieldname("snp_start")
    private String snpStart;

    @Fieldname("snp_end")
    private String snpEnd;

    public long getCnvFragmentId() {
        return cnvFragmentId;
    }

    public void setCnvFragmentId(long cnvFragmentId) {
        this.cnvFragmentId = cnvFragmentId;
    }

    public long getCnvId() {
        return cnvId;
    }

    public void setCnvId(long cnvId) {
        this.cnvId = cnvId;
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

    public String getLengths() {
        return lengths;
    }

    public void setLengths(String lengths) {
        this.lengths = lengths;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public long getCopyNumber() {
        return copyNumber;
    }

    public void setCopyNumber(long copyNumber) {
        this.copyNumber = copyNumber;
    }

    public long getNumSnp() {
        return numSnp;
    }

    public void setNumSnp(long numSnp) {
        this.numSnp = numSnp;
    }

    public String getSnpStart() {
        return snpStart;
    }

    public void setSnpStart(String snpStart) {
        this.snpStart = snpStart;
    }

    public String getSnpEnd() {
        return snpEnd;
    }

    public void setSnpEnd(String snpEnd) {
        this.snpEnd = snpEnd;
    }
}
