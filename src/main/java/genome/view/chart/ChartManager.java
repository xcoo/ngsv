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

package genome.view.chart;

import java.util.ArrayList;
import java.util.List;

/**
 * @author T. Takeuchi
 */
public class ChartManager {

    private List<Chart> chartList = new ArrayList<Chart>();

    private Chart draggingChart;

    public void addChart(Chart chart) {
        chartList.add(chart);
    }

    public void setDraggingChart(Chart chart) {
        draggingChart = chart;
    }

    public void releaseDragging() {
        if (isDragging()) {
            draggingChart.releaseDragging();
            draggingChart = null;
        }
    }

    public boolean isDragging() {
        return draggingChart != null;
    }

    public boolean isDraggingChart(Chart chart) {
        return draggingChart == chart;
    }
}
