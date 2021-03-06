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
package edu.columbia.rdf.htsview.tracks.ext.ucsc;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.loaders.SampleLoader;

/**
 * The Class SampleLoaderBedGraph.
 */
public class SampleLoaderBedGraph extends SampleLoader {
  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.loaders.SampleLoader#openSample(org.abh.
   * common.ui.window.ModernWindow, java.nio.file.Path,
   * org.abh.common.tree.TreeNode)
   */
  @Override
  public Track openSample(ModernWindow parent, Path file, TreeNode<Track> root)
      throws IOException {
    /*
    if (!mApplyToAll) {
      BedGraphStyleDialog dialog = new BedGraphStyleDialog(parent);

      dialog.setVisible(true);

      if (dialog.getStatus() == ModernDialogStatus.OK) {

        mOpenAsBedGraph = dialog.isBedGraphStyle();
        mApplyToAll = dialog.getApplyToAll();
      } else {
        return null;
      }
    }
     */
    
    List<BedGraph> bedGraphs = BedGraph.parse(file);

    Track ret = null;

    for (BedGraph bedGraph : bedGraphs) {
      System.err.println("bedgraph");
      ret = load(new BedGraphPlotTrack(bedGraph, file), root);
    }

    return ret;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return "BedGraph";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.loaders.SampleLoader#getExt()
   */
  @Override
  public String getExt() {
    return "bedgraph";
  }
}
