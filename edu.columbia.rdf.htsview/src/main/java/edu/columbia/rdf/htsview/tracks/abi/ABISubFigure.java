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
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.graphplot.figure.LayerType;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.plotbox.PlotBox;

import edu.columbia.rdf.htsview.ext.abi.ABITrace;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * The Class ABISubFigure.
 */
public class ABISubFigure extends TrackSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  // private UCSCTrack mBedGraph;

  /** The m style. */
  private PlotStyle mStyle = null;

  /** The style not set. */
  private boolean styleNotSet = false;

  /**
   * Creates the.
   *
   * @param name the name
   * @param trace the trace
   * @param style the style
   * @param titlePosition the title position
   * @return the ABI sub figure
   */
  public static ABISubFigure create(String name,
      ABITrace trace,
      PlotStyle style,
      TitleProperties titlePosition) {
    ABISubFigure subFigure = new ABISubFigure();

    Axes axes = subFigure.currentAxes();

    for (char base : ABITrace.BASES) {
      Color color;

      switch (base) {
      case 'C':
        color = Color.BLUE;
        break;
      case 'G':
        color = Color.BLACK;
        break;
      case 'T':
        color = Color.RED;
        break;
      default:
        color = Color.GREEN;
        break;
      }

      Plot plot = new ABIPlot("ABI " + base, trace, base, color);

      plot.getAllSeries().add(XYSeries.createXYSeries("Points", Color.BLACK));
      plot.getAllSeries().getCurrent().getMarker().setVisible(false);
      plot.setStyle(style);
      axes.addChild(plot);
    }

    Track.setTitle(name, titlePosition, axes);

    return subFigure;
  }

  // public void setBedGraph(UCSCTrack bedGraph) {
  // mBedGraph = bedGraph;
  // }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.TrackSubFigure#update(org.jebtk.
   * bioinformatics.genome.GenomicRegion, int, double, int, int, int,
   * java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
   */
  @Override
  public void update(GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      Color lineColor,
      Color fillColor,
      PlotStyle style) {

    // int start = displayRegion.getStart();

    // int end = displayRegion.getEnd();

    // The end cannot be equal to the start for display purposes
    // if (end == start) {
    // ++end;
    // }

    // set the graph limits and limit to the size of the chromosome
    // getCurrentAxes().getXAxis().setLimits(start, end);
    // getCurrentAxes().getXAxis().startEndTicksOnly();

    super.update(displayRegion,
        resolution,
        yMax,
        width,
        height,
        margin,
        lineColor,
        fillColor,
        style);

    if (styleNotSet || mStyle != style) {
      setStyle(style);
      mStyle = style;
      styleNotSet = false;
    }

    currentAxes().getX1Axis().startEndTicksOnly();

    // System.err.println("regions " + start + " " + end + " " +
    // getCurrentAxes().toPlotX(end));

    // if (mBedGraph == null) {
    // return;
    // }

    // Create a series for each bedgraph in the group

    List<PlotBox> layers = currentAxes().getChild(GridLocation.CENTER)
        .getByType(LayerType.PLOT);

    for (PlotBox layer : layers) {
      if (layer instanceof ABIPlot) {
        ABIPlot p = (ABIPlot) layer;

        p.update(displayRegion,
            resolution,
            yMax,
            width,
            height,
            margin,
            lineColor,
            fillColor,
            style);
      }
    }

    /*
     * mPlot.setBarWidth(1);
     * 
     * // Use the default series for plotting. XYSeries series =
     * mPlot.getAllSeries().getCurrent();
     * 
     * // Use the bedgraph to set the series color if
     * (!lineColor.equals(series.getStyle().getLineStyle().getColor())) {
     * series.getStyle().getLineStyle().setColor(lineColor); }
     * 
     * if (!fillColor.equals(series.getStyle().getFillStyle().getColor())) {
     * series.getStyle().getFillStyle().setColor(fillColor); }
     * 
     * //series.addRegex("x"); //series.addRegex("y");
     * 
     * List<UCSCTrackRegion> regions =
     * UCSCTrackRegions.getFixedGapSearch(mBedGraph.getRegions()).getFeatureSet(
     * displayRegion.getChr(), displayRegion.getStart(),
     * displayRegion.getEnd());
     * 
     * mPlot.setMatrix(new BedGraphRegionMatrix(regions));
     * 
     * for (MovableLayer l : plots) { Plot p = (Plot)l;
     * 
     * System.err.println("sdfsdf " + p.getId() + " " + p.getName() + " " +
     * (p.getMatrix() == null) + " " +
     * p.getAllSeries().getCurrent().getColor()); }
     */
  }
}
