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

package genome.db;

import genome.config.Config;
import genome.data.Bed;
import genome.data.BedFragment;
import genome.data.Chromosome;
import genome.data.CytoBand;
import genome.data.HistogramBin;
import genome.data.RefGene;
import genome.data.Sam;
import genome.data.SamHistogram;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.sql.MySQL;
import casmi.sql.Query;

public class SQLLoader {

    static Logger logger = LoggerFactory.getLogger(SQLLoader.class);
    
    MySQL mysql = null;

    public SQLLoader() throws SQLException {
        // create instance
        Config config = Config.getInstance();
        mysql = new MySQL(config.getHost(), 
                config.getDatabase(),
                config.getUser(),
                config.getPassword());

        // connect database
        mysql.connect();
    }

    public void close() {
        if (mysql != null)
            mysql.close();
    }

    public Sam[] loadSamFiles() {
        try {
            return mysql.all(Sam.class);
        } catch (SQLException e) {
            logger.error("Failed to load sam.");
            logger.error(e.getMessage());
            e.printStackTrace();
        } 

        return null;
    }

    public Chromosome[] loadChromosome() {
        try {
            return mysql.all(Chromosome.class);
        } catch (SQLException e) {
            logger.error("Failed to load chromosome.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }


    public CytoBand[] loadCytoBand() {
        try {
            return mysql.all(CytoBand.class);
        } catch (SQLException e) {
            logger.error("Failed to load cytoband.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }

        return null;
    }

    public SamHistogram[] loadSamHistogram(long samId) {
        try {
            String whereStatement = "sam_id = " + samId;
            return mysql.all(SamHistogram.class, new Query().where(whereStatement));
        } catch (SQLException e) {
            logger.error("Failed to load samHistogram.");
            logger.error(e.getMessage());
            e.printStackTrace();
        } 

        return null;
    }

    public HistogramBin[] loadHistgramBin(long samHistogramId, Chromosome c, long start, long end) {
        HistogramBin[] hb = null;
        try {
            String whereStatement = 
                String.format(" sam_histogram_id=%d AND chr_id=%d AND position>=%d AND position<=%d",
                              samHistogramId, c.getChrId(), start, end);
            hb = mysql.all(HistogramBin.class, new Query().select("chr_id", "position", "value").where(whereStatement));
        } catch (SQLException e) {
            logger.error("Failed to load histogramBin");
            logger.error(e.getMessage());
            e.printStackTrace();
        } 
        return hb;
    }

    public long getMaxHistogram(long samHistogramId) {
        try {
            Query query = new Query();
            query.select("value");
            query.where("sam_histogram_id=" + samHistogramId);
            query.order("value");
            query.desc(true);

            HistogramBin hb = mysql.first(HistogramBin.class, query);

            if (hb == null) return 0;

            return hb.getValue();
        } catch (SQLException e) {
            logger.error("Failed to get the max value of histogram.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public long getMaxHistogram(long samHistogramId, long chrId) {
        try {
            Query query = new Query();
            query.select("value");
            query.where("sam_histogram_id=" + samHistogramId + " AND chr_id=" + chrId);
            query.order("value");
            query.desc(true);
            HistogramBin hb = mysql.first(HistogramBin.class, query);
            return hb.getValue();
        } catch (SQLException e) {
            logger.error("Failed to get the max value of histogram.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return 0;
    }

    public String getChromosomeName(long chrId) {
        try {
            Query query = new Query();
            query.select("chromosome");
            query.where(" chr_id=" + chrId);
            Chromosome c = mysql.first(Chromosome.class, query);
            return c.getChromosome();
        } catch (SQLException e) {
            logger.error("Failed to get a name of the chromosome.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public Position getGenePosition(String geneName) {
        try {
            Query query = new Query();
            query.select("chr_id", "tx_start", "tx_end");
            query.where(" gene_name = '" + geneName + "'");
            RefGene refGene = mysql.first(RefGene.class, query);
            return new Position(refGene.getChrId(), refGene.getTxStart(), refGene.getTxEnd(), getChromosomeName(refGene.getChrId()));
        } catch (SQLException e) {
            logger.error("Failed to get a position of the gene.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;  
    }

    public GeneLoaderResult loadGene(Chromosome c, long start, long end) {
        RefGene[] refGene = null;
        GeneLoaderResult geneLoaderResult = null;
        
        try {
            Query query = new Query();
            query.select("ref_gene_id", "name", "gene_name", "chr_id", "strand", "tx_start", "tx_end", "exon_count", "exon_starts", "exon_ends");
            query.where(
                String.format(" chr_id='%d' AND tx_end>='%d' AND tx_start<='%d'",
                              c.getChrId(), start, end));
            refGene = mysql.all(RefGene.class, query);
            geneLoaderResult = GeneLoader.getGenes(refGene);
        } catch (SQLException e) {
            logger.error("Failed to get a position of the gene.");
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        
        if (geneLoaderResult == null) {
            return new GeneLoaderResult(null, null);
        }
        
        return geneLoaderResult;
    }


    public Bed[] loadBedFiles() {
        try {
            return mysql.all(Bed.class, new Query().select("bed_id", "file_name"));
        } catch (SQLException e) {
            logger.error("Failed to load bed.");
            e.printStackTrace();
        } 

        return null; 
    }

    public BedFragment[] loadBedFragment(long bedId, Chromosome c) {
        BedFragment[] bf = null;

        try {
            String whereStatement = " bed_id = " + bedId + " and chr_id = " + c.getChrId();
            bf = mysql.all(BedFragment.class, new Query().select("chr_id", "chr_start", "chr_end", "name").where(whereStatement));			
        } catch (SQLException e) {
            logger.error("Failed to load bedFragment.");
            e.printStackTrace();
        } 

        if (bf == null) return new BedFragment[0];

        return bf;
    }

    public BedFragment[] loadBedFragment(long bedId, Chromosome c, long start, long end) {		
        BedFragment[] bfs = null;
        
        try {
            String whereStatement = "bed_id=" + bedId + " and chr_id=" + c.getChrId() + " and chr_end >= " + start + " and chr_start <= " + end;
            bfs = mysql.all(BedFragment.class, new Query().select("chr_id", "chr_start", "chr_end", "name").where(whereStatement));
        } catch (SQLException e) {
            logger.error("Failed to load bedFragment.");
            e.printStackTrace();
        }
        
        return bfs;
    }
    
    public static Map<String, Long> loadCytoBandLength(CytoBand[] cytoBands, Chromosome[] chromosomes) {
        Map<String, Long> result = new HashMap<String, Long>();

        for( CytoBand b : cytoBands ) {
            String chrName = findChromosome(b.getChrId(), chromosomes);
            b.setChrName(chrName);
            Long curr = result.get(b.getChrId());

            if( curr != null ) {
                if(curr < b.getEnd()) {
                    result.put(chrName, b.getEnd());
                }
            } else {
                result.put(chrName, b.getEnd());
            }
        }

        return result;
    }

    private static String findChromosome(long chrId, Chromosome[] chromosomes) {
        for (Chromosome c : chromosomes) {
            if (chrId == c.getChrId()) {
                return c.getChromosome();
            }
        }
        return null;
    }

}
