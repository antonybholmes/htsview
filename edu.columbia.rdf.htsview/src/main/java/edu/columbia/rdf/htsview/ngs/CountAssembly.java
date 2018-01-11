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
import java.util.Collections;
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;

// TODO: Auto-generated Javadoc
/**
 * The class CountAssembly.
 */
public abstract class CountAssembly {

  /**
   * Gets the starts.
   *
   * @param region the region
   * @param window the window
   * @return the starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getStarts(String region, int window) throws IOException {
    return getStarts(GenomicRegion.parse(region), window);
  }

  /**
   * Gets the starts.
   *
   * @param region the region
   * @param window the window
   * @return the starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getStarts(GenomicRegion region, int window)
      throws IOException {
    return Collections.emptyList();
  }

  /**
   * Gets the strands.
   *
   * @param region the region
   * @param window the window
   * @return the strands
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Strand> getStrands(String region, int window) throws IOException {
    return getStrands(GenomicRegion.parse(region), window);
  }

  /**
   * Gets the strands.
   *
   * @param region the region
   * @param window the window
   * @return the strands
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Strand> getStrands(GenomicRegion region, int window)
      throws IOException {
    return Collections.emptyList();
  }

  /**
   * Gets the counts.
   *
   * @param region the region
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getCounts(String region) throws IOException {
    return getCounts(region, 1);
  }

  /**
   * Gets the counts.
   *
   * @param region the region
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getCounts(String region, int window) throws IOException {
    return getCounts(GenomicRegion.parse(region), window);
  }

  /**
   * Gets the counts.
   *
   * @param region the region
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getCounts(GenomicRegion region) throws IOException {
    return getCounts(region, 1);
  }

  /**
   * Gets the counts.
   *
   * @param region the region
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public abstract List<Integer> getCounts(GenomicRegion region, int window)
      throws IOException;

  /**
   * Gets the values.
   *
   * @param region the region
   * @param window the window
   * @return the values
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Double> getValues(String region, int window) throws IOException {
    return getValues(GenomicRegion.parse(region), window);
  }

  /**
   * Gets the values.
   *
   * @param region the region
   * @return the values
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Double> getValues(GenomicRegion region) throws IOException {
    return getValues(region, 1);
  }

  /**
   * Returns values in a position range.
   *
   * @param region the region
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Double> getValues(GenomicRegion region, int window)
      throws IOException {
    return Collections.emptyList();
  }

  /**
   * Optional method for closing file handles if necessary.
   */
  public void close() {
    // Do nothing
  }

  /**
   * Gets the read length.
   *
   * @return the read length
   */
  public int getReadLength() {
    return -1;
  }

  /**
   * Should return the number of reads in the library.
   *
   * @return the read count
   */
  public int getReadCount() {
    return -1;
  }

  /**
   * Gets the genome.
   *
   * @return the genome
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public String getGenome() throws IOException {
    return null;
  }
}
