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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.button.ModernCheckSwitch;
import org.jebtk.modern.dialog.ModernDialogTaskWindow;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.panel.ModernPanel;
import org.jebtk.modern.panel.VBox;
import org.jebtk.modern.widget.ModernTwoStateWidget;

// TODO: Auto-generated Javadoc
/**
 * The Class AnnotationTracksBox.
 */
public class AnnotationTracksBox extends VBox {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m check all. */
  private ModernTwoStateWidget mCheckAll = new ModernCheckSwitch("Select All");

  /** The m track map. */
  private Map<ModernTwoStateWidget, Track> mTrackMap = new HashMap<ModernTwoStateWidget, Track>();

  /**
   * The Class CheckAllEvents.
   */
  private class CheckAllEvents implements ModernClickListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
     * event. ModernClickEvent)
     */
    @Override
    public void clicked(ModernClickEvent e) {
      for (ModernTwoStateWidget c : mTrackMap.keySet()) {
        c.setSelected(mCheckAll.isSelected());
      }
    }

  }

  /**
   * Instantiates a new annotation tracks box.
   *
   * @param node the node
   */
  public AnnotationTracksBox(TreeNode<Track> node) {
    add(mCheckAll);

    add(ModernPanel.createVGap());

    for (TreeNode<Track> child : node) {
      if (child.getChildCount() == 0) {
        ModernTwoStateWidget checkBox = new ModernCheckSwitch(child.getName());

        // Ui.setSize(checkBox, ModernWidget.EXTRA_LARGE_SIZE);

        add(checkBox);

        add(ModernPanel.createVGap());

        mTrackMap.put(checkBox, child.getValue());
      } else {
        ModernDialogTaskWindow.midSectionHeader(child.getName(), this);

        for (TreeNode<Track> child2 : child) {
          ModernTwoStateWidget checkBox = new ModernCheckSwitch(
              child2.getName());

          // Ui.setSize(checkBox, ModernWidget.EXTRA_LARGE_SIZE);

          add(checkBox);

          add(ModernPanel.createVGap());

          mTrackMap.put(checkBox, child2.getValue());
        }
      }
    }

    mCheckAll.addClickListener(new CheckAllEvents());
  }

  /**
   * Gets the tracks.
   *
   * @return the tracks
   */
  public List<Track> getTracks() {
    List<Track> tracks = new ArrayList<Track>();

    for (ModernTwoStateWidget c : mTrackMap.keySet()) {
      if (c.isSelected()) {
        tracks.add(mTrackMap.get(c));
      }
    }

    return tracks;
  }
}
