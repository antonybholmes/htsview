/**
 * Copyright (C) 2016, Antony Holmes
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *  1. Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *  2. Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *  3. Neither the name of copyright holder nor the names of its contributors 
 *     may be used to endorse or promote products derived from this software 
 *     without specific prior written permission. 
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package edu.columbia.rdf.htsview.ngs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.json.Json;
import org.jebtk.core.json.JsonParser;
import org.jebtk.core.network.UrlBuilder;

// TODO: Auto-generated Javadoc
/**
 * Maintains a connection to a caArray server.
 *
 * @author Antony Holmes Holmes
 */
public class ReadAssemblyWeb extends ReadAssembly {
	/**
	 * The member url.
	 */
	private UrlBuilder mUrl;
	
	/**
	 * The member samples url.
	 */
	private UrlBuilder mSamplesUrl;
	
	/**
	 * The member counts url.
	 */
	private UrlBuilder mCountsUrl;
	
	/**
	 * The member groups url.
	 */
	private URL mGroupsUrl;
	
	/**
	 * The member sub groups url.
	 */
	private UrlBuilder mSubGroupsUrl;
	
	/**
	 * The member mapped url.
	 */
	private UrlBuilder mMappedUrl;
	
	/**
	 * The member parser.
	 */
	private JsonParser mParser;

	/**
	 * Instantiates a new read assembly web.
	 *
	 * @param url the url
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public ReadAssemblyWeb(URL url) throws IOException {
		mUrl = new UrlBuilder(url);

		mGroupsUrl = new UrlBuilder(mUrl).resolve("groups").toUrl();

		mSubGroupsUrl = new UrlBuilder(mUrl).resolve("subgroups");

		mSamplesUrl = new UrlBuilder(mUrl).resolve("samples");

		mCountsUrl = new UrlBuilder(mUrl).resolve("counts");

		mMappedUrl = new UrlBuilder(mUrl).resolve("mapped");
		
		mParser = new JsonParser();
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.reads.ReadAssembly#getGroups()
	 */
	@Override
	public List<String> getGroups() throws IOException, ParseException {
		List<String> groups = new ArrayList<String>();

		Json json = mParser.parse(mGroupsUrl);

		for (int i = 0; i < json.size(); ++i) {
			groups.add(json.get(i).getAsString());
		}

		return groups;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.reads.ReadAssembly#getSubGroups(java.lang.String)
	 */
	@Override
	public List<String> getSubGroups(String group) throws IOException, ParseException {
		List<String> samples = new ArrayList<String>();

		try {
			URL url = new UrlBuilder(mSubGroupsUrl).resolve(group).toUrl();

			Json json = mParser.parse(url);

			for (int i = 0; i < json.size(); ++i) {
				samples.add(json.get(i).getAsString());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return samples;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.reads.ReadAssembly#getSamples(java.lang.String, java.lang.String)
	 */
	@Override
	public List<String> getSamples(String group, String subGroup) throws IOException, ParseException {
		List<String> samples = new ArrayList<String>();

		try {
			URL url = new UrlBuilder(mSamplesUrl).resolve(group).resolve(subGroup).toUrl();

			Json json = mParser.parse(url);

			for (int i = 0; i < json.size(); ++i) {
				samples.add(json.get(i).getAsString());
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return samples;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.reads.ReadAssembly#getCounts(java.lang.String, java.lang.String, java.lang.String, int, edu.columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
	 */
	@Override
	public List<Integer> getCounts(String group,
			String subGroup,
			String sample, 
			int window,
			GenomicRegion region) throws IOException, ParseException {
		List<Integer> ret = new ArrayList<Integer>();

		try {
			URL url = new UrlBuilder(mCountsUrl).resolve(group).resolve(subGroup).resolve(sample).resolve(window).resolve(region.getChr().toString()).resolve(region.getStart()).resolve(region.getEnd()).toUrl();

			//System.err.println(url);

			Json json = mParser.parse(url);

			Json scoresJson = json.get(0).get("counts");

			for (int i = 0; i < scoresJson.size(); ++i) {
				ret.add(scoresJson.get(i).getAsInt());
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ret;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.reads.ReadAssembly#getMappedReads(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public int getMappedReads(String group,
			String subGroup,
			String sample) throws IOException, ParseException {
		int ret = -1;

		try {
			URL url = new UrlBuilder(mMappedUrl).resolve(group).resolve(subGroup).resolve(sample).toUrl();

			//System.err.println(url);

			Json json = mParser.parse(url);

			ret = json.get(0).get("mapped_reads").getAsInt();			
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		return ret;
	}
}
