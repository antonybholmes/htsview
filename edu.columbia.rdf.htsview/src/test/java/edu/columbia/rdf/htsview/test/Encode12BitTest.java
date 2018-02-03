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

import org.jebtk.core.io.PathUtils;
import org.junit.Test;

import edu.columbia.rdf.htsview.ngs.CountAssembly;
import edu.columbia.rdf.htsview.ngs.ReadCountsFile32Bit;

public class Encode12BitTest {
  @Test
  public void encodeTest() throws IOException {
    CountAssembly a = new ReadCountsFile32Bit(PathUtils.getPath(
        "/ifs/scratch/cancer/Lab_RDF/abh2138/ChIP_seq/data/samples/hg19/bradner/Bradner_HBL1_H3K27AC_BD015/reads_hg19"));

    System.err.println("12bit counts " + a.getCounts("hg19", "chr1:1-50000", 1000));
  }
}
