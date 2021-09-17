/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

import static leaf.swing.LeafExpandPane.TITLE_HEIGHT;

/**
 * {@link LeafExpandPane}の展開収納アニメーションを実現します。
 *
 * @author 無線部開発班
 * @since 2011年11月12日
 */
final class ExpandLayout extends BorderLayout {
	private static final long serialVersionUID = 1L;
	private static final int STEP = 30;
	private final LeafExpandPane leafExpandPane;
	private int height;
	private int pheight;
	private Timer animator = null;
	private boolean isExpanded;

	public ExpandLayout(LeafExpandPane leafExpandPane) {
		super(2, 2);
		this.leafExpandPane = leafExpandPane;
		this.isExpanded = leafExpandPane.isExpanded;
		this.height = LeafExpandPane.TITLE_HEIGHT;
		this.pheight = LeafExpandPane.TITLE_HEIGHT;
	}

	public boolean isExpandingOrFolding() {
		return animator != null;
	}

	@Override
	public Dimension preferredLayoutSize(Container target) {
		var ps = super.preferredLayoutSize(target);
		pheight = ps.height;
		if (animator != null) {
			if (isExpanded) {
				if (leafExpandPane.getHeight() > TITLE_HEIGHT) {
					height -= pheight / STEP;
				}
			} else if (leafExpandPane.getHeight() < pheight) {
				height += pheight / STEP;
			}
			if (height <= TITLE_HEIGHT || height >= pheight) {
				animator.stop();
				animator = null;
				if (height > TITLE_HEIGHT) height = pheight;
				else height = TITLE_HEIGHT;
				isExpanded = height != TITLE_HEIGHT;
				if (!isExpanded) {
					leafExpandPane.remove(leafExpandPane.content);
				}
				SwingUtilities.invokeLater(new ScrollTask());
				leafExpandPane.firePropertyChange("expanded", !isExpanded, isExpanded);
				leafExpandPane.fireExpandListeners();
			}
		}
		ps.height = height;
		return ps;
	}

	public void setExpanded(boolean expanded) {
		if (isExpanded != expanded && animator == null) {
			animator = new Timer(5, new ExpandTask());
			animator.start();
		}
	}

	private class ScrollTask implements Runnable {
		@Override
		public void run() {
			var rect = leafExpandPane.content.getBounds();
			leafExpandPane.content.scrollRectToVisible(rect);
		}
	}

	private class ExpandTask implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			ExpandLayout.this.leafExpandPane.revalidate();
		}
	}

}
