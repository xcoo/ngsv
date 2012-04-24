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

package genome.db;

public class Position {
    private long chrId;
    private long start;
    private long end;
    private String chrName;
    
    public Position(long chrId, long start, long end){
        this.chrId = chrId;
        this.start = start;
        this.end = end;
    }
  
    public Position(long chrId, long start, long end, String chrName){
        this.chrId = chrId;
        this.start = start;
        this.end = end;
        this.chrName = chrName;
    }
    
    public long getChrId() {
        return chrId;
    }
    public void setChrId(long chrId) {
        this.chrId = chrId;
    }
    public long getStart() {
        return start;
    }
    public void setStart(long start) {
        this.start = start;
    }
    public long getEnd() {
        return end;
    }
    public void setEnd(long end) {
        this.end = end;
    }
    public String getChrName() {
        return chrName;
    }

    public void setChrName(String chrName) {
        this.chrName = chrName;
    }
    
}
