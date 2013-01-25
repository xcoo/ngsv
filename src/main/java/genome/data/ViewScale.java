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

package genome.data;

/**
 * Scale for sequence
 * 
 * @author K. Nishimura
 *
 */
public class ViewScale {
    private String chr;
    private long start;
    private long end;
    private long length;

    public ViewScale(String chr, long start, long end) {
        this.chr = chr;
        this.start = start;
        this.end = end;
        this.length = Math.abs(end - start);
    }

    public void setViewScale(String chr, long start, long end){
        this.chr = chr;
        this.start = start;
        this.end = end;
        this.length = Math.abs(end - start);  
    }

    public String getChr() {
        return chr;
    }

    public void setChr(String chr) {
        this.chr = chr;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long initStart) {
        this.start = initStart;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    public long getLength() {
        return length;
    }

    public void setLength(long length) {
        this.length = length;
    }
}
