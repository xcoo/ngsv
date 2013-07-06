/*
 *   ngsv
 *   https://github.com/xcoo/ngsv
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

package genome.view.ui;

import casmi.Mouse;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Circle;
import casmi.graphics.element.Element;
import casmi.graphics.element.Line;
import casmi.graphics.element.MouseClickCallback;
import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class ScaleController extends Group {

    private final double minValue;
    private final double maxValue;
    private final Mouse mouse;

    private double value;

    private Line axisLine;
    private Line topScaleLine, bottomScaleLine;
    private Circle valueCircle;

    private ScaleControllerCallback callback;

    public ScaleController(double minValue, double maxValue, double value, final Mouse mouse) {
        super();

        this.minValue = minValue;
        this.maxValue = maxValue;
        this.value = value;
        this.mouse = mouse;

        setup();
    }

    @Override
    public void setup() {
        axisLine = new Line(0, 100, 0, -100);
        axisLine.setStrokeColor(ColorSet.WHITE);
        add(axisLine);

        topScaleLine = new Line(-8, 100, 8, 100);
        topScaleLine.setStrokeColor(ColorSet.WHITE);
        add(topScaleLine);

        bottomScaleLine = new Line(-8, -100, 8, -100);
        bottomScaleLine.setStrokeColor(ColorSet.WHITE);
        add(bottomScaleLine);

        valueCircle = new Circle(10);
        valueCircle.setStrokeColor(ColorSet.WHITE);
        valueCircle.setFillColor(ColorSet.GRAY);

        valueCircle.addMouseEventCallback(new MouseClickCallback() {

            @Override
            public void run(MouseClickTypes eventtype, Element element) {
                if (eventtype == MouseClickTypes.DRAGGED) {
                    double newY = valueCircle.getY() + mouse.getY() - mouse.getPrvX();
                    if (newY > 100)
                        newY = 100;
                    else if (newY < -100)
                        newY = -100;
                    valueCircle.setY(newY);

                    value = yToValue(newY);

                    if (callback != null) {
                        callback.run(value);
                    }
                }
            }
        });

        add(valueCircle);
    }

    @Override
    public void update() {}

    public void addCallback(ScaleControllerCallback callback) {
        this.callback = callback;
    }

    private double valueToY(double value) {
        return -100 + 200 * value / (this.maxValue - this.minValue);
    }

    private double yToValue(double y) {
        return this.minValue + (this.maxValue - this.minValue) * (y + 100) / 200;
    }

    public double getMinValue() {
        return minValue;
    }

    public double getMaxValue() {
        return maxValue;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;

        double newY = valueToY(value);
        valueCircle.setY(newY);
    }
}
