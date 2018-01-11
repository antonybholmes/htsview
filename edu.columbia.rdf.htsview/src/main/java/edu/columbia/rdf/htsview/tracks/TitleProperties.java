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

// TODO: Auto-generated Javadoc
/**
 * The enum TitlePosition.
 */
public class TitleProperties {

  /** The m pos. */
  private TitlePosition mPos;

  /** The m visible. */
  private boolean mVisible;

  /**
   * Instantiates a new title properties.
   *
   * @param position the position
   */
  public TitleProperties(TitlePosition position) {
    this(position, true);
  }

  /**
   * Instantiates a new title properties.
   *
   * @param position the position
   * @param visible the visible
   */
  public TitleProperties(TitlePosition position, boolean visible) {
    mPos = position;
    mVisible = visible;
  }

  /**
   * Gets the position.
   *
   * @return the position
   */
  public TitlePosition getPosition() {
    return mPos;
  }

  /**
   * Gets the visible.
   *
   * @return the visible
   */
  public boolean getVisible() {
    return mVisible;
  }

}
