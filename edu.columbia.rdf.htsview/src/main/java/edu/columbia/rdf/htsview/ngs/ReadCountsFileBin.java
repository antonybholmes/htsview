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
import java.io.RandomAccessFile;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.core.io.FileUtils;
import org.jebtk.core.json.Json;


// TODO: Auto-generated Javadoc
/**
 * Decodes values stored at positions in a tree.
 *
 * @author Antony Holmes Holmes
 */
public abstract class ReadCountsFileBin extends ReadCountsFile {
	
	/** The Constant GENOME. */
	protected static final byte[] GENOME = new byte[8];
	
	/**
	 * The member directory.
	 */
	protected Path mDirectory;

	/**
	 * The member file map.
	 */
	protected Map<Chromosome, Path> mFileMap = 
			new HashMap<Chromosome, Path>();

	/**
	 * The member offset map.
	 */
	protected Map<Chromosome, Integer> mOffsetMap = 
			new HashMap<Chromosome, Integer>();

	/**
	 * The member read length map.
	 */
	protected Map<Chromosome, Integer> mReadLengthMap = 
			new HashMap<Chromosome, Integer>();

	/** The m genome. */
	protected String mGenome;


	/** The m meta file. */
	protected Path mMetaFile;

	/** The m read length. */
	private int mReadLength;


	/**
	 * Directory containing genome files which must be of the form
	 * chr.n.txt. Each file must contain exactly one line consisting
	 * of the entire chromosome.
	 *
	 * @param metaFile the meta file
	 */
	public ReadCountsFileBin(Path metaFile) {
		mMetaFile = metaFile;
		mDirectory = metaFile.getParent();
		
		try {
			mGenome = Json.fromJson(metaFile).getAsString("Genome");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getReadLength()
	 */
	@Override
	public int getReadLength() {
		return mReadLength; //Map.values().iterator().next();
	}

	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.ngs.CountAssembly#getGenome()
	 */
	@Override
	public String getGenome() throws IOException {
		return mGenome; //Json.parse(mMetaFile).getAsString("Genome");
	}


	/**
	 * Gets the file.
	 *
	 * @param chr the chr
	 * @param window the window
	 * @param ext the ext
	 * @return the file
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected Path getFile(Chromosome chr, int window, String ext) throws IOException {
		if (!mFileMap.containsKey(chr)) {
			Path file = mDirectory.resolve(chr + "." + ext);
			
			mFileMap.put(chr, file);

			RandomAccessFile in = FileUtils.newRandomAccess(file);
			
			//System.err.println("load file " + file);

			try {
				mReadLength = in.readInt(); //mReadLengthMap.put(chr, in.readInt());

				// The second 4 bytes tells us where in the file to go to
				// find the read data
				mOffsetMap.put(chr, in.readInt());

				if (mGenome == null) {
					// Read the genome
					
					mGenome = readGenome(in);
				}
			} finally {
				in.close();
			}
		}

		return mFileMap.get(chr);
	}
	
	/**
	 * Read genome.
	 *
	 * @param in the in
	 * @return the string
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public String readGenome(RandomAccessFile in) throws IOException {
		in.read(GENOME);
		
		// find the first null
		int c = 0;
		
		while (c < 8) {
			if (GENOME[c++] == 0) {
				break;
			}
		}

		return new String(GENOME, 0, c);
	}
}
