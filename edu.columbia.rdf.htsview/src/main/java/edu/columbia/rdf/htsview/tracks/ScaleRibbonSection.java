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

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.text.Formatter;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.widget.ModernTwoStateWidget;

/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes
 *
 */
public class ScaleRibbonSection extends RibbonSection {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The m check auto y.
   */
  private ModernTwoStateWidget mCheckAutoY = new ModernCheckSwitch("Auto");

  // private ModernTwoStateWidget mCheckMaxY =
  // new RibbonLargeRadioButton("Max");

  /**
   * The m check common y scale.
   */
  private ModernTwoStateWidget mCheckCommonYScale = new ModernCheckSwitch(
      "Same");

  /**
   * The m check normalize.
   */
  private ModernTwoStateWidget mCheckNormalize = new ModernCheckSwitch(
      "Normalize", true);

  /**
   * The m text y min.
   */
  private ModernCompactSpinner mTextYMin = new ModernCompactSpinner(0, 10000,
      0);

  /**
   * The m text y max.
   */
  private ModernCompactSpinner mTextYMax = new ModernCompactSpinner(1, 10000,
      1);

  /**
   * The m y axis limit model.
   */
  private AxisLimitsModel mYAxisLimitModel;

  /**
   * The class AxisEvents.
   */
  private class AxisEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      mTextYMax
          .setText(Formatter.number().dp(2).format(mYAxisLimitModel.getMax()));
    }

  }

  /**
   * The class KeyEvents.
   */
  private class KeyEvents implements KeyListener {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void keyPressed(KeyEvent e) {
      if (e.getKeyCode() == KeyEvent.VK_ENTER) {
        setLimits();
      }
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyReleased(java.awt.event.KeyEvent)
     */
    @Override
    public void keyReleased(KeyEvent arg0) {
      // TODO Auto-generated method stub

    }

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    @Override
    public void keyTyped(KeyEvent arg0) {
      // TODO Auto-generated method stub

    }
  }

  /**
   * The class AutoYEvents.
   */
  private class AutoYEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
     * ui. event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      mYAxisLimitModel.setAutoLimits(mCheckAutoY.isSelected());
    }
  }

  /**
   * The class CommonYEvents.
   */
  private class CommonYEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
     * ui. event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      mYAxisLimitModel.setCommonYScale(mCheckCommonYScale.isSelected());
    }
  }

  /**
   * The class NormalizeEvents.
   */
  private class NormalizeEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
     * ui. event.ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      mYAxisLimitModel.setNormalize(mCheckNormalize.isSelected());
    }
  }

  /**
   * Instantiates a new scale ribbon section2.
   *
   * @param ribbon the ribbon
   * @param name the name
   * @param yAxisLimitModel the y axis limit model
   */
  public ScaleRibbonSection(Ribbon ribbon, String name,
      AxisLimitsModel yAxisLimitModel) {
    super(ribbon, name);

    mYAxisLimitModel = yAxisLimitModel;

    add(mTextYMax);
    add(UI.createHGap(5));
    add(mCheckAutoY);
    // add(UI.createHGap(2));
    // add(createHGap());
    // add(mCheckMaxY);

    // box.add(new ModernLabel("Max"));
    // box.add(createHGap());

    // box.add(createHGap());
    // box.add(new RibbonSubSectionSeparator());
    // box.add(createHGap());
    // box.add(mCheckCommonYScale);
    add(mCheckNormalize);
    // add(UI.createHGap(2));
    add(mCheckCommonYScale);

    // new ModernButtonGroup(mCheckAutoY, mCheckMaxY);

    mCheckAutoY.setSelected(mYAxisLimitModel.getAutoSetLimits());

    // ModernWidget.setSize(mCheckAutoY, 60);
    // ModernWidget.setSize(mCheckMaxY, 60);

    // ModernWidget.setSize(mCheckAutoY, 60);
    // ModernWidget.setSize(mCheckNormalize, 100);
    // ModernWidget.setSize(mCheckCommonYScale, 60);

    mCheckAutoY.addClickListener(new AutoYEvents());
    // mCheckMaxY.addClickListener(new AutoYEvents());

    mCheckCommonYScale.setSelected(mYAxisLimitModel.getCommonScale());
    mCheckCommonYScale.addClickListener(new CommonYEvents());

    mCheckNormalize.setSelected(mYAxisLimitModel.getNormalize());
    mCheckNormalize.addClickListener(new NormalizeEvents());

    mTextYMin.setEnabled(false);
    mTextYMax.addKeyListener(new KeyEvents());
    mTextYMax.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        setLimits();
      }
    });

    mYAxisLimitModel.addChangeListener(new AxisEvents());

  }

  /**
   * Sets the limits.
   */
  private void setLimits() {
    mCheckAutoY.setSelected(false);
    mYAxisLimitModel.setLimits(mTextYMin.getValue(), mTextYMax.getValue());
  }

}
