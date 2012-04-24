package genome.view.group;

import genome.view.element.RulerElement;

import java.util.ArrayList;
import java.util.List;

import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Line;
import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class RulerGroup extends Group {

    private long start, end;
    
    private double scale;
    
    private final List<RulerElement> rulerElementList = new ArrayList<RulerElement>();
    
    private Line mainLine;
    private double mainLineBaseX;
    
    public RulerGroup(long start, long end, double scale) {
        super();
        
        this.start = start;
        this.end   = end;
        
        setup();
    }
    
    @Override
    public void setup() {
        long   length = end - start;
        int    digit  = (int)Math.log10(length) + 1;
        double step   = Math.pow(10, digit - 2);

        int numScales = (int)(length / step) + 1;

        for (int i = 0; i < numScales; i++) {
            double x = i * step + start;

            RulerElement e = new RulerElement(x, 0, scale);

            rulerElementList.add(e);
            
            add(e.getLine());
            add(e.getText());
        }

        mainLine = new Line(start, 0, end, 0);
        mainLine.setStrokeColor(ColorSet.WHITE);
        mainLineBaseX = start;
        add(mainLine);
    }

    @Override
    public void update() {}

    public List<RulerElement> getRulerElementList() {
        return rulerElementList;
    }
    
    public Line getMainLine() {
        return mainLine;
    }
    
    public double getMainLineBaseX() {
        return mainLineBaseX;
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

}
