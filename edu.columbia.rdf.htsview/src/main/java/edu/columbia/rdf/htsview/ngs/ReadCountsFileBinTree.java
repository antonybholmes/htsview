/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.htsview.ngs;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

// TODO: Auto-generated Javadoc
/**
 * Decodes values stored at positions in a tree.
 *
 * @author Antony Holmes Holmes
 */
public abstract class ReadCountsFileBinTree extends ReadCountsFileBin {

  /** The Constant HEADER_OFFSET_BYTES. */
  public static final int HEADER_OFFSET_BYTES = 16; // 2 * 4;

  /**
   * Each block consists of 6, 4 byte fields.
   */
  public static final int BLOCK_SIZE_BYTES = 6 * 4;

  /**
   * This size of 1 billion is the smallest value 10^x that can fit a whole
   * chromosome inside it.
   */
  public static final int MAX_BIN_WIDTH = 1000000000;

  /**
   * The constant MIN_BIN_WIDTH.
   */
  public static final int MIN_BIN_WIDTH = 1000;

  /** The Constant MIN_BIN_LEVEL. */
  public static final int MIN_BIN_LEVEL = 6;

  /**
   * The bin widths for each level of the tree.
   */
  public static final int[] BIN_WIDTHS = { 1000000000, 100000000, 10000000, 1000000, 100000, 10000, 1000 };

  /** The Constant BIN_COUNTS. */
  public static final int[] BIN_COUNTS = { 1, 10, 100, 1000, 10000, 100000, 1000000 };

  /** The Constant CUM_BIN_COUNTS. */
  public static final int[] CUM_BIN_COUNTS = { 0, 1, 1 + 10, 1 + 10 + 100, 1 + 10 + 100 + 1000,
      1 + 10 + 100 + 1000 + 10000, 1 + 10 + 100 + 1000 + 10000 + 100000 };

  /** The Constant TOTAL_BINS. */
  public static final int TOTAL_BINS = 1 + 10 + 100 + 1000 + 10000 + 100000 + 1000000;

  /**
   * The constant BIN_DIVISIONS.
   */
  public static final int BIN_DIVISIONS = 10;

  /** The Constant MULTI_RES_WIDTH_BYTES. */
  public static final int MULTI_RES_WIDTH_BYTES = 4; // 4 + 4;

  /** The Constant RAW_OFFSET. */
  public static final int RAW_OFFSET = TOTAL_BINS * MULTI_RES_WIDTH_BYTES;

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param metaFile
   *          the meta file
   */
  public ReadCountsFileBinTree(Path metaFile) {
    super(metaFile);
  }

  /**
   * Multi res offset.
   *
   * @param b
   *          the b
   * @return the int
   */
  public int multiResOffset(Block b) {
    return (CUM_BIN_COUNTS[b.level] + b.bin) * MULTI_RES_WIDTH_BYTES;
  }

  /**
   * Gets the data offset.
   *
   * @param in
   *          the in
   * @param p
   *          the p
   * @return the data offset
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static Block getDataOffset(RandomAccessFile in, int p) throws IOException {
    return getDataOffset(in, p, MIN_BIN_WIDTH);
  }

  /**
   * Returns the start and end indices of the closest block of reads to a given
   * position. Positions are relative and require the correct data offset to be
   * added to locate them in the file.
   *
   * @param in
   *          The BVT File to scan
   * @param p
   *          a one based genomic position in the chromosome the BRT file
   *          represents.
   * @param window
   *          the window
   * @return the data offset
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public static Block getDataOffset(RandomAccessFile in, int p, int window) throws IOException {
    boolean found = false;
    int positionBin = -1;
    int bin = -1;
    int width = -1;
    int startOffset = -1;
    int endOffset = -1;
    int gso = -1;
    int groupWidth = -1;
    int start = -1;

    // Skip to where the tree starts
    in.seek(HEADER_OFFSET_BYTES);

    int offset = HEADER_OFFSET_BYTES;

    int binSize = MAX_BIN_WIDTH;

    // The bin in the current level that this position would fall into
    positionBin = p / binSize;

    // Which bin level we are on.
    int level = 0;

    Block ret = null;

    while (!found) {
      bin = in.readInt();

      // Skip the start coordinate
      start = in.readInt();

      width = in.readInt();

      startOffset = in.readInt();
      endOffset = in.readInt();

      // Relative pointer to the next lowest level in the tree that
      // we can decompose this block into sub blocks.
      gso = in.readInt();

      // System.err.println("p " + p + " " + pbin + " " + binSize);

      // If we are starting to scan a new group, store the width
      // of the first block we encounter,
      if (groupWidth == -1) {
        groupWidth = width;
      }

      // keep track of the block we currently fit into, thus
      // if we do not quite fit into a given bin

      // We know that we are bound by this bin. Keep trying
      // smaller bins until we cannot go any further.

      // If the bin exceeds the current position bin then we can stop
      // since we are not going to find a bin that the coordinate fits
      // into, but we have the closest.

      // System.err.println("bins " + bin + " " + positionBin + " " + width + " so:" +
      // startOffset + " " + gso + " " + binSize + " " + window);

      if (bin > positionBin) {
        break;
      }

      // If the width of the block changes then we have have moved
      // into a new group and there is not a bin that this coordinate
      // will fit in, so stop searching.
      if (width != groupWidth) {
        break;
      }

      // If we have landed in a bin, keep trying sub bins to increase
      // down the resolution
      if (bin == positionBin) {
        // If we reach the desired window (resolution), we can stop
        // searching and use the current node as a representative
        // of the lower nodes
        if (binSize == window) {
          break;
        }

        // If gso == -1, we are at the lowest level of the tree (leaves)
        // and cannot search any further (pointer does not point to a
        // new group). We can stop searching.
        if (gso == -1) {
          break;
        }

        // We can try the next level with smaller bins.
        ++level;

        // Set the bin size to the new level
        binSize = BIN_WIDTHS[level];

        // Set the test bin to the new bin level
        positionBin = p / binSize;

        // We are moving to a new level with bins 10x as small
        groupWidth = -1;

        // Update the offset to point to the next lowest level
        // group in the tree.
        offset += gso;

        // Skip to where the next group starts.
        in.seek(offset);
      }
    }

    ret = new Block(bin, start, level, binSize, startOffset, endOffset);

    return ret;
  }
}
