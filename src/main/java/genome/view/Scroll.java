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


/**
 * Manages scroll amount and inertial scroll behavior.
 *
 * @author T. Takeuchi
 */
public class Scroll {

    private final double speedEps;
    private final double powerFactor;
    private final double speedDampingFactor;

    private double[] position = {0.0, 0.0};
    private double[] speed = {0.0, 0.0};
    private double[] min = {-Double.MAX_VALUE, -Double.MAX_VALUE};
    private double[] max = {Double.MAX_VALUE, Double.MAX_VALUE};

    public Scroll(double scrollSpeedEps, double scrollPowerFactor, double scrollSpeedDampingFactor) {
        this.speedEps = scrollSpeedEps;
        this.powerFactor = scrollPowerFactor;
        this.speedDampingFactor = scrollSpeedDampingFactor;
    }

    public void update(double fps, double scale) {
        updateX(fps, scale);
        updateY(fps);
    }

    private void updateX(double fps, double scale) {
        position[0] += speed[0] / fps / Math.pow(scale, powerFactor);
        if (position[0] < min[0]) {
            position[0] = min[0];
            speed[0] = 0;
        } else if (max[0] < position[0]) {
            position[0] = max[0];
            speed[0] = 0;
        }

        speed[0] *= speedDampingFactor;
        if (Math.abs(speed[0]) < speedEps)
            speed[0] = 0.0;
    }

    private void updateY(double fps) {
        position[1] += speed[1] / fps;
        if (position[1] < min[1]) {
            position[1] = min[1];
            speed[1] = 0;
        } else if (max[1] < position[1]) {
            position[1] = max[1];
            speed[1] = 0;
        }

        speed[1] *= speedDampingFactor;
        if (Math.abs(speed[1]) < speedEps)
            speed[1] = 0.0;
    }

    public double getX() {
        return position[0];
    }

    public void setX(double x) {
        this.position[0] = x;
    }

    public double getY() {
        return position[1];
    }

    public void setY(double y) {
        this.position[1] = y;
    }

    public double getSpeedX() {
        return speed[0];
    }

    public void setSpeedX(double speedX) {
        this.speed[0] = speedX;

    }
    public double getSpeedY() {
        return speed[1];
    }

    public void setSpeedY(double speedY) {
        this.speed[1] = speedY;
    }

    public double getMinX() {
        return min[0];
    }

    public void setMinX(double minX) {
        this.min[0] = minX;
    }

    public double getMaxX() {
        return max[0];
    }

    public void setMaxX(double maxX) {
        this.max[0] = maxX;
    }

    public double getMinY() {
        return min[1];
    }

    public void setMinY(double minY) {
        this.min[1] = minY;
    }

    public double getMaxY() {
        return max[1];
    }

    public void setMaxY(double maxY) {
        this.max[1] = maxY;
    }
}
