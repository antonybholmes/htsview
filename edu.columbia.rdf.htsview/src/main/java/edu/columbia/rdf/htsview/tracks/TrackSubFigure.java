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

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.SubFigure;

/**
 * The Class TrackSubFigure.
 */
public abstract class TrackSubFigure extends SubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * Update.
   *
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param width the width
   * @param height the height
   * @param margin the margin
   * @throws IOException 
   */
  public void update(GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {
    update(displayRegion, resolution, -1, width, height, margin);
  }

  /**
   * Update.
   *
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param yMax the y max
   * @param width the width
   * @param height the height
   * @param margin the margin
   * @throws IOException 
   */
  public void update(GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin) throws IOException {
    update(displayRegion,
        resolution,
        yMax,
        width,
        height,
        margin,
        PlotStyle.FILLED_SMOOTH);
  }

  /**
   * Update.
   *
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param yMax the y max
   * @param width the width
   * @param height the height
   * @param margin the margin
   * @param style the style
   * @throws IOException 
   */
  public void update(GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      PlotStyle style) throws IOException {
    update(displayRegion,
        resolution,
        yMax,
        width,
        height,
        margin,
        null,
        null,
        style);
  }

  /**
   * Should update the figure to correspond to the coordinates being looked at.
   * Should also update colors and set the width and height of the plot. All
   * figures should observe the width property so that the plots are vertically
   * aligned and appear uniform going vertically. The height parameter can be
   * ignored if for example the figure auto adjusts its height based on the
   * number of features being displayed.
   *
   * @param displayRegion the display region
   * @param resolution the resolution
   * @param yMax the y max
   * @param width the width
   * @param height the height
   * @param margin The left margin.
   * @param lineColor the line color
   * @param fillColor the fill color
   * @param style the style
   * @throws IOException 
   */
  public void update(GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      Color lineColor,
      Color fillColor,
      PlotStyle style) throws IOException {

    // disable lots of event triggering
    currentAxes().setFireEvents(false);

    currentAxes().setInternalSize(width, height);

    currentAxes().setLeftMargin(margin);

    currentAxes().getX1Axis().setLimits(displayRegion.getStart(),
        displayRegion.getEnd());

    currentAxes().getY1Axis().setLimits(0, yMax);

    currentAxes().setFireEvents(true);
  }
}
