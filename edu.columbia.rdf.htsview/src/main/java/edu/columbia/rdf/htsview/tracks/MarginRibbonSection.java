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
import org.jebtk.modern.ribbon.Ribbon;
import org.jebtk.modern.ribbon.RibbonSection;
import org.jebtk.modern.ribbon.RibbonStripContainer;
import org.jebtk.modern.spinner.ModernCompactSpinner;

// TODO: Auto-generated Javadoc
/**
 * Allows user to select a color map.
 *
 * @author Antony Holmes Holmes
 *
 */
public class MarginRibbonSection extends RibbonSection {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The m text y min.
   */
  private ModernCompactSpinner mWidthField = new ModernCompactSpinner(1, 10000,
      400);

  /** The m model. */
  private MarginModel mModel;

  /**
   * The class KeyEvents.
   */
  private class KeyEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      mModel.set(mWidthField.getIntValue());
    }
  }

  /**
   * Instantiates a new scale ribbon section2.
   *
   * @param ribbon the ribbon
   * @param model the model
   */
  public MarginRibbonSection(Ribbon ribbon, MarginModel model) {
    super(ribbon, "Margin");

    mModel = model;

    Box box = new RibbonStripContainer();

    box.add(mWidthField);

    mWidthField.setValue(model.get());

    add(box);

    mWidthField.addChangeListener(new KeyEvents());

    model.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        mWidthField.updateValue(mModel.get());
      }
    });
  }
}
