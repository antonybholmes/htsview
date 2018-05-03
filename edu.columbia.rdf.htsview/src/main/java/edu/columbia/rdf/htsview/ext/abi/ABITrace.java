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
package edu.columbia.rdf.htsview.ext.abi;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import org.jebtk.core.Mathematics;
import org.jebtk.core.NameProperty;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.sys.SysUtils;

/**
 * The Class ABITrace encapsulate an ABI trace (ab1) file. This class is
 * currently designed to extract the peak locations and color values for each
 * base at those locations.
 */
public class ABITrace implements NameProperty {

  /** The standard 4 bases a, c, g, t. */
  public static final char[] BASES = { 'A', 'C', 'G', 'T' };

  /** The m X. */
  private short[] mX;

  /** The m A. */
  private short[] mA;

  /** The m C. */
  private short[] mC;

  /** The m G. */
  private short[] mG;

  /** The m T. */
  private short[] mT;

  /** The m name. */
  private String mName;

  /**
   * Instantiates a new ABI trace.
   *
   * @param name the name
   * @param x The x coordinates
   * @param a The a color values for each x coordinate.
   * @param c The c color values for each x coordinate.
   * @param g The g color values for each x coordinate.
   * @param t The t color values for each x coordinate.
   */
  private ABITrace(String name, short[] x, short[] a, short[] c, short[] g,
      short[] t) {
    mName = name;
    mX = x;
    mA = a;
    mC = c;
    mG = g;
    mT = t;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.NameProperty#getName()
   */
  @Override
  public String getName() {
    return mName;
  }

  /**
   * Gets the num bases.
   *
   * @return the num bases
   */
  public int getNumBases() {
    return mX.length;
  }

  /**
   * Gets the num colors.
   *
   * @return the num colors
   */
  public int getNumColors() {
    return mA.length;
  }

  /**
   * Gets the color value for a given base at a given location.
   *
   * @param base the base
   * @param index the index
   * @return the base
   */
  public short getColor(char base, int index) {
    switch (base) {
    case 'c':
    case 'C':
      return getColorC(index);
    case 'g':
    case 'G':
      return getColorG(index);
    case 't':
    case 'T':
      return getColorT(index);
    default:
      return getColorA(index);
    }
  }

  /**
   * Gets the x.
   *
   * @param index the index
   * @return the x
   */
  public short getX(int index) {
    if (!Mathematics.inBound(index, 0, mX.length)) {
      return -1;
    }

    return mX[index];
  }

  /**
   * Gets the a.
   *
   * @param index the index
   * @return the a
   */
  public short getColorA(int index) {
    if (!Mathematics.inBound(index, 0, mX.length)) {
      return -1;
    }

    return mA[mX[index]];
  }

  /**
   * Gets the c.
   *
   * @param index the index
   * @return the c
   */
  public short getColorC(int index) {
    if (!Mathematics.inBound(index, 0, mX.length)) {
      return -1;
    }

    return mC[mX[index]];
  }

  /**
   * Gets the g.
   *
   * @param index the index
   * @return the g
   */
  public short getColorG(int index) {
    if (!Mathematics.inBound(index, 0, mX.length)) {
      return -1;
    }

    return mG[mX[index]];
  }

  /**
   * Gets the t.
   *
   * @param index the index
   * @return the t
   */
  public short getColorT(int index) {
    if (!Mathematics.inBound(index, 0, mX.length)) {
      return -1;
    }

    return mT[mX[index]];
  }

  /**
   * Parses the.
   *
   * @param file the file
   * @return the ABI trace
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public static ABITrace parse(Path file) throws IOException {
    if (!FileUtils.exists(file)) {
      return null;
    }

    SysUtils.err().println("Parsing ABI file:", file);

    RandomAccessFile in = FileUtils.newRandomAccess(file);

    // Buffer for reading entity names
    byte[] buf4 = new byte[4];

    // String header;
    // int version;
    int tag;
    // int type;
    // int elementSize;
    int numDirs;
    int numElements;
    // int dataSize;
    int dirOffset;
    int offset;
    long currentOffset;
    String dirName;
    // String baseOrder;

    short[] xp = null;

    // The color values are always longer than the positions
    short[] araw = null;
    short[] craw = null;
    short[] graw = null;
    short[] traw = null;

    try {
      // Parse header
      in.read(buf4);

      // Version
      in.readShort();

      // dir name
      in.read(buf4);
      dirName = new String(buf4);

      tag = in.readInt();

      // Type
      in.readShort();

      // Element Size
      in.readShort();

      numDirs = in.readInt();
      // Data size
      in.readInt();

      dirOffset = in.readInt();

      // SysUtils.err().println("header:", header, version);
      // SysUtils.err().println("dir:", dirName, tag, type, elementSize,
      // numDirs,
      // dataSize, dirOffset);

      // Process directories
      in.seek(dirOffset);

      // Channels DATA 9-12 hold the color data. Apparently ABI choose
      // to color order then GATC rather than ACGT so DATA 9 is G,
      // DATA 10 is A, DATA 11 is T, and DATA 12 is C

      // Iterate over the directories
      for (int i = 0; i < numDirs; ++i) {
        in.read(buf4);
        dirName = new String(buf4);
        tag = in.readInt();
        in.readShort(); // type
        in.readShort(); // element size
        numElements = in.readInt();
        in.readInt(); // data size
        offset = in.readInt();
        // Unused data handle
        in.readInt();

        if (dirName.equals("PLOC") && tag == 2) {
          // PLOC 2 seems to be where the base caller sits the peaks
          // so we can use these indices in the DATA arrays to find
          // the x coordinate where the peak sits. This should
          // correspond roughly to the peak high point
          currentOffset = in.getFilePointer();

          xp = new short[numElements];

          in.seek(offset);

          for (int j = 0; j < numElements; ++j) {
            xp[j] = in.readShort();
          }

          in.seek(currentOffset);
        } else if (dirName.equals("DATA") && tag == 9) {
          currentOffset = in.getFilePointer();

          // For a smoother curve, there are more color data points
          // than peak positions (roughly 10 color points per base)
          graw = new short[numElements];

          in.seek(offset);

          for (int j = 0; j < numElements; ++j) {
            graw[j] = in.readShort();
          }

          in.seek(currentOffset);
        } else if (dirName.equals("DATA") && tag == 10) {
          currentOffset = in.getFilePointer();

          araw = new short[numElements];

          in.seek(offset);

          for (int j = 0; j < numElements; ++j) {
            araw[j] = in.readShort();
          }

          in.seek(currentOffset);
        } else if (dirName.equals("DATA") && tag == 11) {
          currentOffset = in.getFilePointer();

          traw = new short[numElements];

          in.seek(offset);

          for (int j = 0; j < numElements; ++j) {
            traw[j] = in.readShort();
          }

          in.seek(currentOffset);
        } else if (dirName.equals("DATA") && tag == 12) {
          currentOffset = in.getFilePointer();

          craw = new short[numElements];

          in.seek(offset);

          for (int j = 0; j < numElements; ++j) {
            craw[j] = in.readShort();
          }

          // Move back to the end of the last directory we were
          // in so we can continue parsing
          in.seek(currentOffset);
        } else {
          // Do nothing
        }

        // SysUtils.err().println("dir2:", i, dirName, tag, type, elementSize,
        // numElements, dataSize, offset);
      }

    } finally {
      in.close();
    }

    return new ABITrace(PathUtils.getNameNoExt(file), xp, araw, craw, graw,
        traw);
  }
}
