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

import javax.swing.Box;

import org.jebtk.bioinformatics.ext.ucsc.TrackDisplayMode;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;

// TODO: Auto-generated Javadoc
/**
 * The Class BedEditDialog.
 */
public class BedEditDialog extends ModernDialogTaskWindow {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m line color button. */
  private ColorSwatchButton mLineColorButton;

  /** The m name field. */
  private ModernTextField mNameField = new ModernClipboardTextField("Name");

  /** The m check full. */
  private ModernRadioButton mCheckFull = new ModernRadioButton("Full");

  /** The m check compact. */
  private ModernRadioButton mCheckCompact = new ModernRadioButton("Compact",
      true);

  /** The m track. */
  private UcscPlotTrack mTrack;

  /**
   * Instantiates a new bed edit dialog.
   *
   * @param parent the parent
   * @param track the track
   */
  public BedEditDialog(ModernWindow parent, UcscPlotTrack track) {
    super(parent);

    mTrack = track;

    setTitle("Track Editor", track.getName());

    createUi();

    setup();
  }

  /**
   * Setup.
   */
  private void setup() {
    ModernButtonGroup group = new ModernButtonGroup();

    group.add(mCheckFull);
    group.add(mCheckCompact);

    if (mTrack.getDisplayMode() == TrackDisplayMode.FULL) {
      mCheckFull.doClick();
    } else {
      mCheckCompact.doClick();
    }

    setSize(480, 260);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    mNameField.setText(mTrack.getName());

    Box box = VBox.create();

    int[] rows = { ModernWidget.WIDGET_HEIGHT };
    int[] cols = { 80, 300 };

    MatrixPanel matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING,
        ModernWidget.PADDING);

    matrixPanel.add(new ModernAutoSizeLabel("Name"));
    matrixPanel.add(new ModernTextBorderPanel(mNameField));

    matrixPanel.add(new ModernAutoSizeLabel("Color"));

    mLineColorButton = new ColorSwatchButton(mParent, mTrack.getFillColor());

    Box box2 = HBox.create();
    box2.add(mLineColorButton);

    matrixPanel.add(box2);

    box.add(matrixPanel);
    box.add(ModernPanel.createVGap());
    box.add(mCheckFull);
    box.add(mCheckCompact);

    setContent(box);
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.awt.Component#getName()
   */
  public String getName() {
    return mNameField.getText();
  }

  /**
   * Gets the line color.
   *
   * @return the line color
   */
  public Color getLineColor() {
    return mLineColorButton.getSelectedColor();
  }

  /**
   * Gets the mode.
   *
   * @return the mode
   */
  public TrackDisplayMode getMode() {
    if (mCheckFull.isSelected()) {
      return TrackDisplayMode.FULL;
    } else {
      return TrackDisplayMode.COMPACT;
    }
  }
}
