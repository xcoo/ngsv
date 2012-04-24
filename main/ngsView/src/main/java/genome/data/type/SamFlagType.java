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

/**
 * QueryFlag of samtools
 * <p>
 * http://samtools.sourceforge.net/samtools.shtml
 * 
 * @author K. Nishimura
 * 
 * @see genome.data
 */

public enum SamFlagType {
	SAMFLAG_PAIRED,     // Signals paired-end read (irrespective of whether or not it is mapped as a pair
	SAMFLAG_PROPER,     // Read is mapped as a proper pair
	SAMFLAG_NOMAP,      // Query sequence itself was not mapped
	SAMFLAG_MATENOMAP,  // The mate of the query sequence was not mapped
	SAMFLAG_STRAND,     // Strand of the query sequence (0 forward, 1 reverse)
	SAMFLAG_MATESTRAND, // Strand of the mate (0 forward, 1 reverse)
	SAMFLAG_1stMATE,    // Read is first mate of a pair
	SAMFLAG_2ndMATE,    // Read is 2nd mate of a pair
	SAMFLAG_NOTPRIMARY, // Alignment is not primary
	SAMFLAG_CHECKFAIL,  // Read failed platform/vendor quality checks
	SAMFLAG_DUPLICATE;  // Read is an optical duplicate or a PCR duplicate

	/**
	 * Returns Hex values from QueryFlag.
	 * 
	 * @param colorSet
	 *            The ColorSet.
	 * 
	 * @return
	 *            Array of RGB values from 0 to 255.
	 */
	private static final int value(SamFlagType queryFlag) {
		int ret = 0x0000;
		switch (queryFlag) {
		case SAMFLAG_PAIRED:
			ret = 0x0001;
			break;
		case SAMFLAG_PROPER:
			ret = 0x0002;
			break;
		case SAMFLAG_NOMAP:
			ret = 0x0004;
			break;
		case SAMFLAG_MATENOMAP:
			ret = 0x0008;
			break;
		case SAMFLAG_STRAND:
			ret = 0x0010;
			break;
		case SAMFLAG_MATESTRAND:
			ret = 0x0020;
			break;
		case SAMFLAG_1stMATE:
			ret = 0x0040;
			break;
		case SAMFLAG_2ndMATE:
			ret = 0x0080;
			break;
		case SAMFLAG_NOTPRIMARY:
			ret = 0x0100;
			break;
		case SAMFLAG_CHECKFAIL:
			ret = 0x0200;
			break;
		case SAMFLAG_DUPLICATE:
			ret = 0x0400;
			break;
		default:
			break;
		}        
		return ret;
	}
	
	public static boolean checkFlag(int flag, SamFlagType type) {
		return (flag & value(type)) > 0;
	}
}
