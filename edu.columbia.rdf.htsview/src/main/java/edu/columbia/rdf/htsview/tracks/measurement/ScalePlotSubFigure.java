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
package edu.columbia.rdf.htsview.tracks.measurement;

import java.awt.Color;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

// TODO: Auto-generated Javadoc
/**
 * The Class ScalePlotSubFigure.
 */
public class ScalePlotSubFigure extends MeasurementSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m layer. */
  private ScaleCanvasLayer mLayer;

  /**
   * Instantiates a new scale plot sub figure.
   *
   * @param titlePosition
   *          the title position
   */
  public ScalePlotSubFigure(TitleProperties titlePosition) {
    mLayer = new ScaleCanvasLayer();

    currentAxes().addChild(mLayer);

    Track.setTitle(ScalePlotTrack.TITLE, titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param titlePosition
   *          the title position
   * @return the scale plot sub figure
   */
  public static ScalePlotSubFigure create(TitleProperties titlePosition) {

    // mBedGraphGroup = bedGraphGroup;
    // mGenomicModel = genomicModel;

    ScalePlotSubFigure canvas = new ScalePlotSubFigure(titlePosition);

    Axes axes = canvas.currentAxes();

    // set the graph limits
    axes.getX1Axis().getTitle().setVisible(false);
    axes.getX1Axis().startEndTicksOnly();

    axes.getY1Axis().getTitle().setVisible(false);
    axes.getY1Axis().startEndTicksOnly();

    axes.setInternalSize(Track.MEDIUM_TRACK_SIZE);
    // canvas.getGraphSpace().getLayoutProperties().setMargins(MARGINS);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.measurement.MeasurementSubFigure#update(org.
   * jebtk.bioinformatics.genome.GenomicRegion, int, double, int, int, int,
   * java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
   */
  @Override
  public void update(GenomicRegion displayRegion, int resolution, double yMax, int width, int height, int margin,
      Color lineColor, Color fillColor, PlotStyle style) {
    super.update(displayRegion, resolution, yMax, width, height, margin, lineColor, fillColor, style);

    mLayer.update(displayRegion);
  }
}
