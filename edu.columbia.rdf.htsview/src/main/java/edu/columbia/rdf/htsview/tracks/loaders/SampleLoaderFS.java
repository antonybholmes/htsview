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
package edu.columbia.rdf.htsview.tracks.loaders;

import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.core.tree.TreeNode;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.sample.ReadsFsPlotTrack;
import edu.columbia.rdf.htsview.tracks.sample.SampleFsPlotTrack;

// TODO: Auto-generated Javadoc
/**
 * Sample loader for streaming data directly from a file.
 * 
 * @author Antony Holmes Holmes
 *
 */
public abstract class SampleLoaderFS extends SampleLoader {

  /**
   * Open sample fs.
   *
   * @param sample
   *          the sample
   * @param assembly
   *          the assembly
   * @param metaFile
   *          the meta file
   * @param root
   *          the root
   * @return the track
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public Track openSampleFs(Sample sample, SampleAssembly assembly, Path metaFile, TreeNode<Track> root)
      throws IOException {
    return load(sample, createSampleFs(sample, assembly, metaFile), root);
  }

  /**
   * Creates the sample fs.
   *
   * @param sample
   *          the sample
   * @param assembly
   *          the assembly
   * @param metaFile
   *          the meta file
   * @return the sample fs plot track
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public SampleFsPlotTrack createSampleFs(Sample sample, SampleAssembly assembly, Path metaFile) throws IOException {
    return new SampleFsPlotTrack(sample, assembly, metaFile);
  }

  /**
   * Open reads fs.
   *
   * @param sample
   *          the sample
   * @param assembly
   *          the assembly
   * @param metaFile
   *          the meta file
   * @param root
   *          the root
   * @return the track
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public Track openReadsFs(Sample sample, SampleAssembly assembly, Path metaFile, TreeNode<Track> root)
      throws IOException {
    return load(sample, createReadsFs(sample, assembly, metaFile), root);
  }

  /**
   * Creates the reads fs.
   *
   * @param sample
   *          the sample
   * @param assembly
   *          the assembly
   * @param metaFile
   *          the meta file
   * @return the reads fs plot track
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public ReadsFsPlotTrack createReadsFs(Sample sample, SampleAssembly assembly, Path metaFile) throws IOException {
    return new ReadsFsPlotTrack(sample, assembly, metaFile);
  }

}
