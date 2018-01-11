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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.UI;
import org.jebtk.modern.dialog.ModernDialogMultiCardWindow;
import org.jebtk.modern.dialog.ModernDialogTaskType;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.tree.ModernTree;
import org.jebtk.modern.window.ModernWindow;
import org.jebtk.modern.window.WindowWidgetFocusEvents;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotationTracksDialog.
 */
public class AnnotationTracksDialog extends ModernDialogMultiCardWindow {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  // private AnnotationTracksTreePanel mTracksPanel;

  /** The m tree. */
  private ModernTree<Track> mTree;

  /** The m boxes. */
  private List<AnnotationTracksBox> mBoxes = new ArrayList<AnnotationTracksBox>();

  /**
   * Instantiates a new annotation tracks dialog.
   *
   * @param parent the parent
   * @param tree the tree
   * @throws IOException Signals that an I/O exception has occurred.
   */
  public AnnotationTracksDialog(ModernWindow parent, ModernTree<Track> tree)
      throws IOException {
    super(parent, "Annotations", "htsview.annotations.help.url",
        ModernDialogTaskType.OK_CANCEL);

    mTree = tree;

    createUi();

    setup();
  }

  /**
   * Setup.
   */
  private void setup() {
    addWindowListener(new WindowWidgetFocusEvents(mOkButton));

    setResizable(true);

    setSize(640, 480);

    mTabsModel.changeTab(0);

    UI.centerWindowToScreen(this);

  }

  /**
   * Creates the ui.
   *
   * @throws IOException Signals that an I/O exception has occurred.
   */
  private final void createUi() throws IOException {
    for (TreeNode<Track> child : mTree) {
      AnnotationTracksBox box = new AnnotationTracksBox(child);

      ModernScrollPane scrollPane = new ModernScrollPane(box);
      scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);

      addTab(child.getName(), scrollPane);

      mBoxes.add(box);
    }
  }

  /**
   * Gets the tracks.
   *
   * @return the tracks
   */
  public List<Track> getTracks() {
    List<Track> tracks = new ArrayList<Track>();

    for (AnnotationTracksBox box : mBoxes) {
      tracks.addAll(box.getTracks());
    }

    return tracks;
  }
}
