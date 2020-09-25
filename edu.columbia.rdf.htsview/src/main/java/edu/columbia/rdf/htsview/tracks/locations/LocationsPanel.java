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
package edu.columbia.rdf.htsview.tracks.locations;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.nio.file.Path;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.KeyStroke;

import org.jebtk.bioinformatics.genomic.Chromosome;
import org.jebtk.bioinformatics.genomic.ChromosomeService;
import org.jebtk.bioinformatics.genomic.GenesService;
import org.jebtk.bioinformatics.genomic.Genome;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.bioinformatics.genomic.GenomicType;
import org.jebtk.bioinformatics.ui.GenomeModel;
import org.jebtk.core.collections.CollectionUtils;
import org.jebtk.core.event.ChangeEvent;
import org.jebtk.core.event.ChangeListener;
import org.jebtk.core.text.TextUtils;
import org.jebtk.math.external.microsoft.Excel;
import org.jebtk.math.ui.external.microsoft.ExcelDialog;
import org.jebtk.modern.AssetService;
import org.jebtk.modern.ModernComponent;
import org.jebtk.modern.button.ModernButton;
import org.jebtk.modern.dialog.DialogEvent;
import org.jebtk.modern.dialog.DialogEventListener;
import org.jebtk.modern.dialog.ModernDialogStatus;
import org.jebtk.modern.dialog.ModernMessageDialog;
import org.jebtk.modern.event.ModernClickEvent;
import org.jebtk.modern.event.ModernClickListener;
import org.jebtk.modern.event.ModernSelectionListener;
import org.jebtk.modern.graphics.icons.FolderBwVectorIcon;
import org.jebtk.modern.io.RecentFilesService;
import org.jebtk.modern.panel.HBox;
import org.jebtk.modern.ribbon.RibbonButton;
import org.jebtk.modern.scrollpane.ModernScrollPane;
import org.jebtk.modern.scrollpane.ScrollBarPolicy;
import org.jebtk.modern.window.ModernRibbonWindow;

/**
 * The Class LocationsPanel.
 */
public class LocationsPanel extends ModernComponent
    implements ModernClickListener {

  /** The Constant serialVersionUID. */
  private static final long serialVersionUID = 1L;

  /** The m location list. */
  private LocationList mLocationList = new LocationList();

  /** The m list model. */
  private LocationListModel mListModel = new LocationListModel();

  /** The m open button. */
  private ModernButton mOpenButton = new RibbonButton(
      AssetService.getInstance().loadIcon(FolderBwVectorIcon.class, 16));

  /** The m delete button. */
  private ModernButton mDeleteButton = new RibbonButton(
      AssetService.getInstance().loadIcon("trash_bw", 16));

  /** The m model. */
  private GenomicRegionModel mModel;

  /** The m parent. */
  private ModernRibbonWindow mParent;

  /** The m genome model. */
  private GenomeModel mGenomeModel;

  /** The m used. */
  private Set<String> mUsed = new HashSet<String>();

  /**
   * The Class DeleteEvents.
   */
  private class DeleteEvents implements DialogEventListener {

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.abh.common.ui.dialog.DialogEventListener#statusChanged(org.abh.common
     * .ui. dialog.DialogEvent)
     */
    @Override
    public void statusChanged(DialogEvent e) {
      if (e.getStatus() == ModernDialogStatus.OK) {
        ArrayList<Integer> indices = new ArrayList<Integer>();

        for (int i : mLocationList.getSelectionModel()) {
          indices.add(i);

          mUsed.remove(mLocationList.getValueAt(i));
        }

        mListModel.removeValuesAt(indices);
      }
    }
  }

  /**
   * The Class PasteAction.
   */
  private class PasteAction extends AbstractAction {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;

    /*
     * (non-Javadoc)
     * 
     * @see
     * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      
      List<String> str = TextUtils.tabSplit(AssetService.getClipboardText()
          .replaceAll("[\\r\\n]+", TextUtils.TAB_DELIMITER));
      
      String[] ret = new String[str.size()];
      
      str.toArray(ret);
      
      loadLocations(ret);
    }

  }

  /**
   * The Class SelectionEvents.
   */
  private class SelectionEvents implements ModernSelectionListener {

    @Override
    public void selectionAdded(ChangeEvent e) {
      GenomicRegion region = parse(mGenomeModel.get(),
          mLocationList.getSelectedItem());

      if (region != null) {
        mModel.set(region);
      }
    }

    @Override
    public void selectionRemoved(ChangeEvent e) {
      // TODO Auto-generated method stub

    }
  }

  /**
   * Instantiates a new locations panel.
   *
   * @param parent the parent
   * @param genomeModel the genome model
   * @param model the model
   */
  public LocationsPanel(ModernRibbonWindow parent, GenomeModel genomeModel,
      GenomicRegionModel model) {

    mParent = parent;
    mGenomeModel = genomeModel;
    mModel = model;

    setup();

    createUi();

    mLocationList.addSelectionListener(new SelectionEvents());

    // mModel.addChangeListener(new GenomicEvents());

    getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(
        KeyStroke.getKeyStroke(KeyEvent.VK_V, InputEvent.CTRL_MASK),
        "paste");

    getActionMap().put("paste", new PasteAction());

    mModel.addChangeListener(new ChangeListener() {

      @Override
      public void changed(ChangeEvent e) {
        refresh();
      }
    });
  }

  /**
   * Creates the ui.
   */
  public void createUi() {
    Box box = HBox.create();

    mOpenButton.setToolTip("Load", "Load locations from a file.");
    box.add(mOpenButton);
    box.add(createHGap());
    mDeleteButton.setToolTip("Delete", "Delete selected locations.");
    box.add(mDeleteButton);
    box.setBorder(BOTTOM_BORDER);

    setHeader(box);

    ModernScrollPane scrollPane = new ModernScrollPane(mLocationList);
    scrollPane.setHorizontalScrollBarPolicy(ScrollBarPolicy.NEVER);
    // scrollPane.setBorder(LARGE_BORDER);
    setBody(scrollPane);

    setBorder(LARGE_BORDER);

    /*
     * Box box = new ToolbarBottomBox();
     * 
     * mSamplesButton.setToolTip("Samples Database",
     * "Load ChIP-seq samples from database."); box.add(mSamplesButton);
     * box.add(ModernTheme.createHorizontalGap());
     * mTracksButton.setToolTip("Load Annotation Tracks",
     * "Load additional annotation tracks."); box.add(mTracksButton);
     * box.add(ModernTheme.createHorizontalGap());
     * mEditButton.setToolTip("Edit Tracks", "Edit track properties.");
     * box.add(mEditButton); box.add(ModernTheme.createHorizontalGap());
     * //box.add(Box.createHorizontalGlue()); mDeleteButton.setToolTip("Delete",
     * "Delete selected tracks."); box.add(mDeleteButton);
     * //box.add(ModernTheme.createHorizontalGap());
     * //mClearButton.setToolTip("Clear", "Remove all tracks.");
     * //box.add(mClearButton);
     * 
     * add(box, BorderLayout.PAGE_END);
     */

  }

  /**
   * Setup.
   */
  private void setup() {
    mLocationList.setModel(mListModel);

    mOpenButton.addClickListener(this);
    mDeleteButton.addClickListener(this);
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.abh.common.ui.event.ModernClickListener#clicked(org.abh.common.ui.
   * event. ModernClickEvent)
   */
  @Override
  public void clicked(ModernClickEvent e) {
    if (e.getSource().equals(mOpenButton)) {
      try {
        browseForFile();
      } catch (Exception e1) {
        e1.printStackTrace();
      }
    } else if (e.getSource().equals(mDeleteButton)) {
      deleteTracks();
    } else {
      // do nothing
    }
  }

  /**
   * Refresh.
   */
  private void refresh() {
    addValue(mModel.get().getLocation());
  }

  /**
   * Load locations.
   *
   * @param entries the entries
   */
  private void loadLocations(String[] entries) {

    // for (String location : entries) {
    // GenomicRegion region = GenomicRegion.parse(location);
    //
    // if (region != null) {
    // mLocationsModel.add(region);
    // }
    // }

    addValues(entries); // ArrayUtils.toString(ArrayUtils.sort(mLocationsModel)));
  }

  /**
   * Adds the values.
   *
   * @param locations the locations
   */
  private void addValues(String[] locations) {
    for (String l : locations) {
      addValue(l);
    }

  }

  /**
   * Adds the value.
   *
   * @param location the location
   */
  private void addValue(String location) {
    if (!mUsed.contains(location)) {
      mListModel.addValue(location);
      mUsed.add(location);
    }
  }

  /**
   * Delete tracks.
   */
  private void deleteTracks() {
    ModernMessageDialog.createOkCancelWarningDialog(mParent,
        "Are you sure you want to delete the selected locations?",
        new DeleteEvents());
  }

  /**
   * Browse for file.
   *
   * @throws Exception the exception
   */
  public void browseForFile() throws Exception {
    browseForFile(RecentFilesService.getInstance().getPwd());
  }

  /**
   * Browse for file.
   *
   * @param pwd the pwd
   * @throws Exception the exception
   */
  public void browseForFile(Path pwd) throws Exception {
    openFiles(ExcelDialog.open(mParent).xlsx().getFiles(pwd));
  }

  /**
   * Open files.
   *
   * @param files the files
   * @throws Exception the exception
   */
  public void openFiles(List<Path> files) throws Exception {
    if (CollectionUtils.isNullOrEmpty(files)) {
      return;
    }

    for (Path file : files) {
      String[] lines = Excel.getTextFromFile(file, true);

      addValues(lines);

      // for (String location : lines) {
      // GenomicRegion region = GenomicRegion.parse(location);
      //
      // if (region != null) {
      // locations.add(region);
      // }
      // }
    }

    // mListModel.addValues(locations);

    // mListModel.addValues(ArrayUtils.toString(ArrayUtils.sort(mLocationsModel)));
  }

  /**
   * Parses the.
   *
   * @param text the text
   * @return the genomic region
   * @throws ParseException the parse exception
   */
  private GenomicRegion parse(Genome genome, String text) {
    if (TextUtils.isNullOrEmpty(text)) {
      return null;
    }

    GenomicRegion region = null;

    // Genome genome = mGenomeModel.get();

    if (text.matches("^chr(\\d+|[xymXYM])$")) {
      // use the whole chromosome

      System.err.println("login panel");

      Chromosome chr = ChromosomeService.getInstance().chr(genome, text);

      int size = ChromosomeService.getInstance().size(genome, chr);

      region = new GenomicRegion(chr, 1, size);

    } else if (text.startsWith("chr")) { // remove commas
      region = GenomicRegion.parse(genome, text);

      // Make sure region is within the bounds of the chromosome

      int size = ChromosomeService.getInstance().size(genome, region.mChr);

      region = new GenomicRegion(region.mChr,
          Math.max(1, region.mStart), Math.min(region.mEnd, size));

    } else {
      // assume its a gene

      Iterable<Genome> dbs = GenesService.getInstance()
          .getGeneDbs(genome.getAssembly());

      // Pick the first (essentially at random).
      Genome g = dbs.iterator().next();

      try {
        region = GenesService.getInstance().getGenes(g).getElement(g, text, GenomicType.TRANSCRIPT);
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return region;
  }
}
