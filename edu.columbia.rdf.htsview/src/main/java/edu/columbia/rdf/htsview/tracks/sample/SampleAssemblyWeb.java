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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;
import org.jebtk.core.BufferUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.network.UrlBuilder;
import org.jebtk.core.network.UrlUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.EDBWLogin;
import edu.columbia.rdf.edb.OTKAuthUrl;
import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;

// TODO: Auto-generated Javadoc
/**
 * Maintains a connection to a caArray server.
 */
public class SampleAssemblyWeb extends SampleAssembly {

	/** The Constant LOG. */
	private static final Logger LOG = 
			LoggerFactory.getLogger(SampleAssemblyWeb.class);

	/** The m auth V 1. */
	private OTKAuthUrl mAuthV1;

	/** The m BRT map. */
	private Map<Sample, Boolean> mBRTMap = new HashMap<Sample, Boolean>();

	/** The m BVT map. */
	private Map<Sample, Boolean> mBVTMap = new HashMap<Sample, Boolean>();

	/**
	 * Instantiates a new track assembly web.
	 *
	 * @param login the login
	 * @param url the url
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public SampleAssemblyWeb(EDBWLogin login, UrlBuilder url) throws IOException {
		mAuthV1 = new OTKAuthUrl(url,
				login.getUser(), 
				login.getKey(),
				login.getEpoch(),
				login.getStep());

		//mAuthV1 = new OTKAuthUrl(new UrlBuilder(url).resolve("v2").resolve("auth"),
		//		login.getUser(), 
		//		login.getKey(), 
		//		login.getEpoch(),
		//		login.getStep());
	}

	/*
	@Override
	public List<Integer> getStarts(Sample sample,
			GenomicRegion region) throws IOException {
		List<Integer> ret = new ArrayList<Integer>();

		UrlBuilder startsUrl = 
				mAuthV1.getOTKAuthUrl().resolve("starts").resolve(sample.getId()).resolve(region.getChr()).resolve(region.getStart()).resolve(region.getEnd());

		//LOG.info("starts url: {}", startsUrl);

		Json json = new JsonParser().parse(startsUrl.toUrl());

		Json startsJson = json.get(0).get("starts");

		for (int i = 0; i < startsJson.size(); ++i) {
			ret.add(startsJson.get(i).getAsInt());
		}

		return ret;
	}
	 */

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getStarts(edu.columbia.rdf.edb.Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
	 */
	@Override
	public List<Integer> getStarts(Sample sample,
			GenomicRegion region,
			int window) throws IOException {

		//List<Integer> ret = getJsonStarts(sample, region);

		//List<Integer> ret = getBinaryStarts(sample, region);

		//System.err.println(ret.size() + " " + ret);
		//System.err.println(ret2.size() + " " + ret2);

		//return ret;

		return getJsonStarts(sample, region, window); //getBinaryStarts(sample, region, window);
	}

	/**
	 * Gets the json starts.
	 *
	 * @param sample the sample
	 * @param region the region
	 * @return the json starts
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Integer> getJsonStarts(Sample sample,
			GenomicRegion region,
			int window) throws IOException {
		List<Integer> ret = new ArrayList<Integer>();

		UrlBuilder url = mAuthV1.getOTKAuthUrl()
				.resolve("starts")
				.resolve(sample.getId())
				.resolve(region.getChr())
				.resolve(region.getStart())
				.resolve(region.getEnd());

		//LOG.info("starts url: {}", url);

		Json json = new JsonParser().parse(url.toUrl());

		Json startsJson = json.get(0).get("s");

		for (int i = 0; i < startsJson.size(); ++i) {
			ret.add(startsJson.getAsInt(i));
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
	public List<Integer> getBinaryStarts(Sample sample,
			GenomicRegion region, 
			int window) throws IOException {

		UrlBuilder url = mAuthV1.getOTKAuthUrl()
				.resolve("starts")
				.resolve(sample.getId())
				.resolve(region.getChr())
				.resolve(region.getStart())
				.resolve(region.getEnd())
				.resolve("b");

		return BufferUtils.byteBuffer().wrap(UrlUtils.read(url).bytes()).ints();
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getStrands(edu.columbia.rdf.edb.Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
	 */
	@Override
	public List<Strand> getStrands(Sample sample,
			GenomicRegion region,
			int window) throws IOException {
		return getJsonStrands(sample, region, window); //getBinaryStrands(sample, region, window);
	}

	/**
	 * Gets the json strands.
	 *
	 * @param sample the sample
	 * @param region the region
	 * @return the json strands
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Strand> getJsonStrands(Sample sample,
			GenomicRegion region,
			int window) throws IOException {
		List<Strand> ret = new ArrayList<Strand>();

		UrlBuilder startsUrl = mAuthV1.getOTKAuthUrl()
				.resolve("strands")
				.resolve(sample.getId())
				.resolve(region.getChr())
				.resolve(region.getStart())
				.resolve(region.getEnd());

		//LOG.info("starts url: {}", startsUrl);

		Json json = new JsonParser().parse(startsUrl.toUrl());

		Json strandsJson = json.get(0).get("s");

		for (int i = 0; i < strandsJson.size(); ++i) {
			ret.add(Strand.parse(strandsJson.getAsChar(i)));
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
	public List<Strand> getBinaryStrands(Sample sample,
			GenomicRegion region,
			int window) throws IOException {
		UrlBuilder url = 
				mAuthV1.getOTKAuthUrl().resolve("strands").resolve(sample.getId()).resolve(region.getChr()).resolve(region.getStart()).resolve(region.getEnd()).resolve("b");

		/*
		byte[] bytes = Network.read(url).bytes();

		List<Character> ret = new ArrayList<Character>(bytes.length);

		for (byte b : bytes) {
			ret.add(b == '-' ? '-' : '+');
		}

		return ret;
		 */

		return Strand.parse(BufferUtils.byteBuffer().wrap(UrlUtils.read(url).bytes()).byteChars());
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getCounts(edu.columbia.rdf.edb.Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
	 */
	@Override
	public List<Integer> getCounts(Sample sample,
			GenomicRegion region,
			int window) throws IOException {

		return getJsonCounts(sample, region, window);

		/*
		if (hasReadSupport(sample)) {
			return getBinaryCounts(sample, region, window);
		} else {
			return getJsonCounts(sample, region, window);
		}
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
	public List<Integer> getJsonCounts(Sample sample,
			GenomicRegion region,
			int window) throws IOException {
		List<Integer> ret = new ArrayList<Integer>();

		UrlBuilder url = mAuthV1.getOTKAuthUrl();

		url = url.resolve("counts")
				.resolve(sample.getId())
				.resolve(region.getChr())
				.resolve(region.getStart())
				.resolve(region.getEnd())
				.resolve(window);

		//LOG.info("Count url: {}", url);

		Json json = new JsonParser().parse(url.toUrl());

		Json countsJson = json.get(0).get("c");

		for (int i = 0; i < countsJson.size(); ++i) {
			ret.add(countsJson.get(i).getAsInt());
		}

		return ret;
	}

	/**
	 * Gets the binary counts.
	 *
	 * @param sample the sample
	 * @param region the region
	 * @param window the window
	 * @return the binary counts
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Integer> getBinaryCounts(Sample sample,
			GenomicRegion region,
			int window) throws IOException {

		UrlBuilder url = mAuthV1.getOTKAuthUrl();

		url = url.resolve("counts").resolve(sample.getId()).resolve(region.getChr()).resolve(region.getStart()).resolve(region.getEnd()).resolve(window).resolve("b");

		//LOG.info("Count url: {}", url);

		return BufferUtils.byteBuffer().wrap(UrlUtils.read(url).bytes()).ints();
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getMappedReads(edu.columbia.rdf.edb.Sample)
	 */
	@Override
	public int getMappedReads(Sample sample) throws IOException {
		int ret = -1;

		UrlBuilder mappedUrl;

		mappedUrl = mAuthV1.getOTKAuthUrl();

		mappedUrl = mappedUrl.resolve("mapped").resolve(sample.getId());

		//LOG.info("Mapped url: {}", mappedUrl);

		Json json = new JsonParser().parse(mappedUrl.toUrl());

		ret = json.getAsInt(0);			

		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getGenome(edu.columbia.rdf.edb.Sample)
	 */
	@Override
	public String getGenome(Sample sample) throws IOException {
		UrlBuilder url = 
				mAuthV1.getOTKAuthUrl().resolve("genome").resolve(sample.getId());

		Json json = new JsonParser().parse(url.toUrl());

		return json.get(0).getAsString("genome");
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

		UrlBuilder url = mAuthV1.getOTKAuthUrl().resolve("type").resolve(sample.getId());

		LOG.info("BRT url: {}", url);

		Json json = new JsonParser().parse(url.toUrl());

		System.err.println("track web " + json + " " + json.getAsString(0));

		boolean isBRT = json.get(0).equals("brt");

		//LOG.info("BRT type: {} {} {}", json.get(0).get("type"), isBRT);

		mBRTMap.put(sample, isBRT);

		return isBRT;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#isBVT(edu.columbia.rdf.edb.Sample)
	 */
	@Override
	public boolean isBVT(Sample sample) throws IOException {
		if (mBVTMap.containsKey(sample)) {
			return mBVTMap.get(sample);
		}

		UrlBuilder url = mAuthV1.getOTKAuthUrl().resolve("type").resolve(sample.getId());

		//LOG.info("BRT url: {}", url);

		Json json = new JsonParser().parse(url.toUrl());

		boolean isBVT = json.get(0).getAsString("type").equals("bvt");

		//LOG.info("BRT type: {} {} {}", json.get(0).get("type"), isBRT);

		mBVTMap.put(sample, isBVT);

		return isBVT;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getReadLength(edu.columbia.rdf.edb.Sample)
	 */
	@Override
	public int getReadLength(Sample sample) throws IOException {
		UrlBuilder url = mAuthV1.getOTKAuthUrl().resolve("length").resolve(sample.getId());

		//LOG.info("Read length url: {}", url);

		Json json = new JsonParser().parse(url.toUrl());

		return json.get(0).getAsInt("length");	
	}
}
