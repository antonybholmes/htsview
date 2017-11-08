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
import java.awt.Graphics2D;
import java.util.List;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrackRegion;
import org.jebtk.bioinformatics.ext.ucsc.UCSCTrackRegions;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.Figure;
import org.jebtk.graphplot.figure.Plot;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.figure.series.XYSeries;
import org.jebtk.modern.graphics.DrawingContext;

// TODO: Auto-generated Javadoc
/**
 * The Class BedGraphPlot.
 */
public class BedGraphPlot extends Plot {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The m bed graph. */
	private UCSCTrack mBedGraph;

	/**
	 * Instantiates a new bed graph plot.
	 *
	 * @param name the name
	 */
	public BedGraphPlot(String name) {
		super(name);
		
		//setAAMode(true);
	}

	/**
	 * Sets the bed graph.
	 *
	 * @param bedGraph the new bed graph
	 */
	public void setBedGraph(UCSCTrack bedGraph) {
		mBedGraph = bedGraph;
	}
	
	/**
	 * Update.
	 *
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @param yMax the y max
	 * @param width the width
	 * @param height the height
	 * @param margin the margin
	 * @param lineColor the line color
	 * @param fillColor the fill color
	 * @param style the style
	 */
	public void update(GenomicRegion displayRegion, 
			int resolution,
			double yMax,
			int width,
			int height,
			int margin,
			Color lineColor, 
			Color fillColor,
			PlotStyle style) {
		
		//System.err.println("regions " + start + " " + end + " " + getCurrentAxes().toPlotX(end));
		
		if (mBedGraph == null) {
			return;
		}

		// Create a series for each bedgraph in the group
		
		setBarWidth(1);
		
		// Use the default series for plotting.
		XYSeries series = getAllSeries().getCurrent();

		// Use the bedgraph to set the series color
		//System.err.println(lineColor + " " + getName() + " " + series.getName() + " " + series.getStyle().getLineStyle().getColor());
		
		if (lineColor == null || !lineColor.equals(series.getStyle().getLineStyle().getColor())) {
			series.getStyle().getLineStyle().setColor(lineColor);
		}
		
		if (fillColor == null || !fillColor.equals(series.getStyle().getFillStyle().getColor())) {
			series.getStyle().getFillStyle().setColor(fillColor);
		}
		
		//series.addRegex("x");
		//series.addRegex("y");
		
		List<UCSCTrackRegion> regions = UCSCTrackRegions.getFixedGapSearch(mBedGraph.getRegions()).getFeatureSet(displayRegion);
		
		setMatrix(new BedGraphRegionMatrix(regions));
	}
	
	/* (non-Javadoc)
	@Override
	public void drawPlot(Graphics2D g2, 
			DrawingContext context,
			Figure figure, 
			SubFigure subFigure, 
			Axes axes) {
		aaPlot(g2, context, figure, subFigure, axes);
	}
	*/
}
