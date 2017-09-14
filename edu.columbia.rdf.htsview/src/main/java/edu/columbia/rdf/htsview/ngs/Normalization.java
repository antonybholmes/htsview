package edu.columbia.rdf.htsview.ngs;

import java.util.ArrayList;
import java.util.List;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.math.matrix.AnnotatableMatrix;
import org.jebtk.math.matrix.AnnotationMatrix;
import org.jebtk.math.statistics.Statistics;

public class Normalization {
	/**
	 * Assuming each column represents a sample, calculate the geometric
	 * mean of each row and then use this to scale each sample's counts
	 * to normalize them for differential expression analysis. This is
	 * essentially the same technique that deseq2 uses.
	 * 
	 * @param m
	 * @return
	 */
	public static AnnotationMatrix medianRatios(final AnnotationMatrix m) {
		AnnotationMatrix ret = AnnotatableMatrix.createNumericalMatrix(m);
		
		List<Double> pseudoRef = new ArrayList<Double>(ret.getRowCount());
		
		for (int i = 0; i < m.getRowCount(); ++i) {
			pseudoRef.add(Statistics.geometricMean(m.rowAsDouble(i)));
		}
		
		List<Double> factors = new ArrayList<Double>(ret.getColumnCount());
		
		
		
		for (int i = 0; i < m.getColumnCount(); ++i) {
			List<Double> values = new ArrayList<Double>(ret.getRowCount());
			
			for (int j = 0; j < m.getRowCount(); ++j) {
				values.add(m.getValue(j, i) / pseudoRef.get(j));
			}
			
			double med = Statistics.median(values);
			
			if (med > 0) {
				factors.add(med);
			} else {
				factors.add(1.0);
			}
		}
		
		// Scale the counts
		for (int i = 0; i < m.getColumnCount(); ++i) {
			double f = factors.get(i);
			
			for (int j = 0; j < m.getRowCount(); ++j) {
				ret.set(j, i, m.getValue(j, i) / f);
			}
		}
		
		return ret;
	}
	
	public static AnnotationMatrix tpm(final AnnotationMatrix m,
			final List<GenomicRegion> locations) {
		AnnotationMatrix ret = AnnotatableMatrix.createNumericalMatrix(m);
		
		for (int i = 0; i < m.getColumnCount(); ++i) {
			// reads per kilobase
			List<Double> rpks = new ArrayList<Double>(ret.getRowCount());
			
			double factor = 0;
			
			for (int j = 0; j < m.getRowCount(); ++j) {
				
				// length in kb
				double l = locations.get(j).getLength() / 1000.0;
				
				double rpk = m.getValue(j, i) * l;
				
				rpks.add(rpk);
				factor += rpk;
			}
			
			for (int j = 0; j < m.getRowCount(); ++j) {
				// tpm
				ret.set(j, i, (rpks.get(j) / factor) * 1000000);
			}
		}
		
		return ret;
	}
	
	public static AnnotationMatrix rpm(final AnnotationMatrix m,
			final List<Integer> counts) {
		AnnotationMatrix ret = AnnotatableMatrix.createNumericalMatrix(m);
		
		for (int i = 0; i < m.getColumnCount(); ++i) {
			double f = 1000000.0 / counts.get(i);
			
			for (int j = 0; j < m.getRowCount(); ++j) {
				ret.set(j, i, m.getValue(j, i) * f);
			}
		}
		
		return ret;
	}
	
	public static AnnotationMatrix rpkm(final AnnotationMatrix m,
			final List<Integer> counts,
			final List<GenomicRegion> locations) {
		AnnotationMatrix ret = rpm(m, counts);
		
		for (int i = 0; i < m.getRowCount(); ++i) {
			double l = locations.get(i).getLength() / 1000.0;
			
			double f = 1.0 / l;
			
			for (int j = 0; j < m.getColumnCount(); ++j) {
				ret.set(j, i, m.getValue(j, i) * f);
			}
		}
		
		return ret;
	}
}
