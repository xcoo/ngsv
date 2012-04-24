package genome.view.group;

import genome.data.Sam;
import genome.data.ShortRead;
import genome.view.element.ShortReadElement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import casmi.graphics.color.Color;
import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class ShortReadGroup extends Group {

    private final List<ShortReadElement> shortReadElementList = new CopyOnWriteArrayList<ShortReadElement>();
    
    private final Sam sam;
    
    private double scale;
    
    public ShortReadGroup(Sam sam, double scale) {
        super();
        
        this.sam = sam;
        this.scale = scale;
        
        setup();
    }
    
    public void setup(ShortRead[] shortReads) {
        shortReadElementList.clear();
        clear();
        
        for (ShortRead sr : shortReads) {
            ShortReadElement e = new ShortReadElement(sr, scale);
            
            shortReadElementList.add(e);
            add(e);
        }
    }

    @Override
    public void update() {}

    public void setColor(Color color) {
        for (ShortReadElement e : shortReadElementList) {
            e.setFillColor(color);
        }
    }
    
    public List<ShortReadElement> getShortReadElementList() {
        return shortReadElementList;
    }
    
    public Sam getSam() {
        return sam;
    }
}
