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
package edu.columbia.rdf.htsview.ext.samtools;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.Strand;

import edu.columbia.rdf.htsview.ngs.ReadCountsFile;
import htsjdk.samtools.SAMRecord;
import htsjdk.samtools.SAMRecordIterator;
import htsjdk.samtools.SamReader;
import htsjdk.samtools.SamReaderFactory;


// TODO: Auto-generated Javadoc
/**
 * Decodes counts using a multi resolution file.
 *
 * @author Antony Holmes Holmes
 */
public class ReadCountsFileBam extends ReadCountsFile {

	/** The m file. */
	private Path mFile;

	/** The m reads. */
	private int mReads = -1;

	/** The m read length. */
	private int mReadLength = -1;

	/**
	 * Directory containing genome files which must be of the form
	 * chr.n.txt. Each file must contain exactly one line consisting
	 * of the entire chromosome.
	 *
	 * @param file the file
	 */
	public ReadCountsFileBam(Path file) {
		mFile = file;

		try {
			mReads = SamUtils.getTotalReadsFromIndexedBam(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			mReadLength = SamUtils.getReadLengthFromBam(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getCounts(edu.columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
	 */
	@Override
	public List<Integer> getCounts(GenomicRegion region, int window) throws IOException {
		return getCounts(region.getChr(),
				region.getStart(),
				region.getEnd(),
				window);
	}

	/**
	 * Gets the counts.
	 *
	 * @param chr the chr
	 * @param start the start
	 * @param end the end
	 * @param window the window
	 * @return the counts
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Integer> getCounts(Chromosome chr,
			int start,
			int end,
			int window) throws IOException {
		return binCounts(getStarts(chr, start, end, window), start, end, window);
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.lib.bioinformatics.reads.CountAssembly#getStarts(edu.columbia.rdf.lib.bioinformatics.genome.GenomicRegion)
	 */
	@Override
	public List<Integer> getStarts(GenomicRegion region, int window) throws IOException {
		return getStarts(region.getChr(),
				region.getStart(),
				region.getEnd(),
				window);
	}

	/**
	 * Gets the starts.
	 *
	 * @param chr the chr
	 * @param start the start
	 * @param end the end
	 * @param window the window
	 * @return the starts
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Integer> getStarts(Chromosome chr,
			int start,
			int end,
			int window) throws IOException {

		List<Integer> starts = new ArrayList<Integer>();

		SAMRecordIterator iter = null;

		SamReader inputSam = 
				SamReaderFactory.makeDefault().open(mFile.toFile());

		try {
			iter = inputSam.queryContained(chr.toString(), start, end);
		} catch (Exception e1) {
			//e1.printStackTrace();

			inputSam.close();

			inputSam = SamReaderFactory.makeDefault().open(mFile.toFile());

			try {
				// In cases where chromosome are called 1, 2, 3 etc rather than
				// chr1, chr2, chr3 etc.
				iter = inputSam.queryContained(chr.toString().substring(3), start, end);
			} catch (Exception e2) {
				inputSam.close();

				return Collections.emptyList();
			}
		}

		try {
			SAMRecord record;

			while(iter.hasNext()) {
				record = iter.next();

				starts.add(record.getStart());
			}
		} finally {
			inputSam.close();
		}

		return starts;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getStrands(org.jebtk.bioinformatics.genome.GenomicRegion, int)
	 */
	@Override
	public List<Strand> getStrands(GenomicRegion region, int window) throws IOException {
		return getStrands(region.getChr(), 
				region.getStart(), 
				region.getEnd(), 
				window);
	}

	/**
	 * Gets the strands.
	 *
	 * @param chr the chr
	 * @param start the start
	 * @param end the end
	 * @param window the window
	 * @return the strands
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public List<Strand> getStrands(Chromosome chr,
			int start,
			int end,
			int window) throws IOException {
		SAMRecordIterator iter = null;

		SamReader inputSam = 
				SamReaderFactory.makeDefault().open(mFile.toFile());

		try {
			iter = inputSam.queryContained(chr.toString(), start, end);
		} catch (Exception e1) {
			//e1.printStackTrace();

			inputSam.close();

			inputSam = SamReaderFactory.makeDefault().open(mFile.toFile());

			try {
				// In cases where chromosome are called 1, 2, 3 etc rather than
				// chr1, chr2, chr3 etc.
				iter = inputSam.queryContained(chr.toString().substring(3), start, end);
			} catch (Exception e2) {
				inputSam.close();

				return Collections.emptyList();
			}
		}

		SAMRecord record;

		List<Strand> strands = new ArrayList<Strand>();

		try {
			if (iter != null) {
				while(iter.hasNext()) {
					record = iter.next();

					strands.add(SamUtils.strand(record.getFlags()));
				}
			}
		} finally {
			inputSam.close();
		}

		return strands;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getReadCount()
	 */
	@Override
	public int getReadCount() {
		return mReads;
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getReadLength()
	 */
	@Override
	public int getReadLength() {
		return mReadLength;
	}
	
	private static SAMRecordIterator openSam(Path file,
			Chromosome chr,
			int start,
			int end,
			int window) {
		SamReader inputSam = 
				SamReaderFactory.makeDefault().open(file.toFile());

		SAMRecordIterator iter = null;
		
		try {
			iter = inputSam.queryContained(chr.toString(), start, end);
		} catch (Exception e1) {
			//e1.printStackTrace();

			try {
				inputSam.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			iter = null;
			inputSam = SamReaderFactory.makeDefault().open(file.toFile());

			try {
				// In cases where chromosome are called 1, 2, 3 etc rather than
				// chr1, chr2, chr3 etc.
				iter = inputSam.queryContained(chr.toString().substring(3), start, end);
			} catch (Exception e2) {
				try {
					inputSam.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				iter = null;
			}
		}
		
		return iter;
	}
}
