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

package genome.view.group;

import genome.data.Sam;
import genome.data.ShortRead;
import genome.view.element.ShortReadElement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import casmi.graphics.color.Color;
import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class ShortReadGroup extends Group {

    private final List<ShortReadElement> shortReadElementList = new CopyOnWriteArrayList<ShortReadElement>();
    
    private final Sam sam;
    
    private double scale;
    
    public ShortReadGroup(Sam sam, double scale) {
        super();
        
        this.sam = sam;
        this.scale = scale;
        
        setup();
    }
    
    public void setup(ShortRead[] shortReads) {
        shortReadElementList.clear();
        clear();
        
        for (ShortRead sr : shortReads) {
            ShortReadElement e = new ShortReadElement(sr, scale);
            
            shortReadElementList.add(e);
            add(e);
        }
    }

    @Override
    public void update() {}

    public void setColor(Color color) {
        for (ShortReadElement e : shortReadElementList) {
            e.setFillColor(color);
        }
    }
    
    public List<ShortReadElement> getShortReadElementList() {
        return shortReadElementList;
    }
    
    public Sam getSam() {
        return sam;
    }
}
