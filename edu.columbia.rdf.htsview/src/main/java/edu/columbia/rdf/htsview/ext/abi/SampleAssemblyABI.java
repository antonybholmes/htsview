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
package edu.columbia.rdf.htsview.ext.abi;

import java.nio.file.Path;

import edu.columbia.rdf.htsview.tracks.SampleAssemblyFile;

// TODO: Auto-generated Javadoc
/**
 * Stream a track directly from a r tree binary file.
 *
 * @author Antony Holmes Holmes
 */
public class SampleAssemblyABI extends SampleAssemblyFile {

	/**
	 * Instantiates a new sample assembly ABI.
	 *
	 * @param file the file
	 * @param base the base
	 */
	public SampleAssemblyABI(Path file, char base) {
		super(new ReadCountsFileABI(file, base));
	}
}
