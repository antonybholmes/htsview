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
import java.nio.file.Path;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.JsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;

/**
 * The Class SampleFsPlotTrack.
 */
public class SampleFsPlotTrack extends SamplePlotTrack {

  /** The m meta file. */
  private Path mMetaFile;

  /**
   * Instantiates a new sample fs plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param metaFile the meta file
   */
  public SampleFsPlotTrack(Sample sample, SampleAssembly assembly,
      Path metaFile) {
    super(sample, assembly);

    mMetaFile = metaFile;
  }

  /**
   * Instantiates a new sample fs plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param metaFile the meta file
   * @param color the color
   * @param fillColor the fill color
   */
  public SampleFsPlotTrack(Sample sample, SampleAssembly assembly,
      Path metaFile, Color color, Color fillColor) {
    super(sample, assembly, color, fillColor);

    mMetaFile = metaFile;
  }

  /**
   * Instantiates a new sample fs plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param dir the dir
   * @param color the color
   */
  public SampleFsPlotTrack(Sample sample, SampleAssembly16bit assembly,
      Path dir, Color color) {
    super(sample, assembly, color);

    mMetaFile = dir;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack#toXml(org.w3c.dom.
   * Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "sample-fs");
    trackElement.setAttribute("meta-file", PathUtils.toString(mMetaFile));
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("line-color", ColorUtils.toHtml(getLineColor()));
    trackElement.setAttribute("fill-color", ColorUtils.toHtml(getFillColor()));
    trackElement.setAttribute("height", Integer.toString(getHeight()));

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.sample.SamplePlotTrack#toJson(org.abh.
   * common. json.JsonBuilder)
   */
  @Override
  public void toJson(JsonBuilder json) {
    commonJson(json);

    json.add("type", "sample-fs");
    json.add("meta-file", PathUtils.toString(mMetaFile));

    json.endObject();
  }
}
