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
package edu.columbia.rdf.htsview.tracks.ext.ucsc;

import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraphElement;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.math.matrix.DataFrame;
import org.jebtk.math.matrix.EmptyMatrix;

/**
 * The Class BedGraphRegionMatrix.
 */
public class BedGraphRegionMatrix extends DataFrame {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m regions. */
  private List<GenomicElement> mRegions;

  /**
   * Instantiates a new bed graph region matrix. Each region forms two points,
   * the start and end of a line, so the matrix must be double the size of the
   * number of regions.
   *
   * @param regions the regions
   */
  public BedGraphRegionMatrix(List<GenomicElement> regions) {
    super(new EmptyMatrix(regions.size() * 2, 2));

    setColumnNames("Points x", "Points y");

    mRegions = regions;
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.math.matrix.AnnotatableMatrix#getValue(int, int)
   */
  @Override
  public double getValue(int row, int column) {
    int r2 = row / 2;

    if (column == 0) {
      if (row % 2 == 0) {
        return mRegions.get(r2).getStart();
      } else {
        return mRegions.get(r2).getEnd();
      }
    } else {
      return ((BedGraphElement) mRegions.get(r2)).getValue();
    }
  }
}
