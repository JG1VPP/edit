/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;

/**
 * 閉じるアイコンの実装です。
 *
 * @author 無線部開発班
 * @since 2010年5月22日
 */
public final class CloseIcon implements Icon {
	@Override
	public void paintIcon(Component comp, Graphics g, int x, int y) {
		g.translate(x, y);
		g.setColor(Color.BLACK);
		g.drawLine(4, 4, 11, 11);
		g.drawLine(4, 5, 10, 11);
		g.drawLine(5, 4, 11, 10);
		g.drawLine(11, 4, 4, 11);
		g.drawLine(11, 5, 5, 11);
		g.drawLine(10, 4, 4, 10);
		g.translate(-x, -y);
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
