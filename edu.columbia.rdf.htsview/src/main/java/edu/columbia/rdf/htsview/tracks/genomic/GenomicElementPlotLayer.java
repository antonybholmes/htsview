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
package edu.columbia.rdf.htsview.tracks.genomic;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.ext.ucsc.BedElement;
import org.jebtk.bioinformatics.ext.ucsc.BedGraphGroupModel;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.bioinformatics.genomic.GenomicEntity;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.geom.IntRect;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.widget.ModernWidget;

import edu.columbia.rdf.htsview.tracks.TrackDisplayMode;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedPlotTrack;

/**
 * The Class BedPlotLayer.
 */
public class GenomicElementPlotLayer extends AxesClippedLayer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  // private static final int BAR_HEIGHT = 20;

  /** The m bed graph group. */
  protected BedGraphGroupModel mBedGraphGroup;

  /** The m regions. */
  private List<GenomicElement> mElements;

  /** The m color. */
  private Color mColor;

  /** The m display mode. */
  private TrackDisplayMode mDisplayMode = TrackDisplayMode.COMPACT;


  /** The m update. */
  // Notify the system that the coordinate
  private boolean mUpdate = false;

  /** The m dims. */
  private Map<GenomicElement, IntRect> mDims = new HashMap<GenomicElement, IntRect>();

  /**
   * Instantiates a new bed plot layer.
   *
   * @param color the color
   */
  public GenomicElementPlotLayer(Color color) {
    mColor = color;
  }

  @Override
  public String getType() {
    return "Genomic Elements Layer";
  }

  /**
   * Update.
   *
   * @param regions the regions
   * @param color the color
   * @param displayMode the display mode
   */
  public void update(List<GenomicElement> regions,
      Color color,
      TrackDisplayMode displayMode) {
    mElements = regions;
    mColor = color;
    mDisplayMode = displayMode;

    mUpdate = true;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.graphplot.figure.AxesClippedLayer#plotLayer(java.awt.Graphics2D,
   * org.abh.common.ui.graphics.DrawingContext, org.graphplot.figure.SubFigure,
   * org.graphplot.figure.Axes)
   */
  @Override
  public void plotLayer(Graphics2D g2,
      DrawingContext context,
      Figure figure,
      SubFigure subFigure,
      Axes axes) {

    if (CollectionUtils.isNullOrEmpty(mElements)) {
      return;
    }

    int x1;
    int x2;
    // int px1;
    // int px2;

    // int h = BAR_HEIGHT; //space.getPlotSize().getH();
    int w;
    int y = 0;
    int yp;
    int sx1;
    int sx2;

    if (mUpdate) {
      mDims.clear();

      for (GenomicElement bed : mElements) {
        x1 = axes.toPlotX1(bed.getStart());
        x2 = axes.toPlotX1(bed.getEnd());

        mDims.put(bed, new IntRect(x1, y, x2 - x1, BedPlotTrack.BAR_HEIGHT));

        // In full mode, each feature is draw separately on its
        // own row
        if (mDisplayMode == TrackDisplayMode.FULL) {
          y += BedPlotTrack.BLOCK_HEIGHT;
        }
      }

      mUpdate = false;
    }

    IntRect rect;

    for (GenomicElement element : mElements) {
      rect = mDims.get(element);

      x1 = rect.getX();
      w = rect.getW();
      x2 = x1 + w;
      y = rect.getY();

      if (element.getColor() != null) {
        g2.setColor(element.getColor());
      } else {
        g2.setColor(mColor);
      }

      x1 = axes.toPlotX1(element.getStart());
      x2 = axes.toPlotX1(element.getEnd());

      // Determine the correct y

      /*
       * if (bed.getStrand() == '+' || bed.getStrand() == '-') { // draw track
       * lower down so direction arrow can be put // in place yp = y + yDiff2; }
       * else { // center the blocks within the space yp = y + yDiff1; }
       */

      yp = y; // + yDiff1;

      if (element.getChildCount(GenomicType.EXON) == 0) {
        // Default mode when there are no blocks is to draw a block
        // spanning the whole region

        // Must be a minimum of 1 pixel wide
        w = Math.max(1, x2 - x1 + 1);
        
        if (w > 1) {
          g2.fillRect(x1, yp, w, BedPlotTrack.BAR_HEIGHT);
        } else {
          // If the bar is one pixel wide, draw it as a line
          // rather than rectangle.
          g2.drawLine(x1, yp, x1, yp + BedPlotTrack.BAR_HEIGHT);
        }
      } else {
        // draw for the sub regions

        g2.drawLine(x1,
            yp + BedPlotTrack.HALF_BAR_HEIGHT,
            x2,
            yp + BedPlotTrack.HALF_BAR_HEIGHT);

        for (GenomicRegion subRegion : element.getChildren(GenomicType.EXON)) {
          sx1 = axes.toPlotX1(subRegion.getStart());
          sx2 = axes.toPlotX1(subRegion.getEnd());

          w = Math.max(1, sx2 - sx1 + 1);

          if (w > 1) {
            g2.fillRect(sx1, yp, w, BedPlotTrack.BAR_HEIGHT);
          } else {
            g2.drawLine(sx1, yp, sx1, yp + BedPlotTrack.BAR_HEIGHT);
          }
        }
      }

      

      // In full mode, each feature is draw separately on its
      // own row
      if (mDisplayMode == TrackDisplayMode.FULL) {

        // Draw a label for the peak
        g2.setColor(Color.BLACK);

        String s;

        if (element instanceof BedElement) {
          String name = ((BedElement) element).getName();

          if (!name.contains("chr:")) {
            s = name + " (" + element.getLocation() + ")";
          } else {
            s = element.getLocation();
          }
        } else {
          s = element.getLocation();
        }

        g2.drawString(s,
            x2 + BedPlotTrack.BAR_HEIGHT,
            yp + ModernWidget.getTextYPosCenter(g2, BedPlotTrack.BAR_HEIGHT));
      }
    }
  }
}
