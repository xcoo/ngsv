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

package genome.data.type;

public class SamFlag {
	
	private boolean samFlagPaired; //Signals paired-end read (irrespective of whether or not it is mapped
	private boolean samFlagProper; // Read is mapped as a proper pair
	private boolean samFlagNomap; // Query sequence itself was not mapped
	private boolean samFlagMateNomap; // The mate of the query sequence was not mapped
	private boolean samFlagStrand; // Strand of the query sequence (0 forward, 1 reverse)
	private boolean samFlagMateStarnd; // Strand of the mate (0 forward, 1 reverse)
	private boolean samFlag1stMate; // Read is first mate of a pair
	private boolean samFlag2ndMate; // Read is 2nd mate of a pair
	private boolean samFlagNotPrimary; // Alignment is not primary
	private boolean samFlagCheckFail; // Read failed platform/vendor quality checks
	private boolean samFlagDuplicate; // Read is an optical duplicate or a PCR duplicate

	public SamFlag(int flag) {
		samFlagPaired = false;
		samFlagProper = false;
		samFlagNomap = false;
		samFlagMateNomap = false;
		samFlagStrand = false;
		samFlagMateStarnd = false;
		samFlag1stMate = false;
		samFlag2ndMate = false;
		samFlagNotPrimary = false;
		samFlagCheckFail = false;
		samFlagDuplicate = false;

		init(flag);
	}

	private void init(int flag) {
//		int flagHex = Integer.parseInt(String.valueOf(flag), 16); // TODO ???
		
		this.samFlagPaired = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_PAIRED);
		this.samFlagProper = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_PROPER);
		this.samFlagNomap = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_NOMAP);
		this.samFlagMateNomap = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_MATENOMAP);
		this.samFlagStrand = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_STRAND);
		this.samFlagMateStarnd = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_MATESTRAND);
		this.samFlag1stMate = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_1stMATE);
		this.samFlag2ndMate = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_2ndMATE);
		this.samFlagNotPrimary = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_NOTPRIMARY);
		this.samFlagCheckFail = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_CHECKFAIL);
		this.samFlagDuplicate = SamFlagType.checkFlag(flag, SamFlagType.SAMFLAG_DUPLICATE);
	}

	public boolean isSamflagPaired() {
		return samFlagPaired;
	}

	public void setSamflagPaired(boolean samflagPaired) {
		this.samFlagPaired = samflagPaired;
	}

	public boolean isSamFlagProper() {
		return samFlagProper;
	}

	public void setSamFlagProper(boolean samFlagProper) {
		this.samFlagProper = samFlagProper;
	}

	public boolean isSamFlagNomap() {
		return samFlagNomap;
	}

	public void setSamFlagNomap(boolean samFlagNomap) {
		this.samFlagNomap = samFlagNomap;
	}

	public boolean isSamFlagMateNomap() {
		return samFlagMateNomap;
	}

	public void setSamFlagMateNomap(boolean samFlagMateNomap) {
		this.samFlagMateNomap = samFlagMateNomap;
	}

	public boolean isSamFlagStrand() {
		return samFlagStrand;
	}

	public void setSamFlagStrand(boolean samFlagStrand) {
		this.samFlagStrand = samFlagStrand;
	}

	public boolean isSamFlagMateStarnd() {
		return samFlagMateStarnd;
	}

	public void setSamFlagMateStarnd(boolean samFlagMateStarnd) {
		this.samFlagMateStarnd = samFlagMateStarnd;
	}

	public boolean isSamFlag1stMate() {
		return samFlag1stMate;
	}

	public void setSamFlag1stMate(boolean samFlag1stMate) {
		this.samFlag1stMate = samFlag1stMate;
	}

	public boolean isSamFlag2ndMate() {
		return samFlag2ndMate;
	}

	public void setSamFlag2ndMate(boolean samFlag2ndMate) {
		this.samFlag2ndMate = samFlag2ndMate;
	}

	public boolean isSamFlagNotPrimary() {
		return samFlagNotPrimary;
	}

	public void setSamFlagNotPrimary(boolean samFlagNotPrimary) {
		this.samFlagNotPrimary = samFlagNotPrimary;
	}

	public boolean isSamFlagCheckFail() {
		return samFlagCheckFail;
	}

	public void setSamFlagCheckFail(boolean samFlagCheckFail) {
		this.samFlagCheckFail = samFlagCheckFail;
	}

	public boolean isSamFlagDuplicate() {
		return samFlagDuplicate;
	}

	public void setSamFlagDuplicate(boolean samFlagDuplicate) {
		this.samFlagDuplicate = samFlagDuplicate;
	}
}
