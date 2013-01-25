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

import genome.data.Exon;
import genome.data.Gene;

import java.util.List;

public class GeneLoaderResult {
	private List<Exon> exons;
	private List<Gene> genes;

	public GeneLoaderResult(List<Exon> exons, List<Gene> genes) {
		this.exons = exons;
		this.genes = genes;
	}

	public List<Exon> getExons() {
		return exons;
	}

	public List<Gene> getGenes() {
		return genes;
	}
}
