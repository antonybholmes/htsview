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

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.GenomicRegion;

import edu.columbia.rdf.htsview.tracks.AnnotationPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotationBedPlotTrack.
 */
public class AnnotationBedPlotTrack extends AnnotationPlotTrack {

  /** The m bed. */
  private UCSCTrack mBed;

  /** The m sub figure. */
  private BedPlotSubFigure mSubFigure = null;

  /**
   * Instantiates a new annotation bed plot track.
   *
   * @param bed the bed
   */
  public AnnotationBedPlotTrack(UCSCTrack bed) {
    super(bed.getName());

    mBed = bed;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(String genome,
      TitleProperties titlePosition) {
    mSubFigure = BedPlotSubFigure.create(mBed, titlePosition);

    // mPlot.getGraphSpace().setPlotSize(PLOT_SIZE);

    mSubFigure.currentAxes().setInternalSize(Track.MEDIUM_TRACK_SIZE);

    setMargins(getName(), titlePosition, mSubFigure);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#updateGraph(org.jebtk.bioinformatics.
   * genome.GenomicRegion, int, int, int, int)
   */
  @Override
  public TrackSubFigure updateGraph(GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) {
    mSubFigure.update(displayRegion, resolution, width, height, margin);

    return mSubFigure;
  }
}
