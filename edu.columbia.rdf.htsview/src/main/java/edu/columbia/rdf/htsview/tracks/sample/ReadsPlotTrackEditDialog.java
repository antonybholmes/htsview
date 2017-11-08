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

import javax.swing.Box;

import org.jebtk.modern.BorderService;
import org.jebtk.modern.UI;
import org.jebtk.modern.button.CheckBox;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.graphics.color.ColorSwatchButton;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;
import org.jebtk.modern.text.ModernClipboardTextField;
import org.jebtk.modern.text.ModernTextBorderPanel;
import org.jebtk.modern.text.ModernTextField;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernWindow;


// TODO: Auto-generated Javadoc
/**
 * The Class ReadsPlotTrackEditDialog.
 */
public class ReadsPlotTrackEditDialog extends ModernDialogTaskWindow implements ModernClickListener {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The m line color button. */
	private ColorSwatchButton mLineColorButton;
	
	/** The m fill color button. */
	private ColorSwatchButton mFillColorButton;

	/** The m name field. */
	private ModernTextField mNameField = 
			new ModernClipboardTextField("Name");

	/** The m track. */
	private ReadsPlotTrack mTrack;

	/** The m check forward. */
	private CheckBox mCheckForward = 
			new ModernCheckSwitch("Forward Strand", true);
	
	/** The m check line color. */
	private CheckBox mCheckLineColor =
			new ModernCheckSwitch("Line color");
	
	/** The m check fill color. */
	private CheckBox mCheckFillColor =
			new ModernCheckSwitch("Fill color");
	
	/** The m check neg. */
	private CheckBox mCheckNeg = 
			new ModernCheckSwitch("Negative Strand", true);
	
	/** The m check neg line color. */
	private CheckBox mCheckNegLineColor =
			new ModernCheckSwitch("Line color");
	
	/** The m check neg fill color. */
	private CheckBox mCheckNegFillColor =
			new ModernCheckSwitch("Fill color");
	
	/** The m spinner read height. */
	private ModernCompactSpinner mSpinnerReadHeight =
			new ModernCompactSpinner(1, 32, 1);
	
	/** The m spinner gap. */
	private ModernCompactSpinner mSpinnerGap =
			new ModernCompactSpinner(0, 32, 1);
	
	/** The m neg line color button. */
	private ColorSwatchButton mNegLineColorButton;
	
	/** The m neg fill color button. */
	private ColorSwatchButton mNegFillColorButton;


	/**
	 * Instantiates a new reads plot track edit dialog.
	 *
	 * @param parent the parent
	 * @param track the track
	 */
	public ReadsPlotTrackEditDialog(ModernWindow parent, ReadsPlotTrack track) {
		super(parent);
		
		mTrack = track;
		
		setTitle("Reads Plot Track Editor", track.getName());
		
		createUi();
		
		setup();
	}

	/**
	 * Setup.
	 */
	private void setup() {
		mNameField.setEditable(false);
		
		mCheckForward.setSelected(mTrack.getForwardVisible());
		mCheckLineColor.setSelected(mTrack.getLineColor() != null);
		mCheckFillColor.setSelected(mTrack.getFillColor() != null);
		
		mCheckNeg.setSelected(mTrack.getNegVisible());
		mCheckNegLineColor.setSelected(mTrack.getNegLineColor() != null);
		mCheckNegFillColor.setSelected(mTrack.getNegFillColor() != null);
		
		mSpinnerReadHeight.setValue(mTrack.getReadHeight());
		mSpinnerGap.setValue(mTrack.getGap());
	
		setSize(480, 460);
		
		UI.centerWindowToScreen(this);
	}


	/**
	 * Creates the ui.
	 */
	private final void createUi() {
		mNameField.setText(mTrack.getName());
		
		
		Box box = VBox.create();
		
		Box box2 = HBox.create();
		
		box2.add(new ModernAutoSizeLabel("Name", 50));
		box2.add(new ModernTextBorderPanel(mNameField, 300));
		
		box.add(box2);
		
		box.add(UI.createVGap(10));
		
		box.add(mCheckForward);
		
		box.add(UI.createVGap(5));
		
		box2 = HBox.create();
		
		box2.setBorder(BorderService.getInstance().createLeftBorder(20));
		
		UI.setSize(mCheckLineColor, 100, ModernWidget.WIDGET_HEIGHT);
		box2.add(mCheckLineColor);
		
		mLineColorButton = new ColorSwatchButton(mParent, 
				mTrack.getLineColor());
		
		box2.add(mLineColorButton);
		
		box.add(box2);
		
		box.add(UI.createVGap(5));
		
		box2 = HBox.create();
		
		box2.setBorder(BorderService.getInstance().createLeftBorder(20));
		
		UI.setSize(mCheckFillColor, 100, ModernWidget.WIDGET_HEIGHT);
		box2.add(mCheckFillColor);
		
		mFillColorButton = new ColorSwatchButton(mParent, 
				mTrack.getFillColor());
		
		box2.add(mFillColorButton);
		
		box.add(box2);
		
		box.add(UI.createVGap(10));
		
		//
		// Reverse
		//
		
		box.add(mCheckNeg);
		
		box.add(UI.createVGap(5));
		
		box2 = HBox.create();
		
		box2.setBorder(BorderService.getInstance().createLeftBorder(20));
		
		UI.setSize(mCheckNegLineColor, 100, ModernWidget.WIDGET_HEIGHT);
		box2.add(mCheckNegLineColor);
		
		mNegLineColorButton = new ColorSwatchButton(mParent, 
				mTrack.getNegLineColor());
		
		box2.add(mNegLineColorButton);
		
		box.add(box2);
		
		box.add(UI.createVGap(5));
		
		box2 = HBox.create();
		
		box2.setBorder(BorderService.getInstance().createLeftBorder(20));
		
		UI.setSize(mCheckNegFillColor, 100, ModernWidget.WIDGET_HEIGHT);
		box2.add(mCheckNegFillColor);
		
		mNegFillColorButton = new ColorSwatchButton(mParent, 
				mTrack.getNegFillColor());
		
		box2.add(mNegFillColorButton);
		
		box.add(box2);
		
		box.add(UI.createVGap(20));
		
		box2 = HBox.create();
		box2.add(new ModernAutoSizeLabel("Read height", 100));
		box2.add(mSpinnerReadHeight);
		box.add(box2);
		
		box.add(UI.createVGap(5));
		
		box2 = HBox.create();
		box2.add(new ModernAutoSizeLabel("Gap", 100));
		box2.add(mSpinnerGap);
		box.add(box2);
		
		setDialogCardContent(box);
	}

	/* (non-Javadoc)
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
		return mCheckLineColor.isSelected() ? mLineColorButton.getSelectedColor() : null;
	}
	
	/**
	 * Gets the fill color.
	 *
	 * @return the fill color
	 */
	public Color getFillColor() {
		return mCheckFillColor.isSelected() ? mFillColorButton.getSelectedColor() : null;
	}

	/**
	 * Gets the neg line color.
	 *
	 * @return the neg line color
	 */
	public Color getNegLineColor() {
		return mCheckNegLineColor.isSelected() ? mNegLineColorButton.getSelectedColor() : null;
	}
	
	/**
	 * Gets the neg fill color.
	 *
	 * @return the neg fill color
	 */
	public Color getNegFillColor() {
		return mCheckNegFillColor.isSelected() ? mNegFillColorButton.getSelectedColor() : null;
	}

	/**
	 * Gets the forward visible.
	 *
	 * @return the forward visible
	 */
	public boolean getForwardVisible() {
		return mCheckForward.isSelected();
	}
	
	/**
	 * Gets the neg visible.
	 *
	 * @return the neg visible
	 */
	public boolean getNegVisible() {
		return mCheckNeg.isSelected();
	}
	
	/**
	 * Gets the read height.
	 *
	 * @return the read height
	 */
	public int getReadHeight() {
		return mSpinnerReadHeight.getIntValue();
	}
	
	/**
	 * Gets the gap.
	 *
	 * @return the gap
	 */
	public int getGap() {
		return mSpinnerGap.getIntValue();
	}
}
