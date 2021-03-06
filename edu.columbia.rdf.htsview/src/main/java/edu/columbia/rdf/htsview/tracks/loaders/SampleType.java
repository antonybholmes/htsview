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

/**
 * If a sample supports both sample and read data, determine which to show.
 * 
 * @author Antony Holmes
 *
 */
public enum SampleType {

  /** The sample only. */
  SAMPLE_ONLY,

  /** The reads only. */
  READS_ONLY,

  /** The all. */
  ALL
}
