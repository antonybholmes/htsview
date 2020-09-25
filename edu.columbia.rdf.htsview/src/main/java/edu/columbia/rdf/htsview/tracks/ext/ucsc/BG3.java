package edu.columbia.rdf.htsview.tracks.ext.ucsc;

import java.awt.Color;
import java.nio.file.Path;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.BedGraph;
import org.jebtk.bioinformatics.genomic.GenomicElement;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.NameGetter;
import org.jebtk.core.text.TextUtils;

public class BG3 extends BedGraph implements NameGetter {

  public final Path file;

  public BG3(Path file, 
      Color color, 
      int height) {
    super(TextUtils.NA, TextUtils.NA, color, height);
    
    this.file = file;
  }

  public static BG3 parseBG3(Path file) {
    return new BG3(file, DEFAULT_BEDGRAPH_COLOR, DEFAULT_HEIGHT);
  }

  public String getName() {
    // TODO Auto-generated method stub
    return null;
  }

  public List<GenomicElement> getElements(GenomicRegion region) {
    // TODO Auto-generated method stub
    return null;
  }

  public Path getFile() {
    return file;
  }

  public BedGraph getBedGraph(GenomicRegion region) {
    return null;
  }

}
