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
import java.awt.Dimension;
import java.io.IOException;
import java.text.ParseException;

import org.jebtk.bioinformatics.ext.ucsc.UCSCTrack;
import org.jebtk.bioinformatics.genomic.Dna;
import org.jebtk.bioinformatics.genomic.GenomicRegion;
import org.jebtk.core.Mathematics;
import org.jebtk.core.event.ChangeListeners;
import org.jebtk.core.json.JsonBuilder;
import org.jebtk.core.json.ToJson;
import org.jebtk.core.settings.SettingsService;
import org.jebtk.core.text.Formatter;
import org.jebtk.core.xml.XmlRepresentation;
import org.jebtk.graphplot.figure.Axes;
import org.jebtk.graphplot.figure.GridLocation;
import org.jebtk.graphplot.figure.PlotStyle;
import org.jebtk.graphplot.figure.TitleRightPlotLayer;
import org.jebtk.graphplot.figure.properties.MarginProperties;
import org.jebtk.modern.window.ModernWindow;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

// TODO: Auto-generated Javadoc
/**
 * The Class Track.
 */
public abstract class Track extends ChangeListeners implements Comparable<Track>, XmlRepresentation, ToJson {
	
	private static final long serialVersionUID = 1L;

	/** The Constant PLOT_WIDTH. */
	public static final int PLOT_WIDTH = 
			SettingsService.getInstance().getAsInt("htsview.plot.width");

	/** The Constant SMALL_MARGIN. */
	public static final int SMALL_MARGIN = 5;

	/** The Constant MARGIN. */
	public static final int MARGIN = 10;

	/** The Constant MEDIUM_MARGIN. */
	public static final int MEDIUM_MARGIN = 50;

	/** The Constant LARGE_MARGIN. */
	public static final int LARGE_MARGIN = 100;

	/** The Constant EXTRA_LARGE_MARGIN. */
	public static final int EXTRA_LARGE_MARGIN = 200;

	/** The Constant HUGE_MARGIN. */
	public static final int HUGE_MARGIN = 400;

	/** The Constant LEFT_MARGIN. */
	public static final int LEFT_MARGIN = HUGE_MARGIN;

	/** The Constant RIGHT_MARGIN. */
	public static final int RIGHT_MARGIN = EXTRA_LARGE_MARGIN;

	/** The Constant MARGINS. */
	public static final MarginProperties MARGINS = 
			new MarginProperties(MEDIUM_MARGIN, 
					LEFT_MARGIN, 
					MEDIUM_MARGIN, 
					RIGHT_MARGIN);


	/** The Constant SMALL_TRACK_SIZE. */
	public static final Dimension SMALL_TRACK_SIZE = 
			new Dimension(Track.PLOT_WIDTH, 24);

	/** The Constant MEDIUM_TRACK_SIZE. */
	public static final Dimension MEDIUM_TRACK_SIZE = 
			new Dimension(Track.PLOT_WIDTH, MEDIUM_MARGIN);

	/** The Constant END. */
	public static final int END = LEFT_MARGIN + PLOT_WIDTH;


	/**
	 * Gets the type.
	 *
	 * @return the type
	 */
	public abstract String getType();

	/**
	 * Should return the name of the track so users can see what they are
	 * looking at.
	 *
	 * @return the name
	 */
	public abstract String getName();


	/**
	 * Sets the name.
	 *
	 * @param name the new name
	 */
	public void setName(String name) {
		// do nothing
	}

	/**
	 * Set the track color.
	 *
	 * @param color the new line color
	 */
	public void setLineColor(Color color) {
		// do nothing
	}

	/**
	 * Sets the fill color.
	 *
	 * @param color the new fill color
	 */
	public void setFillColor(Color color) {
		// do nothing
	}

	/**
	 * Get the track color.
	 *
	 * @return the fill color
	 */
	public Color getFillColor() {
		return Color.GRAY;
	}

	/**
	 * Gets the line color.
	 *
	 * @return the line color
	 */
	public Color getLineColor() {
		return Color.GRAY;
	}

	/**
	 * Sets the height.
	 *
	 * @param height the new height
	 */
	public void setHeight(int height) {
		// do nothing
	}

	/**
	 * Get the track height.
	 *
	 * @return the height
	 */
	public int getHeight() {
		return -1;
	}

	/**
	 * Set all the y properties at once using an axis limits model.
	 * Equivalent to calling:
	 * 
	 * setAutoY(...);
	 * setNormalizeY(...);
	 * setCommonY(...);
	 * setYMax(...);
	 *
	 * @param model the new y properties
	 */
	public void setYProperties(AxisLimitsModel model) {
		setAutoY(model.getAutoSetLimits());
		setNormalizeY(model.getNormalize());
		setCommonY(model.getCommonScale());
		setYMax(model.getMax());
	}

	/**
	 * Set the maximum y to show on the y axis.
	 *
	 * @param yMax the new y max
	 */
	public void setYMax(double yMax) {
		// Do nothing
	}

	/**
	 * Returns the maximum y to show on the y axis.
	 *
	 * @param normalize the normalize
	 * @return the y max
	 */
	public double getYMax(boolean normalize) {
		return -1;
	}

	/**
	 * Should return true if the y axis should be autoscaled.
	 *
	 * @return the auto Y
	 */
	public boolean getAutoY() {
		return false;
	}

	/**
	 * If set to true, applicable graphs will automatically determine a
	 * suitable y value for the display. This will be dynamic and change
	 * with the data currently being displayed.
	 *
	 * @param autoY the new auto Y
	 */
	public void setAutoY(boolean autoY) {
		// Do nothing
	}

	/**
	 * Should return true if the y axis should display normalized counts.
	 *
	 * @return the normalize Y
	 */
	public boolean getNormalizeY() {
		return false;
	}

	/**
	 * Sets the normalize Y.
	 *
	 * @param normalize the new normalize Y
	 */
	public void setNormalizeY(boolean normalize) {
		// Do nothing
	}

	/**
	 * Gets the common Y.
	 *
	 * @return the common Y
	 */
	public boolean getCommonY() {
		return false;
	}

	/**
	 * Sets the common Y.
	 *
	 * @param mCommon the new common Y
	 */
	public void setCommonY(boolean mCommon) {
		// Do nothing
	}

	/**
	 * Should return true if the plot allows itself to be autosized.
	 *
	 * @return the common height
	 */
	public boolean getCommonHeight() {
		return false;
	}

	/**
	 * Sets the common height.
	 *
	 * @param mCommon the new common height
	 */
	public void setCommonHeight(boolean mCommon) {
		// Do nothing
	}

	/**
	 * Sets the style.
	 *
	 * @param style the new style
	 */
	public void setStyle(PlotStyle style) {
		// Do nothing
	}

	/**
	 * Gets the style.
	 *
	 * @return the style
	 */
	public PlotStyle getStyle() {
		return null;
	}

	/**
	 * Should return a graph canvas representation of the underlying
	 * track data so it can be displayed graphically.
	 *
	 * @param genome the genome
	 * @param titlePosition the title position
	 * @return the track sub figure
	 * @throws Exception the exception
	 */
	public abstract TrackSubFigure createGraph(String genome,
			TitleProperties titlePosition) throws Exception;

	/**
	 * Called when the display region is changed.
	 *
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @param width the width
	 * @param height the height
	 * @param margin the margin
	 * @return the track sub figure
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public abstract TrackSubFigure updateGraph(GenomicRegion displayRegion, 
			int resolution,
			int width,
			int height,
			int margin) throws IOException;
	
	/**
	 * Should return the current graph without any modifications.
	 *
	 * @return the graph
	 */
	public abstract TrackSubFigure getGraph();

	/**
	 * Gets the bed graph.
	 *
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @return the bed graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ParseException the parse exception
	 */
	public UCSCTrack getBedGraph(GenomicRegion displayRegion, 
			int resolution) throws IOException {
		return getBedGraph(displayRegion, resolution, true);
	}

	/**
	 * Should return a BedGraph representation of the track data.
	 *
	 * @param displayRegion the display region
	 * @param resolution the resolution
	 * @param normalize the normalize
	 * @return the bed graph
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public UCSCTrack getBedGraph(GenomicRegion displayRegion, 
			int resolution,
			boolean normalize) throws IOException {
		return null;
	}

	/**
	 * Should enable a UI dialog or similar to allow the track to be
	 * edited.
	 *
	 * @param parent the parent
	 */
	public void edit(ModernWindow parent) {
		// do nothing
	}

	/**
	 * Right title width.
	 *
	 * @param name the name
	 * @return the int
	 */
	public static int rightTitleWidth(String name) {
		return TitleRightPlotLayer.OFFSET + name.length() * 15;
	}

	/**
	 * Create a human readable version of a bp measure.
	 *
	 * @param bp the bp
	 * @return the string
	 */
	public static String formatBp(int bp) {
		if (bp < Dna.KILOBASE) {
			return Formatter.number().format(bp) + " bp";
		} else if (bp < Dna.MEGABASE) {
			return Mathematics.round((double)bp / Dna.KILOBASE, 3) + " kb";
		} else {
			return Mathematics.round((double)bp / Dna.MEGABASE, 3) + " Mb";
		}
	}
	
	/**
	 * Sets the margins.
	 *
	 * @param name the name
	 * @param titlePosition the title position
	 * @param subFigure the sub figure
	 */
	protected static void setMargins(String name, 
			TitleProperties titlePosition,
			TrackSubFigure subFigure) {
		switch(titlePosition.getPosition()) {
		case RIGHT:
		case COMPACT_RIGHT:
			setSmallMargins(name, subFigure);
			break;
		default:
			setStandardMargins(name, subFigure);
			break;
		}
	}

	/**
	 * Sets the title.
	 *
	 * @param name the name
	 * @param titlePosition the title position
	 * @param axes the axes
	 */
	public static void setTitle(String name, 
			TitleProperties titlePosition,
			Axes axes) {
		axes.getTitle().setText(name);

		// Remove any existing titles
		//axes.removeByName("Axes Title");

		if (titlePosition.getVisible()) {
			switch(titlePosition.getPosition()) {
			case RIGHT:
			case COMPACT_RIGHT:
				//axes.addChild(new TitleRightPlotLayer());
				axes.getTitle().setPosition(GridLocation.E);
				break;
			default:
				//axes.addChild(new AxesTitleLayer());
				axes.getTitle().setPosition(GridLocation.N);
				break;
			}
		}
	}

	/**
	 * Sets the standard margins.
	 *
	 * @param name the name
	 * @param mPlot the m plot
	 */
	protected static void setStandardMargins(String name, 
			TrackSubFigure mPlot) {
		mPlot.currentAxes().setMargins(MARGINS);

		System.err.println("standard margins " + mPlot.getName() + " " + mPlot.getMargins());
	}

	/**
	 * Sets the small margins.
	 *
	 * @param name the name
	 * @param mPlot the m plot
	 */
	protected static void setSmallMargins(String name, 
			TrackSubFigure mPlot) {
		int right = rightTitleWidth(name);

		mPlot.currentAxes().setMargins(SMALL_MARGIN, 
				MARGINS.getLeft(), 
				SMALL_MARGIN, 
				right);

		System.err.println("small margins " + mPlot.getName() + " " + mPlot.currentAxes().getMargins());
	}
	
	/**
	 * Sets the small margins.
	 *
	 * @param name the name
	 * @param axes the axes
	 */
	protected static void setSmallMargins(String name, 
			Axes axes) {
		int right = rightTitleWidth(name);

		axes.setMargins(SMALL_MARGIN, 
				MARGINS.getLeft(), 
				SMALL_MARGIN, 
				right);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.json.ToJson#toJson(org.abh.common.json.JsonBuilder)
	 */
	@Override
	public void toJson(JsonBuilder json) {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.abh.common.xml.XmlRepresentation#toXml(org.w3c.dom.Document)
	 */
	@Override
	public Element toXml(Document doc) {
		return null;
	}

	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	@Override
	public int compareTo(Track track) {
		return getName().compareTo(track.getName());
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object o) {
		if (o instanceof Track) {
			return compareTo((Track)o) == 0;
		} else {
			return false;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		return getName().hashCode();
	}
}
