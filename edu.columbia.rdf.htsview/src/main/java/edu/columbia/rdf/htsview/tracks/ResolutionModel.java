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

import org.jebtk.core.model.ItemModel;
import org.jebtk.core.settings.SettingsService;

/**
 * Centrally keep track of selected experiments in the order they were selected.
 * 
 * @author Antony Holmes
 *
 */
public class ResolutionModel extends ItemModel<Integer> {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * Instantiates a new resolution model.
   */
  public ResolutionModel() {
    set(SettingsService.getInstance().getInt("edb.reads.default-resolution"));
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.lib.model.ItemModel#set(java.lang.Object)
   */
  @Override
  public void set(Integer resolution) {
    // System.err.println("resolution " + resolution);

    super.set(resolution);

    // Store the setting
    SettingsService.getInstance().update("edb.reads.default-resolution",
        resolution);
  }
}
