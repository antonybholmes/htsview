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
import java.awt.Graphics2D;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.plotbox.PlotBoxPanel;
import org.jebtk.modern.graphics.CanvasMouseAdapter;
import org.jebtk.modern.graphics.CanvasMouseEvent;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.theme.ModernTheme;
import org.jebtk.modern.theme.ThemeService;

/**
 * The Class TracksFigure.
 */
public class TracksFigurePanel extends PlotBoxPanel { // Figure { //
                                                      // PlotBoxColumn {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /**
   * The smallest value the max y can be (has to be non-zero).
   */
  public static final double MIN_MAX_Y = 0.1;

  /** The Constant SELECTION_COLOR. */
  private static final Color SELECTION_COLOR = ThemeService.getInstance()
      .colors().getColorHighlight(5);

  /** The Constant SELECTION_COLOR_TRANS. */
  private static final Color SELECTION_COLOR_TRANS = ColorUtils
      .getTransparentColor75(SELECTION_COLOR);

  private static final int X_GAP = SettingsService.getInstance()
      .getAsInt("sequencing.tracks.mouse.drag.x-gap");

  /** The m drag start. */
  private int mDragStart = -1;

  /** The m drag end. */
  private int mDragEnd = -1;

  /** The m dist. */
  private int mDist = -1;

  /** The m genomic model. */
  private GenomicRegionModel mGenomicModel;

  /** The m drag start region. */
  private GenomicRegion mDragStartRegion;

  /** The m selection X. */
  private int mSelectionStart = -1;
  private int mSelectionEnd = -1;

  private int mDragBin = Integer.MIN_VALUE;

  private TracksFigure mFigure;

  /**
   * The Class CanvasMouseEvents.
   */
  private class CanvasMouseEvents extends CanvasMouseAdapter {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseAdapter#canvasMousePressed(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMousePressed(CanvasMouseEvent e) {
      int x = e.getScaledPos().getX();

      if (e.isControlDown()) {
        mSelectionStart = Math.max(Track.LEFT_MARGIN, Math.min(Track.END, x));
      } else {
        mDragStart = x;

        mDragStartRegion = mGenomicModel.get();
      }

      // System.err.println(mSelectionX);

      fireCanvasRedraw();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseAdapter#canvasMouseReleased(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseReleased(CanvasMouseEvent e) {
      if (e.isControlDown()) {
        alterSelection();
      }

      mSelectionStart = -1;
      mSelectionEnd = -1;

      mDragStart = -1;
      mDragEnd = -1;

      fireCanvasRedraw();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseAdapter#canvasMouseDragged(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseDragged(CanvasMouseEvent e) {

      if (e.isControlDown()) {
        int x = e.getScaledPos().getX();
        mSelectionEnd = Math.max(Track.LEFT_MARGIN, Math.min(Track.END, x));

        fireCanvasRedraw();
      } else {
        mDragEnd = e.getScaledPos().getX();

        mDist = mDragEnd - mDragStart;

        dragMove();
      }

    }

  }

  /**
   * Instantiates a new tracks figure.
   *
   * @param genomicModel the genomic model
   * @param sizes the sizes
   */
  public TracksFigurePanel(TracksFigure figure,
      GenomicRegionModel genomicModel) {
    super(figure);

    mFigure = figure;
    mGenomicModel = genomicModel;

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

    addCanvasMouseListener(new CanvasMouseEvents());
  }

  @Override
  public final void plot(Graphics2D g2,
      DrawingContext context,
      Object... params) {
    super.plot(g2, context, params);

    plotSelection(g2, context, params);
  }

  public void plotSelection(Graphics2D g2,
      DrawingContext context,
      Object... params) {

    int h = getParent().getHeight();

    if (mSelectionStart != -1 && mSelectionEnd != -1) {
      int minX = Math.min(mSelectionStart, mSelectionEnd);
      int maxX = Math.max(mSelectionStart, mSelectionEnd);

      g2.setColor(SELECTION_COLOR_TRANS);

      g2.fillRect(minX + 1, 0, maxX - minX - 1, h);
    }

    g2.setStroke(ModernTheme.DASHED_LINE_STROKE);
    g2.setColor(SELECTION_COLOR);

    if (mSelectionStart != -1) {
      g2.drawLine(mSelectionStart, 0, mSelectionStart, h);
    }

    if (mSelectionEnd != -1) {
      g2.drawLine(mSelectionEnd, 0, mSelectionEnd, h);
    }

  }

  /**
   * Alter selection.
   *
   * @param x the x
   */
  private void alterSelection() {
    if (mSelectionStart == -1 || mSelectionEnd == -1) {
      return;
    }

    GenomicRegion region = mGenomicModel.get();

    double p = Math.min(1,
        Math.max(0,
            (mSelectionStart - Track.LEFT_MARGIN) / (double) Track.PLOT_WIDTH));

    int start = region.getStart() + (int) (p * region.getLength());

    p = Math.min(1,
        Math.max(0,
            (mSelectionEnd - Track.LEFT_MARGIN) / (double) Track.PLOT_WIDTH));

    int end = region.getStart() + (int) (p * region.getLength());

    GenomicRegion newRegion = new GenomicRegion(region.getChr(), start, end);

    mGenomicModel.set(newRegion);
  }

  /**
   * Drag move.
   */
  private void dragMove() {
    if (mDragStartRegion == null) {
      return;
    }

    int bin = mDist / X_GAP;

    if (bin == mDragBin) {
      return;
    }

    mDragBin = bin;

    double w = mFigure.currentSubFigure().currentAxes().getInternalSize()
        .getW();

    double p = -mDist / w;

    int shift = (int) (p * mDragStartRegion.getLength());

    GenomicRegion newRegion = GenomicRegion.shift(mDragStartRegion, shift, 10);

    // System.err.println("new " + mDragStartRegion + " " + newRegion + " " +
    // mDragStartRegion.getLength() + " " + p);

    mGenomicModel.set(newRegion);
  }
}
