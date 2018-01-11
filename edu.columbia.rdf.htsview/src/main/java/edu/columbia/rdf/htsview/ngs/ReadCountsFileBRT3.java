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
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.sys.SysUtils;

// TODO: Auto-generated Javadoc
/**
 * Decodes counts using a multi resolution file.
 *
 * @author Antony Holmes Holmes
 */
public class ReadCountsFileBRT3 extends ReadCountsFileBinTree {

  /**
   * The constant READ_START_WIDTH_BYTES.
   */

  public static final int COUNT_BLOCK_SIZE_BYTES = 4 + 4;

  /**
   * The constant FILE_EXT.
   */
  public static final String FILE_EXT = "brt3";

  /** The Constant GROUP_SIZE_BYTES. */
  // start width, count array index, level, block count
  public static final int GROUP_SIZE_BYTES = 4 + 4 + 4 + 4 + 1;

  /** The Constant BLOCK_SIZE_BYTES. */
  public static final int BLOCK_SIZE_BYTES = 4 + 4;

  /** The Constant HEADER_SIZE_BYTES. */
  // count, readlength, genome
  public static final int HEADER_SIZE_BYTES = 4 + 2 + 1 + 8;

  /** The m reads. */
  private int mReads = -1;

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param metaFile the directory
   */
  public ReadCountsFileBRT3(Path metaFile) {
    super(metaFile);

    try {
      mReads = Json.fromJson(mMetaFile).getAsInt("Mapped Reads");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getCounts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public List<Integer> getCounts(GenomicRegion region, int window)
      throws IOException {
    return getCounts(region.getChr(),
        region.getStart(),
        region.getEnd(),
        window);
  }

  /**
   * Gets the counts.
   *
   * @param chr the chr
   * @param start the start
   * @param end the end
   * @param window the window
   * @return the counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public List<Integer> getCounts(Chromosome chr, int start, int end, int window)
      throws IOException {
    Path file = getFile(chr, window, FILE_EXT);

    RandomAccessFile in = FileUtils.newRandomAccess(file);

    List<Integer> counts = new ArrayList<Integer>(1000);

    // int level = getLevel(window);

    try {
      Offsets so = getCountOffset(in, start, window);
      Offsets eo = getCountOffset(in, end, window);

      // int l = eo - so + 1;

      // Skip to structured block
      in.seek(so.startOffset);

      int b = so.startOffset;

      int s;
      int v;

      SysUtils.err().println("eh", so.startOffset, eo.endOffset);

      // s = in.readInt();
      // v = in.readInt();
      // SysUtils.err().println("count", s, v);

      while (b <= eo.endOffset) {
        s = in.readInt();
        v = in.readInt();

        SysUtils.err().println("count", s, v, start, end);

        if (s >= start && s < end) {
          counts.add(v);
        }

        b += COUNT_BLOCK_SIZE_BYTES;
      }
    } finally {
      in.close();
    }

    return counts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getReadCount()
   */
  @Override
  public int getReadCount() {
    return mReads;
  }

  /**
   * Gets the count offset.
   *
   * @param in the in
   * @param p the p
   * @param window the window
   * @return the count offset
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static Offsets getCountOffset(RandomAccessFile in, int p, int window)
      throws IOException {
    boolean found = false;
    int bin = -1;
    int width = -1;
    int start = -1;

    // Skip to where the tree starts
    in.seek(HEADER_SIZE_BYTES);

    // int binSize = MAX_BIN_WIDTH;

    // The bin in the current level that this position would fall into

    // Which bin level we are on.
    // int l = -1;
    int coMin = -1;
    int coMax = -1;

    int binCount = -1;
    int go = -1;
    // int ret = -1;

    while (!found) {
      start = in.readInt();
      width = in.readInt();
      coMin = in.readInt();
      coMax = in.readInt();
      // l = in.read();

      // If we are at the desired level then return the memory address
      // of the count index
      if (width == window) {
        break;
      }

      binCount = in.read();

      SysUtils.err().println(p, start, width, coMin, coMax, window, binCount);
      // System.exit(0);

      switch (binCount) {
      case 0:
        break;
      case 10:
        // Jump directly to bin

        bin = p / width;

        // Jump from the offset to the position bin * BLOCK_SIZE
        // the skip ahead 1 int (4 bytes) to read the offset since
        // we don't care about the start
        in.skipBytes((bin * BLOCK_SIZE_BYTES) + 4);

        // Read the go offset
        in.seek(in.readInt()); // seek(offset);

        break;
      default:
        // Manual search

        found = true;

        for (int i = 0; i < binCount; ++i) {
          start = in.readInt();

          go = in.readInt();

          SysUtils.err().println("test", start, width, go, width, p);

          if (p >= start && p < start + width) {

            // offset = go;
            in.seek(go); // seek(offset);

            found = false;

            break;
          }
        }

        break;
      }
    }

    SysUtils.err().println("co", coMin, coMax);

    return new Offsets(coMin, coMax);
  }
}
