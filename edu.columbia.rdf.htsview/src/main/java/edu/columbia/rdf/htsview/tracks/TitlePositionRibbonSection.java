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

import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckButton;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeRadioButton;
import org.jebtk.modern.ribbon.RibbonSection;

/**
 * Allows user to select the resolution to view sequences.
 *
 * @author Antony Holmes Holmes
 */
public class TitlePositionRibbonSection extends RibbonSection
    implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The m top button.
   */
  private ModernCheckButton mTopButton = new RibbonLargeRadioButton("Top",
      UIService.getInstance().loadIcon("title_top", 32));

  /**
   * The m right button.
   */
  private ModernCheckButton mRightButton = new RibbonLargeRadioButton("Right",
      UIService.getInstance().loadIcon("title_right", 32));

  /** The m check visible. */
  private ModernCheckButton mCheckVisible = new RibbonLargeRadioButton(
      "Visible");

  /**
   * The m model.
   */
  private TitlePositionModel mModel;

  /**
   * Instantiates a new title position ribbon section.
   *
   * @param ribbon the ribbon
   * @param model the model
   */
  public TitlePositionRibbonSection(Ribbon ribbon, TitlePositionModel model) {
    super(ribbon, "Title Position");

    mModel = model;

    add(mTopButton);
    add(UI.createHGap(2));
    add(mRightButton);
    add(UI.createHGap(2));

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mTopButton);
    add(UI.createHGap(2));
    group.add(mRightButton);

    mTopButton.addClickListener(this);
    mRightButton.addClickListener(this);

    mTopButton.setSelected(true);
  }

  /**
   * Change.
   *
   * @param e the e
   */
  private void change(ModernClickEvent e) {
    if (mTopButton.isSelected()) {
      mModel.set(
          new TitleProperties(TitlePosition.TOP, mCheckVisible.isSelected()));
    } else if (mRightButton.isSelected()) {
      mModel.set(
          new TitleProperties(TitlePosition.RIGHT, mCheckVisible.isSelected()));
    } else {

    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.abh.common.ui.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * ui. event.ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    change(e);
  }
}
