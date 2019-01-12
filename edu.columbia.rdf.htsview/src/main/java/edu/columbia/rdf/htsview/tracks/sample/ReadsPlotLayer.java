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
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraphGroupModel;
import org.jebtk.bioinformatics.genomic.Strand;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;

/**
 * Draw peaks.
 *
 * @author Antony Holmes
 */
public class ReadsPlotLayer extends AxesClippedLayer {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The constant HEIGHT.
   */
  // public static final int HEIGHT =
  // SettingsService.getInstance().getInt("edb.reads.tracks.reads-plot.height");

  /**
   * The constant BLOCK.
   */
  // public static final int BLOCK =
  // SettingsService.getInstance().getInt("edb.reads.tracks.reads-plot.block-height");

  // private static final int BAR_HEIGHT = 20;

  /**
   * The member bed graph group.
   */
  protected BedGraphGroupModel mBedGraphGroup;

  /**
   * The member starts.
   */
  private ListMultiMap<Integer, Integer> mStarts = null;

  /**
   * The member start map.
   */
  // private MultiMap<Integer, Integer> mStartMap =
  // DefaultListMultiMap.create();

  // private MultiMap<Integer, Character> mStrandMap =
  // DefaultListMultiMap.create();

  /**
   * The member gap.
   */
  private int mWidth;

  /**
   * The member line color.
   */
  private Color mLineColor;

  /**
   * The member fill color.
   */
  private Color mFillColor;

  /**
   * The member current id.
   */
  // private String mCurrentId = null;

  private ListMultiMap<Integer, Strand> mStrands;

  // private boolean mStrandVisible;

  // private boolean mNegStrandVisible;

  /** The m neg strand line color. */
  private Color mNegStrandLineColor;

  /** The m anti strand fill color. */
  private Color mAntiStrandFillColor;

  /** The m read height. */
  private int mReadHeight;

  /** The m block height. */
  private int mBlockHeight;

  /**
   * Instantiates a new reads plot layer.
   *
   * @param readLength the read length
   */
  public ReadsPlotLayer(int readLength) {

  }

  @Override
  public String getType() {
    return "Reads Layer";
  }

  /**
   * Update.
   *
   * @param starts the starts
   * @param strands the strands
   * @param width the width
   * @param strandVisible the strand visible
   * @param lineColor the line color
   * @param fillColor the fill color
   * @param negStrandVisible the neg strand visible
   * @param negStrandLineColor the neg strand line color
   * @param negStrandFillColor the neg strand fill color
   * @param readHeight the read height
   * @param gap the gap
   */
  public void update(ListMultiMap<Integer, Integer> starts,
      ListMultiMap<Integer, Strand> strands,
      int width,
      boolean strandVisible,
      Color lineColor,
      Color fillColor,
      boolean negStrandVisible,
      Color negStrandLineColor,
      Color negStrandFillColor,
      int readHeight,
      int gap) {
    mStarts = starts;
    mStrands = strands;
    mWidth = width;

    // mStrandVisible = strandVisible;
    mLineColor = lineColor;
    mFillColor = fillColor;

    // mNegStrandVisible = negStrandVisible;
    mNegStrandLineColor = negStrandLineColor;
    mAntiStrandFillColor = negStrandFillColor;

    mReadHeight = readHeight;
    mBlockHeight = readHeight + gap;

    fireChanged();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.lib.bioinformatics.plot.figure.AxesClippedLayer#plotLayer(
   * java.awt.Graphics2D, org.abh.common.ui.ui.graphics.DrawingContext,
   * edu.columbia.rdf.lib.bioinformatics.plot.figure.SubFigure,
   * edu.columbia.rdf.lib.bioinformatics.plot.figure.Axes)
   */
  @Override
  public void plotLayer(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {

    if (CollectionUtils.isNullOrEmpty(mStarts)) {
      return;
    }

    int x1 = 0;

    int y = 0;

    /*
     * String id = getId(axes);
     * 
     * if (mUpdate || mCurrentId == null || !id.equals(mCurrentId)) {
     * mStartMap.clear(); mStrandMap.clear();
     * 
     * mGap = -1;
     * 
     * for (int i = 0; i < mStarts.size(); ++i) { int start = mStarts.get(i);
     * char strand = mStrands.get(i);
     * 
     * if ((!mStrandVisible && strand == '+') || (!mNegStrandVisible && strand
     * == '-')) { continue; }
     * 
     * x1 = axes.toPlotX(start); x2 = axes.toPlotX(start + mReadLength);
     * 
     * if (mGap == -1) { mGap = Math.max(1, x2 - x1); }
     * 
     * int row = -1;
     * 
     * for (int r : mStartMap.keySet()) { boolean fit = true;
     * 
     * for (int s : mStartMap.get(r)) { int e = s + mGap;
     * 
     * if ((x1 >= s && x1 < e) || (x2 >= s && x2 < e)) { fit = false; break; } }
     * 
     * if (fit) { row = r; break; } }
     * 
     * if (row == -1) { row = mStartMap.size(); }
     * 
     * // debug //row = 0;
     * 
     * mStartMap.get(row).add(x1); mStrandMap.get(row).add(strand); } }
     * 
     * mUpdate = false; mCurrentId = id;
     */

    // System.err.println("coord " + axes.getXAxis().getMax() + " " +
    // axes.getY1Axis().getMax() + " " + axes.getY1Axis().getMin());

    if (context == DrawingContext.UI) {
      BufferedImage bis = new BufferedImage(mWidth, mReadHeight,
          BufferedImage.TYPE_INT_RGB);

      Graphics2D g2Temp = bis.createGraphics();

      g2Temp.setColor(mFillColor);
      g2Temp.fillRect(0, 0, mWidth, mReadHeight);
      g2Temp.setColor(mLineColor);
      g2Temp.drawRect(0, 0, mWidth, mReadHeight - 1);
      g2Temp.dispose();

      BufferedImage bia = new BufferedImage(mWidth, mReadHeight,
          BufferedImage.TYPE_INT_RGB);

      g2Temp = bia.createGraphics();

      g2Temp.setColor(mAntiStrandFillColor);
      g2Temp.fillRect(0, 0, mWidth, mReadHeight);
      g2Temp.setColor(mNegStrandLineColor);
      g2Temp.drawRect(0, 0, mWidth, mReadHeight - 1);
      g2Temp.dispose();

      for (int row : mStarts.keySet()) {
        List<Integer> starts = (List<Integer>) mStarts.get(row);
        List<Strand> strands = (List<Strand>) mStrands.get(row);

        int ry = y + row * mBlockHeight;

        for (int i = 0; i < starts.size(); ++i) {
          int s = starts.get(i);
          Strand strand = strands.get(i);

          x1 = s;

          if (strand == Strand.SENSE) {
            g2.drawImage(bis, x1, ry, null);
          } else {
            g2.drawImage(bia, x1, ry, null);
          }
        }
      }
    } else {
      for (int row : mStarts.keySet()) {
        List<Integer> starts = (List<Integer>) mStarts.get(row);
        List<Strand> strands = (List<Strand>) mStrands.get(row);

        int ry = y + row * mBlockHeight;

        for (int i = 0; i < starts.size(); ++i) {
          int s = starts.get(i);
          Strand strand = strands.get(i);

          x1 = s;

          if (strand == Strand.SENSE) {
            g2.setColor(mFillColor);
          } else {
            g2.setColor(mAntiStrandFillColor);
          }

          // System.err.println(x1 + " " + y + row * BLOCK + " " + mGap + " " +
          // mFillColor);

          g2.fillRect(x1, ry, mWidth, mReadHeight);

          if (strand == Strand.SENSE) {
            g2.setColor(mLineColor);
          } else {
            g2.setColor(mNegStrandLineColor);
          }

          // System.err.println(x1 + " " + y + row * BLOCK + " " + mGap + " " +
          // mColor);

          g2.drawRect(x1, ry, mWidth, mReadHeight - 1);
        }
      }
    }
  }
}
