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

import java.awt.Dimension;

import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;

// TODO: Auto-generated Javadoc
/**
 * The Class GraphPlotTrack.
 */
public abstract class GraphPlotTrack extends Track {

  /** The Constant PLOT_SIZE. */
  public static final Dimension PLOT_SIZE = new Dimension(PLOT_WIDTH, 100);

  /** The m sub figure. */
  protected TrackSubFigure mSubFigure;

  /**
   * Should enable a UI dialog or similar to allow the track to be edited.
   *
   * @param parent
   *          the parent
   */
  @Override
  public void edit(ModernWindow parent) {
    TrackEditDialog dialog = new TrackEditDialog(parent, this);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setName(dialog.getName());
    setLineColor(dialog.getLineColor());
    setFillColor(dialog.getFillColor());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getGraph()
   */
  @Override
  public TrackSubFigure getGraph() {
    return mSubFigure;
  }

}
