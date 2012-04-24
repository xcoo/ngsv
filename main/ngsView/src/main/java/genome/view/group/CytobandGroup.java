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

import genome.data.CytoBand;
import genome.view.element.CytobandElement;

import java.util.ArrayList;
import java.util.List;

import casmi.graphics.group.Group;

/**
 * @author T. Takeuchi
 */
public class CytobandGroup extends Group {

    private final CytoBand[] cytobands;

    private final String chr;
    
    private double height;
    
    private double scale;

    private final List<CytobandElement> cytobandElementList = new ArrayList<CytobandElement>();

    public CytobandGroup(CytoBand[] cytobands, String chr, double height, double scale) {
        super();
        
        this.cytobands = cytobands;
        this.chr       = chr;
        this.height    = height;
        this.scale     = scale;
        
        setup();
    }

    @Override
    public void setup() {
        for (CytoBand c : cytobands) {
            if (c.getChrName().equalsIgnoreCase(chr) || c.getChrName().equalsIgnoreCase("chr" + chr)) {
                CytobandElement e = new CytobandElement(c, scale, height);
                cytobandElementList.add(e);
                add(e);
            }
        }
    }

    @Override
    public void update() {}
    
    public List<CytobandElement> getCytobandElementList() {
        return cytobandElementList;
    }

}
