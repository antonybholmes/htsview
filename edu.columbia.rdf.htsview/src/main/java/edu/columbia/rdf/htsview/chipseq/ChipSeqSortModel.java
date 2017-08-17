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

import edu.columbia.rdf.edb.ui.SampleSortModel;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByDate;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByExperiment;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByGroup;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByName;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByOrganism;
import edu.columbia.rdf.edb.ui.sort.SortSamplesByPerson;


// TODO: Auto-generated Javadoc
/**
 * The Class ChipSeqSortModel.
 */
public class ChipSeqSortModel extends SampleSortModel {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * Instantiates a new chip seq sort model.
	 */
	public ChipSeqSortModel() {
		add(new SortSamplesByExperiment());
		add(new SortSamplesByName());
		add(new SortSamplesBySeqId());
		add(new SortSamplesByOrganism());
		
		add(new SortSamplesByCellType());
		add(new SortSamplesByClassification());
		add(new SortSamplesByTreatment());
		add(new SortSamplesByDate());
		
		add(new SortSamplesByGenome());
		add(new SortSamplesByPerson());
		add(new SortSamplesByGroup());
		
		setDefault("Sample Name");
	}
}
