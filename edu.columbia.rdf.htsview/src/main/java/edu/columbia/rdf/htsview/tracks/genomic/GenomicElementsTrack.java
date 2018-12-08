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
package edu.columbia.rdf.htsview.tracks.genomic;

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicElementsDB;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackDisplayMode;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedEditDialog;

/**
 * The Class UcscPlotTrack.
 */
public class GenomicElementsTrack extends GraphPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** The m bed. */
  // private static final Dimension PLOT_SIZE = new Dimension(PLOT_WIDTH, 20);
  protected GenomicElementsDB mUcsc;

  /**
   * Instantiates a new ucsc plot track.
   *
   * @param bed the bed
   * @param fillColor the fill color
   * @param mode the mode
   */
  public GenomicElementsTrack(String name, GenomicElementsDB bed, Color fillColor, TrackDisplayMode mode) {
    mUcsc = bed;
    
    setName(name);

    setFillColor(fillColor);

    setDisplayMode(mode);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getType()
   */
  @Override
  public String getType() {
    return "Genomic Element Track";
  }


  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(Genome genome,
      TitleProperties titlePosition) throws IOException {
    mSubFigure = GenomicElementsSubFigure.create(this, mUcsc, titlePosition);

    // mPlot.getGraphSpace().setPlotSize(PLOT_SIZE);

    setMargins(getName(), titlePosition, mSubFigure);

    mSubFigure.currentAxes().getX1Axis().getTitle().setText(null);
    mSubFigure.currentAxes().getY1Axis().setLimits(0, 1);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#updateGraph(org.jebtk.bioinformatics.
   * genome.GenomicRegion, int, int, int, int)
   */
  @Override
  public TrackSubFigure updateGraph(GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {
    // mPlot.setForwardCanvasEventsEnabled(false);
    mSubFigure.update(displayRegion, resolution, width, height, margin);
    // mPlot.setForwardCanvasEventsEnabled(true);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.GraphPlotTrack#edit(org.abh.common.ui.
   * window. ModernWindow)
   */
  @Override
  public void edit(ModernWindow parent) {
    BedEditDialog dialog = new BedEditDialog(parent, this);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setName(dialog.getName());
    setFillColor(dialog.getLineColor());
    setDisplayMode(dialog.getMode());
  }
}
