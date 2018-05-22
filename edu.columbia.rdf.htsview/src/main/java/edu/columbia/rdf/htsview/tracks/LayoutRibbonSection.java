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
import org.jebtk.modern.AssetService;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonLargeRadioButton;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.widget.ModernTwoStateWidget;

/**
 * Allows user to select the resolution to view sequences.
 *
 * @author Antony Holmes Holmes
 */
public class LayoutRibbonSection extends RibbonSection
    implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The m top button.
   */
  private RibbonLargeRadioButton mTopButton = new RibbonLargeRadioButton(
      "Titles Top", AssetService.getInstance().loadIcon("title_top", 32));

  /**
   * The m right button.
   */
  private RibbonLargeRadioButton mRightButton = new RibbonLargeRadioButton(
      "Titles Right", AssetService.getInstance().loadIcon("title_right", 32));

  /**
   * The m compact right button.
   */
  private RibbonLargeRadioButton mCompactRightButton = new RibbonLargeRadioButton(
      "Compact Right Titles",
      AssetService.getInstance().loadIcon("title_compact_right", 32));

  /** The m check visible. */
  private ModernTwoStateWidget mCheckVisible = new ModernCheckSwitch("Show");

  /**
   * The m model.
   */
  private TitlePositionModel mModel;

  /**
   * Instantiates a new title position ribbon section2.
   *
   * @param ribbon the ribbon
   * @param model the model
   */
  public LayoutRibbonSection(Ribbon ribbon, TitlePositionModel model) {
    super(ribbon, "Titles");

    mModel = model;

    add(mCheckVisible);
    add(UI.createHGap(2));

    mTopButton.setToolTip("Vertical", "Title are positioned on top of plots.");
    mTopButton.setShowText(false);
    add(mTopButton);

    mRightButton.setToolTip("Wide",
        "Title are positioned on the right of plots to save vertical space.");
    mRightButton.setShowText(false);
    add(UI.createHGap(2));
    add(mRightButton);

    mCompactRightButton.setToolTip("Compact",
        "Title are positioned on the right of plots to save vertical space.");
    mCompactRightButton.setShowText(false);
    add(UI.createHGap(2));
    add(mCompactRightButton);

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mTopButton);
    group.add(mRightButton);
    group.add(mCompactRightButton);

    mTopButton.addClickListener(this);
    mRightButton.addClickListener(this);
    mCompactRightButton.addClickListener(this);
    mCheckVisible.addClickListener(this);

    switch (mModel.get().getPosition()) {
    case COMPACT_RIGHT:
      mCompactRightButton.setSelected(true);
      break;
    case RIGHT:
      mRightButton.setSelected(true);
      break;
    default:
      mTopButton.setSelected(true);
      break;
    }

    mCheckVisible.setSelected(mModel.get().getVisible());
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
    } else if (mCompactRightButton.isSelected()) {
      mModel.set(new TitleProperties(TitlePosition.COMPACT_RIGHT,
          mCheckVisible.isSelected()));
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
