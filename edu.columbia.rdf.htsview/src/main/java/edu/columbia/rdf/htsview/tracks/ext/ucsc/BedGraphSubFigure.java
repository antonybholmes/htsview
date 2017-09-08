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
package edu.columbia.rdf.htsview.tracks.ext.ucsc;

import java.awt.Color;

import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.graphplot.plotbox.ApplyFunc;
import org.jebtk.graphplot.plotbox.PlotBox;

import edu.columbia.rdf.htsview.tracks.TitleProperties;
import edu.columbia.rdf.htsview.tracks.Track;
import edu.columbia.rdf.htsview.tracks.TrackSubFigure;

// TODO: Auto-generated Javadoc
/**
 * The Class BedGraphSubFigure.
 */
public class BedGraphSubFigure extends TrackSubFigure {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	//private UCSCTrack mBedGraph;

	/** The m style. */
	private PlotStyle mStyle = null;

	/** The style not set. */
	private boolean styleNotSet = false;

	/**
	 * Creates the.
	 *
	 * @param name the name
	 * @param style the style
	 * @param titlePosition the title position
	 * @return the bed graph sub figure
	 */
	public static BedGraphSubFigure create(String name,
			PlotStyle style,
			TitleProperties titlePosition) {
		BedGraphSubFigure subFigure = new BedGraphSubFigure();

		Axes axes = subFigure.currentAxes();
		
		Plot plot = new BedGraphPlot("BedGraph Plot 1"); //axes.getCurrentPlot();
		
		plot.getAllSeries().add(XYSeries.createXYSeries("Points", Color.BLACK));
		plot.getAllSeries().getCurrent().getMarker().setVisible(false);
		//plot.setStyle(style);
		
		

		axes.addChild(plot);

		Track.setTitle(name, titlePosition, axes);
		
		return subFigure;
	}
	
	//public void setBedGraph(UCSCTrack bedGraph) {
	//	mBedGraph = bedGraph;
	//}
	
	/* (non-Javadoc)
	 * @see edu.columbia.rdf.htsview.tracks.TrackSubFigure#update(org.jebtk.bioinformatics.genome.GenomicRegion, int, double, int, int, int, java.awt.Color, java.awt.Color, org.graphplot.figure.PlotStyle)
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
			
		//int start = displayRegion.getStart();
		
		//int end = displayRegion.getEnd();
		
		// The end cannot be equal to the start for display purposes
		//if (end == start) {
		//	++end;
		//}
		
		// set the graph limits and limit to the size of the chromosome
		//getCurrentAxes().getXAxis().setLimits(start, end);
		//getCurrentAxes().getXAxis().startEndTicksOnly();
		
		super.update(displayRegion, 
				resolution, 
				yMax, 
				width, 
				height, 
				margin, 
				lineColor, 
				fillColor,
				style);
		
		if (styleNotSet || mStyle != style) {
			setStyle(style);
			
			// Find all lines and make them antialiased
			applyByName(new ApplyFunc() {
				@Override
				public void apply(PlotBox plot) {
					plot.setAAMode(true);
				}},
					"Spline Line", 
					"Lines");
		
			mStyle = style;
			styleNotSet = false;
		}
		
		currentAxes().getX1Axis().startEndTicksOnly();
		
		//System.err.println("regions " + start + " " + end + " " + getCurrentAxes().toPlotX(end));
		
		//if (mBedGraph == null) {
		//	return;
		//}

		// Create a series for each bedgraph in the group
		
		Iterable<Plot> layers = currentAxes().getPlots();
		
		
		for (PlotBox layer : layers) {
			if (layer instanceof BedGraphPlot) {
				BedGraphPlot p = (BedGraphPlot)layer;
				
				p.update(displayRegion, 
						resolution, 
						yMax, 
						width, 
						height,
						margin,
						lineColor,
						fillColor,
						style);
			}
		}
		
		/*
		mPlot.setBarWidth(1);
		
		// Use the default series for plotting.
		XYSeries series = mPlot.getAllSeries().getCurrent();

		// Use the bedgraph to set the series color
		if (!lineColor.equals(series.getStyle().getLineStyle().getColor())) {
			series.getStyle().getLineStyle().setColor(lineColor);
		}
		
		if (!fillColor.equals(series.getStyle().getFillStyle().getColor())) {
			series.getStyle().getFillStyle().setColor(fillColor);
		}
		
		//series.addRegex("x");
		//series.addRegex("y");
		
		List<UCSCTrackRegion> regions = UCSCTrackRegions.getFixedGapSearch(mBedGraph.getRegions()).getFeatureSet(displayRegion.getChr(), 
				displayRegion.getStart(), 
				displayRegion.getEnd());
		
		mPlot.setMatrix(new BedGraphRegionMatrix(regions));
		
		for (MovableLayer l : plots) {
			Plot p = (Plot)l;
			
			System.err.println("sdfsdf " + p.getId() + " " + p.getName() + " " + (p.getMatrix() == null) + " " + p.getAllSeries().getCurrent().getColor());
		}
		*/
	}
}
