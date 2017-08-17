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

import java.io.IOException;
import java.nio.file.Path;

import org.jebtk.core.NameProperty;
import org.jebtk.core.io.PathUtils;
import org.jebtk.core.json.Json;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.htsview.tracks.Track;

// TODO: Auto-generated Javadoc
/**
 * The Class TrackJsonParser.
 */
public abstract class TrackJsonParser implements NameProperty {

	/**
	 * Creates a new track from the json.
	 *
	 * @param window the window
	 * @param name the name
	 * @param id the id
	 * @param annotationTree the annotation tree
	 * @param trackJson the track json
	 * @param rootNode the root node
	 * @return true, if successful
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract boolean parse(ModernWindow window,
			String name, 
			int id,
			ModernTree<Track> annotationTree,
			Json trackJson, 
			TreeNode<Track> rootNode) throws IOException;

	/**
	 * Returns the type this parser is designed to deal with.
	 *
	 * @return the type
	 */
	public abstract String getType();
	
	
	/**
	 * Gets the file.
	 *
	 * @param json the json
	 * @return the file
	 */
	protected static Path getFile(Json json) {
		String path = json.getAsString("file");

		if (path != null) {
			return PathUtils.getPath(path);
		}

		path = json.getAsString("meta-file");

		if (path != null) {
			return PathUtils.getPath(path);
		}

		return null;
	}

}
