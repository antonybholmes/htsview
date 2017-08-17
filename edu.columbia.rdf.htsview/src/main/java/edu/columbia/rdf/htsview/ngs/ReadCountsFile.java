/**
 * Copyright 2017 Antony Holmes
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package edu.columbia.rdf.htsview.ngs;

import java.util.List;
import java.util.Map;

import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.DefaultHashMap;

// TODO: Auto-generated Javadoc
/**
 * The Class ReadCountsFile.
 */
public abstract class ReadCountsFile extends CountAssembly {
	

	
	/**
	 * Bins a list of starts into buckets with each bucket representing
	 * a bin of size window. The array begins at the bin containing
	 * the start and ends at the bin containing the end. All reads falling
	 * between these two bins will be counted. Each array element is
	 * the count of the number of reads in the bin
	 *
	 * @param starts the starts
	 * @param start the start
	 * @param end the end
	 * @param window the bin size
	 * @return the counts
	 */
	protected static List<Integer> binCounts(final List<Integer> starts,
			int start,
			int end,
			int window) {
		int startBin = start / window;
		int endBin = end / window;
		int l = endBin - startBin + 1;

		//System.err.println(start + " " + end + " " + s + " " + e + " " + l);

		Map<Integer, Integer> map = DefaultHashMap.create(0);

		for (int rs : starts) {
			int sbin = rs / window - startBin;

			if (sbin >= 0 && sbin < l) {
				map.put(sbin, map.get(sbin) + 1);
			}
		}

		List<Integer> ret = Mathematics.intZeros(l);

		for (int bin : map.keySet()) {
			ret.set(bin, map.get(bin));
		}

		return ret;
	}
}
