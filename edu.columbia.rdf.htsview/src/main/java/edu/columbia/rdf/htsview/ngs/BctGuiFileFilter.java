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

import java.io.File;
import java.io.FilenameFilter;

import org.jebtk.modern.io.GuiFileExtFilter;

// TODO: Auto-generated Javadoc
/**
 * The class BedGuiFileFilter.
 */
public class BctGuiFileFilter extends GuiFileExtFilter
    implements FilenameFilter {

  /**
   * Instantiates a new bed gui file filter.
   */
  public BctGuiFileFilter() {
    super("bct.json");
  }

  /*
   * (non-Javadoc)
   * 
   * @see javax.swing.filechooser.FileFilter#getDescription()
   */
  public final String getDescription() {
    return "BCT (*.bct.json)";
  }

  /*
   * (non-Javadoc)
   * 
   * @see java.io.FilenameFilter#accept(java.io.File, java.lang.String)
   */
  @Override
  public boolean accept(File arg0, String name) {
    return name.endsWith(".bct.json");
  }
}
