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

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import org.jebtk.bioinformatics.genomic.ChromosomeSizes;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.bioinformatics.genomic.GenomicRegionModel;
import org.jebtk.core.ColorUtils;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.SubFigure;
import org.jebtk.graphplot.plotbox.PlotBox;
import org.jebtk.graphplot.plotbox.PlotBoxRow;
import org.jebtk.graphplot.plotbox.PlotBoxSubFigure;
import org.jebtk.modern.graphics.CanvasMouseEvent;
import org.jebtk.modern.graphics.DrawingContext;
import org.jebtk.modern.graphics.ModernCanvasMouseAdapter;
import org.jebtk.modern.theme.ModernTheme;
import org.jebtk.modern.theme.ThemeService;


// TODO: Auto-generated Javadoc
/**
 * The Class TracksFigure.
 */
public class TracksFigure extends PlotBoxRow { //Figure { // PlotBoxColumn {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;

	/**
	 * The smallest value the max y can be (has to be non-zero).
	 */
	public static final double MIN_MAX_Y = 0.1;

	/** The Constant SELECTION_COLOR. */
	private static final Color SELECTION_COLOR = 
			ThemeService.getInstance().colors().getColorHighlight(5);

	/** The Constant SELECTION_COLOR_TRANS. */
	private static final Color SELECTION_COLOR_TRANS = 
			ColorUtils.getTransparentColor75(SELECTION_COLOR);

	private static final int X_GAP =
			SettingsService.getInstance().getAsInt("sequencing.tracks.mouse.drag.x-gap");

	/** The m drag start. */
	private int mDragStart = -1;

	/** The m drag end. */
	private int mDragEnd = -1;

	/** The m dist. */
	private int mDist = -1;

	/** The m genomic model. */
	private GenomicRegionModel mGenomicModel;

	/** The m drag start region. */
	private GenomicRegion mDragStartRegion;

	/** The m tracks. */
	private TrackTree mTracks = new TrackTree();

	/** The m selection X. */
	private List<Integer> mSelectionX = new ArrayList<Integer>();

	/** The m sizes. */
	private ChromosomeSizes mSizes;

	private int mWidth;

	private int mDragBin = Integer.MIN_VALUE;


	/**
	 * The Class CanvasMouseEvents.
	 */
	private class CanvasMouseEvents extends ModernCanvasMouseAdapter {

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseAdapter#canvasMousePressed(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMousePressed(CanvasMouseEvent e) {
			//System.err.println("canvas x " + e.getX());

			int x = e.getScaledPos().getX();

			alterSelection(x);

			if (e.isControlDown()) {
				if (mSelectionX.size() < 2) {
					mSelectionX.add(Math.max(Track.LEFT_MARGIN, Math.min(Track.END, x)));
				}
			} else {
				mSelectionX.clear();
			}

			mDragStart = x;

			mDragStartRegion = mGenomicModel.get();

			//System.err.println(mSelectionX);

			fireCanvasRedraw();
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseAdapter#canvasMouseReleased(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseReleased(CanvasMouseEvent e) {
			mDragStart = -1;
			mDragEnd = -1;
		}

		/* (non-Javadoc)
		 * @see org.abh.common.ui.graphics.ModernCanvasMouseAdapter#canvasMouseDragged(org.abh.common.ui.graphics.CanvasMouseEvent)
		 */
		@Override
		public void canvasMouseDragged(CanvasMouseEvent e) {
			mDragEnd = e.getScaledPos().getX();

			mDist = mDragEnd - mDragStart;

			dragMove();
		}

	}

	/**
	 * Instantiates a new tracks figure.
	 *
	 * @param genomicModel the genomic model
	 * @param sizes the sizes
	 */
	public TracksFigure(GenomicRegionModel genomicModel,
			ChromosomeSizes sizes) {
		mGenomicModel = genomicModel;
		mSizes = sizes;

		//setLayout(new FigureLayoutVBox());

		//setMargins(50);

		//getGraphSpace().getGraphProperties().addChangeListener(new PlotEvents());

		// the default plot size
		//getSubPlotLayout().setPlotSize(new Dimension(1200, 100));
		//getSubPlotLayout().setMargins(50, 250, 50, 50);

		// Maximize the space, but allow a margin for drawing genes
		//getFigureProperties().setMargins(0, 0, 200, 0);

		// Don't draw grid lines
		//getFigureProperties().getXAxis().getGrid().setVisible(false);
		//getFigureProperties().getY1Axis().getGrid().setVisible(false);

		addCanvasMouseListener(new CanvasMouseEvents());
	}


	/**
	 * Alter selection.
	 *
	 * @param x the x
	 */
	private void alterSelection(int x) {
		if (mSelectionX.size() < 2) {
			return;
		}

		// adjust if we click inside the selected region
		if (x < mSelectionX.get(0) || x > mSelectionX.get(1)) {
			return;
		}

		GenomicRegion region = mGenomicModel.get();

		double p = Math.min(1, Math.max(0, (mSelectionX.get(0) - Track.LEFT_MARGIN) / (double)Track.PLOT_WIDTH));

		int start = region.getStart() + (int)(p * region.getLength());

		p =  Math.min(1, Math.max(0, (mSelectionX.get(1) - Track.LEFT_MARGIN) / (double)Track.PLOT_WIDTH));

		int end = region.getStart() + (int)(p * region.getLength());

		GenomicRegion newRegion = 
				new GenomicRegion(region.getChr(), start, end);

		mGenomicModel.set(newRegion);

		mSelectionX.clear();
	}

	/* (non-Javadoc)
	 * @see org.graphplot.figure.Figure#plot(java.awt.Graphics2D, org.abh.common.ui.graphics.DrawingContext)
	 */
	@Override
	public final void plot(Graphics2D g2, DrawingContext context) {
		super.plot(g2, context);

		if (mSelectionX.size() == 0) {
			return;
		}

		if (mSelectionX.size() > 1) {
			int minX = Math.min(mSelectionX.get(0), mSelectionX.get(1));
			int maxX = Math.max(mSelectionX.get(0), mSelectionX.get(1));

			g2.setColor(SELECTION_COLOR_TRANS);

			g2.fillRect(minX + 1, 0, maxX - minX - 1, getCanvasSize().getH());
		}

		g2.setStroke(ModernTheme.DASHED_LINE_STROKE);
		g2.setColor(SELECTION_COLOR);

		for (int x : mSelectionX) {
			//System.err.println(x + " " + getHeight());
			g2.drawLine(x, 0, x, getCanvasSize().getH());
		}

	}

	/**
	 * Drag move.
	 */
	private void dragMove() {
		int bin = mDist / X_GAP;

		if (bin == mDragBin) {
			return;
		}

		mDragBin = bin;

		double p = -mDist / (double)mWidth;

		int shift = (int)(p * mDragStartRegion.getLength());

		GenomicRegion newRegion = 
				GenomicRegion.shift(mDragStartRegion, shift, 10, mSizes);

		//System.err.println("new " + mDragStartRegion + " " + newRegion + " " + mDragStartRegion.getLength() + " " + p);

		mGenomicModel.set(newRegion);
	}

	/**
	 * Sets the tracks.
	 *
	 * @param tracks the tracks
	 * @param genome the genome
	 * @param style the style
	 * @param titlePosition the title position
	 * @throws Exception the exception
	 */
	public void setTracks(TrackTree tracks,
			String genome,
			PlotStyle style,
			TitleProperties titlePosition) throws Exception {
		mTracks = tracks;

		List<SubFigure> subFigures = new ArrayList<SubFigure>(100);

		//
		// Raw data tracks
		//

		for (TreeNode<Track> node : tracks) {
			Track track = node.getValue();

			//track.setStyle(style);

			TrackSubFigure trackSubFigure = track.createGraph(genome,
					titlePosition);

			Axes axes = trackSubFigure.getCurrentAxes();

			// If an axes object has previously been a child, reset the
			// properties so we can now see the axes
			Axes.enableAllFeatures(axes);

			axes.getX1Axis().getGrid().setVisible(false);
			axes.getX1Axis().getTitle().getFontStyle().setVisible(false);
			axes.getY1Axis().getGrid().setVisible(false);
			axes.getY1Axis().getTitle().getFontStyle().setVisible(false);
			axes.getY1Axis().startEndTicksOnly();

			axes.getX1Axis().getTicks().getMajorTicks().getFontStyle().setVisible(titlePosition.getPosition() != TitlePosition.COMPACT_RIGHT);

			List<TreeNode<Track>> children = node.getChildrenAsList();

			// Add all children as sub plots
			for (TreeNode<Track> child : children) {

				TrackSubFigure subFigure = child.getValue().createGraph(genome,
						titlePosition);

				// Stop sub titles appearing
				//f.getCurrentAxes().getTitle().getFontStyle().setVisible(false);

				//Axes.disableAllFeatures(f.getCurrentAxes());

				//trackFigure.getAxesZModel().setZ(subFigure.getCurrentAxes());

				// Add the plots from this sub figure to the axis of the
				// current figure
				axes.putZ(subFigure.getCurrentAxes().getCurrentPlot());
			}

			//
			// Set some properties based on the parent
			//

			//axes.getXAxis().getLineStyle().copy(axes.getXAxis().getLineStyle());
			//axes.getY1Axis().getLineStyle().copy(axes.getY1Axis().getLineStyle());

			//axes.getXAxis().getTitle().copy(axes.getXAxis().getTitle());
			//axes.getY1Axis().getTitle().copy(axes.getY1Axis().getTitle());

			//peakPlot.getGraphProperties().getXAxis().getMajorTicks().copy(gp.getXAxis().getMajorTicks());
			//peakPlot.getGraphProperties().getYAxis().getMajorTicks().copy(gp.getYAxis().getMajorTicks());

			//axes.getXAxis().getGrid().copy(axes.getXAxis().getGrid());
			//axes.getY1Axis().getGrid().copy(axes.getY1Axis().getGrid());

			//peakPlot.getGraphSpace().getLayoutProperties().setPlotSize(new Dimension(PLOT_WIDTH, 100));
			//peakPlot.getGraphSpace().getLayoutProperties().setMargins(50, 250, 50, 50);

			subFigures.add(trackSubFigure);
		}

		// TODO: do we need these?
		//getSubFigureZModel().clearUnreservedLayers();
		//getSubFigureZModel().putZ(subFigures);

		setSubFigures(subFigures);
	}

	/*
	public void refreshY(GenomicRegion displayRegion,
			int resolution,
			double yMax,
			boolean autoMaxY,
			boolean commonYScale,
			boolean normalize,
			int width,
			int height) throws IOException, ParseException  {
		if (autoMaxY && commonYScale) {
			yMax = getCommonYMax(mTracks, displayRegion, resolution, normalize);
		}

		//
		// Raw data tracks
		//

		TrackSubFigure subFigure;

		for (TreeNode<Track> track : mTracks) {

			// Update a new plot
			subFigure = track.getValue().updateGraph(displayRegion, 
					resolution, 
					normalize,
					width,
					height);

			// Set the Y max for each plot
			if (autoMaxY && !commonYScale) {
				yMax = getYMax(track, displayRegion, resolution, normalize);
			}

			for (int z : subFigure.getAxesZModel()) {
				Axes axes = subFigure.getAxesZModel().getAtZ(z);

				//
				// Set all plots to have the same y axis
				//

				// round y max up so it is aesthetically more pleasing.
				axes.getY1Axis().setLimits(0, Math.ceil(yMax));
				axes.getY1Axis().startEndTicksOnly();
			}

			//figure.setForwardCanvasEventsEnabled(true);
		}

		update(displayRegion, resolution, yMax, autoMaxY, commonYScale, normalize, width, height);



		fireCanvasRedraw();
	}
	 */

	/**
	 * Called when the location or scale changes, but not the actual
	 * tracks on display. This reduces re-laying out components on each
	 * redraw.
	 *
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @param width the width
	 * @param height the height
	 * @param margin the margin
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void refresh(GenomicRegion displayRegion,
			int resolution,
			int width,
			int height,
			int margin) throws IOException  {

		update(displayRegion, 
				resolution, 
				width, 
				height,
				margin);

		fireCanvasRedraw();
	}

	/**
	 * Update.
	 *
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @param width the width
	 * @param height the height
	 * @param margin the margin
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void update(GenomicRegion displayRegion,
			int resolution,
			int width,
			int height,
			int margin) throws IOException  {

		mWidth = width;

		//
		// Raw data tracks
		//

		for (TreeNode<Track> node : mTracks) {


			Track track = node.getValue();

			// Update a new plot
			track.updateGraph(displayRegion, 
					resolution,
					width,
					height,
					margin);

			List<TreeNode<Track>> children = node.getChildrenAsList();

			// update the children of this plot
			for (TreeNode<Track> child : children) {
				TrackSubFigure cf = child.getValue().updateGraph(displayRegion, 
						resolution,
						width,
						height,
						margin);

				cf.getCurrentAxes().getTitle().getFontStyle().setVisible(false);
			}
		}

		double autoYMax = 
				getCommonYMax(mTracks, displayRegion, resolution, false);

		double autoNormYMax = 
				getCommonYMax(mTracks, displayRegion, resolution, true);

		double y;


		for (TreeNode<Track> node : mTracks) {

			Track track = node.getValue();

			if (track.getCommonY()) {
				if (track.getNormalizeY()) {
					y = autoNormYMax;
				} else {
					y = autoYMax;
				}
			} else {
				y = getYMax(node, displayRegion, resolution, track.getNormalizeY());
			}

			// Update a new plot
			TrackSubFigure subFigure = track.getGraph();

			// Set the Y max for each plot
			//if (auto && !common) {
			//	y = getYMax(node, displayRegion, resolution, norm);
			//}

			/*
			} else {
				if (track.getAutoY()) {
					yMax = getYMax(node, displayRegion, resolution, track.getNormalizeY());
				} else {
					yMax = track.getYMax();
				}
			}
			 */

			//System.err.println("ymax " + yMax + " " + " " + auto + " " + common + " " + norm);

			List<TreeNode<Track>> children = node.getChildrenAsList();

			// update the children of this plot
			for (TreeNode<Track> child : children) {
				TrackSubFigure cf = child.getValue().updateGraph(displayRegion, 
						resolution,
						width,
						height,
						margin);

				cf.getCurrentAxes().getTitle().getFontStyle().setVisible(false);
			}

			for (int z : subFigure.getGridLocations().get(GridLocation.CENTER)) {
				Axes axes = (Axes)subFigure.getGridLocations().getAtZ(z);

				//
				// Set all plots to have the same y axis
				//

				// round y max up so it is aesthetically more pleasing.

				if (y > 1) {
					axes.getY1Axis().setLimits(0, Math.ceil(y));
				}

				axes.getY1Axis().startEndTicksOnly();
			}
		}
	}

	/**
	 * Set the font in all sub figures.
	 * @param font
	 * @param color
	 */
	public void setFont(Font font, Color color) {

		Deque<PlotBox> stack = new ArrayDeque<PlotBox>();

		stack.push(this);

		while (!stack.isEmpty()) {
			PlotBox plot = stack.pop();

			if (plot instanceof PlotBoxSubFigure) {
				((PlotBoxSubFigure)plot).getSubFigure().setFont(font, color);
			}

			for (PlotBox child : this) {
				stack.push(child);
			}
		}

		fireCanvasRedraw();
	}

	//
	// Static methods
	//

	/**
	 * Work out the max y of the plots for common scales.
	 *
	 * @param tracks the tracks
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @param normalize the normalize
	 * @return the common Y max
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static double getCommonYMax(TrackTree tracks,
			GenomicRegion displayRegion,
			int resolution,
			boolean normalize) throws IOException {
		double y = 0;

		for (TreeNode<Track> track : tracks) {
			double tmpy = getYMax(track, displayRegion, resolution, normalize);

			if (tmpy > y) {
				y = tmpy;
			}
		}

		y = Math.max(MIN_MAX_Y, y);

		//System.err.println("max auto y " + y);

		return y;
	}

	/**
	 * Gets the y max.
	 *
	 * @param track the track
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @param normalize the normalize
	 * @return the y max
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private static double getYMax(TreeNode<Track> track,
			GenomicRegion displayRegion,
			int resolution,
			boolean normalize) throws IOException {
		double max = 0;
		double y;

		List<TreeNode<Track>> children = track.getChildrenAsList();

		children.add(track);

		for (TreeNode<Track> child : children) {
			y = child.getValue().getYMax(normalize);

			if (y > max) {
				max = y;
			}

			/*
			UCSCTrack bedGraph = child.getValue().getBedGraph(displayRegion, resolution, normalize);

			// This track is not a bedgraph
			if (bedGraph == null) {
				return MIN_MAX_Y;
			}


			List<UCSCTrackRegion> regions = 
					UCSCTrackRegions.getFixedGapSearch(bedGraph.getRegions()).getFeatureSet(displayRegion.getChr(), displayRegion.getStart(), displayRegion.getEnd());

			if (regions != null) {
				for (UCSCTrackRegion region : regions) {
					double value = ((BedGraphRegion)region).getValue();

					if (value > y) {
						y = value;
					}
				}
			}

			y = Math.max(MIN_MAX_Y, y);
			 */
		}

		//System.err.println("y max " + max);

		max = Math.max(MIN_MAX_Y, max);

		// Add a buffer to account for splines that go higher than
		// y max.
		max *= 1.2;

		return max;
	}
}
