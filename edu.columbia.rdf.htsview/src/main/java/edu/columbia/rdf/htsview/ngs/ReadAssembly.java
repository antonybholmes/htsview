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
import java.text.ParseException;
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;


// TODO: Auto-generated Javadoc
/**
 * The class ReadAssembly.
 */
public abstract class ReadAssembly {

	/**
	 * Gets the groups.
	 *
	 * @return the groups
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public abstract List<String> getGroups() throws IOException, ParseException;
	
	/**
	 * Gets the sub groups.
	 *
	 * @param group the group
	 * @return the sub groups
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public abstract List<String> getSubGroups(String group) throws IOException, ParseException;
	
	/**
	 * Gets the samples.
	 *
	 * @param group the group
	 * @param subGroup the sub group
	 * @return the samples
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public abstract List<String> getSamples(String group, String subGroup) throws IOException, ParseException;
	
	/**
	 * Gets the counts.
	 *
	 * @param group the group
	 * @param subGroup the sub group
	 * @param sample the sample
	 * @param window the window
	 * @param genomicRegion the genomic region
	 * @return the counts
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public List<Integer> getCounts(String group,
			String subGroup,
			String sample,
			int window,
			String genomicRegion) throws IOException, ParseException {
		return getCounts(group, subGroup, sample, window, GenomicRegion.parse(genomicRegion));
	}
	
	/**
	 * Gets the counts.
	 *
	 * @param group the group
	 * @param subGroup the sub group
	 * @param sample the sample
	 * @param window the window
	 * @param region the region
	 * @return the counts
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public abstract List<Integer> getCounts(String group,
			String subGroup,
			String sample,
			int window,
			GenomicRegion region) throws IOException, ParseException;

	/**
	 * Gets the mapped reads.
	 *
	 * @param group the group
	 * @param subGroup the sub group
	 * @param sample the sample
	 * @return the mapped reads
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public abstract int getMappedReads(String group, 
			String subGroup, 
			String sample) throws IOException, ParseException;
}
