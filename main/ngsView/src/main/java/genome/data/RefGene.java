package genome.data;

import casmi.sql.Entity;
import casmi.sql.annotation.Fieldname;
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("refGene")
public class RefGene extends Entity{
    @PrimaryKey
    private long refGeneId;

    @Fieldname("bin")
    private int bin;
    
    @Fieldname("name")
    private String name;

    @Fieldname("chrId")
    private long chrId;

    @Fieldname("strand")
    private int strand;
    
    @Fieldname("txStart")
    private long txStart;
    
    @Fieldname("txEnd")
    private long txEnd;

    @Fieldname("cdsStart")
    private long cdsStart;
    
    @Fieldname("cdsEnd")
    private long cdsEnd;

    @Fieldname("exonCount")
    private long exonCount;
    
    @Fieldname("exonStarts")
    private String exonStarts;
    
    @Fieldname("exonEnds")
    private String exonEnds;

    @Fieldname("score")
    private long score;

    @Fieldname("geneName")
    private String geneName;
    
    @Fieldname("cdsStartStat")
    private int cdsStartStat;
    
    @Fieldname("cdsEndStat")
    private int cdsEndStat;

    @Fieldname("exonFrames")
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
