/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPopupMenuUI;

import static java.awt.AlphaComposite.SRC_OVER;
import static java.awt.RenderingHints.KEY_ANTIALIASING;
import static java.awt.RenderingHints.VALUE_ANTIALIAS_ON;
import static java.awt.image.BufferedImage.TYPE_INT_ARGB;

/**
 * 影による立体効果を{@link JPopupMenu}に追加します。
 *
 * @author 無線部開発班
 * @since 2010年8月9日
 */
public class ShadowedMenuUI extends BasicPopupMenuUI {
	private static final int SHADOW_WIDTH = 5;
	private static final int SHADOW_ARC = 10;
	private static final float SHADOW_ALPHA = 0.125f;
	private static Insets insets;

	private ShadowedMenuUI() {
	}

	/**
	 * JPopupMenuを表示するPopupを返します。
	 *
	 * @param pmenu 表示するPopupを要求するJPopupMenu
	 * @param x     Popupの水平座標
	 * @param y     Popupの垂直座標
	 *
	 * @return JPopupMenuを表示するPopup
	 */
	@Override
	public Popup getPopup(JPopupMenu pmenu, int x, int y) {
		var popup = super.getPopup(pmenu, x, y);
		var panel = (JPanel) pmenu.getParent();
		if (isHeavyWeightContainer(panel)) {
			panel.setBorder(new HeavyShadowBorder(pmenu, x, y));
		} else {
			panel.setBorder(new LightShadowBorder());
		}
		panel.setOpaque(false);
		return popup;
	}

	/**
	 * 影のイメージを生成します。
	 *
	 * @param x イメージの水平座標
	 * @param y イメージの垂直座標
	 * @param w イメージの幅
	 * @param h イメージの高さ
	 *
	 * @return 影のイメージ
	 */
	private static Image createShadowImage(int x, int y, int w, int h) {
		var image = new BufferedImage(w, h, TYPE_INT_ARGB);
		var g2 = image.createGraphics();
		g2.setRenderingHint(KEY_ANTIALIASING, VALUE_ANTIALIAS_ON);
		g2.setComposite(AlphaComposite.getInstance(SRC_OVER, SHADOW_ALPHA));
		g2.setPaint(Color.BLACK);
		g2.translate(x, y);
		for (var i = 0; i < SHADOW_WIDTH; i++) {
			g2.fillRoundRect(SHADOW_WIDTH, SHADOW_WIDTH, w - SHADOW_WIDTH * 2 + i, h - SHADOW_WIDTH * 2 + i, SHADOW_ARC, SHADOW_ARC);
		}
		g2.translate(-x, -y);
		g2.dispose();
		return image;
	}

	/**
	 * PopupMenuUIを生成して返します。
	 *
	 * @param comp このパラメータは無視される
	 *
	 * @return このUIクラスのインスタンス
	 */
	public static ComponentUI createUI(JComponent comp) {
		insets = new Insets(0, 0, SHADOW_WIDTH, SHADOW_WIDTH);
		return new ShadowedMenuUI();
	}

	/**
	 * コンテナが重量コンテナであるか調べます。
	 *
	 * @param cont 対象のコンテナ
	 *
	 * @return 重量コンテナの場合true
	 */
	private static boolean isHeavyWeightContainer(Container cont) {
		var comp = cont.getParent();
		while (comp != null) {
			if (comp instanceof Panel) return true;
			if (comp instanceof JWindow) return true;
			comp = comp.getParent();
		}
		return false;
	}

	// Implementation of Border for lightweight components
	private static final class LightShadowBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;

		@Override
		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			g.drawImage(createShadowImage(x, y, w, h), x, y, null);
		}

		@Override
		public Insets getBorderInsets(Component comp) {
			return insets;
		}
	}

	// Implementation of Border for heavy components
	private static final class HeavyShadowBorder extends AbstractBorder {
		private static final long serialVersionUID = 1L;
		private BufferedImage screenShot = null;

		public HeavyShadowBorder(JComponent comp, int x, int y) {
			var dim = comp.getPreferredSize();
			var rect = new Rectangle(x, y, dim.width + SHADOW_WIDTH, dim.height + SHADOW_WIDTH);
			try {
				screenShot = new Robot().createScreenCapture(rect);
			} catch (AWTException ex) {
			}
		}

		@Override
		public void paintBorder(Component comp, Graphics g, int x, int y, int w, int h) {
			if (screenShot == null) return;
			g.drawImage(screenShot, x, y, comp);
			g.drawImage(createShadowImage(x, y, w, h), x, y, comp);
		}

		@Override
		public Insets getBorderInsets(Component comp) {
			return insets;
		}
	}

}
