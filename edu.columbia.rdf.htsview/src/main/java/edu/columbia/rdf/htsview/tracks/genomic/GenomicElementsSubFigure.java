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
package edu.columbia.rdf.htsview.tracks.genomic;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;

import edu.columbia.rdf.htsview.tracks.FixedYSubFigure;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackDisplayMode;
import edu.columbia.rdf.htsview.tracks.ext.ucsc.BedPlotTrack;

/**
 * The Class BedPlotSubFigure.
 */
public class GenomicElementsSubFigure extends FixedYSubFigure {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m bed layer. */
  private GenomicElementPlotLayer mLayer;

  /** The m bed. */
  private UCSCTrack mBed;

  private Track mTrack;

  // private GenomicRegionsModel mGenomicModel;

  // private GenomicRegion mDisplayRegion;

  /**
   * Instantiates a new bed plot sub figure.
   *
   * @param bed the bed
   * @param color the color
   * @param titlePosition the title position
   */
  public GenomicElementsSubFigure(Track track,
      UCSCTrack bed,
      TitleProperties titlePosition) {
    mTrack = track;
    mBed = bed;
    mLayer = new GenomicElementPlotLayer(track.getFillColor());

    // set the graph limits
    currentAxes().getX1Axis().getTitle().setText(null);
    currentAxes().getY1Axis().setLimits(0, 1);
    currentAxes().addChild(mLayer);

    Track.setTitle(track.getName(), titlePosition, currentAxes());
  }

  /**
   * Creates the.
   *
   * @param bed the bed
   * @param color the color
   * @param titlePosition the title position
   * @return the bed plot sub figure
   */
  public static GenomicElementsSubFigure create(Track track,
      UCSCTrack bed,
      TitleProperties titlePosition) {

    // Now lets create a plot

    GenomicElementsSubFigure canvas = new GenomicElementsSubFigure(track, bed, titlePosition);

    return canvas;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.FixedSubFigure#update(org.jebtk.
   * bioinformatics.genome.GenomicRegion, int, double, int, int, int,
   * java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
   */
  @Override
  public void update(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      double yMax,
      int width,
      int height,
      int margin,
      Color lineColor,
      Color fillColor,
      PlotStyle style) throws IOException {

    //List<GenomicElement> regions = GenomicRegions
    //   .getFixedGapSearch(mBed.getElements().toList())
    //   .getFeatureSet(displayRegion);
    
    List<GenomicElement> elements = mBed.find(displayRegion); //, GenomicType.REGION, 1);
    
    for (GenomicElement e : elements) {
      e.setColor(fillColor);
    }

    int n = 1;

    if (mTrack.getDisplayMode() == TrackDisplayMode.FULL) {
      if (elements != null) {
        n += elements.size();
      }
    }

    height = BedPlotTrack.BLOCK_HEIGHT * n;

    super.update(genome,
        displayRegion,
        resolution,
        yMax,
        width,
        height,
        margin,
        lineColor,
        fillColor,
        style);

    mLayer.update(elements, mTrack.getFillColor(), mTrack.getDisplayMode());

    // GenesPlotCanvasLayer.GAP;

    Axes.disableAllFeatures(currentAxes());

    // Need to make the title visible
    currentAxes().getTitle().getFontStyle().setVisible(true);
  }
}
