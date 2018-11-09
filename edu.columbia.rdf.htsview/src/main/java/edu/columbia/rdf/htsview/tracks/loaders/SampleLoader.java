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

import org.jebtk.core.NameProperty;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class SampleLoader.
 */
public abstract class SampleLoader implements NameProperty {

  /**
   * Open.
   *
   * @param parent the parent
   * @param file the file
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track open(ModernWindow parent, Path file, TreeNode<Track> root)
      throws IOException {
    return openSample(parent, file, root);
  }

  /**
   * Should be called to open a sample track.
   *
   * @param parent the parent
   * @param file the file
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public abstract Track openSample(ModernWindow parent,
      Path file,
      TreeNode<Track> root) throws IOException;

  /**
   * Should be called to open a reads track showing individual reads.
   *
   * @param parent the parent
   * @param file the file
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track openReads(ModernWindow parent, Path file, TreeNode<Track> root)
      throws IOException {
    return null;
  }

  /**
   * Should return the file extension the loader can handle.
   *
   * @return the ext
   */
  public abstract String getExt();

  //
  // Static methods
  //

  /**
   * Load.
   *
   * @param sample the sample
   * @param track the track
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Track load(Sample sample, Track track, TreeNode<Track> root)
      throws IOException {
    return load(sample.getName(), track, root);
  }

  /**
   * Load.
   *
   * @param track the track
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Track load(Track track, TreeNode<Track> root)
      throws IOException {
    return load(track.getName(), track, root);
  }

  /**
   * Load a track into the track tree.
   *
   * @param name the name
   * @param track the track
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Track load(String name, Track track, TreeNode<Track> root)
      throws IOException {

    if (root != null) {
      root.addChild(new TreeNode<Track>(name, track));
    }

    return track;
  }
}
