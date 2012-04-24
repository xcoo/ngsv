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

import casmi.sql.Entity;
import casmi.sql.annotation.Fieldname;
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

/**
 * Chromosome entity class.
 *  
 * @author T. Takeuchi
 */
@Tablename("chromosome")
public class Chromosome extends Entity {

    @PrimaryKey
    @Fieldname("chrId")
    private long chrId;
    
    @Fieldname("chromosome")
    private String chromosome;
    
    public long getChrId() {
        return chrId;
    }
    
    public void setChrId(long chrId) {
        this.chrId = chrId;
    }
    
    public String getChromosome() {
        return chromosome;
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }
}
