/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.text.Element;
import javax.swing.text.LabelView;

/**
 * 水平タブを可視化する{@link LabelView}の実装です。
 *
 * @author 無線部開発班
 * @since 2012年12月28日
 */
public class TabCharacterView extends LabelView {
	private final Color color;

	/**
	 * {@link Element}を指定してビューを構築します。
	 *
	 * @param elm 要素
	 */
	public TabCharacterView(Element elm) {
		super(elm);
		color = getForeground().brighter();
	}

	@Override
	public void paint(Graphics g, Shape a) {
		super.paint(g, a);
		var alloc = a.getBounds();
		var met = g.getFontMetrics();
		var text = getText(getStartOffset(), getEndOffset()).toString();
		var expander = getTabExpander();
		int sumOfTabs = 0, length = text.length();
		for (var i = 0; i < length; i++) {
			if (text.charAt(i) != '\t') continue;
			var prevWidth = met.stringWidth(text.substring(0, i)) + sumOfTabs;
			int sx = alloc.x + prevWidth, sy = alloc.y;
			var tabWidth = (int) expander.nextTabStop((float) sx, i) - sx;
			g.setColor(color);
			g.drawLine(sx, sy + 2, sx + 2, sy);
			g.drawLine(sx + 2, sy, sx + 4, sy + 2);
			sumOfTabs += tabWidth;
		}
	}
}
