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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Axis;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.plotbox.PlotBoxRowLayout;

/**
 * The Class TracksFigure.
 */
public class TracksFigure extends Figure { // Figure { // PlotBoxColumn {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * The smallest value the max y can be (has to be non-zero).
   */
  public static final double MIN_MAX_Y = 0.1;

  /** The m tracks. */
  private TrackTree mTracks = new TrackTree();

  /**
   * Instantiates a new tracks figure.
   *
   * @param genomicModel the genomic model
   * @param sizes the sizes
   */
  public TracksFigure() {
    super("Tracks Figure", new PlotBoxRowLayout());

    // setLayout(new FigureLayoutVBox());

    // setMargins(50);

    // getGraphSpace().getGraphProperties().addChangeListener(new PlotEvents());

    // the default plot size
    // getSubPlotLayout().setPlotSize(new Dimension(1200, 100));
    // getSubPlotLayout().setMargins(50, 250, 50, 50);

    // Maximize the space, but allow a margin for drawing genes
    // getFigureProperties().setMargins(0, 0, 200, 0);

    // Don't draw grid lines
    // getFigureProperties().getXAxis().getGrid().setVisible(false);
    // getFigureProperties().getY1Axis().getGrid().setVisible(false);

    // addCanvasMouseListener(new CanvasMouseEvents());
  }

  /**
   * Sets the tracks.
   *
   * @param tracks the tracks
   * @param genome the genome
   * @param style the style
   * @param titlePosition the title position
   * @throws Exception the exception
   */
  public void setTracks(TrackTree tracks,
      Genome genome,
      PlotStyle style,
      TitleProperties titlePosition) throws Exception {
    mTracks = tracks;

    List<SubFigure> subFigures = new ArrayList<SubFigure>(100);

    //
    // Raw data tracks
    //

    for (TreeNode<Track> node : tracks) {
      Track track = node.getValue();

      // track.setStyle(style);

      TrackSubFigure trackSubFigure = track.createGraph(genome, titlePosition);

      Axes axes = trackSubFigure.currentAxes();

      // If an axes object has previously been a child, reset the
      // properties so we can now see the axes
      Axes.enableAllFeatures(axes);

      axes.getX1Axis().getGrid().setVisible(false);
      axes.getX1Axis().getTitle().getFontStyle().setVisible(false);

      axes.getY1Axis().getGrid().setVisible(false);
      axes.getY1Axis().getTitle().getFontStyle().setVisible(false);
      axes.getY1Axis().startEndTicksOnly();

      Axis.enableAllFeatures(axes.getX2Axis(), false);
      Axis.enableAllFeatures(axes.getY2Axis(), false);

      axes.getX1Axis().getTicks().getMajorTicks().getFontStyle().setVisible(
          titlePosition.getPosition() != TitlePosition.COMPACT_RIGHT);

      List<TreeNode<Track>> children = node.getChildrenAsList();

      // Add all children as sub plots
      for (TreeNode<Track> child : children) {
        TrackSubFigure subFigure = child.getValue().createGraph(genome,
            titlePosition);

        // Stop sub titles appearing
        // f.getCurrentAxes().getTitle().getFontStyle().setVisible(false);

        // Axes.disableAllFeatures(f.getCurrentAxes());

        // trackFigure.getAxesZModel().setZ(subFigure.getCurrentAxes());

        // Add the plots from this sub figure to the axis of the
        // current figure
        Plot plot = subFigure.currentAxes().currentPlot();

        axes.addChild(plot);
      }

      //
      // Set some properties based on the parent
      //

      // axes.getXAxis().getLineStyle().copy(axes.getXAxis().getLineStyle());
      // axes.getY1Axis().getLineStyle().copy(axes.getY1Axis().getLineStyle());

      // axes.getXAxis().getTitle().copy(axes.getXAxis().getTitle());
      // axes.getY1Axis().getTitle().copy(axes.getY1Axis().getTitle());

      // peakPlot.getGraphProperties().getXAxis().getMajorTicks().copy(gp.getXAxis().getMajorTicks());
      // peakPlot.getGraphProperties().getYAxis().getMajorTicks().copy(gp.getYAxis().getMajorTicks());

      // axes.getXAxis().getGrid().copy(axes.getXAxis().getGrid());
      // axes.getY1Axis().getGrid().copy(axes.getY1Axis().getGrid());

      // peakPlot.getGraphSpace().getLayoutProperties().setPlotSize(new
      // Dimension(PLOT_WIDTH, 100));
      // peakPlot.getGraphSpace().getLayoutProperties().setMargins(50, 250, 50,
      // 50);

      subFigures.add(trackSubFigure);
    }

    // TODO: do we need these?
    // getSubFigureZModel().clearUnreservedLayers();
    // getSubFigureZModel().addChild(subFigures);

    setChildren(subFigures);
  }

  /*
   * public void refreshY(GenomicRegion displayRegion, int resolution, double
   * yMax, boolean autoMaxY, boolean commonYScale, boolean normalize, int width,
   * int height) throws IOException, ParseException { if (autoMaxY &&
   * commonYScale) { yMax = getCommonYMax(mTracks, displayRegion, resolution,
   * normalize); }
   * 
   * // // Raw data tracks //
   * 
   * TrackSubFigure subFigure;
   * 
   * for (TreeNode<Track> track : mTracks) {
   * 
   * // Update a new plot subFigure =
   * track.getValue().updateGraph(displayRegion, resolution, normalize, width,
   * height);
   * 
   * // Set the Y max for each plot if (autoMaxY && !commonYScale) { yMax =
   * getYMax(track, displayRegion, resolution, normalize); }
   * 
   * for (int z : subFigure.getAxesZModel()) { Axes axes =
   * subFigure.getAxesZModel().getAtZ(z);
   * 
   * // // Set all plots to have the same y axis //
   * 
   * // round y max up so it is aesthetically more pleasing.
   * axes.getY1Axis().setLimits(0, Math.ceil(yMax));
   * axes.getY1Axis().startEndTicksOnly(); }
   * 
   * //figure.setForwardCanvasEventsEnabled(true); }
   * 
   * update(displayRegion, resolution, yMax, autoMaxY, commonYScale, normalize,
   * width, height);
   * 
   * 
   * 
   * fireCanvasRedraw(); }
   */

  /**
   * Called when the location or scale changes, but not the actual tracks on
   * display. This reduces re-laying out components on each redraw.
   *
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param width the width
   * @param height the height
   * @param margin the margin
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void refresh(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {

    GenomicRegion r = new GenomicRegion(genome, displayRegion);

    update(r, resolution, width, height, margin);

    fireChanged();
  }

  /**
   * Update.
   *
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param width the width
   * @param height the height
   * @param margin the margin
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public void update(GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {

    //
    // Raw data tracks
    //

    for (TreeNode<Track> node : mTracks) {
      Track track = node.getValue();

      // Update a new plot
      track.updateGraph(displayRegion, resolution, width, height, margin);

      List<TreeNode<Track>> children = node.getChildrenAsList();

      // update the children of this plot
      for (TreeNode<Track> child : children) {
        TrackSubFigure cf = child.getValue()
            .updateGraph(displayRegion, resolution, width, height, margin);

        cf.currentAxes().getTitle().setVisible(false);
      }
    }

    double autoYMax = getCommonYMax(mTracks, displayRegion, resolution, false);

    double autoNormYMax = getCommonYMax(mTracks,
        displayRegion,
        resolution,
        true);

    double y;

    for (TreeNode<Track> node : mTracks) {

      Track track = node.getValue();

      if (track.getCommonY()) {
        if (track.getNormalizeY()) {
          y = autoNormYMax;
        } else {
          y = autoYMax;
        }
      } else {
        y = getYMax(node, displayRegion, resolution, track.getNormalizeY());
      }

      // Update a new plot
      TrackSubFigure subFigure = track.getGraph();

      // Set the Y max for each plot
      // if (auto && !common) {
      // y = getYMax(node, displayRegion, resolution, norm);
      // }

      /*
       * } else { if (track.getAutoY()) { yMax = getYMax(node, displayRegion,
       * resolution, track.getNormalizeY()); } else { yMax = track.getYMax(); }
       * }
       */

      // System.err.println("ymax " + yMax + " " + " " + auto + " " + common + "
      // " +
      // norm);

      List<TreeNode<Track>> children = node.getChildrenAsList();

      // update the children of this plot
      for (TreeNode<Track> child : children) {
        TrackSubFigure cf = child.getValue()
            .updateGraph(displayRegion, resolution, width, height, margin);

        cf.currentAxes().getTitle().getFontStyle().setVisible(false);
      }

      Axes axes = subFigure.currentAxes();

      if (y > 1) {
        axes.getY1Axis().setLimits(0, Math.ceil(y));
      }

      axes.getY1Axis().startEndTicksOnly();
    }
  }

  //
  // Static methods
  //

  /**
   * Work out the max y of the plots for common scales.
   *
   * @param tracks the tracks
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param normalize the normalize
   * @return the common Y max
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static double getCommonYMax(TrackTree tracks,
      GenomicRegion displayRegion,
      int resolution,
      boolean normalize) throws IOException {
    double y = 0;

    for (TreeNode<Track> track : tracks) {
      double tmpy = getYMax(track, displayRegion, resolution, normalize);

      if (tmpy > y) {
        y = tmpy;
      }
    }

    y = Math.max(MIN_MAX_Y, y);

    // System.err.println("max auto y " + y);

    return y;
  }

  /**
   * Gets the y max.
   *
   * @param track the track
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param normalize the normalize
   * @return the y max
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private static double getYMax(TreeNode<Track> track,
      GenomicRegion displayRegion,
      int resolution,
      boolean normalize) throws IOException {
    double max = 0;
    double y;

    List<TreeNode<Track>> children = track.getChildrenAsList();

    children.add(track);

    for (TreeNode<Track> child : children) {
      y = child.getValue().getYMax(normalize);

      if (y > max) {
        max = y;
      }

      /*
       * UCSCTrack bedGraph = child.getValue().getBedGraph(displayRegion,
       * resolution, normalize);
       * 
       * // This track is not a bedgraph if (bedGraph == null) { return
       * MIN_MAX_Y; }
       * 
       * 
       * List<UCSCTrackRegion> regions =
       * UCSCTrackRegions.getFixedGapSearch(bedGraph.getRegions()).
       * getFeatureSet( displayRegion.getChr(), displayRegion.getStart(),
       * displayRegion.getEnd());
       * 
       * if (regions != null) { for (UCSCTrackRegion region : regions) { double
       * value = ((BedGraphRegion)region).getValue();
       * 
       * if (value > y) { y = value; } } }
       * 
       * y = Math.max(MIN_MAX_Y, y);
       */
    }

    // System.err.println("y max " + max);

    max = Math.max(MIN_MAX_Y, max);

    // Add a buffer to account for splines that go higher than
    // y max.
    max *= 1.2;

    return max;
  }
}
