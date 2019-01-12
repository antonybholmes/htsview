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
package edu.columbia.rdf.htsview.tracks.loaders;

import java.io.IOException;

import javax.swing.Box;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

/**
 * Users can decide if they want bedgraph display as bed or graph.
 * 
 * @author Antony Holmes
 *
 */
public class BedGraphStyleDialog extends ModernDialogTaskWindow
    implements ModernClickListener {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The m bed graph button.
   */
  private ModernRadioButton mBedGraphButton = new ModernRadioButton("BedGraph",
      true);

  /**
   * The m bed button.
   */
  private ModernRadioButton mBedButton = new ModernRadioButton("BED");

  /**
   * The m check all.
   */
  private ModernCheckBox mCheckAll = new ModernCheckBox("Apply to all", true);

  /**
   * Instantiates a new bed graph style dialog.
   *
   * @param parent the parent
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public BedGraphStyleDialog(ModernWindow parent) throws IOException {
    super(parent);

    setTitle("Display Style");

    setup();

    createUi();
  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mBedGraphButton);
    group.add(mBedButton);

    setSize(400, 180);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private final void createUi() throws IOException {
    // this.getWindowContentPanel().add(new JLabel("Change " +
    // getProductDetails().getProductName() + " settings", JLabel.LEFT),
    // BorderLayout.PAGE_START);

    Box box = Box.createVerticalBox();

    box.add(mBedGraphButton);
    box.add(ModernPanel.createVGap());
    box.add(mBedButton);

    setContent(box);
  }

  /**
   * Checks if is bed graph style.
   *
   * @return true, if is bed graph style
   */
  public boolean isBedGraphStyle() {
    return mBedGraphButton.isSelected();
  }

  /**
   * Gets the apply to all.
   *
   * @return the apply to all
   */
  public boolean getApplyToAll() {
    return mCheckAll.isSelected();
  }
}
