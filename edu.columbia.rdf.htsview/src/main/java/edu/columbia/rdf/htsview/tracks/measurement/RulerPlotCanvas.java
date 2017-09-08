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
package edu.columbia.rdf.htsview.tracks.measurement;

import java.awt.Color;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.properties.MarginProperties;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;

// TODO: Auto-generated Javadoc
/**
 * The Class RulerPlotCanvas.
 */
public class RulerPlotCanvas extends MeasurementSubFigure {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/** The Constant MARGINS. */
	public static final MarginProperties MARGINS = 
			new MarginProperties(Track.MARGINS.getTop(), Track.MARGINS.getLeft(), Track.MEDIUM_MARGIN, Track.SMALL_MARGIN);

	/** The m layer. */
	private RulerCanvasLayer mLayer;
	
	/**
	 * Instantiates a new ruler plot canvas.
	 *
	 * @param titlePosition the title position
	 */
	public RulerPlotCanvas(TitleProperties titlePosition) {
		mLayer = new RulerCanvasLayer();
		
		currentAxes().getX1Axis().getTitle().setVisible(false);
		currentAxes().getY1Axis().setLimits(0, 1);
		
		currentAxes().addChild(mLayer);
		
		Track.setTitle("Ruler", titlePosition, currentAxes());
	}

	/**
	 * Creates the.
	 *
	 * @param titlePosition the title position
	 * @return the ruler plot canvas
	 */
	public static RulerPlotCanvas create(TitleProperties titlePosition) {

		//mBedGraphGroup = bedGraphGroup;
		//mGenomicModel = genomicModel;

		RulerPlotCanvas canvas = new RulerPlotCanvas(titlePosition);

		Axes axes = canvas.currentAxes();
		
		// set the graph limits
		axes.getX1Axis().getTitle().setVisible(false);
		axes.getX1Axis().startEndTicksOnly();

		axes.getY1Axis().getTitle().setVisible(false);
		axes.getY1Axis().startEndTicksOnly();

		axes.setInternalSize(Track.MEDIUM_TRACK_SIZE);
		//canvas.getGraphSpace().getLayoutProperties().setMargins(MARGINS);

		
		return canvas;
	}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.measurement.MeasurementSubFigure#update(org.jebtk.bioinformatics.genome.GenomicRegion, int, double, int, int, int, java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
	 */
	@Override
	public void update(GenomicRegion displayRegion, 
			int resolution,
			double yMax,
			int width,
			int height,
			int margin,
			Color lineColor,
			Color fillColor,
			PlotStyle style) {
		super.update(displayRegion, 
				resolution, 
				yMax, 
				width, 
				height, 
				margin, 
				lineColor, 
				fillColor,
				style);
		
		mLayer.update(displayRegion);
	}
}
