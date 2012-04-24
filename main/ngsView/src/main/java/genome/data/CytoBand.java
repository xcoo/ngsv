package genome.data;

import casmi.sql.Entity;
import casmi.sql.annotation.Fieldname;
import casmi.sql.annotation.Ignore;
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("cytoBand")
public class CytoBand extends Entity {
    @PrimaryKey
    private long cytobandId;

    @Fieldname("chrId")
    private long chrId;
    
    @Fieldname("chrStart")
    private long start;

    @Fieldname("chrEnd")
    private long end;

    @Fieldname("name")
    private String name;

    @Fieldname("gieStain")
    private String color;
    
    @Ignore
    private String chrName;
    

    public long getCytobandId() {
        return cytobandId;
    }

    public void setCytobandId(long cytobandId) {
        this.cytobandId = cytobandId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String gieStain) {
        this.color = gieStain;
    }

    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }
    
    public long getLength() {
        return Math.abs(this.start - this.end);
    }
    

}
