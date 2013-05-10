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
