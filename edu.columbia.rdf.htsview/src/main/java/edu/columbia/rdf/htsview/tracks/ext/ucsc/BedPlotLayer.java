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
package edu.columbia.rdf.htsview.tracks.ext.ucsc;

import java.awt.Color;
import java.awt.Graphics2D;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.ext.ucsc.BedGraphGroupModel;
import org.jebtk.bioinformatics.ext.ucsc.BedRegion;
import org.jebtk.bioinformatics.ext.ucsc.TrackDisplayMode;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrackRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.geom.IntRect;
import org.jebtk.core.network.URLUtils;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.AxesClippedLayer;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.modern.UIService;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.CanvasMouseEvent;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.CanvasMouseListener;
import org.jebtk.modern.menu.ModernIconMenuItem;
import org.jebtk.modern.menu.ModernMenuItem;
import org.jebtk.modern.menu.ModernPopupMenu;
import org.jebtk.modern.widget.ModernWidget;

// TODO: Auto-generated Javadoc
/**
 * The Class BedPlotLayer.
 */
public class BedPlotLayer extends AxesClippedLayer
    implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  // private static final int BAR_HEIGHT = 20;

  /** The m bed graph group. */
  protected BedGraphGroupModel mBedGraphGroup;

  /** The m regions. */
  private List<UCSCTrackRegion> mRegions;

  /** The m color. */
  private Color mColor;

  /** The m display mode. */
  private TrackDisplayMode mDisplayMode = TrackDisplayMode.COMPACT;

  /** The Constant BASE_URL. */
  private static final String BASE_URL = "https://genome.ucsc.edu/cgi-bin/hgTracks?db=hg19&position=";

  /** The m update. */
  // Notify the system that the coordinate
  private boolean mUpdate = false;

  /** The m dims. */
  private Map<UCSCTrackRegion, IntRect> mDims = new HashMap<UCSCTrackRegion, IntRect>();

  /** The m menu. */
  private ModernPopupMenu mMenu;

  /** The m selected bed. */
  private UCSCTrackRegion mSelectedBed;

  /**
   * The Class CanvasEvents.
   */
  private class CanvasEvents implements CanvasMouseListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseClicked(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseClicked(CanvasMouseEvent e) {
      // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseEntered(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseEntered(CanvasMouseEvent e) {
      // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseExited(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseExited(CanvasMouseEvent e) {
      // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMousePressed(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMousePressed(CanvasMouseEvent e) {
      if (e.isPopupTrigger()) {
        for (UCSCTrackRegion bed : mDims.keySet()) {
          IntRect rect = mDims.get(bed);

          // Allow some padding in case the region is small
          if (rect.contains(e.getScaledPos(), 5)) {
            mSelectedBed = bed;

            mMenu.showPopup(e.getComponent(), e.getX(), e.getY());

            break;
          }
        }
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseReleased(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseReleased(CanvasMouseEvent e) {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseDragged(
     * org. abh.common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseDragged(CanvasMouseEvent e) {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.graphics.ModernCanvasMouseListener#canvasMouseMoved(org
     * .abh .common.ui.graphics.CanvasMouseEvent)
     */
    @Override
    public void canvasMouseMoved(CanvasMouseEvent e) {
      // TODO Auto-generated method stub

    }

  }

  /**
   * Instantiates a new bed plot layer.
   *
   * @param color the color
   */
  public BedPlotLayer(Color color) {
    mColor = color;

    //
    // Create Menu
    //

    mMenu = new ModernPopupMenu();

    ModernMenuItem menuItem = new ModernIconMenuItem("DNA");
    menuItem.addClickListener(this);
    mMenu.add(menuItem);

    menuItem = new ModernIconMenuItem("UCSC",
        UIService.getInstance().loadIcon("ucsc", 16));
    menuItem.addClickListener(this);
    mMenu.add(menuItem);

    // addCanvasMouseListener(new CanvasEvents());
  }

  @Override
  public String getType() {
    return "Regions Layer";
  }

  /**
   * Update.
   *
   * @param regions the regions
   * @param color the color
   * @param displayMode the display mode
   */
  public void update(List<UCSCTrackRegion> regions,
      Color color,
      TrackDisplayMode displayMode) {
    mRegions = regions;
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

    if (CollectionUtils.isNullOrEmpty(mRegions)) {
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

      for (UCSCTrackRegion bed : mRegions) {
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

    for (UCSCTrackRegion region : mRegions) {
      rect = mDims.get(region);

      x1 = rect.getX();
      w = rect.getW();
      x2 = x1 + w;
      y = rect.getY();

      if (region.getColor() != null) {
        g2.setColor(region.getColor());
      } else {
        g2.setColor(mColor);
      }

      x1 = axes.toPlotX1(region.getStart());
      x2 = axes.toPlotX1(region.getEnd());

      // Determine the correct y

      /*
       * if (bed.getStrand() == '+' || bed.getStrand() == '-') { // draw track
       * lower down so direction arrow can be put // in place yp = y + yDiff2; }
       * else { // center the blocks within the space yp = y + yDiff1; }
       */

      yp = y; // + yDiff1;

      if (region.getSubRegions().size() == 0) {
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

        for (GenomicRegion subRegion : region.getSubRegions()) {
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

      /*
       * if (bed.getStrand() == '+') { GenesPlotCanvasLayer.drawForwardArrow(g2,
       * x1, yp); } else if (bed.getStrand() == '-') {
       * GenesPlotCanvasLayer.drawReverseArrow(g2, x2, yp); } else { // do
       * nothing }
       */

      // In full mode, each feature is draw separately on its
      // own row
      if (mDisplayMode == TrackDisplayMode.FULL) {

        // Draw a label for the peak
        g2.setColor(Color.BLACK);

        String s;

        if (region instanceof BedRegion) {
          String name = ((BedRegion) region).getName();

          if (!name.contains("chr:")) {
            s = name + " (" + region.getLocation() + ")";
          } else {
            s = region.getLocation();
          }
        } else {
          s = region.getLocation();
        }

        g2.drawString(s,
            x2 + BedPlotTrack.BAR_HEIGHT,
            yp + ModernWidget.getTextYPosCenter(g2, BedPlotTrack.BAR_HEIGHT));
      }
    }
  }

  /*
   * public final void drawForwardArrow(Graphics2D g2, int x, int y) {
   * Graphics2D g2Temp = (Graphics2D)g2.create();
   * 
   * g2Temp.translate(x + HALF_BAR_HEIGHT, y - HALF_BAR_HEIGHT);
   * 
   * 
   * g2Temp.drawLine(-HALF_BAR_HEIGHT, 0, -HALF_BAR_HEIGHT, HALF_BAR_HEIGHT);
   * g2Temp.drawLine(-HALF_BAR_HEIGHT, 0, 0, 0); g2Temp.fill(mForwardArrow);
   * 
   * g2Temp.dispose(); }
   * 
   * public final void drawReverseArrow(Graphics2D g2, int x, int y) {
   * Graphics2D g2Temp = (Graphics2D)g2.create();
   * 
   * g2Temp.translate(x - HALF_BAR_HEIGHT, y - HALF_BAR_HEIGHT);
   * 
   * g2Temp.drawLine(HALF_BAR_HEIGHT, 0, HALF_BAR_HEIGHT, HALF_BAR_HEIGHT);
   * g2Temp.drawLine(0, 0, HALF_BAR_HEIGHT, 0); g2Temp.fill(mReverseArrow);
   * 
   * g2Temp.dispose(); }
   */

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getMessage().equals("UCSC")) {
      try {
        URLUtils.launch(new URL(BASE_URL + mSelectedBed.getLocation()));
      } catch (MalformedURLException e1) {
        e1.printStackTrace();
      } catch (URISyntaxException e1) {
        e1.printStackTrace();
      } catch (IOException e1) {
        e1.printStackTrace();
      }
    }
    /*
     * else if (e.getMessage().equals("DNA")) { DataFrame m =
     * AnnotatableMatrix.createAnnotatableMatrix(1, 1);
     * 
     * m.setName("DNA"); m.setColumnName(0, "Location");
     * 
     * m.set(0, 0, mSelectedBed.getLocation());
     * 
     * 
     * MainMatCalcWindow window;
     * 
     * try { window = MainMatCalc.main(new BioModuleLoader());
     * 
     * window.openMatrix( m); } catch (ClassNotFoundException e1) {
     * e1.printStackTrace(); } catch (InstantiationException e1) {
     * e1.printStackTrace(); } catch (IllegalAccessException e1) {
     * e1.printStackTrace(); } catch (FontFormatException e1) {
     * e1.printStackTrace(); } catch (IOException e1) { e1.printStackTrace(); }
     * catch (UnsupportedLookAndFeelException e1) { e1.printStackTrace(); }
     * 
     * 
     * }
     */
  }

}
