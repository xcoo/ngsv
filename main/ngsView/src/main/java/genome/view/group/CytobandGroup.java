package genome.view.group;

import genome.data.CytoBand;
import genome.view.element.CytobandElement;

import java.util.ArrayList;
import java.util.List;

import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class CytobandGroup extends Group {

    private final CytoBand[] cytobands;

    private final String chr;
    
    private double height;
    
    private double scale;

    private final List<CytobandElement> cytobandElementList = new ArrayList<CytobandElement>();

    public CytobandGroup(CytoBand[] cytobands, String chr, double height, double scale) {
        super();
        
        this.cytobands = cytobands;
        this.chr       = chr;
        this.height    = height;
        this.scale     = scale;
        
        setup();
    }

    @Override
    public void setup() {
        for (CytoBand c : cytobands) {
            if (c.getChrName().equalsIgnoreCase(chr) || c.getChrName().equalsIgnoreCase("chr" + chr)) {
                CytobandElement e = new CytobandElement(c, scale, height);
                cytobandElementList.add(e);
                add(e);
            }
        }
    }

    @Override
    public void update() {}
    
    public List<CytobandElement> getCytobandElementList() {
        return cytobandElementList;
    }

}
