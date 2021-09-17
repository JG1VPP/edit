/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.JTextComponent;

/**
 * {@link LeafTextPane}及び{@link LeafTextArea}のキャレットを描画します。
 *
 * @author 無線部開発班
 * @since 2011年6月12日
 */
final class LeafCaret extends DefaultCaret implements PropertyChangeListener {
	private static final long serialVersionUID = 1L;
	private final JTextComponent comp;
	private LeafTextPane pane = null;
	private FontMetrics met;
	private Color caretColor, background;

	/**
	 * キャレットを生成します。
	 *
	 * @param comp キャレットを表示するテキストコンポーネント
	 */
	public LeafCaret(final JTextComponent comp) {
		super();
		this.comp = comp;
		met = comp.getFontMetrics(comp.getFont());
		caretColor = getOpaqueColor(comp.getCaretColor());
		background = getOpaqueColor(comp.getBackground());
		if (comp instanceof LeafTextPane) pane = (LeafTextPane) comp;
		comp.addPropertyChangeListener("font", this);
		comp.addPropertyChangeListener("caretColor", this);
		comp.addPropertyChangeListener("background", this);
	}

	/**
	 * キャレットの描画領域を壊します。
	 *
	 * @param rect キャレットの描画領域
	 */
	@Override
	protected synchronized void damage(Rectangle rect) {
		if (rect != null) {
			super.x = 0;
			super.y = rect.y;
			super.width = comp.getSize().width;
			super.height = rect.height;
			comp.repaint();
		}
	}

	/**
	 * キャレットを描画します。置換動作時には続く文字に合わせて描画します。
	 *
	 * @param g キャレットを描画するグラフィックス
	 */
	@Override
	public void paint(Graphics g) {
		if (isVisible()) try {
			var rect = comp.modelToView(getDot());
			g.setColor(caretColor);
			g.setXORMode(background);
			if (pane != null) paintLeafTextPaneCaret(g, rect);
			else g.fillRect(rect.x, rect.y, 2, rect.height);
		} catch (BadLocationException ex) {
		}
	}

	/**
	 * 不透明色に変換した色を返します。
	 *
	 * @param color 色
	 *
	 * @return 変換した色
	 */
	private Color getOpaqueColor(Color color) {
		if (color == null) return null;
		final var r = color.getRed();
		final var g = color.getGreen();
		final var b = color.getBlue();
		return new Color(r, g, b);
	}

	/**
	 * {@link LeafTextPane}のキャレットを描画します。
	 *
	 * @param g    キャレットを描画するグラフィックス
	 * @param rect キャレットを描画する位置
	 */
	private void paintLeafTextPaneCaret(Graphics g, Rectangle rect) throws BadLocationException {
		if (pane.getCaretMode() == LeafTextPane.CARET_REPLACE_MODE) {
			var ch = pane.getText(getDot(), 1).charAt(0);
			var width = met.charWidth(ch) - 1;
			if (width > 0) g.fillRect(rect.x, rect.y, width, rect.height);
			else g.fillRect(rect.x, rect.y, 2, rect.height);
		} else g.fillRect(rect.x, rect.y, 2, rect.height);
	}

	/**
	 * 表示フォントサイズの更新に追随します。
	 *
	 * @param e 受信するイベント
	 */
	@Override
	public void propertyChange(PropertyChangeEvent e) {
		met = comp.getFontMetrics(comp.getFont());
		caretColor = getOpaqueColor(comp.getCaretColor());
		background = getOpaqueColor(comp.getBackground());
	}

}
