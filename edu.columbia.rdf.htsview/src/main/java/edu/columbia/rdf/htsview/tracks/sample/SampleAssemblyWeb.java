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

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomeService;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;
import org.jebtk.core.BufferUtils;
import org.jebtk.core.http.URLUtils;
import org.jebtk.core.http.URLPath;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.text.TextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;

/**
 * Maintains a connection to a caArray server.
 */
public class SampleAssemblyWeb extends SampleAssembly {

  /** The Constant LOG. */
  private static final Logger LOG = LoggerFactory
      .getLogger(SampleAssemblyWeb.class);

  /** The m auth V 1. */
  private URLPath mAuthV1;

  /** The m BRT map. */
  private Map<Sample, Boolean> mBRTMap = new HashMap<Sample, Boolean>();

  /** The m BVT map. */
  private Map<Sample, Boolean> mBVTMap = new HashMap<Sample, Boolean>();

  private String mMode = "count";

  /**
   * Instantiates a new track assembly web.
   *
   * @param login the login
   * @param url the url
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public SampleAssemblyWeb(URLPath login) throws IOException {
    mAuthV1 = login; // new SeqUrlBuilder(login);

    // mAuthV1 = new OTKAuthUrl(new
    // UrlBuilder(url).resolve("v2").resolve("auth"),
    // login.getUser(),
    // login.getKey(),
    // login.getEpoch(),
    // login.getStep());
  }

  /*
   * @Override public List<Integer> getStarts(Sample sample, GenomicRegion
   * region) throws IOException { List<Integer> ret = new ArrayList<Integer>();
   * 
   * UrlBuilder startsUrl =
   * getOTKAuthUrl().resolve("starts").resolve(sample.getId()).resolve(
   * region.getChr()).resolve(region.getStart()).resolve(region.getEnd());
   * 
   * //LOG.info("starts url: {}", startsUrl);
   * 
   * Json json = new JsonParser().parse(startsUrl.toUrl());
   * 
   * Json startsJson = json.get(0).get("starts");
   * 
   * for (int i = 0; i < startsJson.size(); ++i) {
   * ret.add(startsJson.get(i).getInt()); }
   * 
   * return ret; }
   */

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getStarts(edu.columbia.rdf.
   * edb .Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public int[] getStarts(Sample sample, GenomicRegion region, int window)
      throws IOException {

    // List<Integer> ret = getJsonStarts(sample, region);

    // List<Integer> ret = getBinaryStarts(sample, region);

    // System.err.println(ret.size() + " " + ret);
    // System.err.println(ret2.size() + " " + ret2);

    // return ret;

    return getJsonStarts(sample, region, window); // getBinaryStarts(sample,
                                                  // region, window);
  }

  /**
   * Gets the json starts.
   *
   * @param sample the sample
   * @param region the region
   * @return the json starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[] getJsonStarts(Sample sample, GenomicRegion region, int window)
      throws IOException {

    URLPath url = mAuthV1.join("starts").join(sample.getId())
        .join(region.getGenome()).join(region.getChr())
        .join(region.getStart()).join(region.getEnd());

    LOG.info("starts url: {}", url);

    Json json = new JsonParser().parse(url.toURL());

    Json startsJson = json.get(0).get("s");

    int[] ret = new int[startsJson.size()];

    for (int i = 0; i < startsJson.size(); ++i) {
      ret[i] = startsJson.getInt(i);
    }

    return ret;
  }

  /**
   * Gets the binary starts.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the binary starts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[] getBinaryStarts(Sample sample,
      GenomicRegion region,
      int window) throws IOException {

    URLPath url = mAuthV1.join("starts").join(sample.getId())
        .join(region.getGenome()).join(region.getChr())
        .join(region.getStart()).join(region.getEnd()).join("b");

    return BufferUtils.byteBuffer().wrap(URLUtils.read(url).bytes()).getInts();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getStrands(edu.columbia.rdf.
   * edb.Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public Strand[] getStrands(Sample sample, GenomicRegion region, int window)
      throws IOException {
    return getJsonStrands(sample, region, window); // getBinaryStrands(sample,
                                                   // region, window);
  }

  /**
   * Gets the json strands.
   *
   * @param sample the sample
   * @param region the region
   * @return the json strands
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Strand[] getJsonStrands(Sample sample,
      GenomicRegion region,
      int window) throws IOException {

    URLPath startsUrl = mAuthV1.join("strands").join(sample.getId())
        .join(region.getGenome()).join(region.getChr())
        .join(region.getStart()).join(region.getEnd());

    // LOG.info("starts url: {}", startsUrl);

    Json json = new JsonParser().parse(startsUrl.toURL());

    Json strandsJson = json.get(0).get("s");

    Strand[] ret = new Strand[strandsJson.size()];

    for (int i = 0; i < strandsJson.size(); ++i) {
      ret[i] = Strand.parse(strandsJson.getChar(i));
    }

    return ret;
  }

  /**
   * Gets the binary strands.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the binary strands
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public Strand[] getBinaryStrands(Sample sample,
      GenomicRegion region,
      int window) throws IOException {
    URLPath url = mAuthV1.join("strands").join(sample.getId())
        .join(region.getGenome()).join(region.getChr())
        .join(region.getStart()).join(region.getEnd()).join("b");

    /*
     * byte[] bytes = Network.read(url).bytes();
     * 
     * List<Character> ret = new ArrayList<Character>(bytes.length);
     * 
     * for (byte b : bytes) { ret.add(b == '-' ? '-' : '+'); }
     * 
     * return ret;
     */

    return Strand.parse(
        BufferUtils.byteBuffer().wrap(URLUtils.read(url).bytes()).byteChars());
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getCounts(edu.columbia.rdf.
   * edb .Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
   */
  @Override
  public int[] getCounts(Sample sample, GenomicRegion region, int window)
      throws IOException {

    //return getBinaryCounts(sample, region, window);
    return getTextCounts(sample, region, window);
    //return getJsonCounts(sample, region, window);

    /*
     * if (hasReadSupport(sample)) { return getBinaryCounts(sample, region,
     * window); } else { return getJsonCounts(sample, region, window); }
     */
  }

  /**
   * Gets the json counts.
   *
   * @param sample the sample
   * @param region the region
   * @param window the window
   * @return the json counts
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public int[] getJsonCounts(Sample sample, GenomicRegion region, int window)
      throws IOException {

    URLPath url = mAuthV1;

    url = url.join("counts").param("id", sample.getId())
        .param("g", region.getGenome().getAssembly())
        .param("chr", region.mChr.toString()).param("s", region.mStart)
        .param("e", region.mEnd).param("bw", window).param("m", mMode);

    //LOG.info("Count url: {}", url);

    Json json = new JsonParser().parse(url.toURL());

    Json countsJson = json.get(0).get("c");

    int[] ret = new int[countsJson.size()];

    for (int i = 0; i < countsJson.size(); ++i) {
      ret[i] = countsJson.getInt(i);
    }

    return ret;
  }
  
  public int[] getBinaryCounts(Sample sample, GenomicRegion region, int window)
      throws IOException {

    URLPath url = mAuthV1;

    url = url.join("counts").param("id", sample.getId())
        .param("g", region.getGenome().getAssembly())
        .param("chr", region.mChr.toString()).param("s", region.mStart)
        .param("e", region.mEnd).param("bw", window).param("m", mMode).param("format", "binary");

    return BufferUtils.byteBuffer().wrap(URLUtils.read(url).bytes()).getInts();
  }
  
  public int[] getTextCounts(Sample sample, GenomicRegion region, int window)
      throws IOException {

    URLPath url = mAuthV1;

    url = url.join("counts").param("id", sample.getId())
        .param("g", region.getGenome().getAssembly())
        .param("chr", region.mChr.toString()).param("s", region.mStart)
        .param("e", region.mEnd).param("bw", window).param("m", mMode).param("format", "text");

    //LOG.info("Count url: {}", url);
    
    BufferedReader reader = URLUtils.newBufferedReader(url);

    List<String> tokens;
    
    try {
      tokens = TextUtils.commaSplit(reader.readLine());
    } finally {
      reader.close();
    }
    
    int[] ret = new int[tokens.size()];

    for (int i = 0; i < tokens.size(); ++i) {
      ret[i] = Integer.parseInt(tokens.get(i));
    }

    return ret;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getMappedReads(edu.columbia.
   * rdf.edb.Sample)
   */
  @Override
  public int getMappedReads(Sample sample, Genome genome, int window)
      throws IOException {
    int ret = -1;

    URLPath mappedUrl = mAuthV1.join("mapped").param("id", sample.getId())
        .param("g", genome.getAssembly()).param("bw", window).param("m", mMode);

    // LOG.info("Mapped url: {}", mappedUrl);

    Json json = new JsonParser().parse(mappedUrl.toURL());

    ret = json.getInt(0);

    return ret;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getGenome(edu.columbia.rdf.
   * edb .Sample)
   */
  @Override
  public Genome getGenome(Sample sample) throws IOException {
    URLPath url = mAuthV1.join("genome").join(sample.getId());

    Json json = new JsonParser().parse(url.toURL());

    return GenomeService.getInstance().guessGenome(json.get(0).getString("genome"));
  }

  /**
   * See if we can use the brt version of the files.
   *
   * @param sample the sample
   * @return true, if successful
   * @throws IOException Signals that an I/O exception has occurred.
   */
  @Override
  public boolean hasReadSupport(Sample sample) throws IOException {
    if (mBRTMap.containsKey(sample)) {
      return mBRTMap.get(sample);
    }

    URLPath url = mAuthV1.join("type").param("id", sample.getId());

    LOG.info("BRT url: {}", url);

    Json json = new JsonParser().parse(url.toURL());

    System.err.println("track web " + json + " " + json.getString(0));

    boolean isBRT = json.get(0).equals("brt");

    // LOG.info("BRT type: {} {} {}", json.get(0).get("type"), isBRT);

    mBRTMap.put(sample, isBRT);

    return isBRT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#isBVT(edu.columbia.rdf.edb.
   * Sample)
   */
  @Override
  public boolean isBVT(Sample sample) throws IOException {
    if (mBVTMap.containsKey(sample)) {
      return mBVTMap.get(sample);
    }

    URLPath url = mAuthV1.join("type").join(sample.getId());

    // LOG.info("BRT url: {}", url);

    Json json = new JsonParser().parse(url.toURL());

    boolean isBVT = json.get(0).getString("type").equals("bvt");

    // LOG.info("BRT type: {} {} {}", json.get(0).get("type"), isBRT);

    mBVTMap.put(sample, isBVT);

    return isBVT;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * edu.columbia.rdf.htsview.tracks.SampleAssembly#getReadLength(edu.columbia.
   * rdf .edb.Sample)
   */
  @Override
  public int getReadLength(Sample sample) throws IOException {
    URLPath url = mAuthV1.join("length").join(sample.getId());

    // LOG.info("Read length url: {}", url);

    Json json = new JsonParser().parse(url.toURL());

    return json.get(0).getInt("length");
  }
}
