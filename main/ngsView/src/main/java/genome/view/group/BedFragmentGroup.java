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

import genome.data.Bed;
import genome.data.BedFragment;
import genome.view.element.BedFragmentElement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class BedFragmentGroup extends Group {

    private final Bed bed;
    
    private double scale;
    
    private List<BedFragmentElement> bedFragmentElementList = new CopyOnWriteArrayList<BedFragmentElement>();
    
    public BedFragmentGroup(Bed bed, double scale) {
        super();
        
        this.bed = bed;
        this.scale = scale;
        
        setup();
    }
    
    public void setup(BedFragment[] bedFragments) {
        bedFragmentElementList.clear();
        clear();
        
        for (BedFragment bf : bedFragments) {
            BedFragmentElement e = new BedFragmentElement(bf, scale);
            bedFragmentElementList.add(e);
            add(e);
        }
    }

    @Override
    public void update() {}

    public List<BedFragmentElement> getBedFragmentElementList() {
        return bedFragmentElementList;
    }

    public Bed getBed() {
        return bed;
    }
    
}
