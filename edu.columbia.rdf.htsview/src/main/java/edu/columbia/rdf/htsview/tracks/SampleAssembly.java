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
package edu.columbia.rdf.htsview.tracks;

import java.io.IOException;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;
import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.ArrayUtils;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ngs.CountAssembly;

/**
 * Track assembly loads counts at a genomic location.
 */
public abstract class SampleAssembly {

  /**
   * Gets the starts.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[] getStarts(Sample sample,
      Genome genome,
      String region,
      int window) throws IOException {
    return getStarts(sample, genome, GenomicRegion.parse(genome, region), window);
  }

  /**
   * Gets the starts.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[] getStarts(Sample sample, 
      Genome genome,
      GenomicRegion region, int window)
      throws IOException {
    return ArrayUtils.EMPTY_INT_ARRAY;
  }

  /**
   * Gets the strands.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the strands
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Strand[] getStrands(Sample sample, Genome genome,
      GenomicRegion region, int window)
      throws IOException {
    return CountAssembly.EMPTY_STRAND_ARRAY;
  }

  /**
   * Gets the counts.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[] getCounts(Sample sample,
      Genome genome,
      String region,
      int window) throws IOException {
    return getCounts(sample, genome, GenomicRegion.parse(genome, region), window);
  }

  /**
   * Gets the counts.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public abstract int[] getCounts(Sample sample,
      Genome genome,
      GenomicRegion region,
      int window) throws IOException;

  /**
   * Gets the Reads per million mapped reads.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the normalized counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public double[] getRPM(Sample sample, Genome genome, GenomicRegion region, int window)
      throws IOException {
    return getRPM(sample, this, genome, region, window);
  }

  /**
   * Should return the number of mapped reads for the sample. If it returns -1,
   * it is assumed the number of reads are unknown so there will not be any
   * normalization of the sample.
   *
   * @param sample the sample
   * @return the mapped reads
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int getMappedReads(Sample sample, Genome genome, int window)
      throws IOException {
    return -1;
  }

  /**
   * Returns the bin a genomic coordinate is in.
   *
   * @param p a one based genomic coordinate
   * @param window a window bin size
   * @return the bin
   */
  public static final int getBin(int p, int window) {
    return (p - 1) / window;
  }

  /**
   * Should return true if this file type supports tracking individual reads.
   *
   * @param sample the sample
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean hasReadSupport(Sample sample) throws IOException {
    return false;
  }

  /**
   * Checks if is bvt.
   *
   * @param sample the sample
   * @return true, if is bvt
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public boolean isBVT(Sample sample) throws IOException {
    return false;
  }

  /**
   * Gets the read length.
   *
   * @param sample the sample
   * @return the read length
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int getReadLength(Sample sample) throws IOException {
    return -1;
  }

  /**
   * Gets the genome.
   *
   * @param sample the sample
   * @return the genome
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Genome getGenome(Sample sample) throws IOException {
    return Genome.HG19;
  }

  public static double[] getRPM(Sample sample,
      SampleAssembly assembly,
      Genome genome,
      GenomicRegion region,
      int window) throws IOException {
    double scaleFactor;

    double mappedReads = assembly
        .getMappedReads(sample, genome, window);

    // System.err.println("mapped reads " + mappedReads);

    if (mappedReads > 0) {
      scaleFactor = 1000000 / mappedReads;
    } else {
      scaleFactor = 1;
    }

    return Mathematics.multiply(assembly.getCounts(sample, genome, region, window),
        scaleFactor);
  }
}
