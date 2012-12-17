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

import genome.data.HistogramBin;
import genome.data.Sam;
import genome.view.element.HistogramBinElement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import casmi.graphics.group.Group;

/**
 * 
 * @author T. Takeuchi
 *
 */
public class HistogramBinGroup extends Group {

    static Logger logger = LoggerFactory.getLogger(HistogramBinGroup.class); 
    
    private final List<HistogramBinElement> histogramBinElementList = new CopyOnWriteArrayList<HistogramBinElement>();
    
    private final Sam sam;
    
    private double scale;
    
    public HistogramBinGroup(Sam sam, double scale) {
        super();
        
        this.sam = sam;
        this.scale = scale;
    }
    
    public void setup(HistogramBin[] histogramBins, long binSize, long dispBinSize, long maxValue) {
        histogramBinElementList.clear();        

        if (binSize == dispBinSize) {        
            for (HistogramBin hb : histogramBins) {                
                if (hb.getValue() == 0) continue;
                HistogramBinElement e = new HistogramBinElement(hb, scale, binSize, maxValue);
                histogramBinElementList.add(e);
            }
        } else {
            long sum = 0;
            long startPos = histogramBins[0].getPosition() - histogramBins[0].getPosition() % dispBinSize;
            long pos = startPos;
            for (HistogramBin hb : histogramBins) {
                if (hb.getPosition() >= pos + dispBinSize) {
                    if (sum != 0) {
                        HistogramBinElement e = new HistogramBinElement(sum, pos, scale, dispBinSize, maxValue);
                        histogramBinElementList.add(e);
                    }
                    sum = 0;
                    pos = hb.getPosition() / dispBinSize * dispBinSize;            
                }                               

                sum += hb.getValue();
            }
        }        
        
        clear();
        addAll(histogramBinElementList);
    }

    @Override
    public void update() {}

    public List<HistogramBinElement> getHistogramBinElementList() {
        return histogramBinElementList;
    }

    public Sam getSam() {
        return sam;
    }
    
    
}
