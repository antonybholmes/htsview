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
import java.awt.Dimension;

import javax.swing.Box;

import org.jebtk.modern.UI;
import org.jebtk.modern.button.ButtonsBox;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernCheckBox;
import org.jebtk.modern.dialog.ModernDialogButton;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernDialogWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.MatrixPanel;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;

/**
 * The Class TrackEditDialog.
 */
public class TrackEditDialog extends ModernDialogWindow
    implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m ok button. */
  private ModernButton mOkButton = new ModernDialogButton(UI.BUTTON_OK);

  /** The m cancel button. */
  private ModernButton mCancelButton = new ModernDialogButton(UI.BUTTON_CANCEL);

  /** The m line color button. */
  private ColorSwatchButton mLineColorButton;

  /** The m fill color button. */
  private ColorSwatchButton mFillColorButton;

  /** The m name field. */
  private ModernTextField mNameField = new ModernClipboardTextField("Name");

  /** The m track. */
  private Track mTrack;

  /** The m check line color. */
  private ModernCheckBox mCheckLineColor = new ModernCheckBox("Line color");

  /** The m check fill color. */
  private ModernCheckBox mCheckFillColor = new ModernCheckBox("Fill color");

  /**
   * Instantiates a new track edit dialog.
   *
   * @param parent the parent
   * @param track the track
   */
  public TrackEditDialog(ModernWindow parent, Track track) {
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
    mNameField.setEditable(false);

    mOkButton.addClickListener(this);
    mCancelButton.addClickListener(this);

    mCheckLineColor.setSelected(mTrack.getLineColor() != null);
    mCheckFillColor.setSelected(mTrack.getFillColor() != null);

    setSize(new Dimension(480, 240));

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    mNameField.setText(mTrack.getName());

    int[] rows = { ModernWidget.WIDGET_HEIGHT };
    int[] cols = { 100, 300 };

    MatrixPanel matrixPanel = new MatrixPanel(rows, cols, ModernWidget.PADDING,
        ModernWidget.PADDING);

    matrixPanel.add(new ModernAutoSizeLabel("Name"));
    matrixPanel.add(new ModernTextBorderPanel(mNameField));

    matrixPanel.add(mCheckLineColor);

    mLineColorButton = new ColorSwatchButton(mParent, mTrack.getLineColor());

    Box box2 = HBox.create();
    box2.add(mLineColorButton);

    matrixPanel.add(box2);

    matrixPanel.add(mCheckFillColor);

    mFillColorButton = new ColorSwatchButton(mParent, mTrack.getFillColor());

    box2 = HBox.create();
    box2.add(mFillColorButton);

    matrixPanel.add(box2);

    setContent(matrixPanel);

    Box buttonPanel = new ButtonsBox();

    buttonPanel.add(mOkButton);
    buttonPanel.add(ModernPanel.createHGap());
    buttonPanel.add(mCancelButton);

    setButtons(buttonPanel);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  public final void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOkButton)) {
      setStatus(ModernDialogStatus.OK);
    }

    close();
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
    return mCheckLineColor.isSelected() ? mLineColorButton.getSelectedColor()
        : null;
  }

  /**
   * Gets the fill color.
   *
   * @return the fill color
   */
  public Color getFillColor() {
    return mCheckFillColor.isSelected() ? mFillColorButton.getSelectedColor()
        : null;
  }
}
