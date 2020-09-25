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

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.ngs.ReadCountsFile;

/**
 * Stream a track directly from a r tree binary file.
 *
 * @author Antony Holmes
 */
public abstract class SampleAssemblyFile extends SampleAssembly {

  /** The m counts. */
  private ReadCountsFile mCounts;

  /**
   * Instantiates a new track assembly web.
   *
   * @param counts the counts
   */
  public SampleAssemblyFile(ReadCountsFile counts) {
    mCounts = counts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getStarts(edu.columbia.rdf.
   * edb .Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public int[] getStarts(Sample sample, 
      Genome genome,
      GenomicRegion region, int window)
      throws IOException {

    return mCounts.getStarts(region, window);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getStrands(edu.columbia.rdf.
   * edb.Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public Strand[] getStrands(Sample sample, 
      Genome genome,
      GenomicRegion region, 
      int window)
      throws IOException {

    return mCounts.getStrands(region, window);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getCounts(edu.columbia.rdf.
   * edb .Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public int[] getCounts(Sample sample, 
      Genome genome,
      GenomicRegion region, 
      int window)
      throws IOException {

    return mCounts.getCounts(region, window);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getMappedReads(edu.columbia.
   * rdf.edb.Sample)
   */
  @Override
  public int getMappedReads(Sample sample, Genome genome, int window)
      throws IOException {
    return mCounts.getReadCount(genome, window);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getGenome(edu.columbia.rdf.
   * edb .Sample)
   */
  @Override
  public Genome getGenome(Sample sample) throws IOException {
    return mCounts.getGenome();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getReadLength(edu.columbia.
   * rdf .edb.Sample)
   */
  @Override
  public int getReadLength(Sample sample) {
    return mCounts.getReadLength();
  }
}
