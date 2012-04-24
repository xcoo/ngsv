package genome.db;

public class Position {
    private long chrId;
    private long start;
    private long end;
    private String chrName;
    
    public Position(long chrId, long start, long end){
        this.chrId = chrId;
        this.start = start;
        this.end = end;
    }
  
    public Position(long chrId, long start, long end, String chrName){
        this.chrId = chrId;
        this.start = start;
        this.end = end;
        this.chrName = chrName;
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
    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }
    
}
