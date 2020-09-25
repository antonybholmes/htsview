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
import java.util.Arrays;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The class ReadsPlotTrack.
 */
public class ReadsPlotTrack extends SamplePlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant MAX_BLOCK_DEPTH.
   */
  // private static final int MAX_BLOCK_DEPTH =
  // SettingsService.getInstance().getInt("edb.reads.tracks.reads-plot.max-blocks");

  private static final Color DEFAULT_LINE_COLOR = SettingsService.getInstance()
      .getColor("edb.reads.tracks.reads-plot.strands.positive.line-color");

  /** The Constant DEFAULT_FILL_COLOR. */
  private static final Color DEFAULT_FILL_COLOR = SettingsService.getInstance()
      .getColor("edb.reads.tracks.reads-plot.strands.positive.fill-color");

  /** The Constant DEFAULT_NEG_STRAND_LINE_COLOR. */
  private static final Color DEFAULT_NEG_STRAND_LINE_COLOR = SettingsService
      .getInstance()
      .getColor("edb.reads.tracks.reads-plot.strands.negative.line-color");

  /** The Constant DEFAULT_NEG_STRAND_FILL_COLOR. */
  private static final Color DEFAULT_NEG_STRAND_FILL_COLOR = SettingsService
      .getInstance()
      .getColor("edb.reads.tracks.reads-plot.strands.negative.fill-color");

  /** The Constant MAX_READS_DISPLAY. */
  private static final int MAX_READS_DISPLAY = SettingsService.getInstance()
      .getInt("edb.reads.tracks.reads-plot.max-display-reads");

  /** The Constant DEFAULT_READ_HEIGHT. */
  private static final int DEFAULT_READ_HEIGHT = SettingsService.getInstance()
      .getInt("edb.reads.tracks.reads-plot.default-read-height");

  /** The Constant DEFAULT_GAP. */
  private static final int DEFAULT_GAP = SettingsService.getInstance()
      .getInt("edb.reads.tracks.reads-plot.default-gap");

  /**
   * The member read length.
   */
  private int mReadLength;

  /** The m neg strand fill color. */
  private Color mNegStrandFillColor;

  /** The m neg strand line color. */
  private Color mNegStrandLineColor;

  /** The m neg strand visible. */
  private boolean mNegStrandVisible;

  /** The m strand visible. */
  private boolean mStrandVisible;

  /** The m read height. */
  protected int mReadHeight;

  /** The m gap. */
  protected int mGap;

  /**
   * Instantiates a new reads plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   */
  public ReadsPlotTrack(Sample sample, SampleAssembly assembly) {
    this(sample, assembly, DEFAULT_LINE_COLOR, DEFAULT_FILL_COLOR,
        DEFAULT_NEG_STRAND_LINE_COLOR, DEFAULT_NEG_STRAND_FILL_COLOR);
  }

  /**
   * Instantiates a new reads plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param color the color
   */
  public ReadsPlotTrack(Sample sample, SampleAssembly assembly, Color color) {
    this(sample, assembly, color, color);
  }

  /**
   * Instantiates a new reads plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param color the color
   * @param negColor the neg color
   */
  public ReadsPlotTrack(Sample sample, SampleAssembly assembly, Color color,
      Color negColor) {
    this(sample, assembly, color, ColorUtils.getTransparentColor50(color),
        negColor, ColorUtils.getTransparentColor50(negColor));
  }

  /**
   * Instantiates a new reads plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param color the color
   * @param fillColor the fill color
   * @param negStrandLineColor the neg strand line color
   * @param negStrandFillColor the neg strand fill color
   */
  public ReadsPlotTrack(Sample sample, SampleAssembly assembly, Color color,
      Color fillColor, Color negStrandLineColor, Color negStrandFillColor) {
    this(sample, assembly, true, color, fillColor, true, negStrandLineColor,
        negStrandLineColor, DEFAULT_READ_HEIGHT, DEFAULT_GAP);

  }

  /**
   * Instantiates a new reads plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param strandVisible the strand visible
   * @param color the color
   * @param fillColor the fill color
   * @param negStrandVisible the neg strand visible
   * @param negStrandLineColor the neg strand line color
   * @param negStrandFillColor the neg strand fill color
   * @param readHeight the read height
   * @param gap the gap
   */
  public ReadsPlotTrack(Sample sample, SampleAssembly assembly,
      boolean strandVisible, Color color, Color fillColor,
      boolean negStrandVisible, Color negStrandLineColor,
      Color negStrandFillColor, int readHeight, int gap) {
    super(sample, assembly, color, fillColor, -1);

    try {
      mReadLength = assembly.getReadLength(sample);
    } catch (IOException e) {
      e.printStackTrace();
    }

    mStrandVisible = strandVisible;

    mNegStrandVisible = negStrandVisible;
    mNegStrandLineColor = negStrandLineColor;
    mNegStrandFillColor = negStrandFillColor;

    mReadHeight = readHeight;
    mGap = gap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.edb.reads.tracks.SamplePlotTrack#getType()
   */
  @Override
  public String getType() {
    return "Reads";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.edb.reads.app.tracks.sample.SamplePlotTrack#getName()
   */
  @Override
  public String getName() {
    return super.getName() + " reads";
  }

  /**
   * Gets the forward visible.
   *
   * @return the forward visible
   */
  public boolean getForwardVisible() {
    return mStrandVisible;
  }

  /**
   * Sets the forward visible.
   *
   * @param visible the new forward visible
   */
  public void setForwardVisible(boolean visible) {
    mStrandVisible = visible;
  }

  /**
   * Gets the neg visible.
   *
   * @return the neg visible
   */
  public boolean getNegVisible() {
    return mNegStrandVisible;
  }

  /**
   * Sets the neg visible.
   *
   * @param visible the new neg visible
   */
  public void setAntiSenseVisible(boolean visible) {
    mNegStrandVisible = visible;
  }

  /**
   * Gets the neg fill color.
   *
   * @return the neg fill color
   */
  public Color getNegFillColor() {
    return mNegStrandFillColor;
  }

  /**
   * Sets the neg fill color.
   *
   * @param color the new neg fill color
   */
  public void setAntiSenseFillColor(Color color) {
    mNegStrandFillColor = color;
  }

  /**
   * Gets the neg line color.
   *
   * @return the neg line color
   */
  public Color getNegLineColor() {
    return mNegStrandLineColor;
  }

  /**
   * Sets the neg line color.
   *
   * @param color the new neg line color
   */
  public void setAntiSenseLineColor(Color color) {
    mNegStrandLineColor = color;
  }

  /**
   * Sets the read height.
   *
   * @param readHeight the new read height
   */
  public void setReadHeight(int readHeight) {
    mReadHeight = readHeight;
  }

  /**
   * Gets the read height.
   *
   * @return the read height
   */
  public int getReadHeight() {
    return mReadHeight;
  }

  /**
   * Sets the gap between reads.
   *
   * @param gap the new gap between reads.
   */
  public void setGap(int gap) {
    mGap = gap;
  }

  /**
   * Returns the gap between reads.
   *
   * @return the gap between reads in pixels.
   */
  public int getGap() {
    return mGap;
  }

  @Override
  public double getYMax(boolean normalize) {
    return -1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.apps.edb.reads.tracks.SamplePlotTrack#getGraph(java.lang.
   * String, edu.columbia.rdf.lib.bioinformatics.plot.figure.FigureStyle,
   * edu.columbia.rdf.apps.edb.reads.TitlePosition)
   */
  @Override
  public TrackSubFigure createGraph(Genome genome,
      TitleProperties titlePosition) throws IOException {
    mSubFigure = ReadsPlotSubFigure
        .create(getName(), mReadLength, titlePosition);

    Axes axes = mSubFigure.currentAxes();

    // axes.setInternalPlotSize(PLOT_SIZE);

    // axes.setInternalPlotSize(Track.PLOT_WIDTH, ReadsPlotLayer.BLOCK *
    // MAX_BLOCK_DEPTH);

    int right;

    switch (titlePosition.getPosition()) {
    case COMPACT_RIGHT:
      right = rightTitleWidth(getName());
      axes.setMargins(SMALL_MARGIN, MARGINS.getLeft(), SMALL_MARGIN, right);
      break;
    case RIGHT:
      right = rightTitleWidth(getName());
      axes.setMargins(SMALL_MARGIN, MARGINS.getLeft(), SMALL_MARGIN, right);
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
   * edu.columbia.rdf.apps.edb.reads.tracks.SamplePlotTrack#updateGraph(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion, int, boolean)
   */
  @Override
  public TrackSubFigure updateGraph(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {

    int[] starts = mAssembly.getStarts(mSample, genome, displayRegion, resolution);

    Strand[] strands = mAssembly.getStrands(mSample, genome, displayRegion, resolution);

    if (starts.length > MAX_READS_DISPLAY) {
      starts = Arrays.copyOf(starts, MAX_READS_DISPLAY); // CollectionUtils.subSample(starts,
                                                         // MAX_READS_DISPLAY);
      strands = Arrays.copyOf(strands, MAX_READS_DISPLAY); // CollectionUtils.subSample(strands,
                                                           // MAX_READS_DISPLAY);
    }

    mSubFigure.update(genome, displayRegion, resolution, width, height, margin);

    ((ReadsPlotSubFigure) mSubFigure).setStarts(starts,
        strands,
        mStrandVisible,
        mLineColor,
        mFillColor,
        mNegStrandVisible,
        mNegStrandLineColor,
        mNegStrandFillColor,
        mReadHeight,
        mGap);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.edb.reads.app.tracks.sample.SamplePlotTrack#edit(org.abh.
   * common.ui.window.ModernWindow)
   */
  @Override
  public void edit(ModernWindow parent) {
    ReadsPlotTrackEditDialog dialog = new ReadsPlotTrackEditDialog(parent,
        this);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setName(dialog.getName());

    setForwardVisible(dialog.getForwardVisible());
    setLineColor(dialog.getLineColor());
    setFillColor(dialog.getFillColor());

    setAntiSenseVisible(dialog.getNegVisible());
    setAntiSenseLineColor(dialog.getNegLineColor());
    setAntiSenseFillColor(dialog.getNegFillColor());

    setReadHeight(dialog.getReadHeight());
    setGap(dialog.getGap());
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.edb.reads.tracks.SamplePlotTrack#toXml()
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "reads");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("id", Integer.toString(mSample.getId()));
    trackElement.setAttribute("color", ColorUtils.toHtml(getLineColor()));
    trackElement.setAttribute("fill-color", ColorUtils.toHtml(getFillColor()));

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.edb.reads.tracks.SamplePlotTrack#toJson()
   */
  @Override
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "reads");
    json.add("name", getName());
    json.add("id", mSample.getId());
    json.add("visible", getForwardVisible());
    json.add("color", ColorUtils.toHtml(getLineColor()));
    json.add("fill-color", ColorUtils.toHtml(getFillColor()));
    json.add("anti-sense-visible", getNegVisible());
    json.add("anti-sense-color", ColorUtils.toHtml(getNegLineColor()));
    json.add("anti-sense-fill-color", ColorUtils.toHtml(getNegFillColor()));
    json.add("read-height", mReadHeight);
    json.add("gap", mGap);

    json.endObject();
  }
}
