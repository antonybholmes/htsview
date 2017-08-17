package edu.columbia.rdf.htsview.test;
/**
 * Copyright 2016 Antony Holmes
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


import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.jebtk.core.io.BufferedTableWriter;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.junit.Test;

import edu.columbia.rdf.htsview.ext.abi.ABITrace;
import edu.columbia.rdf.htsview.ext.abi.ABIUtils;

public class ABITest {
	@Test
	public void abiTest() throws IOException {
		Path file = PathUtils.getPath("/ifs/home/cancer/Lab_RDF/Public/Antony/from_Stefanie/24h_sgEP300_2-Ep300_E2F.ab1");

		ABITrace trace = ABITrace.parse(file);
		
		char[] dna = ABIUtils.call(trace);
		
		
		Path dir = PathUtils.getPath("/ifs/home/cancer/Lab_RDF/Public/Antony/from_Stefanie/");
		
		List<Path> files = FileUtils.findAll(dir, "ab1");
		
		for (Path f : files) {
			Path fout = dir.resolve(PathUtils.getNameNoExt(f) + ".fasta");
			
			System.err.println(fout);
			
			trace = ABITrace.parse(f);
			
			List<StringBuilder> seqs = ABIUtils.topTwo(trace);
		
			ABIUtils.writeFasta(fout, seqs);

			//System.err.println(Arrays.toString(dna));
		
			//System.err.println(trace.getNumBases());
		
			//BufferedTableWriter out = 
			//		FileUtils.newBufferedTableWriter(PathUtils.getPath("/ifs/home/cancer/Lab_RDF/Public/Antony/from_Stefanie/test.txt"));
		}
		
		/*
		try {
			for (char base : ABITrace.BASES) {
				for (int i = 0; i < trace.getNumBases(); ++i) {
					if (i > 0) {
						out.sep();
					}

					out.write(Short.toString(trace.getColor(base, i)));
				}

				out.newLine();
			}
		} finally {
			out.close();
		}
		*/

	}
}
