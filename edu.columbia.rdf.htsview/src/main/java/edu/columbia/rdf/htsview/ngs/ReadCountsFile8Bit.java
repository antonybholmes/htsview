/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.htsview.ngs;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.dna.GenomeAssemblyDir;
import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.io.PathUtils;

// TODO: Auto-generated Javadoc
/**
 * Fast search of genome sequence files to get get actual genomic data. This
 * file reads 4bit encoded genomes (i.e. 2 bases per byte).
 *
 * @author Antony Holmes Holmes
 *
 */
public class ReadCountsFile8Bit extends ReadCountsFile {

  /**
   * The member directory.
   */
  protected Path mDirectory;

  /**
   * The member file map.
   */
  protected Map<Chromosome, Path> mFileMap = new HashMap<Chromosome, Path>();

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param directory the directory
   */
  public ReadCountsFile8Bit(Path directory) {
    mDirectory = directory;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getCounts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public List<Integer> getCounts(GenomicRegion region, int window)
      throws IOException {
    Chromosome chr = region.getChr();

    if (!mFileMap.containsKey(chr)) {
      mFileMap.put(chr,
          mDirectory.resolve(chr + ".counts.win." + window + ".8bit"));
    }

    return getCounts(mFileMap.get(chr),
        region.getStart(),
        region.getEnd(),
        window);
  }

  /**
   * Gets the counts.
   *
   * @param file the file
   * @param start the start
   * @param end the end
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static List<Integer> getCounts(final Path file,
      int start,
      int end,
      int window) throws IOException {

    int s = (start - 1) / window;
    int e = (end - 1) / window;
    int l = e - s + 1;

    byte[] buf = GenomeAssemblyDir.getBytes(file, s, e);

    List<Integer> scores = new ArrayList<Integer>(l);

    for (int i = 0; i < l; ++i) {
      scores.add((int) buf[i]);
    }

    return scores;
  }

  /**
   * The main method.
   *
   * @param args the arguments
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  public static void main(String[] args) throws IOException, ParseException {
    CountAssembly a = new ReadCountsFile8Bit(PathUtils.getPath(
        "/ifs/scratch/cancer/Lab_RDF/abh2138/ChIP_seq/rdf/samples/transcription_factors/bcl6/CB4_BCL6_RK040/hg19/align_2_mismatches/reads/"));

    System.err.println(a.getCounts("chr1:1000000-1100000", 10));
  }
}
