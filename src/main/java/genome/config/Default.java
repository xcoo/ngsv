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

package genome.config;

import java.io.File;
import java.io.IOException;

import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration manager class for default.ini.
 *
 * @author T. Takeuchi
 */
public class Default {

    static Logger logger = LoggerFactory.getLogger(Default.class);

    private static Default instance = new Default();

    private Wini ini;

    private Default() {}

    public static Default getInstance() {
        return instance;
    }

    public void load(String path) {
        try {
            ini = new Wini(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public double getFPS() {
        return ini.get("general", "fps", double.class);
    }

    public int getWindowWidth() {
        return ini.get("general", "window_width", int.class);
    }

    public int getWindowHeight() {
        return ini.get("general", "window_height", int.class);
    }

    public boolean isFullScreen() {
        return ini.get("general", "fullscreen", boolean.class);
    }

    public double getInitialScale() {
        return ini.get("general", "initial_scale", double.class);
    }

    public double getMinScale() {
        return ini.get("general", "min_scale", double.class);
    }

    public double getMaxScale() {
        return ini.get("general", "max_scale", double.class);
    }

    public double getWheelScaleFactor() {
        return ini.get("general", "wheel_scale_factor", double.class);
    }

    public double getScrollSpeedEps() {
        return ini.get("general", "scroll_speed_eps", double.class);
    }

    public double getMouseScrollSpeedFactor() {
        return ini.get("general", "mouse_scroll_speed_factor", double.class);
    }

    public double getScrollSpeedDampingFactor() {
        return ini.get("general", "scroll_speed_damping_factor", double.class);
    }

    public double getScrollPowerFactor() {
        return ini.get("general", "scroll_power_factor", double.class);
    }

    public double getCytobandPosY() {
        return ini.get("cytoband", "pos_y", double.class);
    }

    public double getCytobandHeight() {
        return ini.get("cytoband", "height", double.class);
    }

    public double getRulerPosY() {
        return ini.get("ruler", "pos_y", double.class);
    }

    public double getBedPosY() {
        return ini.get("bed", "pos_y", double.class);
    }

    public double getHistogramPosY() {
        return ini.get("histogram", "pos_y", double.class);
    }
}
