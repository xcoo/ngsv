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

package genome.view.ui;

import java.util.Timer;
import java.util.TimerTask;

import casmi.graphics.color.GrayColor;
import casmi.graphics.element.RoundRect;
import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public final class Indicator extends Group {

    private RoundRect[] roundRects = new RoundRect[12];

    private int[]   highlight = {-1, -1, -1};
    private boolean isStarting = false;

    private Timer timer;

    public Indicator(double x, double y) {
        super();

        this.x = x;
        this.y = y;

        setup();
        setVisible(false);
    }

    @Override
    public void setup() {
        for (int i = 0; i < roundRects.length; i++) {
            RoundRect roundRect = new RoundRect(1, 12, 3);
            roundRect.setStrokeColor(new GrayColor(0.25));
            roundRect.setX(14.0 * Math.cos(Math.toRadians((90.0 - i * 30.0))));
            roundRect.setY(14.0 * Math.sin(Math.toRadians((90.0 - i * 30.0))));
            roundRect.setRotation(90.0 - i * 30.0);

            roundRects[i] = roundRect;

            add(roundRect);
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {

            @Override
            public void run() {
                if (11 < highlight[0]++) highlight[0] = 0;
                if (11 < highlight[1]++) highlight[1] = 0;
                if (11 < highlight[2]++) highlight[2] = 0;
            }
        }, 0, 90);
    }

    @Override
    public void update() {
        if (!isStarting) return;

        for (int i = 0; i < roundRects.length; i++) {
            RoundRect roundRect = roundRects[i];

            if (i == highlight[0]) {
                roundRect.setFillColor(new GrayColor(1.0));
            } else if (i == highlight[1]) {
                roundRect.setFillColor(new GrayColor(0.8));
            } else if (i == highlight[2]) {
                roundRect.setFillColor(new GrayColor(0.6));
            } else {
                roundRect.setFillColor(new GrayColor(0.4));
            }
        }
    }

    public final void start() {
        highlight[0] =  0;
        highlight[1] = -1;
        highlight[2] = -1;

        setVisible(true);

        isStarting = true;
    }

    public final void stop() {
        setVisible(false);
        isStarting = false;
    }

}
