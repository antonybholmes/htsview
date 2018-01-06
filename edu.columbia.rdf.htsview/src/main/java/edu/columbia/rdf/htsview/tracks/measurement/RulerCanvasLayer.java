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
import java.awt.Graphics2D;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.Mathematics;
import org.jebtk.core.text.Formatter;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;

// TODO: Auto-generated Javadoc
/**
 * The Class RulerCanvasLayer.
 */
public class RulerCanvasLayer extends AxesClippedLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m gap. */
  private int mGap;

  /** The m start. */
  private int mStart;

  @Override
  public String getType() {
    return "Ruler Layer";
  }

  /**
   * Update.
   *
   * @param displayRegion
   *          the display region
   */
  public void update(GenomicRegion displayRegion) {
    mGap = (int) Math.pow(10, (int) Mathematics.log10(displayRegion.getLength()));

    mStart = (int) (displayRegion.getStart() / (double) mGap) * mGap;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesClippedLayer#plotLayer(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void plotLayer(Graphics2D g2, DrawingContext context, Figure figure, SubFigure subFigure, Axes axes) {
    // Use the graph properties and subplot layout to
    // create the graph space mapper

    int x1;
    int y = 0;
    int h = axes.getInternalSize().getH();
    int h4 = h / 4;

    g2.setColor(Color.BLACK);

    g2.drawLine(0, y, axes.getInternalSize().getW(), y);

    int s = mStart;

    boolean onScreen = false;

    int ty = y + h - g2.getFontMetrics().getDescent();

    while (true) {
      if (onScreen && !axes.getX1Axis().withinBounds(s)) {
        break;
      }

      if (!onScreen && axes.getX1Axis().withinBounds(s)) {
        onScreen = true;
      }

      if (onScreen) {
        x1 = axes.toPlotX1(s);

        g2.drawLine(x1, y, x1, y + h4);

        String label = Formatter.number().format(s);

        int w = g2.getFontMetrics().stringWidth(label);

        x1 -= w / 2;

        g2.drawString(label, x1, ty);
      }

      s += mGap;
    }
  }
}
