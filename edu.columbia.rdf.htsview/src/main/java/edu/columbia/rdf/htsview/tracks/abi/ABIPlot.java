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

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.series.XYSeries;

import edu.columbia.rdf.htsview.ext.abi.ABITrace;

// TODO: Auto-generated Javadoc
/**
 * The Class ABIPlot.
 */
public class ABIPlot extends Plot {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m trace. */
  private ABITrace mTrace;

  /** The m base. */
  private char mBase;

  /** The m line color. */
  private Color mLineColor;

  /**
   * Instantiates a new ABI plot.
   *
   * @param name
   *          the name
   * @param trace
   *          the trace
   * @param base
   *          the base
   * @param color
   *          the color
   */
  public ABIPlot(String name, ABITrace trace, char base, Color color) {
    super(name);

    mTrace = trace;
    mBase = base;
    mLineColor = color;
  }

  /**
   * Update.
   *
   * @param displayRegion
   *          the display region
   * @param resolution
   *          the resolution
   * @param yMax
   *          the y max
   * @param width
   *          the width
   * @param height
   *          the height
   * @param margin
   *          the margin
   * @param lineColor
   *          the line color
   * @param fillColor
   *          the fill color
   * @param style
   *          the style
   */
  public void update(GenomicRegion displayRegion, int resolution, double yMax, int width, int height, int margin,
      Color lineColor, Color fillColor, PlotStyle style) {

    // System.err.println("regions " + start + " " + end + " " +
    // getCurrentAxes().toPlotX(end));

    if (mTrace == null) {
      return;
    }

    // Create a series for each bedgraph in the group

    setBarWidth(1);

    // Use the default series for plotting.
    XYSeries series = getAllSeries().getCurrent();

    // Use the bedgraph to set the series color
    // System.err.println(lineColor + " " + getName() + " " + series.getName() + " "
    // + series.getStyle().getLineStyle().getColor());

    if (!mLineColor.equals(series.getStyle().getLineStyle().getColor())) {
      series.getStyle().getLineStyle().setColor(mLineColor);
    }

    if (!mLineColor.equals(series.getStyle().getFillStyle().getColor())) {
      series.getStyle().getFillStyle().setColor(mLineColor);
    }

    // series.addRegex("x");
    // series.addRegex("y");

    setMatrix(new ABIMatrix(mTrace, mBase, displayRegion));
  }
}
