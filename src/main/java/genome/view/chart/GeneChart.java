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

package genome.view.chart;

import genome.data.Exon;
import genome.data.Gene;
import genome.view.element.ExonElement;
import genome.view.element.GeneElement;

import java.util.ArrayList;
import java.util.List;

import casmi.Mouse;
import casmi.util.GraphicsUtil;

/**
 * @author T. Takeuchi
 */
public class GeneChart extends Chart {

    private double scale;

    private List<GeneElement> geneElementList = new ArrayList<GeneElement>();
    private List<ExonElement> exonElementList = new ArrayList<ExonElement>();

    public GeneChart(double scale, Mouse mouse) {
        super("known gene", mouse, false);

        this.scale = scale;
    }

    public void setup(List<Gene> geneList, List<Exon> exonList) {
        List<GeneElement> geList = new ArrayList<GeneElement>();
        List<ExonElement> eeList = new ArrayList<ExonElement>();

        for (Gene gene : geneList) {
            final GeneElement e = new GeneElement(gene, scale);
            geList.add(e);
        }

        for (Exon exon : exonList) {
            ExonElement e = new ExonElement(exon, scale);
            eeList.add(e);
        }

        addAll(geList);
        GraphicsUtil.removeAll(geneElementList);
        geneElementList = geList;

        addAll(eeList);
        GraphicsUtil.removeAll(exonElementList);
        exonElementList = eeList;

    }

    public List<GeneElement> getGeneElementList() {
        return geneElementList;
    }

    public List<ExonElement> getExonElementList() {
        return exonElementList;
    }

}
