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
package edu.columbia.rdf.htsview.tracks.sample;

import java.nio.file.Path;

import edu.columbia.rdf.edb.ngs.ReadCountsFileBam;
import edu.columbia.rdf.htsview.tracks.SampleAssemblyFile;

/**
 * Stream a track directly from a r tree binary file.
 *
 * @author Antony Holmes Holmes
 */
public class SampleAssemblyBam extends SampleAssemblyFile {

  /**
   * Instantiates a new sample assembly bam.
   *
   * @param file the file
   */
  public SampleAssemblyBam(Path file) {
    super(new ReadCountsFileBam(file));
  }
}
