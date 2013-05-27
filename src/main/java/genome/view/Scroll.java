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

    private double x = 0.0, y = 0.0;
    private double speed = 0.0;
    private double minX = Double.MIN_VALUE, maxX = Double.MAX_VALUE;
    private double minY = Double.MIN_VALUE, maxY = Double.MAX_VALUE;

    public Scroll(double scrollSpeedEps, double scrollPowerFactor, double scrollSpeedDampingFactor) {
        this.speedEps = scrollSpeedEps;
        this.powerFactor = scrollPowerFactor;
        this.speedDampingFactor = scrollSpeedDampingFactor;
    }

    public void update(double fps, double scale) {
        x += speed / fps / Math.pow(scale, powerFactor);

        if (x < minX) {
            x = minX;
            speed = 0;
        } else if (maxX < x) {
            x = maxX;
            speed = 0;
        }

        speed *= speedDampingFactor;
        if (Math.abs(speed) < speedEps) {
            speed = 0.0;
        }
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }


    public double getMinX() {
        return minX;
    }

    public void setMinX(double minX) {
        this.minX = minX;
    }

    public double getMaxX() {
        return maxX;
    }

    public void setMaxX(double maxX) {
        this.maxX = maxX;
    }

    public double getMinY() {
        return minY;
    }

    public void setMinY(double minY) {
        this.minY = minY;
    }

    public double getMaxY() {
        return maxY;
    }

    public void setMaxY(double maxY) {
        this.maxY = maxY;
    }
}
