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
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.widget.ModernWidget;

import edu.columbia.rdf.htsview.tracks.Track;

// TODO: Auto-generated Javadoc
/**
 * The Class ScaleCanvasLayer.
 */
public class ScaleCanvasLayer extends AxesLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant BAR_SIZE. */
  private static final int BAR_SIZE = 3;

  /** The Constant GAP. */
  private static final int GAP = 10;

  /** The m gap. */
  private int mGap;

  @Override
  public String getType() {
    return "Scale Layer";
  }

  /**
   * Update.
   *
   * @param displayRegion the display region
   */
  public void update(GenomicRegion displayRegion) {
    mGap = (int) Math.pow(10,
        (int) Mathematics.log10(displayRegion.getLength()));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesLayer#plot(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void drawPlot(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {
    // Use the graph properties and subplot layout to
    // create the graph space mapper

    int h = axes.getInternalSize().getH();

    int y = axes.getInternalSize().getH() / 2;

    int w = axes.toPlotX1(axes.getX1Axis().getMin() + mGap)
        - axes.toPlotX1(axes.getX1Axis().getMin());

    int x1 = (axes.getInternalSize().getW() - w) / 2;

    int x2 = x1 + w;

    g2.setColor(Color.BLACK);
    g2.drawLine(x1, y, x2, y);
    g2.drawLine(x1, y - BAR_SIZE, x1, y + BAR_SIZE);
    g2.drawLine(x2, y - BAR_SIZE, x2, y + BAR_SIZE);

    String label = Track.formatBp(mGap);

    x1 -= (g2.getFontMetrics().stringWidth(label) + GAP);

    g2.drawString(label, x1, ModernWidget.getTextYPosCenter(g2, h));

    // s += mGap;
    // }
  }
}
