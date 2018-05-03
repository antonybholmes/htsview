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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.tracks.Track;

/**
 * The Class SampleLoaderService.
 */
public class SampleLoaderService {

  /**
   * The Class SampleLoaderLoader.
   */
  private static class SampleLoaderLoader {

    /** The Constant INSTANCE. */
    private static final SampleLoaderService INSTANCE = new SampleLoaderService();
  }

  /**
   * Gets the single instance of SampleLoaderService.
   *
   * @return single instance of SampleLoaderService
   */
  public static SampleLoaderService getInstance() {
    return SampleLoaderLoader.INSTANCE;
  }

  /** The m parser map. */
  private Map<String, SampleLoader> mParserMap = new HashMap<String, SampleLoader>();

  /**
   * Instantiates a new sample loader service.
   */
  private SampleLoaderService() {
    // autoLoad();
  }

  /**
   * Register.
   *
   * @param parser the parser
   */
  public void register(SampleLoader parser) {
    register(parser.getExt(), parser);
  }

  /**
   * Register.
   *
   * @param type the type
   * @param parser the parser
   */
  private void register(String type, SampleLoader parser) {
    mParserMap.put(type, parser);
  }

  /**
   * Gets the.
   *
   * @param type the type
   * @return the sample loader
   */
  public SampleLoader get(String type) {
    return mParserMap.get(type);
  }

  /**
   * Open multiple sample files. This may trigger dialogs requesting user input.
   * For unattended file opening, use either the {@code openSample()} or
   * {@code openReads()} methods.
   *
   * @param parent the parent
   * @param files the files
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track openFiles(ModernWindow parent,
      List<Path> files,
      TreeNode<Track> root) throws IOException {
    if (CollectionUtils.isNullOrEmpty(files)) {
      return null;
    }

    Track ret = null;

    for (Path file : files) {
      ret = openFile(parent, file, root);
    }

    return ret;
  }

  /**
   * Open file.
   *
   * @param parent the parent
   * @param file the file
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track openFile(ModernWindow parent, Path file, TreeNode<Track> root)
      throws IOException {
    // First try the normal file extension
    String ext = PathUtils.getFileExt(file);

    Track ret = null;

    SampleLoader loader = get(ext);

    if (loader != null) {
      ret = loader.open(parent, file, root);
    } else {
      // Try the longer extension
      ext = PathUtils.getFileExtLong(file);

      loader = get(ext);

      if (loader != null) {
        ret = loader.open(parent, file, root);
      } else {
        // As a last resort try the whole file name for file types
        // such as bct.json
        ext = PathUtils.getName(file);

        loader = get(ext);

        if (loader != null) {
          ret = loader.open(parent, file, root);
        }
      }
    }

    return ret;
  }

  /*
  public Track openSamples(ModernWindow parent,
      List<Path> files,
      TreeNode<Track> root) throws IOException {
    if (CollectionUtils.isNullOrEmpty(files)) {
      return null;
    }

    Track ret = null;

    for (Path file : files) {
      ret = openSample(parent, file, root);
    }

    return ret;
  }
  */

  /*
  public Track openSample(ModernWindow parent, Path file, TreeNode<Track> root)
      throws IOException {
    // First try the normal file extension
    String ext = PathUtils.getFileExt(file);

    Track ret = null;

    SampleLoader loader = get(ext);

    if (loader != null) {
      ret = loader.open(parent, file, root);// loader.openSample(parent, file, root);
    } else {
      // Try the longer extension
      ext = PathUtils.getFileExtLong(file);

      loader = get(ext);

      if (loader != null) {
        ret = loader.open(parent, file, root);
      } else {
        // As a last resort try the whole file name for file types
        // such as bct.json
        ext = PathUtils.getName(file);

        loader = get(ext);

        if (loader != null) {
          ret = loader.open(parent, file, root);
        }
      }
    }

    return ret;
  }
  */

  /**
   * Open reads.
   *
   * @param parent the parent
   * @param files the files
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track openReads(ModernWindow parent,
      List<Path> files,
      TreeNode<Track> root) throws IOException {
    if (CollectionUtils.isNullOrEmpty(files)) {
      return null;
    }

    Track ret = null;

    for (Path file : files) {
      ret = openReads(parent, file, root);
    }

    return ret;
  }

  /**
   * Open reads.
   *
   * @param parent the parent
   * @param file the file
   * @param root the root
   * @return the track
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Track openReads(ModernWindow parent, Path file, TreeNode<Track> root)
      throws IOException {
    // First try the normal file extension
    String ext = PathUtils.getFileExt(file);

    Track ret = null;

    SampleLoader loader = get(ext);

    if (loader != null) {
      ret = loader.openReads(parent, file, root);
    } else {
      // Try the longer extension
      ext = PathUtils.getFileExtLong(file);

      loader = get(ext);

      if (loader != null) {
        ret = loader.openReads(parent, file, root);
      } else {
        // As a last resort try the whole file name for file types
        // such as bct.json
        ext = PathUtils.getName(file);

        loader = get(ext);

        if (loader != null) {
          ret = loader.openReads(parent, file, root);
        }
      }
    }

    return ret;
  }
}
