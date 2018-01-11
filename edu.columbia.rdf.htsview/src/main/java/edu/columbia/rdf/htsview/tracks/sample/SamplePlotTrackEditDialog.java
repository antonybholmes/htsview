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
package edu.columbia.rdf.htsview.tracks.sample;

import java.awt.Color;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;

import javax.swing.Box;

import org.jebtk.bioinformatics.ui.external.samtools.BamGuiFileFilter;
import org.jebtk.core.Mathematics;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.modern.UI;
import org.jebtk.modern.UIService;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.button.ModernButtonGroup;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.button.ModernRadioButton;
import org.jebtk.modern.dialog.ModernDialogFlatButton;
import org.jebtk.modern.dialog.ModernDialogHelpWindow;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.graphics.icons.OpenFolderVectorIcon;
import org.jebtk.modern.io.FileDialog;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.widget.ModernTwoStateWidget;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.chipseq.ChipSeqSamplesDialog;
import edu.columbia.rdf.htsview.ngs.Brt2GuiFileFilter;
import edu.columbia.rdf.htsview.ngs.BvtGuiFileFilter;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;
import edu.columbia.rdf.matcalc.figure.graph2d.Graph2dStyleButton;

// TODO: Auto-generated Javadoc
/**
 * The Class SamplePlotTrackEditDialog.
 */
public class SamplePlotTrackEditDialog extends ModernDialogHelpWindow {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The Constant FIELD_WIDTH. */
  private static final int FIELD_WIDTH = 140;

  /** The m line color button. */
  private ColorSwatchButton mLineColorButton;

  /** The m fill color button. */
  private ColorSwatchButton mFillColorButton;

  /** The m name field. */
  private ModernTextField mNameField = new ModernClipboardTextField("Name");

  /** The m subtract field. */
  private ModernTextField mSubtractField = new ModernClipboardTextField();

  /** The m height field. */
  private ModernCompactSpinner mHeightField = new ModernCompactSpinner(1, 1000,
      FIELD_WIDTH);

  /** The m check common Y. */
  private ModernRadioButton mCheckCommonY = new ModernRadioButton("Common Y",
      FIELD_WIDTH);

  /** The m check auto Y. */
  private ModernRadioButton mCheckAutoY = new ModernRadioButton("Auto Y",
      FIELD_WIDTH);

  /** The m check max Y. */
  private ModernRadioButton mCheckMaxY = new ModernRadioButton("Max Y",
      FIELD_WIDTH);

  /** The m check normalize Y. */
  private ModernTwoStateWidget mCheckNormalizeY = new ModernCheckSwitch(
      "Normalize Y", FIELD_WIDTH);

  /** The m max Y field. */
  private ModernCompactSpinner mMaxYField = new ModernCompactSpinner(0, 10000,
      1);

  /** The m track. */
  private SamplePlotTrack mTrack;

  /** The m check line color. */
  private ModernTwoStateWidget mCheckLineColor = new ModernCheckSwitch(
      "Line color", FIELD_WIDTH);

  /** The m check fill color. */
  private ModernTwoStateWidget mCheckFillColor = new ModernCheckSwitch(
      "Fill color", FIELD_WIDTH);

  /** The m check subtract input. */
  private ModernTwoStateWidget mCheckSubtractInput = new ModernCheckSwitch(
      "Subtract", FIELD_WIDTH);

  /** The m check common height. */
  private ModernRadioButton mCheckCommonHeight = new ModernRadioButton(
      "Common height", FIELD_WIDTH);

  /** The m check max height. */
  private ModernRadioButton mCheckMaxHeight = new ModernRadioButton("Height",
      FIELD_WIDTH);

  /** The m sample db button. */
  private ModernButton mSampleDbButton = new ModernDialogFlatButton(
      UIService.getInstance().loadIcon("database", 16));

  /** The m sample fs button. */
  private ModernButton mSampleFsButton = new ModernDialogFlatButton(
      UIService.getInstance().loadIcon(OpenFolderVectorIcon.class, 16));

  /** The m assembly. */
  private SampleAssembly mAssembly;

  /** The m input sample. */
  private Sample mInputSample;

  /** The m input assembly. */
  private SampleAssembly mInputAssembly;

  /** The m style button. */
  private Graph2dStyleButton mStyleButton;

  /**
   * Instantiates a new sample plot track edit dialog.
   *
   * @param parent the parent
   * @param track the track
   * @param assembly the assembly
   */
  public SamplePlotTrackEditDialog(ModernWindow parent, SamplePlotTrack track,
      SampleAssembly assembly) {
    super(parent, "htsview.sample-editor.help.url");

    mTrack = track;

    mAssembly = assembly;

    mInputSample = track.getInputSample();
    mInputAssembly = track.getInputAssembly();

    mHeightField.setValue(track.getHeight());

    mMaxYField
        .setValue(Mathematics.dp(track.getYMax(track.getNormalizeY()), 2)); // Math.ceil(track.getYMax(track.getNormalizeY())));

    if (track.getCommonY()) {
      mCheckCommonY.setSelected(true);
    } else if (track.getAutoY()) {
      mCheckAutoY.setSelected(true);
    } else {
      mCheckMaxY.setSelected(true);
    }

    new ModernButtonGroup(mCheckMaxHeight, mCheckCommonHeight);

    if (track.getCommonHeight()) {
      mCheckCommonHeight.setSelected(true);
    } else {
      mCheckMaxHeight.setSelected(true);
    }

    mCheckNormalizeY.setSelected(track.getNormalizeY());

    mCheckSubtractInput.setSelected(track.getSubtractInput());

    setTitle("Sample Track Editor", track.getName());

    if (mInputSample != null) {
      mSubtractField.setText(mInputSample.getName());
    }

    createUi();

    setup();
  }

  /**
   * Setup.
   */
  private void setup() {
    // mNameField.setEditable(false);
    mSubtractField.setEditable(false);

    new ModernButtonGroup(mCheckCommonY, mCheckAutoY, mCheckMaxY);

    mSampleDbButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        try {
          loadDbSample();
        } catch (IOException e1) {
          e1.printStackTrace();
        } catch (ParseException e1) {
          e1.printStackTrace();
        }
      }
    });

    mSampleFsButton.addClickListener(new ModernClickListener() {

      @Override
      public void clicked(ModernClickEvent e) {
        try {
          loadFsSample();
        } catch (IOException e1) {
          e1.printStackTrace();
        } catch (ParseException e1) {
          e1.printStackTrace();
        }
      }
    });

    mOkButton.addClickListener(this);
    mCancelButton.addClickListener(this);

    mCheckLineColor.setSelected(mTrack.getLineColor() != null);
    mCheckFillColor.setSelected(mTrack.getFillColor() != null);

    setSize(720, 700);

    UI.centerWindowToScreen(this);
  }

  /**
   * Creates the ui.
   */
  private final void createUi() {
    mNameField.setText(mTrack.getName());

    Box box = VBox.create();

    Box box2;

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Name", FIELD_WIDTH));
    box2.add(new ModernTextBorderPanel(mNameField, 300));
    box.add(box2);
    // box.add(Ui.createVGap(20));

    midSectionHeader("Y Axis", box);

    box.add(mCheckCommonY);
    box.add(UI.createVGap(5));
    box.add(mCheckAutoY);
    box.add(UI.createVGap(5));

    box2 = HBox.create();
    box2.add(mCheckMaxY);
    box2.add(mMaxYField);
    box.add(box2);

    box.add(UI.createVGap(5));

    box.add(mCheckNormalizeY);

    midSectionHeader("Style", box);

    box2 = HBox.create();
    box2.add(new ModernAutoSizeLabel("Style", FIELD_WIDTH));
    mStyleButton = new Graph2dStyleButton(mTrack.getStyle());
    box2.add(mStyleButton);
    box.add(box2);

    box.add(UI.createVGap(5));

    box2 = HBox.create();
    box2.add(mCheckLineColor);

    mLineColorButton = new ColorSwatchButton(mParent, mTrack.getLineColor());

    box2.add(mLineColorButton);
    box.add(box2);
    box.add(UI.createVGap(5));

    box2 = HBox.create();
    box2.add(mCheckFillColor);

    mFillColorButton = new ColorSwatchButton(mParent, mTrack.getFillColor());

    box2.add(mFillColorButton);
    box.add(box2);

    midSectionHeader("Size", box);

    box.add(mCheckCommonHeight);
    box.add(UI.createVGap(5));

    box2 = HBox.create();
    box2.add(mCheckMaxHeight);
    box2.add(mHeightField);
    box.add(box2);

    midSectionHeader("Input", box);

    box2 = HBox.create();
    box2.add(mCheckSubtractInput);
    box2.add(new ModernTextBorderPanel(mSubtractField, 300));

    if (SettingsService.getInstance().getAsBool("edb.modules.edbw.enabled")) {
      box2.add(UI.createHGap(5));
      box2.add(mSampleDbButton);
    }

    box2.add(UI.createHGap(5));
    box2.add(mSampleFsButton);

    box.add(box2);

    setDialogCardContent(box);
  }

  /**
   * Load db sample.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void loadDbSample() throws IOException, ParseException {
    ChipSeqSamplesDialog dialog = new ChipSeqSamplesDialog(mParent);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    mInputSample = dialog.getSelectedSample();
    mInputAssembly = mAssembly;

    if (mInputSample != null) {
      mSubtractField.setText(mInputSample.getName());
      mCheckSubtractInput.setSelected(true);
    }
  }

  /**
   * Load fs sample.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   * @throws ParseException the parse exception
   */
  private void loadFsSample() throws IOException, ParseException {
    browseForTrack();
  }

  /**
   * Browse for tracks.
   *
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void browseForTrack() throws ParseException, IOException {
    browseForTrack(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Browse for tracks.
   *
   * @param pwd the pwd
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void browseForTrack(Path pwd) throws ParseException, IOException {
    openTrack(FileDialog.open(mParent)
        .filter(new AllSamplesGuiFileFilter(),
            new BamGuiFileFilter(),
            new Brt2GuiFileFilter(),
            new BvtGuiFileFilter())
        .getFile(pwd));
  }

  /**
   * Open track.
   *
   * @param dir the dir
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void openTrack(Path dir) throws ParseException, IOException {
    if (SampleTracks.isBRT2Track(dir)) {
      openBRT2Track(dir);
    } else if (SampleTracks.isBRTTrack(dir)) {
      openBRTTrack(dir);
    } else if (SampleTracks.isBVTTrack(dir)) {
      openBVTTrack(dir);
    } else {
      open16bitTrack(dir);
    }
  }

  /**
   * Open16bit track.
   *
   * @param metaFile the meta file
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void open16bitTrack(Path metaFile)
      throws ParseException, IOException {
    Json json = JsonParser.json(metaFile);

    Sample sample = SampleTracks.getSampleFromTrack(json);

    openTrack(sample, new SampleAssembly16bit(metaFile));
  }

  /**
   * Open brt track.
   *
   * @param metaFile the meta file
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void openBRTTrack(Path metaFile) throws ParseException, IOException {
    Json json = JsonParser.json(metaFile);

    Sample sample = SampleTracks.getSampleFromTrack(json);

    openTrack(sample, new SampleAssemblyBRT(metaFile));
  }

  /**
   * Open BRT 2 track.
   *
   * @param metaFile the meta file
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void openBRT2Track(Path metaFile) throws ParseException, IOException {
    Json json = JsonParser.json(metaFile);

    Sample sample = SampleTracks.getSampleFromTrack(json);

    openTrack(sample, new SampleAssemblyBRT2(metaFile));
  }

  /**
   * Open BVT track.
   *
   * @param metaFile the meta file
   * @throws ParseException the parse exception
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private void openBVTTrack(Path metaFile) throws ParseException, IOException {
    Json json = JsonParser.json(metaFile);

    Sample sample = SampleTracks.getSampleFromTrack(json);

    openTrack(sample, new SampleAssemblyBVT(metaFile));
  }

  /**
   * Open track.
   *
   * @param sample the sample
   * @param assembly the assembly
   */
  private void openTrack(Sample sample, SampleAssembly assembly) {
    mInputSample = sample;
    mInputAssembly = assembly;

    if (mInputSample != null) {
      mSubtractField.setText(mInputSample.getName());
      mCheckSubtractInput.setSelected(true);
    }
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

  /**
   * Gets the subtract input.
   *
   * @return the subtract input
   */
  public boolean getSubtractInput() {
    return mCheckSubtractInput.isSelected();
  }

  /**
   * Gets the input sample.
   *
   * @return the input sample
   */
  public Sample getInputSample() {
    return mInputSample;
  }

  /**
   * Gets the input assembly.
   *
   * @return the input assembly
   */
  public SampleAssembly getInputAssembly() {
    return mInputAssembly;
  }

  /**
   * Gets the track height.
   *
   * @return the track height
   */
  public int getTrackHeight() {
    return mHeightField.getIntValue();
  }

  /**
   * Gets the y max.
   *
   * @return the y max
   */
  public double getYMax() {
    return mMaxYField.getValue();
  }

  /**
   * Gets the auto Y.
   *
   * @return the auto Y
   */
  public boolean getAutoY() {
    return mCheckAutoY.isSelected();
  }

  /**
   * Gets the normalize Y.
   *
   * @return the normalize Y
   */
  public boolean getNormalizeY() {
    return mCheckNormalizeY.isSelected();
  }

  /**
   * Gets the common Y.
   *
   * @return the common Y
   */
  public boolean getCommonY() {
    return mCheckCommonY.isSelected();
  }

  /**
   * Gets the common height.
   *
   * @return the common height
   */
  public boolean getCommonHeight() {
    return mCheckCommonHeight.isSelected();
  }

  /**
   * Gets the style.
   *
   * @return the style
   */
  public PlotStyle getStyle() {
    return mStyleButton.getStyle();
  }
}
