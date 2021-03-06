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

import casmi.Applet;
import casmi.graphics.color.ColorSet;
import casmi.graphics.element.Text;
import casmi.graphics.group.Group;

/**
 * Group for displaying dubug information.
 *
 * @author T. Takeuchi
 */
public class DebugView extends Group {

    private final Applet applet;
    private Text fpsText;

    public DebugView(Applet applet) {
        super();
        this.applet = applet;
        setup();
    }

    @Override
    public void setup() {
        fpsText = new Text("");
        fpsText.setStrokeColor(ColorSet.WHITE);
        add(fpsText);
    }

    @Override
    public void update() {
        fpsText.setText(String.format("%4.2f", applet.getWorkingFPS()));
    }
}
