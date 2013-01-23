package genome.net;


public class Selection {
    
    public class JsonBam {    
        public String name;
        public long id;
    }
    
    public class JsonBed {
        public String name;
        public long id;
    }
    
    public class JsonChromosome {
        public String name;
        public long start;
        public long end;
    }
    
    public JsonBam[] bam;
    public JsonBed[] bed;
    public JsonChromosome chromosome;
    
    public boolean hasBam(long id) {
        for (JsonBam b : bam) {
            if (b.id == id)
                return true;
        }
        return false;
    }
    
    public boolean hasBed(long id) {
        for (JsonBed b : bed) {
            if (b.id == id)
                return true;
        }
        return false;
    }
    
    @Override
    public String toString() {        
        return String.format("chr: %s, %s, %s", chromosome.name, chromosome.start, chromosome.end);
    }
}

