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

import javax.swing.Box;

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.modern.UI;
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.ribbon.RibbonStripContainer;
import org.jebtk.modern.spinner.ModernCompactSpinner;
import org.jebtk.modern.text.ModernAutoSizeLabel;



// TODO: Auto-generated Javadoc
/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes Holmes
 *
 */
public class SizeRibbonSection extends RibbonSection {
	
	/**
	 * The constant serialVersionUID.
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * The m text y min.
	 */
	private ModernCompactSpinner mWidthField = 
			new ModernCompactSpinner(1, 10000, 1000);
	
	/** The m height field. */
	private ModernCompactSpinner mHeightField = 
			new ModernCompactSpinner(1, 10000, 1000);

	/** The m width model. */
	private WidthModel mWidthModel;

	/** The m height model. */
	private HeightModel mHeightModel;
	
	
	/**
	 * The class KeyEvents.
	 */
	private class HeightKeyEvents implements ChangeListener {

		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			mHeightModel.set(mHeightField.getIntValue());
		}
	}
	
	/**
	 * The Class WidthKeyEvents.
	 */
	private class WidthKeyEvents implements ChangeListener {

		/* (non-Javadoc)
		 * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
		 */
		@Override
		public void changed(ChangeEvent e) {
			mWidthModel.set(mWidthField.getIntValue());
		}
	}
	
	
	/**
	 * Instantiates a new scale ribbon section2.
	 *
	 * @param ribbon the ribbon
	 * @param widthModel the width model
	 * @param heightModel the height model
	 */
	public SizeRibbonSection(Ribbon ribbon, WidthModel widthModel, HeightModel heightModel) {
		super(ribbon, "Size");
		
		mWidthModel = widthModel;
		mHeightModel = heightModel;
		
		
		Box box = new RibbonStripContainer();
		
		box.add(new ModernAutoSizeLabel("W", 20));
		box.add(mWidthField);
		box.add(UI.createHGap(10));
		box.add(new ModernAutoSizeLabel("H", 20));
		box.add(mHeightField);
		add(box);
		
		mWidthField.setValue(widthModel.get());
		
		mWidthField.addChangeListener(new WidthKeyEvents());
		
		mHeightField.setValue(heightModel.get());

		mHeightField.addChangeListener(new HeightKeyEvents());
		
		
		widthModel.addChangeListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent e) {
				mWidthField.updateValue(mWidthModel.get());
			}});
		
		heightModel.addChangeListener(new ChangeListener() {

			@Override
			public void changed(ChangeEvent e) {
				mHeightField.updateValue(mHeightModel.get());
			}});
	}
}
