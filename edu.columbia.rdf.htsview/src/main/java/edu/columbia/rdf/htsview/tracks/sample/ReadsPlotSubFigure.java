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
package edu.columbia.rdf.htsview.tracks.sample;

import java.awt.Color;
import java.io.IOException;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;
import org.jebtk.core.collections.ArrayListMultiMap;
import org.jebtk.core.collections.ListMultiMap;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

/**
 * Draw peaks.
 * 
 * @author Antony Holmes Holmes
 *
 */
public class ReadsPlotSubFigure extends TrackSubFigure {

  /**
   * The constant serialVersionUID.
   */
  private static final long serialVersionUID = 1L;

  /**
   * The member layer.
   */
  private ReadsPlotLayer mLayer;

  /** The m start map. */
  private ListMultiMap<Integer, Integer> mStartMap = ArrayListMultiMap.create();

  /** The m strand map. */
  private ListMultiMap<Integer, Strand> mStrandMap = ArrayListMultiMap.create();

  /** The m read length. */
  private int mReadLength;

  /**
   * Instantiates a new reads plot sub figure.
   *
   * @param name the name
   * @param readLength the read length
   * @param titlePosition the title position
   */
  public ReadsPlotSubFigure(String name, int readLength,
      TitleProperties titlePosition) {
    mReadLength = readLength;

    mLayer = new ReadsPlotLayer(readLength);

    // set the graph limits
    currentAxes().getX1Axis().getTitle().setText(null);
    currentAxes().getY1Axis().setLimits(0, 1);
    currentAxes().addChild(mLayer);

    Track.setTitle(name, titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param name the name
   * @param readLength the read length
   * @param titlePosition the title position
   * @return the reads plot sub figure
   */
  public static ReadsPlotSubFigure create(String name,
      int readLength,
      TitleProperties titlePosition) {

    // Now lets create a plot

    ReadsPlotSubFigure canvas = new ReadsPlotSubFigure(name, readLength,
        titlePosition);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.apps.edb.reads.tracks.TrackFigure#update(edu.columbia.rdf.
   * lib.bioinformatics.genome.GenomicRegion, int, java.awt.Color,
   * java.awt.Color)
   */
  @Override
  public void update(GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      Color lineColor,
      Color fillColor,
      PlotStyle style) throws IOException {

    // getCurrentAxes().setInternalPlotWidth(width);

    // System.err.println("regions " + bed.getName() + " " +
    // displayRegion.getChr());
    // getCurrentAxes().getXAxis().setLimits(displayRegion.getStart(),
    // displayRegion.getEnd());

    super.update(displayRegion,
        resolution,
        yMax,
        width,
        height,
        margin,
        lineColor,
        fillColor,
        style);

    Axes.disableAllFeatures(currentAxes());

    // Need to make the title visible
    currentAxes().getTitle().getFontStyle().setVisible(true);
  }

  /**
   * Sets the starts.
   *
   * @param starts the new starts
   * @param strands the strands
   * @param strandVisible the strand visible
   * @param lineColor the line color
   * @param fillColor the fill color
   * @param negStrandVisible the neg strand visible
   * @param negStrandLineColor the neg strand line color
   * @param negStrandFillColor the neg strand fill color
   * @param readHeight the read height
   * @param gap the gap
   */
  public void setStarts(int[] starts,
      Strand[] strands,
      boolean strandVisible,
      Color lineColor,
      Color fillColor,
      boolean negStrandVisible,
      Color negStrandLineColor,
      Color negStrandFillColor,
      int readHeight,
      int gap) {

    mStartMap.clear();
    mStrandMap.clear();

    int w = -1;

    for (int i = 0; i < starts.length; ++i) {
      int start = starts[i];
      Strand strand = strands[i];

      if ((!strandVisible && strand == Strand.SENSE)
          || (!negStrandVisible && strand == Strand.ANTISENSE)) {
        continue;
      }

      int x1 = currentAxes().toPlotX1(start);
      int x2 = currentAxes().toPlotX1(start + mReadLength);

      if (w == -1) {
        w = Math.max(1, x2 - x1);
      }

      int row = -1;

      for (int r : mStartMap.keySet()) {
        boolean fit = true;

        for (int s : mStartMap.get(r)) {
          int e = s + w;

          if ((x1 >= s && x1 < e) || (x2 >= s && x2 < e)) {
            fit = false;
            break;
          }
        }

        if (fit) {
          row = r;
          break;
        }
      }

      if (row == -1) {
        row = mStartMap.size();
      }

      // debug
      // row = 0;

      mStartMap.get(row).add(x1);
      mStrandMap.get(row).add(strand);
    }

    mLayer.update(mStartMap,
        mStrandMap,
        w,
        strandVisible,
        lineColor,
        fillColor,
        negStrandVisible,
        negStrandLineColor,
        negStrandFillColor,
        readHeight,
        gap);

    int height = (readHeight + gap) * (1 + mStrandMap.size());

    currentAxes().setInternalHeight(height);
  }
}
