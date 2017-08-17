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
package edu.columbia.rdf.htsview.ext.abi;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.IterHashMap;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.io.FileUtils;

// TODO: Auto-generated Javadoc
/**
 * The Class ABIUtils.
 */
public class ABIUtils {
	
	/**
	 * Instantiates a new ABI utils.
	 */
	private ABIUtils() {
		// Do nothing
	}

	/**
	 * Creates a DNA sequence using the bases with the greatest color values.
	 *
	 * @param trace the trace
	 * @return the char[]
	 */
	public static char[] call(ABITrace trace) {
		char[] ret = new char[trace.getNumBases()];

		for (int i = 0; i < ret.length; ++i) {
			int max = Integer.MIN_VALUE;
			for (char base : ABITrace.BASES) {
				int v = trace.getColor(base, i);

				if (v > max) {
					ret[i] = base;
					max = v;
				}
			}
		}

		return ret;
	}

	/**
	 * Top two.
	 *
	 * @param trace the trace
	 * @return the list
	 */
	public static List<StringBuilder> topTwo(ABITrace trace) {
		char[] ret = new char[trace.getNumBases()];

		List<StringBuilder> seqs = new ArrayList<StringBuilder>();

		seqs.add(new StringBuilder());

		for (int i = 0; i < ret.length; ++i) {
			IterMap<Integer, Character> countMap =
					new IterHashMap<Integer, Character>();

			int c = 0;

			for (char base : ABITrace.BASES) {
				int v = trace.getColor(base, i);

				if (v > 100) {
					countMap.put(v, base);
					++c;
				}
			}

			c = Math.min(4, c);
			
			char base;
			
			switch(c) {
			case 0:
				// Nothing appropriate so add N
				for (StringBuilder seq : seqs) {
					seq.append("N");
				}

				break;
			case 1:
				base = countMap.values().iterator().next();
				
				for (StringBuilder seq : seqs) {
					// Add the only character in the array
					seq.append(base);
				}

				break;
			default:
				// More than one

				// Add a new buffer as a copy of the current up to this
				// point

				while (seqs.size() < c) {
					seqs.add(new StringBuilder(seqs.get(0)));
				}

				// We want the highest counts first
				List<Integer> counts = 
						CollectionUtils.reverse(CollectionUtils.sortKeys(countMap));
				
				for (int si = 0; si < seqs.size(); ++si) {
					// If the number of colors at this position is less than
					// the number of sequences already created, use the
					// lowest position closest to the desired index
					int ci = Math.min(si, counts.size() - 1);
					
					System.err.println(i + " " + counts.size() + " " + si + " " + ci + " " + counts.get(ci));
					
					seqs.get(si).append(countMap.get(counts.get(ci)));
				}
				
				/*
				// Sort by the top two
				for (int count : CollectionUtils.reverse(CollectionUtils.sortKeys(countMap))) {
					for (char b : countMap.get(count)) {
						if (bc == c) {
							break;
						}

						seqs.get(bc).append(b);

						++bc;
					}

					if (bc == c) {
						break;
					}
				}
				*/

			}
		}

		return seqs;
	}

	/**
	 * Write fasta.
	 *
	 * @param file the file
	 * @param seqs the seqs
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public static void writeFasta(Path file, List<StringBuilder> seqs) throws IOException {
		int c = 1;

		BufferedWriter out = FileUtils.newBufferedWriter(file);

		try {
			for (StringBuilder seq : seqs) {
				out.write(">seq_" + c);
				out.newLine();
				out.write(seq.toString());
				out.newLine();

				++c;
			}
		} finally {
			out.close();
		}
	}
}
