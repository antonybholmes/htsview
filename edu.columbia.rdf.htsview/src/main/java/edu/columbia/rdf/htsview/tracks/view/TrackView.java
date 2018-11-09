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
package edu.columbia.rdf.htsview.tracks.view;

import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayDeque;
import java.util.Deque;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeRootNode;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.tracks.MarginModel;
import edu.columbia.rdf.htsview.tracks.TitlePosition;
import edu.columbia.rdf.htsview.tracks.TitlePositionModel;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackTree;
import edu.columbia.rdf.htsview.tracks.TracksPanel;
import edu.columbia.rdf.htsview.tracks.WidthModel;

/**
 * The Class TrackView.
 */
public class TrackView {

  /**
   * Instantiates a new track view.
   */
  private TrackView() {
    // Do nothing
  }

  /**
   * Load json view.
   *
   * @param window the window
   * @param jsonFile the json file
   * @param tracksPanel the tracks panel
   * @param mAnnotationTree the m annotation tree
   * @param mWidthModel the m width model
   * @param mMarginModel the m margin model
   * @param mGenomicModel the m genomic model
   * @param titleModel the title model
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void loadJsonView(ModernWindow window,
      final Path jsonFile,
      TracksPanel tracksPanel,
      final ModernTree<Track> mAnnotationTree,
      WidthModel mWidthModel,
      MarginModel mMarginModel,
      GenomeModel mGenomeModel,
      GenomicRegionModel mGenomicModel,
      TitlePositionModel titleModel) throws IOException {
    TreeRootNode<Track> root = new TreeRootNode<Track>();
    Deque<TreeNode<Track>> queue = new ArrayDeque<TreeNode<Track>>();

    Deque<Json> tracksQueue = new ArrayDeque<Json>();

    queue.push(root);

    Json viewJson = new JsonParser().parse(jsonFile);

    Genome genome;

    if (viewJson.containsKey("genome")) {
      genome = GenomeService.getInstance()
          .guessGenome(viewJson.getString("genome"));
    } else {
      genome = mGenomeModel.get();
    }

    GenomicRegion region = GenomicRegion.parse(genome,
        viewJson.getString("location"));

    if (region == null) {
      return;
    }

    if (viewJson.containsKey("width-px")) {
      mWidthModel.set(viewJson.getInt("width-px"));
    } else if (viewJson.containsKey("width")) {
      mWidthModel.set(viewJson.getInt("width"));
    } else {
      // Do nothing
    }

    if (viewJson.containsKey("margin-px")) {
      mMarginModel.set(viewJson.getInt("margin-px"));
    } else if (viewJson.containsKey("margin")) {
      mMarginModel.set(viewJson.getInt("margin"));
    } else {
      // Do nothing
    }

    if (viewJson.containsKey("titles")) {
      Json titleJson = viewJson.get("titles");

      TitlePosition position = TitlePosition.TOP;

      if (titleJson.containsKey("position")) {
        position = TitlePosition.parse(titleJson.getString("position"));
      }

      boolean visible = true;

      if (titleJson.containsKey("visible")) {
        visible = titleJson.getBool("visible");
      }

      TitleProperties titleProperties = new TitleProperties(position, visible);

      titleModel.set(titleProperties);
    }

    tracksQueue.push(viewJson.get("tracks"));

    while (!tracksQueue.isEmpty()) {
      Json tracksJson = tracksQueue.pop();

      TreeNode<Track> rootNode = queue.pop();

      for (Json trackJson : tracksJson) {
        String type = trackJson.getString("type");
        String name = trackJson.getString("name");

        System.err.println("Loading " + name + " " + type);

        int id = trackJson.getInt("id");

        // Determines if we can add child tracks to the current
        // track
        boolean allowChildren = false;

        TrackJsonParser parser = TrackParserService.getInstance().get(type);

        if (parser != null) {
          allowChildren = parser.parse(window,
              name,
              id,
              genome,
              mAnnotationTree,
              trackJson,
              rootNode);
        }

        Json subTracksJson = trackJson.get("tracks");

        // Only process sub children if the current child is valid
        // and it has children
        if (subTracksJson != null) {
          queue.push(rootNode.getChild(rootNode.getChildCount() - 1));
          tracksQueue.push(subTracksJson);
        }
      }
    }

    tracksPanel.setTracks(root);

    System.err.println("Sdfsdf");

    mGenomicModel.set(region);
  }

  /**
   * Gets the file.
   *
   * @param json the json
   * @return the file
   */
  private static Path getFile(Json json) {
    String path = json.getString("file");

    if (path != null) {
      return PathUtils.getPath(path);
    }

    path = json.getString("meta-file");

    if (path != null) {
      return PathUtils.getPath(path);
    }

    return null;
  }

  /**
   * Save json view.
   *
   * @param file the file
   * @param trackList the track list
   * @param region the region
   * @param titleProperties the title properties
   * @param width the width
   * @param margin the margin
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static void saveJsonView(Path file,
      TrackTree trackList,
      GenomicRegion region,
      TitleProperties titleProperties,
      int width,
      int margin) throws IOException {
    if (file == null || region == null) {
      return;
    }

    JsonBuilder root = JsonBuilder.create().startObject();

    root.add("genome", region.getChr().getGenome().getAssembly());
    root.add("location", region.getLocation());
    root.add("width-px", width);
    root.add("margin-px", margin);

    root.startObject("titles");
    root.add("position",
        titleProperties.getPosition().toString().toLowerCase());
    root.add("visible", titleProperties.getVisible());
    root.endObject();

    // now append each track

    Deque<TreeNode<Track>> nodeQueue = new ArrayDeque<TreeNode<Track>>();
    Deque<JsonBuilder> jsonQueue = new ArrayDeque<JsonBuilder>();

    for (TreeNode<Track> child : CollectionUtils.reverse(trackList.getRoot())) {
      nodeQueue.push(child);
      jsonQueue.push(root);
    }

    root.startArray("tracks");

    for (TreeNode<Track> node : trackList.getRoot()) {
      // TreeNode<Track> node = nodeQueue.pop();
      // JsonBuilder json = jsonQueue.pop();

      node.getValue().toJson(root);

      if (node.isParent()) {

        root.reopen();

        root.startArray("tracks");

        for (TreeNode<Track> child : node) {
          child.getValue().toJson(root);
        }

        root.endArray();

        root.endObject();
      }
    }

    root.endArray();

    root.endObject();

    // Json.prettyWrite(root, file);

    JsonBuilder.write(root, file);
  }
}
