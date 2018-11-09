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
package edu.columbia.rdf.htsview.tracks.ext.ucsc;

import java.awt.Color;

import org.jebtk.bioinformatics.ext.ucsc.TrackDisplayMode;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The Class UcscPlotTrack.
 */
public class UcscPlotTrack extends GraphPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** The m bed. */
  // private static final Dimension PLOT_SIZE = new Dimension(PLOT_WIDTH, 20);
  protected UCSCTrack mBed;

  /**
   * Instantiates a new ucsc plot track.
   *
   * @param bed the bed
   */
  public UcscPlotTrack(UCSCTrack bed) {
    this(bed, bed.getColor());
  }

  /**
   * Instantiates a new ucsc plot track.
   *
   * @param bed the bed
   * @param fillColor the fill color
   */
  public UcscPlotTrack(UCSCTrack bed, Color fillColor) {
    this(bed, fillColor, TrackDisplayMode.COMPACT);
  }

  /**
   * Instantiates a new ucsc plot track.
   *
   * @param bed the bed
   * @param mode the mode
   */
  public UcscPlotTrack(UCSCTrack bed, TrackDisplayMode mode) {
    this(bed, bed.getColor(), mode);
  }

  /**
   * Instantiates a new ucsc plot track.
   *
   * @param bed the bed
   * @param fillColor the fill color
   * @param mode the mode
   */
  public UcscPlotTrack(UCSCTrack bed, Color fillColor, TrackDisplayMode mode) {
    mBed = bed;

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
    return "UCSC Track";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getName()
   */
  @Override
  public String getName() {
    return mBed.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    mBed.setName(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getFillColor()
   */
  @Override
  public Color getFillColor() {
    return mBed.getColor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setFillColor(java.awt.Color)
   */
  @Override
  public void setFillColor(Color color) {
    mBed.setColor(color);
  }

  /**
   * Sets the display mode.
   *
   * @param mode the new display mode
   */
  public void setDisplayMode(TrackDisplayMode mode) {
    mBed.setDisplayMode(mode);
  }

  /**
   * Gets the display mode.
   *
   * @return the display mode
   */
  public TrackDisplayMode getDisplayMode() {
    return mBed.getDisplayMode();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(Genome genome,
      TitleProperties titlePosition) {
    mSubFigure = BedPlotSubFigure.create(mBed, titlePosition);

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
      int margin) {
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

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toXml(org.w3c.dom.Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "bed");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("color", ColorUtils.toHtml(getFillColor()));

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toJson(org.abh.common.json.
   * JsonBuilder)
   */
  @Override
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "bed");
    json.add("name", getName());
    json.add("color", ColorUtils.toHtml(getFillColor()));

    json.endObject();
  }
}
