/**
 * Copyright 2017 Antony Holmes
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
package edu.columbia.rdf.htsview.tracks.view;

import java.util.HashMap;
import java.util.Map;

/**
 * The Class TrackParserService.
 */
public class TrackParserService {

  /**
   * The Class TrackServiceLoader.
   */
  private static class TrackServiceLoader {

    /** The Constant INSTANCE. */
    private static final TrackParserService INSTANCE = new TrackParserService();
  }

  /**
   * Gets the single instance of TrackParserService.
   *
   * @return single instance of TrackParserService
   */
  public static TrackParserService getInstance() {
    return TrackServiceLoader.INSTANCE;
  }

  /** The m parser map. */
  private Map<String, TrackJsonParser> mParserMap = new HashMap<String, TrackJsonParser>();

  /**
   * Instantiates a new track parser service.
   */
  private TrackParserService() {
    // autoLoad();
  }

  /**
   * Register.
   *
   * @param parser the parser
   */
  public void register(TrackJsonParser parser) {
    register(parser.getType(), parser);
  }

  /**
   * Register.
   *
   * @param type the type
   * @param parser the parser
   */
  private void register(String type, TrackJsonParser parser) {
    mParserMap.put(type, parser);
  }

  /**
   * Gets the.
   *
   * @param type the type
   * @return the track json parser
   */
  public TrackJsonParser get(String type) {
    return mParserMap.get(type);
  }
}
