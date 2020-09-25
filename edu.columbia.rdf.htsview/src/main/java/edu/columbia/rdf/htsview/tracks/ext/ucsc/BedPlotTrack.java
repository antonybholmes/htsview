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

import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.bioinformatics.ext.ucsc.Bed;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.htsview.tracks.GraphPlotTrack;
import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.TrackDisplayMode;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;
import edu.columbia.rdf.htsview.tracks.genomic.GenomicElementsSubFigure;

/**
 * The Class BedPlotTrack.
 */
public class BedPlotTrack extends GraphPlotTrack {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  /** The m file. */
  private Path mFile;

  private UCSCTrack mUcsc;

  /** The Constant BAR_HEIGHT. */
  public static final int BAR_HEIGHT = 20;

  /** The Constant HALF_BAR_HEIGHT. */
  public static final int HALF_BAR_HEIGHT = BAR_HEIGHT / 2;

  /** The gap. */
  public static int GAP = HALF_BAR_HEIGHT / 2;

  /** The Constant BLOCK_HEIGHT. */
  public static final int BLOCK_HEIGHT = BAR_HEIGHT + GAP;

  /**
   * Instantiates a new bed plot track.
   *
   * @param file the file
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public BedPlotTrack(Path file) throws IOException {
    this(Bed.parseTracks(GenomicType.REGION, file).get(0), file);
  }

  /**
   * Instantiates a new bed plot track.
   *
   * @param bed the bed
   * @param file the file
   */
  public BedPlotTrack(UCSCTrack bed, Path file) {
    this(file, bed, TrackDisplayMode.COMPACT);
  }

  /**
   * Instantiates a new bed plot track.
   *
   * @param file the file
   * @param mode the mode
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public BedPlotTrack(Path file, TrackDisplayMode mode) throws IOException {
    this(file, Bed.parseTracks(GenomicType.REGION, file).get(0), mode);
  }

  /**
   * Instantiates a new bed plot track.
   *
   * @param bed the bed
   * @param file the file
   * @param mode the mode
   */
  public BedPlotTrack(Path file, UCSCTrack bed, TrackDisplayMode mode) {
    mUcsc = bed;

    mFile = file;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.ext.ucsc.UcscPlotTrack#getType()
   */
  @Override
  public String getType() {
    return "BED";
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.ext.ucsc.UcscPlotTrack#toXml(org.w3c.dom.
   * Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "bed");
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("file", PathUtils.toString(mFile));
    trackElement.setAttribute("color", ColorUtils.toHtml(getFillColor()));

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.ext.ucsc.UcscPlotTrack#toJson(org.abh.
   * common. json.JsonBuilder)
   */
  @Override
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "bed");
    json.add("name", getName());
    json.add("file", PathUtils.toString(mFile));
    json.add("color", ColorUtils.toHtml(getFillColor()));

    json.endObject();
  }
  
  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#createGraph(java.lang.String,
   * edu.columbia.rdf.htsview.tracks.TitleProperties)
   */
  @Override
  public TrackSubFigure createGraph(Genome genome,
      TitleProperties titlePosition) throws IOException {
    mSubFigure = GenomicElementsSubFigure.create(this, mUcsc, titlePosition);

    // mPlot.getGraphSpace().setPlotSize(PLOT_SIZE);

    setMargins(getName(), titlePosition, mSubFigure);

    mSubFigure.currentAxes().getX1Axis().getTitle().setText(null);
    mSubFigure.currentAxes().getY1Axis().setLimits(0, 1);

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
  public TrackSubFigure updateGraph(Genome genome,
      GenomicRegion displayRegion,
      int resolution,
      int width,
      int height,
      int margin) throws IOException {
    // mPlot.setForwardCanvasEventsEnabled(false);
    mSubFigure.update(genome, displayRegion, resolution, width, height, margin);
    // mPlot.setForwardCanvasEventsEnabled(true);

    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.GraphPlotTrack#edit(org.abh.common.ui.
   * window. ModernWindow)
   */
  @Override
  public void edit(ModernWindow parent) {
    BedEditDialog dialog = new BedEditDialog(parent, this);

    dialog.setVisible(true);

    if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
      return;
    }

    setName(dialog.getName());
    setFillColor(dialog.getLineColor());
    setDisplayMode(dialog.getMode());
  }
}
