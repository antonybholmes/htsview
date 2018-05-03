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

import java.nio.file.Path;

import org.jebtk.core.ColorUtils;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.JsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;

/**
 * The class ReadsPlotTrack.
 */
public class ReadsFsPlotTrack extends ReadsPlotTrack {

  /** The m meta file. */
  private Path mMetaFile;

  /**
   * Instantiates a new reads plot track.
   *
   * @param sample the sample
   * @param assembly the assembly
   * @param metaFile the meta file
   */
  public ReadsFsPlotTrack(Sample sample, SampleAssembly assembly,
      Path metaFile) {
    super(sample, assembly);

    mMetaFile = metaFile;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.edb.reads.tracks.SamplePlotTrack#toXml()
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "reads-fs");
    trackElement.setAttribute("meta-file", PathUtils.toString(mMetaFile));
    trackElement.setAttribute("name", getName());
    trackElement.setAttribute("id", Integer.toString(mSample.getId()));
    trackElement.setAttribute("line-color", ColorUtils.toHtml(getLineColor()));
    trackElement.setAttribute("fill-color", ColorUtils.toHtml(getFillColor()));

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.apps.edb.reads.tracks.SamplePlotTrack#toJson()
   */
  @Override
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "reads-fs");
    json.add("meta-file", PathUtils.toString(mMetaFile));
    json.add("name", getName());
    json.add("id", mSample.getId());
    json.add("visible", getForwardVisible());
    json.add("color", ColorUtils.toHtml(getLineColor()));
    json.add("fill-color", ColorUtils.toHtml(getFillColor()));
    json.add("anti-sense-visible", getNegVisible());
    json.add("anti-sense-color", ColorUtils.toHtml(getNegLineColor()));
    json.add("anti-sense-fill-color", ColorUtils.toHtml(getNegFillColor()));
    json.add("read-height", mReadHeight);
    json.add("gap", mGap);

    json.endObject();
  }
}
