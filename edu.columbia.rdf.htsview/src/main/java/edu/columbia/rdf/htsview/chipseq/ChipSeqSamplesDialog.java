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
package edu.columbia.rdf.htsview.chipseq;

import org.jebtk.modern.search.SearchModel;
import org.jebtk.modern.window.ModernWindow;

import edu.columbia.rdf.edb.ui.SamplesDialog;

// TODO: Auto-generated Javadoc
/**
 * The Class ChipSeqSamplesDialog.
 */
public class ChipSeqSamplesDialog extends SamplesDialog {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new chip seq samples dialog.
	 *
	 * @param parent the parent
	 */
	public ChipSeqSamplesDialog(ModernWindow parent) {
		this(parent, new SearchModel());
	}

	/**
	 * Instantiates a new chip seq samples dialog.
	 *
	 * @param parent the parent
	 * @param searchModel the search model
	 */
	public ChipSeqSamplesDialog(ModernWindow parent,
			SearchModel searchModel) {
		
		super(parent, 
				"ChIP-seq Samples", 
				"htsview.samples.help.url",
				"chipseq",
				new ChipSeqSortModel(),
				searchModel);
	}
}
