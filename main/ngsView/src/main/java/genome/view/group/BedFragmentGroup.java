package genome.view.group;

import genome.data.Bed;
import genome.data.BedFragment;
import genome.view.element.BedFragmentElement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class BedFragmentGroup extends Group {

    private final Bed bed;
    
    private double scale;
    
    private List<BedFragmentElement> bedFragmentElementList = new CopyOnWriteArrayList<BedFragmentElement>();
    
    public BedFragmentGroup(Bed bed, double scale) {
        super();
        
        this.bed = bed;
        this.scale = scale;
        
        setup();
    }
    
    public void setup(BedFragment[] bedFragments) {
        bedFragmentElementList.clear();
        clear();
        
        for (BedFragment bf : bedFragments) {
            BedFragmentElement e = new BedFragmentElement(bf, scale);
            bedFragmentElementList.add(e);
            add(e);
        }
    }

    @Override
    public void update() {}

    public List<BedFragmentElement> getBedFragmentElementList() {
        return bedFragmentElementList;
    }

    public Bed getBed() {
        return bed;
    }
    
}
