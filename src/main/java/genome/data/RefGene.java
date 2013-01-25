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
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("ref_gene")
public class RefGene extends Entity {
    
    @PrimaryKey
    @Fieldname("ref_gene_id")
    private long refGeneId;

    @Fieldname("bin")
    private int bin;
    
    @Fieldname("name")
    private String name;

    @Fieldname("chr_id")
    private long chrId;

    @Fieldname("strand")
    private int strand;
    
    @Fieldname("tx_start")
    private long txStart;
    
    @Fieldname("tx_end")
    private long txEnd;

    @Fieldname("cds_start")
    private long cdsStart;
    
    @Fieldname("cds_end")
    private long cdsEnd;

    @Fieldname("exon_count")
    private long exonCount;
    
    @Fieldname("exon_starts")
    private String exonStarts;
    
    @Fieldname("exon_ends")
    private String exonEnds;

    @Fieldname("score")
    private long score;

    @Fieldname("gene_name")
    private String geneName;
    
    @Fieldname("cds_start_stat")
    private int cdsStartStat;
    
    @Fieldname("cds_end_stat")
    private int cdsEndStat;

    @Fieldname("exon_frames")
    private String exonFrames;

    public long getRefGeneId() {
        return refGeneId;
    }

    public void setRefGeneId(long refGeneId) {
        this.refGeneId = refGeneId;
    }

    public int getBin() {
        return bin;
    }

    public void setBin(int bin) {
        this.bin = bin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getChrId() {
        return chrId;
    }

    public void setChrId(long chrId) {
        this.chrId = chrId;
    }

    public int getStrand() {
        return strand;
    }

    public void setStrand(int strand) {
        this.strand = strand;
    }

    public long getTxStart() {
        return txStart;
    }

    public void setTxStart(long txStart) {
        this.txStart = txStart;
    }

    public long getTxEnd() {
        return txEnd;
    }

    public void setTxEnd(long txEnd) {
        this.txEnd = txEnd;
    }

    public long getCdsStart() {
        return cdsStart;
    }

    public void setCdsStart(long cdsStart) {
        this.cdsStart = cdsStart;
    }

    public long getCdsEnd() {
        return cdsEnd;
    }

    public void setCdsEnd(long cdsEnd) {
        this.cdsEnd = cdsEnd;
    }

    public long getExonCount() {
        return exonCount;
    }

    public void setExonCount(long exonCount) {
        this.exonCount = exonCount;
    }

    public String getExonStarts() {
        return exonStarts;
    }

    public void setExonStarts(String exonStarts) {
        this.exonStarts = exonStarts;
    }

    public String getExonEnds() {
        return exonEnds;
    }

    public void setExonEnds(String exonEnds) {
        this.exonEnds = exonEnds;
    }

    public long getScore() {
        return score;
    }

    public void setScore(long score) {
        this.score = score;
    }

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public int getCdsStartStat() {
        return cdsStartStat;
    }

    public void setCdsStartStat(int cdsStartStat) {
        this.cdsStartStat = cdsStartStat;
    }

    public int getCdsEndStat() {
        return cdsEndStat;
    }

    public void setCdsEndStat(int cdsEndStat) {
        this.cdsEndStat = cdsEndStat;
    }

    public String getExonFrames() {
        return exonFrames;
    }

    public void setExonFrames(String exonFrames) {
        this.exonFrames = exonFrames;
    }
}
