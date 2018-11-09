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

import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.core.io.FileUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.edb.Species;

/**
 * The Class SampleTracks.
 */
public class SampleTracks {

  /**
   * Instantiates a new sample tracks.
   */
  private SampleTracks() {
    // Do nothing
  }

  /**
   * Gets the sample from track.
   *
   * @param json the json
   * @return the sample from track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Sample getSampleFromTrack(Json json) throws IOException {
    Sample sample = new Sample(-1, null, null, json.getString("Name"),
        new Species(-1, json.getString("Organism"), json.getString("Organism")),
        null);

    return sample;
  }

  /**
   * Open json.
   *
   * @param dir the dir
   * @param file the file
   * @return the json
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Json openJson(Path dir, String file) throws IOException {
    return new JsonParser().parse(dir.resolve(file));
  }

  /**
   * Open json.
   *
   * @param dir the dir
   * @param file the file
   * @return the json
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Json openJson(Path dir, Path file) throws IOException {
    return new JsonParser().parse(dir.resolve(file));
  }

  /**
   * Checks if is BRT track.
   *
   * @param dir the dir
   * @return true, if is BRT track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static boolean isBRTTrack(Path dir) throws IOException {
    return FileUtils.find(dir, "brt.json") != null;
  }

  /**
   * Checks if is BRT 2 track.
   *
   * @param dir the dir
   * @return true, if is BRT 2 track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static boolean isBRT2Track(Path dir) throws IOException {
    return FileUtils.find(dir, "brt2.json") != null;
  }

  /**
   * Checks if is BVT track.
   *
   * @param dir the dir
   * @return true, if is BVT track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static boolean isBVTTrack(Path dir) throws IOException {
    return FileUtils.find(dir, "bvt.json") != null;
  }
}
