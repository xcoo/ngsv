/*
 *   ngsv
 *   http://github.com/xcoo/ngsv
 *   Copyright (C) 2013, Xcoo, Inc.
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

import genome.data.Cnv;
import genome.view.element.CnvElement;

import java.util.ArrayList;
import java.util.List;

import casmi.graphics.group.Group;
import casmi.util.GraphicsUtil;

/**
 * CnvGroup.
 * 
 * @author T. Takeuchi
 */
public class CnvGroup extends Group {    
    
    private double scale;
    
    private List<CnvElement> cnvElementList = new ArrayList<CnvElement>();
    
    public CnvGroup(double scale) {
        super();        
        this.scale = scale;        
    }
    
    public void setup(Cnv[] cnvs) {
        List<CnvElement> list = new ArrayList<CnvElement>();        
        for (Cnv cnv : cnvs) {
            CnvElement e = new CnvElement(cnv.getCopyNumber(),
                cnv.getChrStart(), cnv.getChrEnd(), scale);
            list.add(e);
        }
        addAll(list);
        GraphicsUtil.removeAll(cnvElementList);
        cnvElementList = list;
    }
    
    @Override
    public void update() {}    
    
    public double getScale() {
        return scale;
    }
    
    public List<CnvElement> getCnvElementList() {
        return cnvElementList;
    }    
}
