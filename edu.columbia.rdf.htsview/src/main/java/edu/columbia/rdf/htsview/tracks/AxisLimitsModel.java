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

import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.settings.SettingsService;

/**
 * The class AxisLimitsModel.
 */
public class AxisLimitsModel extends ChangeListeners {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The m min.
   */
  private double mMin = 0;

  /**
   * The m max.
   */
  private double mMax = 1;

  /**
   * The m auto limits.
   */
  private boolean mAutoLimits = false;

  /**
   * The m common scale.
   */
  private boolean mCommonScale = true;

  /**
   * The m normalize.
   */
  private boolean mNormalize;

  /**
   * The class ChangeEvents.
   */
  private class ChangeEvents implements ChangeListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.lib.event.ChangeListener#changed(org.abh.lib.event.ChangeEvent)
     */
    @Override
    public void changed(ChangeEvent e) {
      SettingsService.getInstance().update("edb.reads.scale.max-y", mMax);
      SettingsService.getInstance().update("edb.reads.scale.auto-y",
          mAutoLimits);
      SettingsService.getInstance().update("edb.reads.scale.same-y",
          mCommonScale);
      SettingsService.getInstance().update("edb.reads.scale.normalize-y",
          mNormalize);
    }
  }

  /**
   * Instantiates a new axis limits model.
   */
  public AxisLimitsModel() {
    mMax = SettingsService.getInstance().getAsDouble("edb.reads.scale.max-y");
    mAutoLimits = SettingsService.getInstance()
        .getAsBool("edb.reads.scale.auto-y");
    mCommonScale = SettingsService.getInstance()
        .getAsBool("edb.reads.scale.same-y");
    mNormalize = SettingsService.getInstance()
        .getAsBool("edb.reads.scale.normalize-y");

    addChangeListener(new ChangeEvents());
  }

  /**
   * Sets the limits.
   *
   * @param min the min
   * @param max the max
   */
  public void setLimits(double min, double max) {
    mMin = min;
    mMax = max;
    // Assume if you set limits, you don't want auto limits.
    mAutoLimits = false;

    fireChanged();
  }

  /**
   * Gets the min.
   *
   * @return the min
   */
  public double getMin() {
    return mMin;
  }

  /**
   * Gets the max.
   *
   * @return the max
   */
  public double getMax() {
    return mMax;
  }

  /**
   * Sets the auto limits.
   *
   * @param autoLimits the new auto limits
   */
  public void setAutoLimits(boolean autoLimits) {
    mAutoLimits = autoLimits;

    fireChanged();
  }

  /**
   * Gets the auto set limits.
   *
   * @return the auto set limits
   */
  public boolean getAutoSetLimits() {
    return mAutoLimits;
  }

  /**
   * Sets the common y scale.
   *
   * @param commonScale the new common y scale
   */
  public void setCommonYScale(boolean commonScale) {
    mCommonScale = commonScale;

    fireChanged();
  }

  /**
   * Gets the common y scale.
   *
   * @return the common y scale
   */
  public boolean getCommonScale() {
    return mCommonScale;
  }

  /**
   * Sets the normalize.
   *
   * @param normalize the new normalize
   */
  public void setNormalize(boolean normalize) {
    mNormalize = normalize;

    fireChanged();
  }

  /**
   * Gets the normalize.
   *
   * @return the normalize
   */
  public boolean getNormalize() {
    return mNormalize;
  }
}
