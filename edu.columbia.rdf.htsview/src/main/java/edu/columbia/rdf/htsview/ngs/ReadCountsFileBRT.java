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
import org.jebtk.bioinformatics.genomic.Strand;

// TODO: Auto-generated Javadoc
/**
 * Decodes counts using a multi resolution file.
 *
 * @author Antony Holmes Holmes
 */
public class ReadCountsFileBRT extends ReadCountsFileBinTree {

  /**
   * The constant READ_START_WIDTH_BYTES.
   */
  public static final int READ_START_WIDTH_BYTES = 4;

  /** The Constant READ_FLAGS_WIDTH_BYTES. */
  public static final int READ_FLAGS_WIDTH_BYTES = 1;

  /** The Constant READ_WIDTH_BYTES. */
  public static final int READ_WIDTH_BYTES = READ_START_WIDTH_BYTES + READ_FLAGS_WIDTH_BYTES;

  /**
   * The constant FILE_EXT.
   */
  public static final String FILE_EXT = "brt";

  /** The Constant FLAG_STRAND_MASK. */
  private static final byte FLAG_STRAND_MASK = 1;

  /**
   * Directory containing genome files which must be of the form chr.n.txt. Each
   * file must contain exactly one line consisting of the entire chromosome.
   *
   * @param metaFile
   *          the directory
   */
  public ReadCountsFileBRT(Path metaFile) {
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
    Chromosome chr = region.getChr();

    Path file = getFile(chr, window, FILE_EXT);

    // System.err.println("read count file:" + file + " " + region + " " +
    // mReadLengthMap.get(chr) + " " + mOffsetMap.get(chr));

    List<Integer> counts = getCounts(file, region.getStart(), region.getEnd(), window, mOffsetMap.get(chr));

    return counts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getStarts(edu.
   * columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
   */
  @Override
  public List<Integer> getStarts(GenomicRegion region, int window) throws IOException {
    Chromosome chr = region.getChr();

    Path file = getFile(chr, window, FILE_EXT);

    List<Integer> starts = getStarts(file, region.getStart(), region.getEnd(), window, mOffsetMap.get(chr));

    return starts;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getStrands(org.jebtk.
   * bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public List<Strand> getStrands(GenomicRegion region, int window) throws IOException {
    Chromosome chr = region.getChr();

    Path file = getFile(chr, window, FILE_EXT);

    List<Strand> strands = getStrands(file, region.getStart(), region.getEnd(), mOffsetMap.get(chr));

    return strands;
  }

  /**
   * Gets the counts.
   *
   * @param file
   *          the file
   * @param start
   *          the start
   * @param end
   *          the end
   * @param window
   *          the window
   * @param dataOffset
   *          the data offset
   * @return the counts
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  public List<Integer> getCounts(final Path file, int start, int end, int window, int dataOffset) throws IOException {

    List<Integer> starts = getStarts(file, start, end, window, dataOffset);

    List<Integer> counts = binCounts(starts, start, end, window);

    return counts;
  }

  /**
   * Get the counts from the file,.
   *
   * @param file
   *          The r tree binary file.
   * @param start
   *          The 1 based genomic start coordinate.
   * @param end
   *          The 1 based genomic end coordinate.
   * @param window
   *          The size of the window being viewed.
   * @param dataOffset
   *          The byte offset in the file where the reads begin.
   * @return the starts
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private static List<Integer> getStarts(final Path file, int start, int end, int window, int dataOffset)
      throws IOException {

    RandomAccessFile in = new RandomAccessFile(file.toFile(), "r");

    List<Integer> starts = new ArrayList<Integer>();

    try {
      // first get the buffer offset of the start

      Block so = getDataOffset(in, start, window);
      Block eo = getDataOffset(in, end, window);

      int l = eo.endOffset - so.startOffset + 1;

      // System.err.println("starts " + start + " " + end + " " + Arrays.toString(so)
      // + " " + Arrays.toString(eo) + " " + l);

      // To read the starts, first skip to dataOffset (the byte position
      // after the R Tree where the counts are written sequentially),
      // then skip to the array index (i * 4 bytes (width of int)).
      in.seek(dataOffset + so.startOffset * READ_WIDTH_BYTES);

      for (int i = 0; i < l; ++i) {
        // System.err.println("ha " + i + " " + Arrays.toString(e));

        int s = in.readInt();

        // skip flags
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

  /**
   * Gets the strands.
   *
   * @param file
   *          the file
   * @param start
   *          the start
   * @param end
   *          the end
   * @param dataOffset
   *          the data offset
   * @return the strands
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private static List<Strand> getStrands(final Path file, int start, int end, int dataOffset) throws IOException {

    List<Byte> params = getFlags(file, start, end, dataOffset);

    // System.err.println("flags:" + params);

    List<Strand> strands = new ArrayList<Strand>(params.size());

    for (byte b : params) {
      if ((b & FLAG_STRAND_MASK) == FLAG_STRAND_MASK) {
        strands.add(Strand.ANTISENSE);
      } else {
        strands.add(Strand.SENSE);
      }
    }

    return strands;
  }

  /**
   * Extract the 1 byte param field associated with a read.
   *
   * @param file
   *          the file
   * @param start
   *          the start
   * @param end
   *          the end
   * @param dataOffset
   *          the data offset
   * @return the flags
   * @throws IOException
   *           Signals that an I/O exception has occurred.
   */
  private static List<Byte> getFlags(final Path file, int start, int end, int dataOffset) throws IOException {

    RandomAccessFile in = new RandomAccessFile(file.toFile(), "r");

    List<Byte> flags = new ArrayList<Byte>();

    try {
      // first get the buffer offset of the start

      Block so = getDataOffset(in, start);
      Block eo = getDataOffset(in, end);

      int l = eo.endOffset - so.startOffset + 1;

      // System.err.println("ends " + Arrays.toString(so) + " " + Arrays.toString(eo)
      // + " " + l);

      // To read the starts, first skip to dataOffset (the byte position
      // after the R Tree where the counts are written sequentially),
      // then skip to the array index (i * 4 bytes (width of int)).
      in.seek(dataOffset + so.startOffset * READ_WIDTH_BYTES);

      for (int i = 0; i < l; ++i) {
        int s = in.readInt();
        byte flag = in.readByte();

        // System.err.println("ha " + i + " " + s + " " + flag);

        if (s >= start && s <= end) {
          flags.add(flag);
        }
      }
    } finally {
      in.close();
    }

    return flags;
  }

}
