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
package edu.columbia.rdf.htsview.tracks;

import org.jebtk.core.json.JsonBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotationPlotTrack.
 */
public abstract class AnnotationPlotTrack extends Track {

  private static final long serialVersionUID = 1L;

  /** The m name. */
  protected String mName;

  /** The m sub figure. */
  protected TrackSubFigure mSubFigure = null;

  /**
   * Instantiates a new annotation plot track.
   *
   * @param name
   *          the name
   */
  public AnnotationPlotTrack(String name) {
    setName(name);
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getGraph()
   */
  @Override
  public TrackSubFigure getGraph() {
    return mSubFigure;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getType()
   */
  @Override
  public String getType() {
    return "Annotation";
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#setName(java.lang.String)
   */
  @Override
  public void setName(String name) {
    mName = name;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#getName()
   */
  @Override
  public String getName() {
    return mName;
  }

  /*
   * (non-Javadoc)
   * 
   * @see edu.columbia.rdf.htsview.tracks.Track#toXml(org.w3c.dom.Document)
   */
  @Override
  public Element toXml(Document doc) {
    Element trackElement = doc.createElement("track");

    trackElement.setAttribute("type", "annotation");
    trackElement.setAttribute("name", getName());

    return trackElement;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.Track#toJson(org.abh.common.json.JsonBuilder)
   */
  @Override
  public void toJson(JsonBuilder json) {
    json.startObject();

    json.add("type", "annotation");
    json.add("name", getName());

    json.endObject();
  }
}
