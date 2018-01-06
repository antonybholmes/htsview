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
package edu.columbia.rdf.htsview.tracks.abi;

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.htsview.ext.abi.ABITrace;
import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;
import edu.columbia.rdf.htsview.tracks.TracksFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class ABIPlotTrack.
 */
public class ABIPlotTrack extends GraphPlotTrack {

  /** The m height. */
  private int mHeight = -1;

  /** The m fill color. */
  protected Color mFillColor;

  /** The m line color. */
  protected Color mLineColor;

  /** The m Y max. */
  private double mYMax = 1;

  /** The m auto Y. */
  private boolean mAutoY = true;

  /** The m normalize. */
  private boolean mNormalize = true;

  /**
   * Tracks default to having common y axis.
   */
  private boolean mCommon = true;

  /** The m common height. */
  private boolean mCommonHeight = true;

  /** The m style. */
  private PlotStyle mStyle = PlotStyle.JOINED_SMOOTH;

  // private ABIPlot mPlot;

  /** The m name. */
  private String mName;

  /** The m trace. */
  private ABITrace mTrace;

  /** The m region. */
  private GenomicRegion mRegion;

  /** The Constant DEFAULT_COLOR. */
  private static final Color DEFAULT_COLOR = SettingsService.getInstance()
      .getAsColor("edb.reads.tracks.sample-plot.default-color");

  /**
   * Instantiates a new ABI plot track.
   *
   * @param trace
   *          the trace
   */
  public ABIPlotTrack(ABITrace trace) {
    this(trace, DEFAULT_COLOR);
  }

  /**
   * Instantiates a new ABI plot track.
   *
   * @param trace
   *          the trace
   * @param color
   *          the color
   */
  public ABIPlotTrack(ABITrace trace, Color color) {
    this(trace, color, ColorUtils.getTransparentColor50(color), PLOT_SIZE.height);
  }

  /**
   * Instantiates a new ABI plot track.
   *
   * @param trace
   *          the trace
   * @param lineColor
   *          the line color
   * @param fillColor
   *          the fill color
   */
  public ABIPlotTrack(ABITrace trace, Color lineColor, Color fillColor) {
    this(trace, lineColor, fillColor, PLOT_SIZE.height);
  }

  /**
   * Instantiates a new ABI plot track.
   *
   * @param trace
   *          the trace
   * @param lineColor
   *          the line color
   * @param fillColor
   *          the fill color
   * @param height
   *          the height
   */
  public ABIPlotTrack(ABITrace trace, Color lineColor, Color fillColor, int height) {
    this(trace.getName(), trace, lineColor, fillColor, height);
  }

  /**
   * Instantiates a new ABI plot track.
   *
   * @param name
   *          the name
   * @param trace
   *          the trace
   * @param lineColor
   *          the line color
   * @param fillColor
   *          the fill color
   * @param height
   *          the height
   */
  public ABIPlotTrack(String name, ABITrace trace, Color lineColor, Color fillColor, int height) {
    mName = name;
    mTrace = trace;

    setLineColor(lineColor);
    setFillColor(fillColor != null ? fillColor : lineColor);
    setHeight(height);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getName()
   */
  @Override
  public String getName() {
    return mName; // mSample.getName();
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    mName = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getFillColor()
   */
  @Override
  public Color getFillColor() {
    return mFillColor;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setFillColor(java.awt.Color)
   */
  @Override
  public void setFillColor(Color color) {
    mFillColor = color;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getLineColor()
   */
  @Override
  public Color getLineColor() {
    return mLineColor;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setLineColor(java.awt.Color)
   */
  @Override
  public void setLineColor(Color color) {
    mLineColor = color;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getYMax(boolean)
   */
  @Override
  public double getYMax(boolean normalize) {
    if (getAutoY()) {
      return autoY();
    } else {
      return mYMax;
    }
  }

  /**
   * Auto Y.
   *
   * @return the double
   */
  private double autoY() {
    int max = 0;

    for (char base : ABITrace.BASES) {
      for (int s = mRegion.getStart(); s <= mRegion.getEnd(); ++s) {
        int y = mTrace.getColor(base, s);

        if (y > max) {
          max = y;
        }
      }
    }

    return Math.max(TracksFigure.MIN_MAX_Y, max);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setYMax(double)
   */
  @Override
  public void setYMax(double yMax) {
    mYMax = yMax;
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
   * @see edu.columbia.rdf.htsview.tracks.Track#getNormalizeY()
   */
  @Override
  public boolean getNormalizeY() {
    return mNormalize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setNormalizeY(boolean)
   */
  @Override
  public void setNormalizeY(boolean normalize) {
    mNormalize = normalize;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getCommonY()
   */
  @Override
  public boolean getCommonY() {
    return mCommon;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setCommonY(boolean)
   */
  @Override
  public void setCommonY(boolean common) {
    mCommon = common;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getCommonHeight()
   */
  @Override
  public boolean getCommonHeight() {
    return mCommonHeight;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setCommonHeight(boolean)
   */
  @Override
  public void setCommonHeight(boolean common) {
    mCommonHeight = common;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getHeight()
   */
  @Override
  public int getHeight() {
    return mHeight;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setHeight(int)
   */
  @Override
  public void setHeight(int height) {
    mHeight = height;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getType()
   */
  @Override
  public String getType() {
    return "Sample";
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
    mSubFigure = ABISubFigure.create(getName(), mTrace, mStyle, titlePosition);

    Axes axes = mSubFigure.currentAxes();

    axes.setInternalSize(PLOT_SIZE);

    int right;

    switch (titlePosition.getPosition()) {
    case COMPACT_RIGHT:
      right = rightTitleWidth(getName());
      axes.setMargins(MARGIN, MARGINS.getLeft(), MARGIN, right);
      break;
    case RIGHT:
      right = rightTitleWidth(getName());
      axes.setMargins(MARGIN, MARGINS.getLeft(), MEDIUM_MARGIN, right);
      break;
    default:
      axes.setMargins(MARGINS);
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
    mRegion = displayRegion;

    if (!getCommonHeight()) {
      height = mHeight;
    }

    mSubFigure.update(displayRegion, resolution, mYMax, width, height, margin, mLineColor, mFillColor, mStyle);

    mSubFigure.currentAxes().getTitle().setText(mName);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.GraphPlotTrack#getGraph()
   */
  @Override
  public TrackSubFigure getGraph() {
    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#getBedGraph(org.jebtk.bioinformatics.
   * genome.GenomicRegion, int)
   */
  @Override
  public UCSCTrack getBedGraph(GenomicRegion displayRegion, int resolution) throws IOException {
    return getBedGraph(displayRegion, resolution, mNormalize);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.GraphPlotTrack#edit(org.abh.common.ui.window.
   * ModernWindow)
   */
  @Override
  public void edit(ModernWindow parent) {
    ABIEditDialog dialog = new ABIEditDialog(parent, this);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setName(dialog.getName());
    setLineColor(dialog.getLineColor());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toXml(org.w3c.dom.Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "ab1");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("color", ColorUtils.toHtml(getLineColor()));
    trackElement.setAttribute("fill-color", ColorUtils.toHtml(getFillColor()));
    trackElement.setAttribute("height", Integer.toString(getHeight()));

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
    commonJson(json);

    json.add("type", "ab1");

    json.endObject();
  }

  /**
   * Common json.
   *
   * @param json
   *          the json
   */
  public void commonJson(JsonBuilder json) {
    json.startObject();

    json.add("name", getName());
    json.add("line-color", ColorUtils.toHtml(getLineColor()));
    json.add("fill-color", ColorUtils.toHtml(getFillColor()));
    json.add("height", getHeight());
    json.add("common-height", getCommonHeight());
    json.add("auto-y", getAutoY());
    json.add("common-y", getCommonY());
    json.add("normalize-y", getNormalizeY());
    json.add("y-max", getYMax(false));
  }
}
