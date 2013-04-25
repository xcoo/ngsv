/*
 *   ngsv
 *   http://github.com/xcoo/ngsv
 *   Copyright (C) 2012-2013, Xcoo, Inc.
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

import casmi.Mouse;
import casmi.graphics.color.ColorSet;
import casmi.graphics.color.GrayColor;
import casmi.graphics.color.RGBColor;
import casmi.graphics.element.Element;
import casmi.graphics.element.MouseClickCallback;
import casmi.graphics.element.MouseOverCallback;
import casmi.graphics.element.Rect;
import casmi.graphics.element.Text;
import casmi.graphics.font.Font;
import casmi.graphics.font.FontStyle;
import casmi.graphics.group.Group;

/**
 * Parent class for each column of data chart.
 * Chart title and dragging action are defined here.
 *
 * @author T. Takeuchi
 */
public class ChartGroup extends Group {

    private static final Font TITLE_FONT;
    static {
        TITLE_FONT = new Font("Sans-Serif", FontStyle.PLAIN, 14.0);
    }

    protected String title;
    protected final Mouse mouse;
    private boolean draggable;

    protected Rect dragRect;
    protected Text titleText;

    public ChartGroup(String title, final Mouse mouse){
        this(title, mouse, true);
    }

    public ChartGroup(String title, final Mouse mouse, boolean draggable) {
        this.title = title;
        this.mouse = mouse;
        this.draggable = draggable;

        setupDragRect();
        setupTitleText();
    }

    private void setupDragRect() {
        dragRect = new Rect(50, 100);
        dragRect.setPosition(0.0, 0.0);
        dragRect.setStroke(false);
        dragRect.setFill(true);
        dragRect.setFillColor(new RGBColor(ColorSet.RED, 0.0));

        if (draggable) {
            dragRect.addMouseEventCallback(new MouseClickCallback() {

                @Override
                public void run(MouseClickTypes eventtype, Element element) {
                    if (eventtype == MouseClickTypes.DRAGGED) {
                        setY(getY() + mouse.getY() - mouse.getPrvY());
                    }
                }
            });

            dragRect.addMouseEventCallback(new MouseOverCallback() {

                @Override
                public void run(MouseOverTypes eventtype, Element element) {
                    if (eventtype == MouseOverTypes.EXISTED) {
                        dragRect.setFillColor(new RGBColor(ColorSet.RED, 0.5));
                    } else {
                        dragRect.setFillColor(new RGBColor(ColorSet.RED, 0.0));
                    }
                }
            });
        }

        add(dragRect);
    }

    private void setupTitleText() {
        titleText = new Text(title, TITLE_FONT, 5, 15);
        titleText.setStrokeColor(ColorSet.LIGHT_GRAY);
        add(titleText);
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (visible) {
            titleText.setStrokeColor(ColorSet.LIGHT_GRAY);
        } else {
            titleText.setStrokeColor(new GrayColor(0.2));
        }
    }

    @Override
    public void update() {}

}
