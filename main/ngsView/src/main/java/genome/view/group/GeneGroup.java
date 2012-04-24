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

package genome.view.group;

import genome.data.Exon;
import genome.data.Gene;
import genome.view.element.ExonElement;
import genome.view.element.GeneElement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import casmi.graphics.group.Group;


/**
 * @author T. Takeuchi
 *
 */
public class GeneGroup extends Group {

    private double scale;
    
    private final List<GeneElement> geneElementList = new CopyOnWriteArrayList<GeneElement>();
    private final List<ExonElement> exonElementList = new CopyOnWriteArrayList<ExonElement>();
    
    public GeneGroup(double scale) {
        super();
        
        this.scale = scale;
    }
    
    public void setup(List<Gene> geneList, List<Exon> exonList) {
        geneElementList.clear();
        exonElementList.clear();
        clear();

        for (Exon exon : exonList) {
            ExonElement e = new ExonElement(exon, scale);
            exonElementList.add(e);
            add(e);
        }

        for (Gene gene : geneList) {
            final GeneElement e = new GeneElement(gene, scale);
            geneElementList.add(e);
            add(e);
        }
    }

    @Override
    public void update() {}

    public List<GeneElement> getGeneElementList() {
        return geneElementList;
    }
    
    public List<ExonElement> getExonElementList() {
        return exonElementList;
    }

}
