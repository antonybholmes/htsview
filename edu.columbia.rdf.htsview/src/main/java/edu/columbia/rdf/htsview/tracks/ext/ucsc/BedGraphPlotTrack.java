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
import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.graphplot.figure.PlotStyle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class BedGraphPlotTrack.
 */
public class BedGraphPlotTrack extends GraphPlotTrack {

  /** The m bed graph. */
  private UCSCTrack mBedGraph;

  /** The m file. */
  private Path mFile;

  /** The m Y max. */
  private int mYMax = 1;

  /** The m style. */
  private PlotStyle mStyle = PlotStyle.FILLED_SMOOTH;

  /**
   * Instantiates a new bed graph plot track.
   *
   * @param bedGraph
   *          the bed graph
   * @param file
   *          the file
   */
  public BedGraphPlotTrack(UCSCTrack bedGraph, Path file) {
    mBedGraph = bedGraph;
    mFile = file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getType()
   */
  @Override
  public String getType() {
    return "BedGraph";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getName()
   */
  @Override
  public String getName() {
    return mBedGraph.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setFillColor(java.awt.Color)
   */
  @Override
  public void setFillColor(Color color) {
    mBedGraph.setColor(color);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getFillColor()
   */
  @Override
  public Color getFillColor() {
    return mBedGraph.getColor();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getYMax(boolean)
   */
  @Override
  public double getYMax(boolean normalize) {
    return mYMax;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setYMax(double)
   */
  @Override
  public void setYMax(double yMax) {
    mYMax = (int) yMax;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setStyle(org.graphplot.figure.
   * PlotStyle)
   */
  @Override
  public void setStyle(PlotStyle style) {
    mStyle = style;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getStyle()
   */
  @Override
  public PlotStyle getStyle() {
    return mStyle;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(String genome, TitleProperties titlePosition) throws IOException {
    mSubFigure = BedGraphSubFigure.create(mBedGraph.getName(), mStyle, titlePosition);

    ((BedGraphPlot) mSubFigure.currentAxes().currentPlot()).setBedGraph(mBedGraph);

    mSubFigure.currentAxes().setInternalSize(PLOT_SIZE);

    switch (titlePosition.getPosition()) {
    case RIGHT:
    case COMPACT_RIGHT:
      int right = rightTitleWidth(getName());

      mSubFigure.currentAxes().setMargins(MEDIUM_MARGIN, MARGINS.getLeft(), LARGE_MARGIN, right);

      break;
    default:
      mSubFigure.currentAxes().setMargins(MARGINS);

      break;
    }

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
  public TrackSubFigure updateGraph(GenomicRegion displayRegion, int resolution, int width, int height, int margin)
      throws IOException {
    // Turn off updating so that we reduce drawing events
    // mPlot.setForwardCanvasEventsEnabled(false);
    mSubFigure.update(displayRegion, resolution, mYMax, width, height, margin, getFillColor(), getFillColor(),
        getStyle());
    // mPlot.setForwardCanvasEventsEnabled(true);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#getBedGraph(org.jebtk.bioinformatics.
   * genome.GenomicRegion, int, boolean)
   */
  public UCSCTrack getBedGraph(GenomicRegion displayRegion, int resolution, boolean normalize) {
    return mBedGraph;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toXml(org.w3c.dom.Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "bedgraph");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("file", PathUtils.toString(mFile));
    trackElement.setAttribute("color", ColorUtils.toHtml(getLineColor()));
    trackElement.setAttribute("fill-color", ColorUtils.toHtml(getFillColor()));

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#toJson(org.abh.common.json.JsonBuilder)
   */
  @Override
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "bedgraph");
    json.add("name", getName());
    json.add("file", PathUtils.toString(mFile));
    json.add("color", ColorUtils.toHtml(getLineColor()));
    json.add("fill-color", ColorUtils.toHtml(getFillColor()));

    json.endObject();
  }
}
