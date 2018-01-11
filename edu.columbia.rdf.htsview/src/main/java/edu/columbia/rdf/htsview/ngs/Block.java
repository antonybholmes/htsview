/**
 * Copyright 2017 Antony Holmes
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
package edu.columbia.rdf.htsview.ngs;

import java.util.ArrayList;
import java.util.List;

// TODO: Auto-generated Javadoc
/**
 * The Class Block.
 */
public class Block extends Offsets implements Comparable<Block> {

  /** The start. */
  public int start = -1;

  /** The end. */
  public int end = -1;

  /** The width. */
  public int width = -1;

  /** The bin. */
  public int bin = -1;

  /** The level. */
  public int level = -1;

  /** The children. */
  public List<Block> children = new ArrayList<Block>(10);

  /**
   * Instantiates a new block.
   *
   * @param bin the bin
   * @param start the start
   * @param level the level
   * @param width the width
   * @param so the so
   * @param eo the eo
   */
  public Block(int bin, int start, int level, int width, int so, int eo) {
    super(so, eo);

    this.bin = bin;
    this.start = start;
    this.level = level;
    this.width = width;
    this.end = start + width - 1;
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Comparable#compareTo(java.lang.Object)
   */
  @Override
  public int compareTo(Block b) {
    if (start > b.start) {
      return 1;
    } else if (start < b.start) {
      return -1;
    } else {
      return 0;
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.lang.Object#toString()
   */
  @Override
  public String toString() {
    return start + ":" + width;
  }
}