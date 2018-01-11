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
package edu.columbia.rdf.htsview.tracks.locations;

import java.awt.Component;
import java.awt.Graphics2D;

import org.jebtk.modern.list.ModernList;
import org.jebtk.modern.list.ModernListCellRenderer;

// TODO: Auto-generated Javadoc
/**
 * The Class LocationListRenderer.
 */
public class LocationListRenderer extends ModernListCellRenderer {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m text. */
  private String mText = "";

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.widget.ModernWidget#drawForegroundAAText(java.awt.
   * Graphics2D)
   */
  @Override
  public void drawForegroundAAText(Graphics2D g2) {
    int x = PADDING;

    // fill(g2, mFillColor);

    // g2.setColor(COLOR);
    g2.setColor(TEXT_COLOR);

    // g2.setColor(mFillColor);
    g2.drawString(mText, x, getTextYPosCenter(g2, getHeight()));

    // g2.setColor(mFillColor);
    // g2.fillOval(x2, y, ORB_WIDTH, ORB_WIDTH);
    //
    // g2.drawLine(x2, y, x2 + ORB_WIDTH, y);
    // g2.drawLine(x2, y + ORB_WIDTH / 2, x2 + ORB_WIDTH, y + ORB_WIDTH / 2);
    // g2.drawLine(x2, y+ ORB_WIDTH, x2 + ORB_WIDTH, y + ORB_WIDTH);
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
  public Component getCellRendererComponent(ModernList<?> list,
      Object value,
      boolean highlight,
      boolean isSelected,
      boolean hasFocus,
      int row) {

    mText = (String) value;

    return super.getCellRendererComponent(list,
        value,
        highlight,
        isSelected,
        hasFocus,
        row);
  }
}