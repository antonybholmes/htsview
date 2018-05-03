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
import org.jebtk.bioinformatics.ext.ucsc.TrackDisplayMode;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.JsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * The Class BedPlotTrack.
 */
public class BedPlotTrack extends UcscPlotTrack {

  /** The m file. */
  private Path mFile;

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
    this(Bed.parseTracks(file).get(0), file);
  }

  /**
   * Instantiates a new bed plot track.
   *
   * @param bed the bed
   * @param file the file
   */
  public BedPlotTrack(UCSCTrack bed, Path file) {
    this(bed, file, TrackDisplayMode.COMPACT);
  }

  /**
   * Instantiates a new bed plot track.
   *
   * @param file the file
   * @param mode the mode
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public BedPlotTrack(Path file, TrackDisplayMode mode) throws IOException {
    this(Bed.parseTracks(file).get(0), file, mode);
  }

  /**
   * Instantiates a new bed plot track.
   *
   * @param bed the bed
   * @param file the file
   * @param mode the mode
   */
  public BedPlotTrack(UCSCTrack bed, Path file, TrackDisplayMode mode) {
    super(bed, mode);

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
}
