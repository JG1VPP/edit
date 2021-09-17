/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Point2D;
import javax.swing.*;
import javax.swing.plaf.ComponentUI;

/**
 * 文字列が折り返して表示されるラベルです。
 *
 * @author 無線部開発班
 * @since 2012年3月30日
 */
public class LeafWrappedLabel extends JComponent {
	private static final long serialVersionUID = 1L;
	private GlyphVector glyph = null;
	private int prevwidth = -1;
	private String text;

	/**
	 * 何も表示されていないラベルを構築します。
	 */
	public LeafWrappedLabel() {
		this("");
	}

	/**
	 * デフォルトの表示文字列を指定してラベルを構築します。
	 *
	 * @param text 表示する文字列
	 */
	public LeafWrappedLabel(String text) {
		super();
		setUI(new WrappedLabelUI());
		setText(text);
	}

	private GlyphVector createWrappedGlyphVector(float wrap, Font font) {
		var frc = getFontMetrics(font).getFontRenderContext();
		Point2D position = new Point2D.Double(0d, 0d);
		var glyph = font.createGlyphVector(frc, text);
		var lineHeight = (float) glyph.getLogicalBounds().getHeight();
		float xpos = 0f, advance;
		var lineCount = 0;
		for (var i = 0; i < glyph.getNumGlyphs(); i++) {
			advance = glyph.getGlyphMetrics(i).getAdvance();
			var ch = text.charAt(glyph.getGlyphCharIndex(i));
			if (xpos < wrap && wrap <= xpos + advance || ch == '\n') {
				lineCount++;
				xpos = 0f;
			}
			position.setLocation(xpos, lineHeight * lineCount);
			glyph.setGlyphPosition(i, position);
			xpos += advance;
		}
		return glyph;
	}

	/**
	 * ラベルに表示する文字列を返します。
	 *
	 * @return 表示する文字列
	 */
	public String getText() {
		return text;
	}

	/**
	 * ラベルに表示する文字列を設定します。
	 *
	 * @param text 表示する文字列
	 */
	public void setText(String text) {
		final var old = this.text;
		this.text = text;
		setupWrappedGlyphVector(getWidth());
		firePropertyChange("text", old, text);
	}

	private void setupWrappedGlyphVector(int width) {
		if (width > 0) glyph = createWrappedGlyphVector(width, getFont());
		revalidate();
	}

	private final class WrappedLabelUI extends ComponentUI {
		@Override
		public void installUI(JComponent c) {
			LookAndFeel.installColorsAndFont(c, "Label.background", "label.foreground", "Label.font");
			LookAndFeel.installProperty(c, "opaque", Boolean.FALSE);
		}

		@Override
		public void paint(Graphics g, JComponent comp) {
			var w = getWidth() - getInsets().left - getInsets().right;
			if (w != prevwidth) setupWrappedGlyphVector(w);
			prevwidth = w;
			var g2 = (Graphics2D) g;
			if (glyph != null) {
				var i = getInsets();
				g2.drawGlyphVector(glyph, i.left, getFont().getSize() + i.top);
			} else super.paint(g, comp);
		}

		@Override
		public Dimension getPreferredSize(JComponent comp) {
			if (glyph == null) return null;
			var insets = getInsets();
			var x = (float) insets.left;
			var y = (float) (getFont().getSize() + insets.top);
			var gsize = glyph.getPixelBounds(null, x, y).getSize();
			gsize.width += insets.left + insets.right;
			gsize.height += insets.top + insets.bottom + getFont().getSize();
			return gsize;
		}
	}
}
