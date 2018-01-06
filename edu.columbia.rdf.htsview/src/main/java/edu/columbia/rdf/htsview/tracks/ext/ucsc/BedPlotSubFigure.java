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
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.TrackDisplayMode;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrackRegion;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrackRegions;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.FixedSubFigure;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

// TODO: Auto-generated Javadoc
/**
 * The Class BedPlotSubFigure.
 */
public class BedPlotSubFigure extends FixedSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m bed layer. */
  private BedPlotLayer mBedLayer;

  /** The m bed. */
  private UCSCTrack mBed;

  // private GenomicRegionsModel mGenomicModel;

  // private GenomicRegion mDisplayRegion;

  /**
   * Instantiates a new bed plot sub figure.
   *
   * @param bed
   *          the bed
   * @param color
   *          the color
   * @param titlePosition
   *          the title position
   */
  public BedPlotSubFigure(UCSCTrack bed, Color color, TitleProperties titlePosition) {
    mBed = bed;
    mBedLayer = new BedPlotLayer(color);

    // set the graph limits
    currentAxes().getX1Axis().getTitle().setText(null);
    currentAxes().getY1Axis().setLimits(0, 1);
    currentAxes().addChild(mBedLayer);

    Track.setTitle(mBed.getName(), titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param bed
   *          the bed
   * @param titlePosition
   *          the title position
   * @return the bed plot sub figure
   */
  public static BedPlotSubFigure create(UCSCTrack bed, TitleProperties titlePosition) {
    return create(bed, bed.getColor(), titlePosition);
  }

  /**
   * Creates the.
   *
   * @param bed
   *          the bed
   * @param color
   *          the color
   * @param titlePosition
   *          the title position
   * @return the bed plot sub figure
   */
  public static BedPlotSubFigure create(UCSCTrack bed, Color color, TitleProperties titlePosition) {

    // Now lets create a plot

    BedPlotSubFigure canvas = new BedPlotSubFigure(bed, color, titlePosition);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.FixedSubFigure#update(org.jebtk.
   * bioinformatics.genome.GenomicRegion, int, double, int, int, int,
   * java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
   */
  @Override
  public void update(GenomicRegion displayRegion, int resolution, double yMax, int width, int height, int margin,
      Color lineColor, Color fillColor, PlotStyle style) {

    List<UCSCTrackRegion> regions = UCSCTrackRegions.getFixedGapSearch(mBed.getRegions()).getFeatureSet(displayRegion);

    int n = 1;

    if (mBed.getDisplayMode() == TrackDisplayMode.FULL) {
      if (regions != null) {
        n += regions.size();
      }
    }

    height = BedPlotTrack.BLOCK_HEIGHT * n;

    super.update(displayRegion, resolution, yMax, width, height, margin, lineColor, fillColor, style);

    mBedLayer.update(regions, mBed.getColor(), mBed.getDisplayMode());

    // GenesPlotCanvasLayer.GAP;

    Axes.disableAllFeatures(currentAxes());

    // Need to make the title visible
    currentAxes().getTitle().getFontStyle().setVisible(true);
  }

  /*
   * @Override public void update(GenomicRegion displayRegion, int resolution,
   * Color lineColor, Color fillColor) {
   * 
   * int start = displayRegion.getStart();
   * 
   * int end = displayRegion.getEnd();
   * 
   * // The end cannot be equal to the start for display purposes if (end ==
   * start) { ++end; }
   * 
   * // set the graph limits and limit to the size of the chromosome
   * getCurrentAxes().getXAxis().setLimits(start, end);
   * getCurrentAxes().getXAxis().startEndTicksOnly();
   * 
   * // Create a series for each bedgraph in the group Plot plot =
   * getCurrentAxes().getCurrentPlot();
   * 
   * plot.setBarWidth(1);
   * 
   * // Use the default series for plotting. XYSeries series =
   * plot.getColumnSeriesGroup().getCurrent();
   * 
   * // Use the bedgraph to set the series color
   * series.getStyle().getLineStyle().setColor(lineColor);
   * series.getStyle().getFillStyle().setColor(fillColor);
   * series.addRegex("start"); series.addRegex("end");
   * 
   * List<UCSCTrackRegion> regions =
   * mBed.getRegions().getFeatureList(displayRegion.getChr(),
   * displayRegion.getStart(), displayRegion.getEnd());
   * 
   * if (regions != null) { DataFrame m = new AnnotatableMatrix(regions.size(),
   * 2);
   * 
   * m.setColumnNames("start", "end");
   * 
   * for (int i = 0; i < regions.size(); i++) { UCSCTrackRegion region =
   * regions.get(i);
   * 
   * // Each series consists of the start and end points of each peak. //
   * Duplicates are removed later. m.setValue(i, 0, region.getStart());
   * m.setValue(i, 1, region.getEnd()); }
   * 
   * plot.setMatrix(m); }
   * 
   * int h = Track.MEDIUM_TRACK_HEIGHT;
   * 
   * switch(mDisplayMode) { case FULL: if (regions != null) { h *= (1 +
   * regions.size()); }
   * 
   * break; default: break; }
   * 
   * getCurrentAxes().setInternalPlotSize(Track.PLOT_WIDTH, h); }
   */
}
