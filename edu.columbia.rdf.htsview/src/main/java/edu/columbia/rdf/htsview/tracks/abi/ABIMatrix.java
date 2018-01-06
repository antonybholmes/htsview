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
package edu.columbia.rdf.htsview.tracks.abi;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.EmptyMatrix;

import edu.columbia.rdf.htsview.ext.abi.ABITrace;

// TODO: Auto-generated Javadoc
/**
 * The Class ABIMatrix.
 */
public class ABIMatrix extends DataFrame {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m trace. */
  private ABITrace mTrace;

  /** The m region. */
  private GenomicRegion mRegion;

  /** The m base. */
  private char mBase;

  /**
   * Instantiates a new ABI matrix.
   *
   * @param trace
   *          the trace
   * @param base
   *          the base
   * @param region
   *          the region
   */
  public ABIMatrix(ABITrace trace, char base, GenomicRegion region) {
    super(new EmptyMatrix(trace.getNumBases(), 2));

    setColumnNames("Points x", "Points y");

    mTrace = trace;
    mBase = base;
    mRegion = region;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.math.matrix.AnnotatableMatrix#getValue(int, int)
   */
  @Override
  public double getValue(int row, int column) {
    int s = mRegion.getStart() + row;

    if (column == 0) {
      return s;
    } else {
      return mTrace.getColor(mBase, s);
    }
  }
}
