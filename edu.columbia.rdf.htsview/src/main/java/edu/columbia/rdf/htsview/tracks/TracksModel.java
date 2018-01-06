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
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.model.ListModel;
import org.jebtk.core.tree.TreeNode;

// TODO: Auto-generated Javadoc
/**
 * The Class TracksModel.
 */
public class TracksModel extends ListModel<TreeNode<Track>> {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Returns everything in the track model that can be represented as a BedGraph.
   * Tracks such as genes or BED files are excluded.
   *
   * @param displayRegion
   *          the display region
   * @param resolution
   *          the resolution
   * @param normalize
   *          the normalize
   * @return the bed graphs
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   * @throws ParseException
   *           the parse exception
   */
  public List<UCSCTrack> getBedGraphs(GenomicRegion displayRegion, int resolution, boolean normalize)
      throws IOException, ParseException {
    List<UCSCTrack> bedGraphs = new ArrayList<UCSCTrack>();

    for (TreeNode<Track> track : this) {
      UCSCTrack bedGraph = track.getValue().getBedGraph(displayRegion, resolution, normalize);

      if (bedGraph != null) {
        bedGraphs.add(bedGraph);
      }
    }

    return bedGraphs;
  }

}
