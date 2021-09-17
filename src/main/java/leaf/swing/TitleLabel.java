/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;

/**
 * {@link LeafExpandPane}のタイトルラベルです。
 *
 * @author 無線部開発班
 * @since 2013/02/17
 */
final class TitleLabel extends JLabel {
	private static final long serialVersionUID = 1L;

	public TitleLabel(String text) {
		super(text);
	}

	public TitleLabel(Icon icon) {
		super(icon);
	}

	@Override
	public void paintComponent(Graphics g) {
		var g2 = (Graphics2D) g;
		var bgc = getBackground();
		g2.setPaint(new GradientPaint(0, 0, bgc.brighter(), 0, getHeight(), bgc.darker(), true));
		g2.fillRect(0, 0, getWidth(), getHeight());
		super.paintComponent(g);
	}

}
