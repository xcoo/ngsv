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

import org.ini4j.Profile.Section;
import org.ini4j.Wini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Configuration manager class for viewer.ini.
 *
 * @author T. Takeuchi
 */
public class ViewerConfig {

    static Logger logger = LoggerFactory.getLogger(ViewerConfig.class);

    private class Default {
        static final double FPS = 30.0;
        static final int WINDOW_WIDTH = 1024;
        static final int WINDOW_HEIGHT = 768;
        static final boolean FULLSCREEN = false;
        static final double INITIAL_SCALE = 1.0;
        static final double MIN_SCALE = 0.000004;
        static final double MAX_SCALE = 2.0;
        static final double SCALE_WHEEL_FACTOR = 0.007;
        static final double SCROLL_SPEED_EPS = 0.01;
        static final double SCROLL_MOUSE_SPEED_FACTOR = 15.0;
        static final double SCROLL_SPEED_DAMPING_FACTOR = 0.8;
        static final double SCROLL_POWER_FACTOR = 0.8;

        static final double CYTOBAND_POS_Y = 100.0;
        static final double CYTOBAND_HEIGHT = 20.0;

        static final double RULER_POS_Y = 200.0;

        static final double BED_POS_Y = 700.0;
        static final String BED_FILL_COLOR = "#AB274A";

        static final double HISTOGRAM_POS_Y = 500.0;
        static final String HISTOGRAM_FILL_COLOR = "#1C618C";
        static final String HISTOGRAM_STROKE_COLOR = "#2E78A0";
    }

    private static ViewerConfig instance = new ViewerConfig();

    private String path;
    private Wini ini;

    private ViewerConfig() {}

    public static ViewerConfig getInstance() {
        return instance;
    }

    public void load(String path) {
        this.path = path;
        try {
            ini = new Wini(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            logger.error(e.getMessage());
        }
    }

    public void reload() {
        load(path);
    }

    private <T> T get(String section, String key, T defaultValue) {
        if (ini != null) {
            Section sec = ini.get(section);
            @SuppressWarnings("unchecked")
            Class<T> cls = (Class<T>)defaultValue.getClass();
            return sec.containsKey(key) ? sec.get(key, cls) : defaultValue;
        } else {
            return defaultValue;
        }
    }

    public double getFPS() {
        return get("general", "fps", Default.FPS);
    }

    public int getWindowWidth() {
        return get("general", "window.width", Default.WINDOW_WIDTH);
    }

    public int getWindowHeight() {
        return get("general", "window.height", Default.WINDOW_HEIGHT);
    }

    public boolean isFullScreen() {
        return get("general", "window.fullscreen", Default.FULLSCREEN);
    }

    public double getInitialScale() {
        return get("general", "scale.init", Default.INITIAL_SCALE);
    }

    public double getMinScale() {
        return get("general", "scale.min", Default.MIN_SCALE);
    }

    public double getMaxScale() {
        return get("general", "scale.max", Default.MAX_SCALE);
    }

    public double getWheelScaleFactor() {
        return get("general", "scale.wheel_factor", Default.SCALE_WHEEL_FACTOR);
    }

    public double getScrollSpeedEps() {
        return get("general", "scroll.speed_eps", Default.SCROLL_SPEED_EPS);
    }

    public double getMouseScrollSpeedFactor() {
        return get("general", "scroll.mouse_speed_factor", Default.SCROLL_MOUSE_SPEED_FACTOR);
    }

    public double getScrollSpeedDampingFactor() {
        return get("general", "scroll.speed_damping_factor", Default.SCROLL_SPEED_DAMPING_FACTOR);
    }

    public double getScrollPowerFactor() {
        return get("general", "scroll.power_factor", Default.SCROLL_POWER_FACTOR);
    }

    public double getCytobandPosY() {
        return get("cytoband", "pos_y", Default.CYTOBAND_POS_Y);
    }

    public double getCytobandHeight() {
        return get("cytoband", "height", Default.CYTOBAND_HEIGHT);
    }

    public double getRulerPosY() {
        return get("ruler", "pos_y", Default.RULER_POS_Y);
    }

    public double getBedPosY() {
        return get("bed", "pos_y", Default.BED_POS_Y);
    }

    public String getBedFillColor() {
        return get("bed", "color.fill", Default.BED_FILL_COLOR);
    }

    public double getHistogramPosY() {
        return get("histogram", "pos_y", Default.HISTOGRAM_POS_Y);
    }

    public String getHistogramFillColor() {
        return get("histogram", "color.fill", Default.HISTOGRAM_FILL_COLOR);
    }

    public String getHistogramStrokeColor() {
        return get("histogram", "color.stroke", Default.HISTOGRAM_STROKE_COLOR);
    }
}
