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
import java.awt.Component;
import java.awt.Graphics2D;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.text.TextUtils;
import org.jebtk.modern.list.ModernList;
import org.jebtk.modern.list.ModernListCellRenderer;
import org.jebtk.modern.theme.ThemeService;

// TODO: Auto-generated Javadoc
/**
 * The Class TrackListRenderer.
 */
public class TrackListRenderer extends ModernListCellRenderer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant ORB_WIDTH. */
  private static final int ORB_WIDTH = 8;

  /** The m text. */
  private String mText = "";

  /** The m color. */
  private Color mColor = Color.RED;

  /** The m fill color. */
  private Color mFillColor;

  /** The m row. */
  private int mRow;

  /** The Constant LINE_COLOR. */
  public static final Color LINE_COLOR = ThemeService.getInstance().colors().getHighlight(2);

  /** The Constant COLOR. */
  public static final Color COLOR = ThemeService.getInstance().colors().getHighlight(6);

  /** The Constant NUM_WIDTH. */
  private static final int NUM_WIDTH = 20;

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.widget.ModernWidget#drawForegroundAAText(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawForegroundAAText(Graphics2D g2) {
    int x = DOUBLE_PADDING;
    int y = getHeight() / 2;
    int x2 = getWidth() - ORB_WIDTH - DOUBLE_PADDING;

    // fill(g2, mFillColor);

    // g2.setColor(COLOR);
    g2.setColor(TEXT_COLOR);

    String t = mRow + ".";

    g2.drawString(t, x + NUM_WIDTH - g2.getFontMetrics().stringWidth(t) - PADDING, getTextYPosCenter(g2, getHeight()));

    x += NUM_WIDTH;

    int tw = x2 - x - PADDING;

    // Keep truncating the text until it fits into the available space.
    for (int i = mText.length(); i >= 0; --i) {
      t = TextUtils.truncate(mText, i);

      if (g2.getFontMetrics().stringWidth(t) <= tw) {
        break;
      }
    }

    // g2.setColor(mFillColor);
    g2.drawString(t, x, getTextYPosCenter(g2, getHeight()));

    // g2.setColor(mFillColor);
    // g2.fillOval(x2, y, ORB_WIDTH, ORB_WIDTH);
    //
    // g2.drawLine(x2, y, x2 + ORB_WIDTH, y);
    // g2.drawLine(x2, y + ORB_WIDTH / 2, x2 + ORB_WIDTH, y + ORB_WIDTH / 2);
    // g2.drawLine(x2, y+ ORB_WIDTH, x2 + ORB_WIDTH, y + ORB_WIDTH);

    g2.setColor(mFillColor);

    g2.drawLine(x2, y, x2 + ORB_WIDTH, y);

    y -= 2;

    g2.drawLine(x2, y, x2 + ORB_WIDTH, y);

    y += 4;

    g2.drawLine(x2, y, x2 + ORB_WIDTH, y);

    // y = getHeight() - 1;
    // g2.setColor(LINE_COLOR);
    // g2.drawLine(0, y, getWidth(), y);

  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.list.ModernListCellRenderer#getCellRendererComponent(org.
   * abh.common.ui.list.ModernList, java.lang.Object, boolean, boolean, boolean,
   * int)
   */
  @Override
  public Component getCellRendererComponent(ModernList<?> list, Object value, boolean highlight, boolean isSelected,
      boolean hasFocus, int row) {

    Track t = (Track) value;

    mText = t.getName();
    mColor = t.getFillColor();
    mFillColor = ColorUtils.getTransparentColor60(mColor);
    mRow = row + 1;

    return super.getCellRendererComponent(list, value, highlight, isSelected, hasFocus, row);
  }
}