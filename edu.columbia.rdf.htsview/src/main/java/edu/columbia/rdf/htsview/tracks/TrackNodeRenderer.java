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
import java.awt.Graphics2D;

import org.jebtk.core.geom.IntDim;
import org.jebtk.core.tree.TreeNode;
import org.jebtk.modern.theme.ThemeService;
import org.jebtk.modern.tree.ModernTreeBranchNodeRenderer;
import org.jebtk.modern.tree.ModernTreeNodeRenderer;
import org.jebtk.modern.tree.Tree;



// TODO: Auto-generated Javadoc
/**
 * The Class TrackNodeRenderer.
 */
public class TrackNodeRenderer extends ModernTreeBranchNodeRenderer {
	
	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 1L;
	
	/** The Constant ORB_WIDTH. */
	private static final IntDim ORB_SIZE = new IntDim(12, 6);
	
	/** The Constant ORB_HEIGHT. */
	//private static final int ORB_HEIGHT = 8;
	
	/** The m text. */
	private String mText = "";
	
	/** The m fill color. */
	private Color mFillColor;

	private Color mLineColor;

	/** The Constant LINE_COLOR. */
	public static final Color LINE_COLOR = 
			ThemeService.getInstance().colors().getHighlight(2);
	
	/** The Constant COLOR. */
	public static final Color COLOR = 
			ThemeService.getInstance().colors().getHighlight(6);
	
	

	/**
	 * Instantiates a new track node renderer.
	 */
	public TrackNodeRenderer() {
		setBranchHeight(36);
	}
	
	/* (non-Javadoc)
	 * @see org.abh.common.ui.tree.ModernTreeNodeRenderer#drawForegroundAAText(java.awt.Graphics2D)
	 */
	@Override
	public void drawForegroundAAText(Graphics2D g2) {
		super.drawForegroundAAText(g2);
		
		int x = getCumulativeXDepthOffset() + BRANCH_OPEN_ICON.getWidth() + PADDING;
		
		int y = (getHeight() - ORB_SIZE.getH()) / 2;
		int x2 = getWidth() - ORB_SIZE.getW() - DOUBLE_PADDING;
		
		//fill(g2, mFillColor);
		
		//g2.setColor(COLOR);
		g2.setColor(TEXT_COLOR);
		
		//String t = (mRow + 1) + ".";
		
		//g2.drawString(t, 
		//		x + NUM_WIDTH - g2.getFontMetrics().stringWidth(t) - PADDING, 
		//		getTextYPosCenter(g2, getHeight()));
		
		//x += NUM_WIDTH;

		//int tw = x2 - x - PADDING;

		

		// Keep truncating the text until it fits into the available space.
		//for (int i = mText.length(); i >= 0; --i) {
		//	t = TextUtils.truncate(mText, i);
		//	
		//	if (g2.getFontMetrics().stringWidth(t) <= tw) {
		//		break;
		//	}
		//}

		
		//g2.setColor(mFillColor);
		g2.drawString(truncate(g2, mText, x2 - x - PADDING), x, getTextYPosCenter(g2, getHeight()));


		//g2.setColor(mFillColor);
		//g2.fillOval(x2, y, ORB_WIDTH, ORB_WIDTH);
		//
		//g2.drawLine(x2, y, x2 + ORB_WIDTH, y);
		//g2.drawLine(x2, y + ORB_WIDTH / 2, x2 + ORB_WIDTH, y + ORB_WIDTH / 2);
		//g2.drawLine(x2, y+ ORB_WIDTH, x2 + ORB_WIDTH, y + ORB_WIDTH);
		
		if (mFillColor != null) {
			g2.setColor(mFillColor);
		} else {
			g2.setColor(Color.GRAY);
		}
		
		g2.fillRect(x2, y, ORB_SIZE.getW(), ORB_SIZE.getH());
		
		if (mLineColor != null) {
			g2.setColor(mFillColor);
		} else {
			g2.setColor(mLineColor);
		}
		
		g2.drawRect(x2, y, ORB_SIZE.getW() - 1, ORB_SIZE.getH() - 1);
	}

	/* (non-Javadoc)
	 * @see org.abh.common.ui.tree.ModernTreeNodeRenderer#getRenderer(org.abh.common.ui.tree.Tree, org.abh.common.tree.TreeNode, boolean, boolean, boolean, boolean, int, int)
	 */
	@Override
	public ModernTreeNodeRenderer getRenderer(Tree<?> tree,
			TreeNode<?> node,
			boolean nodeIsHighlighted,
			boolean nodeIsSelected,
			boolean hasFocus,
			boolean isDragToNode,
			int depth,
			int row) {
		super.getRenderer(tree, 
				node, 
				nodeIsHighlighted, 
				nodeIsSelected, 
				hasFocus, 
				isDragToNode, 
				depth, 
				row);

		Track t = (Track)node.getValue();
		
		mText = t.getName();
		mFillColor = t.getFillColor(); 
		mLineColor = t.getLineColor();

		return this;
	}
}