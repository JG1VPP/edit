/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;

/**
 * ステータスバーのコーナーアイコンの実装です。
 *
 * @author 無線部開発班
 * @since 2011年5月5日
 */
public final class CornerIcon implements Icon {
	@Override
	public void paintIcon(Component comp, Graphics g, int x, int y) {
		var g2 = (Graphics2D) g;
		g2.setPaint(Color.GRAY);
		g2.draw3DRect(4, 12, 1, 1, false);
		g2.draw3DRect(8, 8, 1, 1, false);
		g2.draw3DRect(8, 12, 1, 1, false);
		g2.draw3DRect(12, 4, 1, 1, false);
		g2.draw3DRect(12, 8, 1, 1, false);
		g2.draw3DRect(12, 12, 1, 1, false);
	}

	@Override
	public int getIconWidth() {
		return 16;
	}

	@Override
	public int getIconHeight() {
		return 16;
	}
}
