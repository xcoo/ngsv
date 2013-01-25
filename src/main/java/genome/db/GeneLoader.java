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

import genome.data.Exon;
import genome.data.Gene;
import genome.data.RefGene;
import genome.data.type.GeneType;
import genome.data.type.GeneOrientaion;

import java.util.ArrayList;
import java.util.List;

/**
 * class for Loading RefGene Data f
 * 
 * @author K. Nishimura
 *
 */
public class GeneLoader {

    /*
     * Read RefGene Data from cache
     */
    public static GeneLoaderResult getGenes(RefGene[] refGene) {
        List<Exon> exons = new ArrayList<Exon>();
        List<Gene> genes = new ArrayList<Gene>();
        List<Gene> tmpGenes = new ArrayList<Gene>();

        for(RefGene r: refGene){
            // add new gene
            genes.add(new Gene(r.getRefGeneId(), r.getChrId(), r.getTxStart(), r.getTxEnd(), convertGeneOrientation(r.getStrand()), GeneType.KNOWN, r.getName(), r.getGeneName()));
            String[] tmpExonStarts  = r.getExonStarts().replaceAll("\"", "").split(",", 0);
            String[] tmpExonEnds = r.getExonEnds().replaceAll("\"", "").split(",", 0);
            for(int i = 0; i<r.getExonCount(); i++){
                exons.add(new Exon(r.getRefGeneId(), r.getChrId(), Integer.valueOf(tmpExonStarts[i]), Integer.valueOf(tmpExonEnds[i]), convertGeneOrientation(r.getStrand()), GeneType.KNOWN, r.getName(), r.getGeneName()));
            }
        }

        for(Gene g: genes){

            int order = 0;

            switch( g.getOrientation() ) {
            case PLUS:
                order ++;
                break;
            case MINUS:
                order --;
                break;
            }

            for(Gene tg: tmpGenes) {

                if( g != tg && g.getOrientation() == tg.getOrientation() ) {

                    if( g.checkOverlap(tg) ) {
                        switch( g.getOrientation() ) {
                        case PLUS:
                            order ++;
                            break;
                        case MINUS:
                            order --;
                            break;
                        }
                    }   
                }
            }

            g.setOrder(order);

            tmpGenes.add(g);

            // setup order of exons
            for(Exon e: exons){
                if(e.getID() == g.getID()){
                    e.setOrder(order);
                }
            }
        }


        return new GeneLoaderResult(exons, genes);
    }


    private static GeneOrientaion convertGeneOrientation(int orientation) { 
        if( orientation == 0 ) {
            return GeneOrientaion.PLUS;
        } else if( orientation == 1) {
            return GeneOrientaion.MINUS;
        } else {
            return GeneOrientaion.UNKNOWN;
        }
    }
}