package genome.view;

import casmi.Mouse;
import casmi.graphics.element.Element;
import casmi.graphics.element.MouseOverCallback;
import casmi.graphics.element.Text;

/**
 * @author T. Takeuchi
 */
public class AnnotationMouseOverCallback implements MouseOverCallback {

    private String annotation;
    private Text   text;
    private Mouse  mouse;

    public AnnotationMouseOverCallback(String annotation, Text text, Mouse mouse) {
        
        this.annotation = annotation;
        this.text       = text;
        this.mouse      = mouse;
        
    }

    @Override
    public void run(MouseOverTypes eventtype, Element element) {

        switch (eventtype) {
        case ENTERED:
        default:
            this.text.setText(annotation);
            break;
        case EXITED:
            this.text.setText("");
            break;
        }
        
        this.text.setPosition(mouse.getX() + 5, mouse.getY() + 5, 0);
        
    }
}
