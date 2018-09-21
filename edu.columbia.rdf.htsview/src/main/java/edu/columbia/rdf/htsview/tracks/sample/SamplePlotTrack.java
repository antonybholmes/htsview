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
package edu.columbia.rdf.htsview.tracks.sample;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.bioinformatics.ext.ucsc.BedGraphRegion;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrackRegion;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrackRegions;
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

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;
import edu.columbia.rdf.htsview.tracks.TracksFigure;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedGraphPlot;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedGraphSubFigure;

/**
 * The Class SamplePlotTrack.
 */
public class SamplePlotTrack extends GraphPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** The m height. */
  private int mHeight = -1;

  /** The m sample. */
  protected Sample mSample;

  /** The m assembly. */
  protected SampleAssembly mAssembly;

  /** The m fill color. */
  protected Color mFillColor;

  /** The m line color. */
  protected Color mLineColor;

  /** The m input sample. */
  protected Sample mInputSample = null;

  /** The m input assembly. */
  protected SampleAssembly mInputAssembly = null;

  /** The m subtract. */
  private boolean mSubtract;

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
  private PlotStyle mStyle = PlotStyle.FILLED_SMOOTH;

  /** The m plot. */
  private BedGraphPlot mPlot;

  /** The m name. */
  private String mName;

  /** The m region. */
  private GenomicRegion mRegion;

  /** The m resolution. */
  private int mResolution;

  private UCSCTrack mBedGraph;

  /** The Constant DEFAULT_COLOR. */
  private static final Color DEFAULT_COLOR = SettingsService.getInstance()
      .getColor("edb.reads.tracks.sample-plot.default-color");

  /**
   * Instantiates a new sample plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   */
  public SamplePlotTrack(Sample sample, SampleAssembly assembly) {
    this(sample, assembly, DEFAULT_COLOR);
  }

  /**
   * Instantiates a new sample plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param color the color
   */
  public SamplePlotTrack(Sample sample, SampleAssembly assembly, Color color) {
    this(sample, assembly, color, ColorUtils.tint(color, 0.5),
        PLOT_SIZE.height);
  }

  /**
   * Instantiates a new sample plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param lineColor the line color
   * @param fillColor the fill color
   */
  public SamplePlotTrack(Sample sample, SampleAssembly assembly,
      Color lineColor, Color fillColor) {
    this(sample, assembly, lineColor, fillColor, PLOT_SIZE.height);
  }

  /**
   * Instantiates a new sample plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param lineColor the line color
   * @param fillColor the fill color
   * @param height the height
   */
  public SamplePlotTrack(Sample sample, SampleAssembly assembly,
      Color lineColor, Color fillColor, int height) {
    this(sample.getName(), sample, assembly, lineColor, fillColor, height);
  }

  /**
   * Instantiates a new sample plot track.
   *
   * @param name the name
   * @param sample the sample
   * @param assembly the assembly
   * @param lineColor the line color
   * @param fillColor the fill color
   * @param height the height
   */
  public SamplePlotTrack(String name, Sample sample, SampleAssembly assembly,
      Color lineColor, Color fillColor, int height) {
    mName = name;
    mSample = sample;
    mAssembly = assembly;

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
    if (mBedGraph == null) {
      try {

        mBedGraph = getBedGraph(mRegion, mResolution, normalize);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    // This track is not a bedgraph
    if (mBedGraph == null) {
      return TracksFigure.MIN_MAX_Y;
    }

    List<UCSCTrackRegion> regions = UCSCTrackRegions
        .getFixedGapSearch(mBedGraph.getRegions()).getFeatureSet(mRegion);

    double y = 0;

    for (UCSCTrackRegion region : regions) {
      double value = ((BedGraphRegion) region).getValue();

      if (value > y) {
        y = value;
      }
    }

    return Math.max(TracksFigure.MIN_MAX_Y, y);
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

  /**
   * Gets the sample.
   *
   * @return the sample
   */
  public Sample getSample() {
    return mSample;
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

  /**
   * Gets the assembly.
   *
   * @return the assembly
   */
  public SampleAssembly getAssembly() {
    return mAssembly;
  }

  /**
   * Sets the subtract input.
   *
   * @param subtract the new subtract input
   */
  public void setSubtractInput(boolean subtract) {
    mSubtract = subtract;
  }

  /**
   * Sets the input.
   *
   * @param inputSample the input sample
   * @param inputAssembly the input assembly
   */
  public void setInput(Sample inputSample, SampleAssembly inputAssembly) {
    mInputSample = inputSample;
    mInputAssembly = inputAssembly;

    setSubtractInput(true);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(String genome,
      TitleProperties titlePosition) throws IOException {
    mSubFigure = BedGraphSubFigure.create(getName(), mStyle, titlePosition);

    Axes axes = mSubFigure.currentAxes();

    // Keep track of the current plot we have created
    mPlot = (BedGraphPlot) axes.findByName("Plot 1");

    // mPlot.getPlotLayers().addChild(new
    // MouseHighlightPeakPlotLayer(mPlot.getAllSeries().getCurrent().getName()),
    // 1000);

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
  public TrackSubFigure updateGraph(GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {
    mRegion = displayRegion;
    mResolution = resolution;

    mBedGraph = getBedGraph(displayRegion, resolution, mNormalize);

    mPlot.setBedGraph(mBedGraph);

    if (!getCommonHeight()) {
      height = mHeight;
    }

    mSubFigure.update(displayRegion,
        resolution,
        getYMax(getNormalizeY()),
        width,
        height,
        margin,
        mLineColor,
        mFillColor,
        mStyle);

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
  public UCSCTrack getBedGraph(GenomicRegion displayRegion, int resolution)
      throws IOException {
    return getBedGraph(displayRegion, resolution, mNormalize);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#getBedGraph(org.jebtk.bioinformatics.
   * genome.GenomicRegion, int, boolean)
   */
  @Override
  public UCSCTrack getBedGraph(GenomicRegion displayRegion,
      int resolution,
      boolean normalize) throws IOException {
    List<Integer> counts = mAssembly
        .getCounts(mSample, displayRegion, resolution);

    // Subtract the input if desired
    if (mSubtract && mInputSample != null) {
      List<Integer> mInputCounts = mInputAssembly
          .getCounts(mInputSample, displayRegion, resolution);

      for (int i = 0; i < counts.size(); ++i) {
        counts.set(i, Math.max(0, counts.get(i) - mInputCounts.get(i)));
      }
    }

    int mappedReads = mAssembly.getMappedReads(mSample);

    // per million
    double scaleFactor;

    if (normalize && mappedReads != -1) {
      scaleFactor = 1000000.0 / (double) mappedReads;
    } else {
      scaleFactor = 1;
    }

    String id = mSample.getName() + " " + displayRegion.toString();

    BedGraph bedGraph = new BedGraph(id, id, mFillColor);

    int start = displayRegion.getStart() / resolution * resolution;

    double normalizedCount;

    for (int count : counts) {
      normalizedCount = count * scaleFactor;

      BedGraphRegion br = new BedGraphRegion(displayRegion.getChr(), start,
          start + resolution - 1, normalizedCount);

      bedGraph.getRegions().add(br);

      start += resolution;
    }

    return bedGraph;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.GraphPlotTrack#edit(org.abh.common.ui.
   * window. ModernWindow)
   */
  @Override
  public void edit(ModernWindow parent) {
    SamplePlotTrackEditDialog dialog = new SamplePlotTrackEditDialog(parent,
        this, mAssembly);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setName(dialog.getName());
    setLineColor(dialog.getLineColor());
    setFillColor(dialog.getFillColor());
    setHeight(dialog.getTrackHeight());
    setYMax(dialog.getYMax());

    // TODO test
    setAutoY(dialog.getAutoY());

    setNormalizeY(dialog.getNormalizeY());

    setCommonY(dialog.getCommonY());
    setCommonHeight(dialog.getCommonHeight());

    setStyle(dialog.getStyle());

    setInput(dialog.getInputSample(), dialog.getInputAssembly());
    setSubtractInput(dialog.getSubtractInput());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toXml(org.w3c.dom.Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "sample");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("id", Integer.toString(mSample.getId()));
    trackElement.setAttribute("color", ColorUtils.toHtml(getLineColor()));
    trackElement.setAttribute("fill-color", ColorUtils.toHtml(getFillColor()));
    trackElement.setAttribute("height", Integer.toString(getHeight()));

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
    commonJson(json);

    json.add("type", "sample");

    json.endObject();
  }

  /**
   * Common json.
   *
   * @param json the json
   */
  public void commonJson(JsonBuilder json) {
    json.startObject();

    json.add("name", getName());
    json.add("id", mSample.getId());
    json.add("line-color", ColorUtils.toHtml(getLineColor()));
    json.add("fill-color", ColorUtils.toHtml(getFillColor()));
    json.add("height", getHeight());
    json.add("common-height", getCommonHeight());
    json.add("auto-y", getAutoY());
    json.add("common-y", getCommonY());
    json.add("normalize-y", getNormalizeY());
    json.add("y-max", getYMax(getNormalizeY()));
  }

  /**
   * Gets the input sample.
   *
   * @return the input sample
   */
  public Sample getInputSample() {
    return mInputSample;
  }

  /**
   * Gets the subtract input.
   *
   * @return the subtract input
   */
  public boolean getSubtractInput() {
    return mSubtract;
  }

  /**
   * Gets the input assembly.
   *
   * @return the input assembly
   */
  public SampleAssembly getInputAssembly() {
    return mInputAssembly;
  }
}
