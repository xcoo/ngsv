/*
 *   ngs View
 *   http://casmi.github.com/
 *   Copyright (C) 2011-2012, Xcoo, Inc.
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

import genome.data.type.SamFlag;
import casmi.sql.Entity;
import casmi.sql.annotation.Fieldname;
import casmi.sql.annotation.Ignore;
import casmi.sql.annotation.PrimaryKey;
import casmi.sql.annotation.Tablename;

@Tablename("shortRead")
public class ShortRead extends Entity {
	
	@PrimaryKey
	private long shortReadId;

	@Fieldname("refStart")
	private long refStart;
	
	@Fieldname("refEnd")
	private long refEnd;
	
	@Fieldname("name")
	private String name;
	
	@Fieldname("chrId")
	private long chrId;
	
	@Fieldname("refLength")
	private long refLength;
	
	@Ignore
	private long queryStart;
	
	@Ignore
	private long queryEnd;
	
	@Ignore
	private long queryLength;
	
	@Ignore
	private long queryBin;
	
	@Fieldname("queryFlag")
	private int queryFlag;
	
	@Ignore
	private long queryTags;
	
	@Fieldname("sequence")
	private String sequence;

	@Fieldname("samId")
	private long samId;
	
	@Ignore
	private SamFlag samFlag;

	public long getShortReadId() {
		return shortReadId;
	}

	public void setShortReadId(long shortReadId) {
		this.shortReadId = shortReadId;
	}

	public long getRefStart() {
		return refStart;
	}

	public void setRefStart(long refStart) {
		this.refStart = refStart;
	}

	public long getRefEnd() {
		return refEnd;
	}

	public void setRefEnd(long refEnd) {
		this.refEnd = refEnd;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getChrId() {
		return chrId;
	}

	public void setChrId(long chr) {
		this.chrId = chr;
	}

	public long getRefLength() {
		return refLength;
	}

	public void setRefLength(long refLength) {
		this.refLength = refLength;
	}

	public long getQueryStart() {
		return queryStart;
	}

	public void setQueryStart(long queryStart) {
		this.queryStart = queryStart;
	}

	public long getQueryEnd() {
		return queryEnd;
	}

	public void setQueryEnd(long queryEnd) {
		this.queryEnd = queryEnd;
	}

	public long getQueryLength() {
		return queryLength;
	}

	public void setQueryLength(long queryLength) {
		this.queryLength = queryLength;
	}

	public long getQueryBin() {
		return queryBin;
	}

	public void setQueryBin(long queryBin) {
		this.queryBin = queryBin;
	}

	public int getQueryFlag() {
		return queryFlag;
	}

	public void setQueryFlag(int queryFlag) {
		this.queryFlag = queryFlag;
	}

	public long getQueryTags() {
		return queryTags;
	}

	public void setQueryTags(long queryTags) {
		this.queryTags = queryTags;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public long getSamId() {
		return samId;
	}

	public void setSamId(long samId) {
		this.samId = samId;
	}

	public SamFlag getSamFlag() {
		return samFlag;
	}

	public void setSamFlag(SamFlag samFlag) {
		this.samFlag = samFlag;
	}
}
