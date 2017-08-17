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
package edu.columbia.rdf.htsview.tracks;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.core.tree.TreeRootNode;
import org.jebtk.core.xml.XmlUtils;
import org.jebtk.modern.dialog.DialogEvent;
import org.jebtk.modern.dialog.DialogEventListener;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.widget.ModernWidget;
import org.jebtk.modern.window.ModernRibbonWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class TracksPanel.
 */
public abstract class TracksPanel extends ModernWidget {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	//private TrackList mTrackList = new TrackList();
	//private TrackListModel mListModel = new TrackListModel();

	/** The m track list. */
	protected TrackTree mTrackList = null;
	//private TrackListModel mListModel = new TrackListModel();

	//private TracksModel mModel;

	/** The m parent. */
	protected ModernRibbonWindow mParent;

	/** The m tree. */
	private ModernTree<Track> mTree;

	//private Map<String, ChromosomeSizes> mChrMap;

	/**
	 * The Class TrackMouseEvents.
	 */
	private class TrackMouseEvents extends MouseAdapter {

		/* (non-Javadoc)
		 * @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent)
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			if (e.getClickCount() == 2) {
				editTracks();
			}
		}
	}

	/**
	 * The Class DeleteEvents.
	 */
	private class DeleteEvents implements DialogEventListener {
		
		/* (non-Javadoc)
		 * @see org.abh.common.ui.dialog.DialogEventListener#statusChanged(org.abh.common.ui.dialog.DialogEvent)
		 */
		@Override
		public void statusChanged(DialogEvent e) {
			if (e.getStatus() == ModernDialogStatus.OK) {
				ArrayList<Integer> indices = new ArrayList<Integer>();

				for (int i : mTrackList.getSelectionModel()) {
					indices.add(i);
				}

				mTrackList.removeNodes(indices);
			}
		}
	}

	/**
	 * The Class ClearEvents.
	 */
	private class ClearEvents implements DialogEventListener {
		
		/* (non-Javadoc)
		 * @see org.abh.common.ui.dialog.DialogEventListener#statusChanged(org.abh.common.ui.dialog.DialogEvent)
		 */
		@Override
		public void statusChanged(DialogEvent e) {
			if (e.getStatus() == ModernDialogStatus.OK) {
				mTrackList.clear();
			}
		}
	}

	/**
	 * Instantiates a new tracks panel.
	 *
	 * @param parent the parent
	 * @param tree the tree
	 * @param trackList the track list
	 */
	public TracksPanel(ModernRibbonWindow parent,
			ModernTree<Track> tree, 
			TrackTree trackList) {

		mParent = parent;
		mTree = tree;
		mTrackList = trackList;
		
		createUi();

		// Sync ui
		//mTrackList.getModel().fireDataChanged();
		mTrackList.fireTreeNodeChanged(new ChangeEvent(this));
	}

	/**
	 * Creates the ui.
	 */
	private void createUi() {
		//mTrackList.setBorder(ModernWidget.RIGHT_BORDER);
		ModernScrollPane scrollPane = new ModernScrollPane(mTrackList);
		scrollPane.setVerticalScrollBarPolicy(ScrollBarPolicy.AUTO_SHOW);
		scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
		scrollPane.setBorder(ModernWidget.DOUBLE_BORDER);

		setBody(scrollPane);
	}


	// Allow user to edit tracks

	/**
	 * Edits the tracks.
	 */
	protected void editTracks() {
		// tmp disable drag
		for (int i : mTrackList.getSelectionModel()) {
			TreeNode<Track> node = mTrackList.getSelectedNode(i); //getValueAt(i);

			Track track = node.getValue();

			track.edit(mParent);
		}

		// Notify listeners that the tracks may have changed.
		//mListModel.fireDataUpdated();
		
		mTrackList.fireTreeNodeUpdated();
	}


	/**
	 * Load tracks.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	protected void loadTracks() throws IOException {
		AnnotationTracksDialog dialog = new AnnotationTracksDialog(mParent,
				mTree);

		dialog.setVisible(true);

		if (dialog.getStatus() == ModernDialogStatus.CANCEL) {
			return;
		}

		List<TreeNode<Track>> tracks = new ArrayList<TreeNode<Track>>();

		for (Track track : dialog.getTracks()) {
			tracks.add(new TreeNode<Track>(track.getName(), track));
		}

		mTrackList.getRoot().addChildren(tracks);

		//mListModel.addValues(tracks);
	}

	/**
	 * Delete tracks.
	 */
	protected void deleteTracks() {
		ModernMessageDialog.createOkCancelWarningDialog(mParent,
				"Are you sure you want to delete the selected tracks?", 
				new DeleteEvents());
	}

	/**
	 * Clear tracks.
	 */
	protected void clearTracks() {
		ModernMessageDialog.createOkCancelWarningDialog(mParent,
				"Are you sure you want to delete all tracks?", 
				new ClearEvents());
	}

	/**
	 * Save xml view.
	 *
	 * @param file the file
	 * @param region the region
	 * @param width the width
	 * @param margin the margin
	 * @throws TransformerException the transformer exception
	 * @throws ParserConfigurationException the parser configuration exception
	 */
	public void saveXmlView(Path file, 
			GenomicRegion region,
			int width,
			int margin) throws TransformerException, ParserConfigurationException {
		if (file == null || region == null) {
			return;
		}

		Document doc = XmlUtils.createDoc();

		Element rootElement = doc.createElement("view");
		rootElement.setAttribute("location", region.getLocation());
		rootElement.setAttribute("width-px", Integer.toString(width));
		rootElement.setAttribute("margin-px", Integer.toString(margin));
		doc.appendChild(rootElement);

		//XmlElement tracksElement = new XmlElement("tracks");
		//rootElement.appendChild(tracksElement);

		// now append each track

		Deque<TreeNode<Track>> nodeQueue = new ArrayDeque<TreeNode<Track>>();
		Deque<Element> xmlQueue = new ArrayDeque<Element>();

		for (TreeNode<Track> child : CollectionUtils.reverse(mTrackList.getRoot())) {
			nodeQueue.push(child);
			xmlQueue.push(rootElement);
		}

		while (!nodeQueue.isEmpty()) {
			TreeNode<Track> node = nodeQueue.pop();
			Element xml = xmlQueue.pop();

			Element childXml = node.getValue().toXml(doc);

			xml.appendChild(childXml);

			for (TreeNode<Track> child : CollectionUtils.reverse(node)) {
				nodeQueue.push(child);
				xmlQueue.push(childXml);
			}
		}

		//for (Track track : mListModel) {
		//	tracksElement.appendChild(track.toXml());
		//}

		XmlUtils.writeXml(doc, file);
	}



	/**
	 * Sets the tracks.
	 *
	 * @param tracks the new tracks
	 */
	public void setTracks(List<Track> tracks) {
		mTrackList.clear();

		for (Track track : tracks) {
			mTrackList.getRoot().addChild(new TreeNode<Track>(track.getName(), track));
		}
	}

	/**
	 * Sets the tracks.
	 *
	 * @param tracks the new tracks
	 */
	public void setTracks(TreeRootNode<Track> tracks) {
		mTrackList.setRoot(tracks);
	}

	/**
	 * Gets the selected track.
	 *
	 * @return the selected track
	 */
	public Track getSelectedTrack() {
		return mTrackList.getSelectedNode().getValue();
	}

	/**
	 * Gets the selected tracks.
	 *
	 * @return the selected tracks
	 */
	public List<Track> getSelectedTracks() {
		return mTrackList.getSelectedValues();
	}

	/**
	 * Gets the tree.
	 *
	 * @return the tree
	 */
	public TrackTree getTree() {
		return mTrackList;
	}
}
