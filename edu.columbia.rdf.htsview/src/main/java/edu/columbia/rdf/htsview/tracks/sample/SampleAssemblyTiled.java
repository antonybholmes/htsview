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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.bioinformatics.genomic.ChromosomeSizes;
import org.jebtk.bioinformatics.genomic.ChromosomeSizesService;
import org.jebtk.bioinformatics.genomic.GenomeAssembly;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.SubList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.columbia.rdf.edb.Sample;
import edu.columbia.rdf.htsview.tracks.SampleAssembly;

// TODO: Auto-generated Javadoc
/**
 * Caches counts around the region of interest.
 */
public class SampleAssemblyTiled extends SampleAssembly {
	
	/** The Constant LOG. */
	private final static Logger LOG = 
			LoggerFactory.getLogger(SampleAssemblyTiled.class);
	
	/**
	 * The Class TrackTiles.
	 */
	private class TrackTiles {

		/** The m assembly. */
		private SampleAssembly mAssembly;
		
		/** The m num tiles. */
		private int mNumTiles;
		
		/** The m window. */
		private int mWindow;
		
		/** The m sample. */
		private Sample mSample;

		/** The m region. */
		GenomicRegion mRegion;
		
		/** The m chr sizes. */
		private ChromosomeSizes mChrSizes;
		
		/** The m center tile. */
		private int mCenterTile;
		
		/** The m counts. */
		private List<Integer> mCounts = null;
		
		
		/**
		 * Instantiates a new track tiles.
		 *
		 * @param assembly the assembly
		 * @param numTiles the num tiles
		 * @param chrSizes the chr sizes
		 * @param sample the sample
		 * @param window the window
		 */
		public TrackTiles(SampleAssembly assembly, 
				int numTiles, 
				ChromosomeSizes chrSizes,
				Sample sample,
				int window) {
			mAssembly = assembly;
			
			// Ensure the number of tiles is an odd number 
			mNumTiles = numTiles + (numTiles % 2 == 0 ? 1 : 0);
			
			
			mChrSizes = chrSizes;
			mSample = sample;
			mWindow = window;
			
			mCenterTile = mNumTiles / 2;
		}
		
		/**
		 * Gets the counts.
		 *
		 * @param region the region
		 * @return the counts
		 * @throws IOException Signals that an I/O exception has occurred.
		 */
		public List<Integer> getCounts(GenomicRegion region) throws IOException {
			
			// Update the cache if we get a cache miss
			if (mCounts == null || !GenomicRegion.within(region, mRegion)) {
				int w = region.getLength() * mCenterTile;
				
				mRegion = new GenomicRegion(region.getChr(),
						Math.max(1, region.getStart() - w),
						Math.min(mChrSizes.getSize(region.getChr()), region.getEnd() + w));

				mCounts = mAssembly.getCounts(mSample, mRegion, mWindow);
				
				//LOG.info("Cache miss in sample {} at {} window {}", mSample.getName(), mRegion, mWindow);
			}
			
			int start = region.getStart();
			int end = region.getEnd();
			
			int s = getBin(start, mWindow);
			int e = getBin(end, mWindow);
			int l = e - s + 1;
			
			int offset = (start - mRegion.getStart()) / mWindow;
			
			return new SubList<Integer>(mCounts, offset, l);
		}
	}
	
	/** The m assembly. */
	private SampleAssembly mAssembly;

	/** The m tile map. */
	private Map<Integer, TrackTiles> mTileMap =
			new HashMap<Integer, TrackTiles>();

	/** The m num tiles. */
	private int mNumTiles;

	/**
	 * Instantiates a new sample assembly tiled.
	 *
	 * @param assembly the assembly
	 */
	public SampleAssemblyTiled(SampleAssembly assembly) {
		this(assembly, 5);
	}
	
	/**
	 * Instantiates a new sample assembly tiled.
	 *
	 * @param assembly the assembly
	 * @param numTiles the num tiles
	 */
	public SampleAssemblyTiled(SampleAssembly assembly, 
			int numTiles) {
		mAssembly = assembly;
		mNumTiles = numTiles;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getCounts(edu.columbia.rdf.edb.Sample, org.jebtk.bioinformatics.genome.GenomicRegion, int)
	 */
	@Override
	public List<Integer> getCounts(Sample sample,
			GenomicRegion region,
			int window) throws IOException {
		
		if (!mTileMap.containsKey(window)) {
			mTileMap.put(window, new TrackTiles(mAssembly, 
					mNumTiles, 
					sample.getOrganism().getScientificName().contains("Homo") ? ChromosomeSizesService.getInstance().getSizes(GenomeAssembly.HG19) : ChromosomeSizesService.getInstance().getSizes(GenomeAssembly.MM10), 
					sample, 
					window));
		}
		
		return mTileMap.get(window).getCounts(region);
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.SampleAssembly#getMappedReads(edu.columbia.rdf.edb.Sample)
	 */
	@Override
	public int getMappedReads(Sample sample) throws IOException {
		return mAssembly.getMappedReads(sample);
	}
}
