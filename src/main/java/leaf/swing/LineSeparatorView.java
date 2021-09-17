/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.ParagraphView;
import javax.swing.text.Position;

/**
 * 改行文字を可視化する{@link ParagraphView}の実装です。
 *
 * @author 無線部開発班
 * @since 2012年12月28日
 */
public class LineSeparatorView extends ParagraphView {
	private final Color color;

	/**
	 * {@link Element}を指定してビューを構築します。
	 *
	 * @param elm 要素
	 */
	public LineSeparatorView(Element elm) {
		super(elm);
		color = Color.GRAY;
	}

	@Override
	public void paint(Graphics g, Shape a) {
		super.paint(g, a);
		try {
			paintLineFeedAndCarriageReturn(g, a);
		} catch (BadLocationException ex) {
			ex.printStackTrace();
		}
	}

	private void paintLineFeedAndCarriageReturn(Graphics g, Shape a) throws BadLocationException {
		final var offset = getEndOffset();
		var p = modelToView(offset, a, Position.Bias.Backward);
		var r = p == null ? a.getBounds() : p.getBounds();
		var h = r.height;
		var w = g.getFontMetrics().stringWidth("m");
		var old = g.getColor();
		g.setColor(color);
		g.drawLine(r.x + 3, r.y + h / 2, r.x + 5, r.y + h / 2 - 2);
		g.drawLine(r.x + 3, r.y + h / 2, r.x + 5, r.y + h / 2 + 2);
		g.drawLine(r.x + 3, r.y + h / 2, r.x + w + 3, r.y + h / 2);
		g.drawLine(r.x + w + 3, r.y + h / 4, r.x + w + 3, r.y + h / 2);
		g.setColor(old);
	}
}
