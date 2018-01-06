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

import java.text.ParseException;
import java.util.Iterator;
import java.util.List;

import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.text.TextUtils;

// TODO: Auto-generated Javadoc
/**
 * The class ResolutionService.
 */
public class ResolutionService implements Iterable<Integer> {

  /**
   * The class ResolutionServiceLoader.
   */
  private static class ResolutionServiceLoader {

    /**
     * The constant INSTANCE.
     */
    private static final ResolutionService INSTANCE = new ResolutionService();
  }

  /**
   * Gets the single instance of SettingsService.
   *
   * @return single instance of SettingsService
   */
  public static ResolutionService getInstance() {
    return ResolutionServiceLoader.INSTANCE;
  }

  /**
   * The m resolutions.
   */
  private List<Integer> mResolutions;

  /**
   * Instantiates a new resolution service.
   */
  private ResolutionService() {
    load();
  }

  /**
   * Load.
   */
  private void load() {
    try {
      mResolutions = CollectionUtils.sort(
          CollectionUtils.toInt(TextUtils.scSplit(SettingsService.getInstance().getAsString("edb.reads.resolutions"))));
    } catch (ParseException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Iterable#iterator()
   */
  @Override
  public Iterator<Integer> iterator() {
    return mResolutions.iterator();
  }

  /**
   * Gets the human readable.
   *
   * @param resolution
   *          the resolution
   * @return the human readable
   */
  public static String getHumanReadable(int resolution) {
    switch (resolution) {
    case 10:
      return "10 bp";
    case 100:
      return "100 bp";
    case 1000:
      return "1 kb";
    case 10000:
      return "10 kb";
    case 100000:
      return "100 kb";
    case 1000000:
      return "1 Mb";
    default:
      return "1 bp";
    }
  }
}
