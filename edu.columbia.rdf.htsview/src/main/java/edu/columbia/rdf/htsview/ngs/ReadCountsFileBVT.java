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
import java.util.Map;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.Mathematics;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.collections.DefaultTreeMap;

// TODO: Auto-generated Javadoc
/**
 * Decodes values stored at positions in a tree.
 *
 * @author Antony Holmes Holmes
 */
public class ReadCountsFileBVT extends ReadCountsFileBinTree {

  /**
   * The constant READ_START_WIDTH_BYTES.
   */
  public static final int READ_START_WIDTH_BYTES = 4;

  /** The Constant READ_VALUE_WIDTH_BYTES. */
  public static final int READ_VALUE_WIDTH_BYTES = 4;

  /** The Constant READ_FLAG_WIDTH_BYTES. */
  public static final int READ_FLAG_WIDTH_BYTES = 1;

  /** The Constant READ_WIDTH_BYTES. */
  public static final int READ_WIDTH_BYTES = READ_START_WIDTH_BYTES + READ_VALUE_WIDTH_BYTES + READ_FLAG_WIDTH_BYTES;

  /**
   * The constant FILE_EXT.
   */
  public static final String FILE_EXT = "bvt";

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param metaFile
   *          the directory
   */
  public ReadCountsFileBVT(Path metaFile) {
    super(metaFile);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.ngs.CountAssembly#getCounts(org.jebtk.bioinformatics
   * .genome.GenomicRegion, int)
   */
  @Override
  public List<Integer> getCounts(GenomicRegion region, int window) throws IOException {
    return CollectionUtils.double2Int(getValues(region, window));
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getStarts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public List<Integer> getStarts(GenomicRegion region, int window) throws IOException {
    return getStarts(region.getChr(), region.getStart(), region.getEnd(), window);
  }

  /**
   * Gets the starts.
   *
   * @param chr
   *          the chr
   * @param start
   *          the start
   * @param end
   *          the end
   * @param window
   *          the window
   * @return the starts
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public List<Integer> getStarts(Chromosome chr, int start, int end, int window) throws IOException {
    ArrayList<Integer> starts = new ArrayList<Integer>();

    Path file = getFile(chr, window, FILE_EXT);

    int dataOffset = mOffsetMap.get(chr);

    RandomAccessFile in = new RandomAccessFile(file.toFile(), "r");

    try {
      // first get the buffer offset of the start

      Block so = getDataOffset(in, start, window);
      Block eo = getDataOffset(in, end, window);

      int l = eo.end - so.start + 1;

      in.seek(dataOffset + RAW_OFFSET + so.startOffset * READ_WIDTH_BYTES);

      int s;

      for (int i = 0; i < l; ++i) {
        // System.err.println("ha " + i + " " + Arrays.toString(e));

        // Skip start
        s = in.readInt();

        // skip value
        in.readFloat();

        // Skip flags
        in.readByte();

        if (s >= start && s <= end) {
          starts.add(s);
        }
      }
    } finally {
      in.close();
    }

    return starts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.ngs.CountAssembly#getValues(org.jebtk.bioinformatics
   * .genome.GenomicRegion, int)
   */
  @Override
  public List<Double> getValues(GenomicRegion region, int window) throws IOException {
    return getValues(region.getChr(), region.getStart(), region.getEnd(), window);
  }

  /**
   * Get the counts from the file,.
   *
   * @param chr
   *          the chr
   * @param start
   *          The 1 based genomic start coordinate.
   * @param end
   *          The 1 based genomic end coordinate.
   * @param window
   *          The size of the window being viewed.
   * @return the starts
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public List<Double> getValues(Chromosome chr, int start, int end, int window) throws IOException {

    Path file = getFile(chr, window, FILE_EXT);

    int dataOffset = mOffsetMap.get(chr);

    RandomAccessFile in = new RandomAccessFile(file.toFile(), "r");

    List<Double> values = new ArrayList<Double>();

    try {
      // first get the buffer offset of the start

      Block so = getDataOffset(in, start, window);
      Block eo = getDataOffset(in, end, window);

      int l = eo.endOffset - so.startOffset + 1;

      // System.err.println("starts " + start + " " + so.level + " " + eo.level + " "
      // + window);

      if (window >= MIN_BIN_WIDTH) {
        // Skip to structured block
        in.seek(dataOffset + multiResOffset(so));

        int b = so.bin;

        while (b <= eo.bin) {
          double v = in.readFloat();

          // System.err.println("s " + b + " " + so.bin + " " + s + " " + v);

          values.add(v);

          ++b;
        }

        values = getValues(values, so, start, end, window);
      } else {
        // Higher resolution so slower to access.

        // To read the starts, first skip to dataOffset (the byte position
        // after the R Tree where the counts are written sequentially),
        // then skip to the array index (i * 4 bytes (width of int)).
        in.seek(dataOffset + RAW_OFFSET + so.startOffset * READ_WIDTH_BYTES);

        int s;
        double v;

        List<Integer> starts = new ArrayList<Integer>(l);

        for (int i = 0; i < l; ++i) {
          // Skip start
          s = in.readInt();

          v = in.readFloat();

          // Skip flags
          in.readByte();

          if (s >= start && s <= end) {
            starts.add(s);
            values.add(v);
          }
        }

        // Group by window size

        values = getValues(starts, values, start, end, window);
      }
    } finally {
      in.close();
    }

    return values;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.ngs.ReadCountsFileBinTree#multiResOffset(edu.
   * columbia.rdf.htsview.ngs.Block)
   */
  @Override
  public int multiResOffset(Block b) {
    return (CUM_BIN_COUNTS[b.level] + b.bin) * MULTI_RES_WIDTH_BYTES;
  }

  /**
   * Gets the counts.
   *
   * @param starts
   *          the starts
   * @param values
   *          the values
   * @param start
   *          the start
   * @param end
   *          the end
   * @param window
   *          the bin size
   * @return the counts
   */
  private static List<Double> getValues(final List<Integer> starts, final List<Double> values, int start, int end,
      int window) {
    int startBin = start / window;
    int endBin = end / window;
    int l = endBin - startBin + 1;

    // System.err.println(start + " " + end + " " + s + " " + e + " " + l);

    Map<Integer, Double> map = DefaultTreeMap.create(0.0);

    for (int i = 0; i < starts.size(); ++i) {
      int rs = starts.get(i);
      double value = values.get(i);

      int sbin = rs / window - startBin;

      map.put(sbin, map.get(sbin) + value);

      /*
       * int ebin = (rs + readLength) / binSize - startBin;
       * 
       * map.put(bin, map.get(bin) + value);
       * 
       * for (int bin = sbin; bin <= ebin; ++bin) { map.put(bin, map.get(bin) +
       * value); }
       */
    }

    List<Double> ret = Mathematics.zeros(l);

    for (int bin : map.keySet()) {
      ret.set(bin, map.get(bin));
    }

    return ret;
  }

  /**
   * Gets the values.
   *
   * @param values
   *          the values
   * @param startBlock
   *          the start block
   * @param start
   *          the start
   * @param end
   *          the end
   * @param window
   *          the window
   * @return the values
   */
  private static List<Double> getValues(final List<Double> values, Block startBlock, int start, int end, int window) {
    int startBin = start / window;
    int endBin = end / window;
    int l = endBin - startBin + 1;

    // System.err.println(start + " " + end + " " + s + " " + e + " " + l);

    Map<Integer, Double> map = DefaultTreeMap.create(0.0);

    int rs = startBlock.bin; // - startBin;

    for (double value : values) {
      map.put(rs, map.get(rs) + value);

      ++rs;
    }

    List<Double> ret = Mathematics.zeros(l);

    for (int bin : map.keySet()) {
      if (bin >= startBin && bin <= endBin) {
        ret.set(bin - startBin, map.get(bin));
      }
    }

    return ret;
  }
}
