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
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.ngs.ReadCountsFile;

// TODO: Auto-generated Javadoc
/**
 * Stream a track directly from a r tree binary file.
 *
 * @author Antony Holmes Holmes
 */
public abstract class SampleAssemblyFile extends SampleAssembly {

  /** The m count. */
  private int mCount = -1;

  /** The m counts. */
  private ReadCountsFile mCounts;

  /**
   * Instantiates a new track assembly web.
   *
   * @param counts the counts
   */
  public SampleAssemblyFile(ReadCountsFile counts) {
    mCounts = counts;

    mCount = counts.getReadCount();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getStarts(edu.columbia.rdf.
   * edb .Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public List<Integer> getStarts(Sample sample,
      GenomicRegion region,
      int window) throws IOException {

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
  public List<Strand> getStrands(Sample sample,
      GenomicRegion region,
      int window) throws IOException {
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
  public List<Integer> getCounts(Sample sample,
      GenomicRegion region,
      int window) throws IOException {

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
  public int getMappedReads(Sample sample) throws IOException {
    return mCount;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getGenome(edu.columbia.rdf.
   * edb .Sample)
   */
  @Override
  public String getGenome(Sample sample) throws IOException {
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
