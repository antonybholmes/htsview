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
package edu.columbia.rdf.htsview.ext.abi;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.Mathematics;

import edu.columbia.rdf.edb.ngs.ReadCountsFile;

// TODO: Auto-generated Javadoc
/**
 * Decodes counts using a multi resolution file.
 *
 * @author Antony Holmes Holmes
 */
public class ReadCountsFileABI extends ReadCountsFile {

  /** The m trace. */
  private ABITrace mTrace;

  /** The m base. */
  private char mBase;

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param file the file
   * @param base the base
   */
  public ReadCountsFileABI(Path file, char base) {
    mBase = base;

    try {
      mTrace = ABITrace.parse(file);
    } catch (IOException e) {
      e.printStackTrace();
    }
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
    return getCounts(region.getChr(),
        region.getStart(),
        region.getEnd(),
        window);
  }

  /**
   * Gets the counts.
   *
   * @param chr the chr
   * @param start the start
   * @param end the end
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getCounts(Chromosome chr, int start, int end, int window)
      throws IOException {
    List<Integer> counts = new ArrayList<Integer>(end - start + 1);

    for (int i = start; i <= end; ++i) {
      counts.add((int) mTrace.getColor(mBase, i));
    }

    return counts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getStarts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public List<Integer> getStarts(GenomicRegion region, int window)
      throws IOException {
    return getStarts(region.getChr(),
        region.getStart(),
        region.getEnd(),
        window);
  }

  /**
   * Gets the starts.
   *
   * @param chr the chr
   * @param start the start
   * @param end the end
   * @param window the window
   * @return the starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getStarts(Chromosome chr, int start, int end, int window)
      throws IOException {

    return Mathematics.sequence(start, end);
  }
}
