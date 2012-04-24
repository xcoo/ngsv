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
