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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.DefaultHashMap;
import org.jebtk.core.collections.HashMapCreator;
import org.jebtk.core.collections.IterMap;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.sys.SysUtils;

// TODO: Auto-generated Javadoc
/**
 * Decodes counts using a multi resolution file.
 *
 * @author Antony Holmes Holmes
 */
public class ReadCountsFileBCT extends ReadCountsFileBinTree {

  /**
   * The header consists of an int count of the number of reads (4) a short for
   * the length of the reads (2), a bit encoding (1) and an 8 byte string of the
   * genome name (e.g. hg19)
   */
  public static final int HEADER_BYTES = 4 + 2 + 1 + 8;

  /** The Constant LOWER_2_BIT_MASK. */
  public static final int LOWER_2_BIT_MASK = 3;

  /** The Constant LOWER_4_BIT_MASK. */
  public static final int LOWER_4_BIT_MASK = 15;

  /** The Constant LOWER_8_BIT_MASK. */
  public static final int LOWER_8_BIT_MASK = 255;

  /** The Constant BIN_COUNTS. */
  public static final int[] BIN_COUNTS = { 1, 3, 30, 300, 3000, 30000, 300000, 3000000, 30000000, 300000000 };

  /**
   * The constant FILE_EXT.
   */
  public static final String FILE_EXT = "bct";

  /** The Constant MIN_WINDOW. */
  private static final int MIN_WINDOW = 10;

  /** The m bit map. */
  protected Map<Path, Integer> mBitMap = new HashMap<Path, Integer>();

  /** The m file map. */
  protected Map<Chromosome, IterMap<Integer, Path>> mFileMap = DefaultHashMap
      .create(new HashMapCreator<Integer, Path>());

  /** The m genome. */
  private Object mGenome;

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param metaFile
   *          the directory
   */
  public ReadCountsFileBCT(Path metaFile) {
    super(metaFile);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getCounts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public List<Integer> getCounts(GenomicRegion region, int window) throws IOException {
    return getCounts(region.getChr(), region.getStart(), region.getEnd(), window);
  }

  /**
   * Gets the counts.
   *
   * @param chr
   *          the chr
   * @param start
   *          the start
   * @param end
   *          the end
   * @param window
   *          the window
   * @return the counts
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public List<Integer> getCounts(Chromosome chr, int start, int end, int window) throws IOException {
    if (window < MIN_WINDOW) {
      return Collections.emptyList();
    }

    Path file = getFile(chr, window, FILE_EXT);

    // How many bits encode each value
    int bits = mBitMap.get(file);

    RandomAccessFile in = FileUtils.newRandomAccess(file);

    List<Integer> counts = new ArrayList<Integer>(1000);

    try {
      // first get the buffer offset of the start

      int so = start / window;
      int eo = end / window;

      // Skip data
      // in.seek(HEADER_BYTES);

      int b = so;
      int v;

      switch (bits) {
      case 2:
        in.seek(HEADER_BYTES + so / 4);

        while (b <= eo) {
          v = in.read();

          counts.add(v >> 6 & LOWER_2_BIT_MASK);
          counts.add(v >> 4 & LOWER_2_BIT_MASK);
          counts.add(v >> 2 & LOWER_2_BIT_MASK);
          counts.add(v & LOWER_2_BIT_MASK);

          b += 4;
        }

        break;
      case 4:
        in.seek(HEADER_BYTES + so / 2);

        while (b <= eo) {
          v = in.read();

          counts.add(v >> 4 & LOWER_4_BIT_MASK);
          counts.add(v & LOWER_4_BIT_MASK);

          b += 2;
        }

        break;
      case 8:
        in.seek(HEADER_BYTES + so);

        while (b <= eo) {
          counts.add(in.read());

          ++b;
        }

        break;
      case 16:
        in.seek(HEADER_BYTES + so * 2);

        while (b <= eo) {
          v = in.readShort(); // (in.read() << LOWER_8_BIT_MASK) | in.read();
          counts.add(v);

          ++b;
        }

        break;
      default:
        // 32 bit
        in.seek(HEADER_BYTES + so * 4);

        while (b <= eo) {
          v = in.readInt();

          counts.add(v);

          ++b;
        }

        break;
      }

      // counts = getCounts(counts,
      // so,
      // start,
      // end,
      // window);

    } finally {
      in.close();
    }

    return counts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.ngs.ReadCountsFileBin#getFile(org.jebtk.
   * bioinformatics.genome.Chromosome, int, java.lang.String)
   */
  @Override
  protected Path getFile(Chromosome chr, int window, String ext) throws IOException {
    if (!mFileMap.containsKey(chr) || !mFileMap.get(chr).containsKey(window)) {
      Path file = createFile(mDirectory, chr, window);

      mFileMap.get(chr).put(window, file);

      RandomAccessFile in = FileUtils.newRandomAccess(file);

      try {
        // The number of reads
        int count = in.readInt();
        // in.seek(4);

        // Read length
        int length = in.readShort();

        // The second 4 bytes tells us where in the file to go to
        // find the read data
        mBitMap.put(file, in.read());

        if (mGenome == null) {
          mGenome = readGenome(in);
        }

        SysUtils.err().println("bct:", file, mBitMap.get(file), count, length, mGenome);

      } finally {
        in.close();
      }
    }

    return mFileMap.get(chr).get(window);
  }

  /**
   * Return crap.
   *
   * @param dir
   *          the dir
   * @param chr
   *          the chr
   * @param window
   *          the window
   * @return the path
   */
  public static Path createFile(Path dir, Chromosome chr, int window) {
    // System.err.println("bct file:" + dir.resolve(chr.toString() + ".w" + window +
    // "." + FILE_EXT));

    return dir.resolve(chr.toString() + ".w" + window + "." + FILE_EXT);
  }
}
