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

import java.io.IOException;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraphElement;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicElement;
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
import edu.columbia.rdf.htsview.tracks.TracksFigure;

public class BG3PlotTrack extends GraphPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** The m Y max. */
  private int mYMax = 1;

  /** The m style. */
  private PlotStyle mStyle = PlotStyle.FILLED_SMOOTH;

  private GenomicRegion mRegion;

  private boolean mAutoY = true;

  private BG3 mBg3;

  /**
   * Instantiates a new bed graph plot track.
   *
   * @param bedGraph the bed graph
   * @param file the file
   */
  public BG3PlotTrack(BG3 bg3) {
    mBg3 = bg3;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getType()
   */
  @Override
  public String getType() {
    return "BG3";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getName()
   */
  @Override
  public String getName() {
    return mBg3.getName();
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
  
  @Override
  public double getYMax(boolean normalize) {
    if (getAutoY()) {
      return autoY(normalize);
    } else {
      return mYMax;
    }
  }

  /**
   * Auto Y.
   *
   * @param normalize the normalize
   * @return the double
   */
  private double autoY(boolean normalize) {
    List<GenomicElement> regions = mBg3.getElements(mRegion);
    
    //GenomicRegions
    ///    .getFixedGapSearch(mBedGraph.getElements().toList())
     //   .getFeatureSet(mRegion);

    double y = 0;

    for (GenomicElement region : regions) {
      double value = ((BedGraphElement) region).getValue();

      if (value > y) {
        y = value;
      }
    }

    return Math.max(TracksFigure.MIN_MAX_Y, y);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getAutoY()
   */
  @Override
  public boolean getAutoY() {
    return mAutoY;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setAutoY(boolean)
   */
  @Override
  public void setAutoY(boolean autoY) {
    mAutoY = autoY;
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
  public TrackSubFigure createGraph(Genome genome,
      TitleProperties titlePosition) throws IOException {
    mSubFigure = BedGraphSubFigure
        .create(mBg3.getName(), mStyle, titlePosition);

    ((BedGraphPlot) mSubFigure.currentAxes().currentPlot())
        .setBedGraph(mBg3);

    mSubFigure.currentAxes().setInternalSize(PLOT_SIZE);

    switch (titlePosition.getPosition()) {
    case RIGHT:
    case COMPACT_RIGHT:
      int right = rightTitleWidth(getName());

      mSubFigure.currentAxes()
          .setMargins(MEDIUM_MARGIN, MARGINS.getLeft(), LARGE_MARGIN, right);

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
  public TrackSubFigure updateGraph(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {
    mRegion = displayRegion;
    
    // Turn off updating so that we reduce drawing events
    // mPlot.setForwardCanvasEventsEnabled(false);
    mSubFigure.update(genome,
        displayRegion,
        resolution,
        mYMax,
        width,
        height,
        margin,
        getFillColor(),
        getFillColor(),
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
  @Override
  public UCSCTrack getBedGraph(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      boolean normalize) {
    return mBg3.getBedGraph(displayRegion);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toXml(org.w3c.dom.Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "bg3");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("file", PathUtils.toString(mBg3.file));
    trackElement.setAttribute("color", ColorUtils.toHtml(getLineColor()));
    trackElement.setAttribute("fill-color", ColorUtils.toHtml(getFillColor()));

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

    json.add("type", "bg3");
    json.add("name", getName());
    json.add("file", PathUtils.toString(mBg3.file));
    json.add("color", ColorUtils.toHtml(getLineColor()));
    json.add("fill-color", ColorUtils.toHtml(getFillColor()));

    json.endObject();
  }
}
