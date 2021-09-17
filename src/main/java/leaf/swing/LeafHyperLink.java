/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.net.URI;
import java.net.URL;
import javax.swing.*;
import javax.swing.plaf.ButtonUI;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.text.View;

/**
 * ハイパーリンクを表示するコンポーネントです。
 *
 * @author 無線部開発班
 * @since 2012/04/30
 */
public class LeafHyperLink extends JButton {
	private static final long serialVersionUID = 1L;
	private static final String uiClassID = "LinkButtonUI";
	private static final LinkButtonUI buttonUI = new LinkButtonUI();

	/**
	 * デフォルトで何も表示しないハイパーリンクを構築します。
	 */
	public LeafHyperLink() {
		this(null, null);
	}

	/**
	 * 指定された{@link Action}を持つハイパーリンクを構築します。
	 *
	 * @param action ボタンのAction
	 */
	public LeafHyperLink(Action action) {
		this(null, null);
		setAction(action);
	}

	/**
	 * 表示するアイコンを指定してハイパーリンクを構築します。
	 *
	 * @param icon ハイパーリンクのアイコン
	 */
	public LeafHyperLink(Icon icon) {
		this(null, icon);
	}

	/**
	 * 表示するテキストを指定してハイパーリンクを構築します。
	 *
	 * @param text ハイパーリンクの文字列
	 */
	public LeafHyperLink(String text) {
		this(text, null);
	}

	/**
	 * テキストとアイコンを指定してハイパーリンクを構築します。
	 *
	 * @param text 文字列
	 * @param icon アイコン
	 */
	public LeafHyperLink(String text, Icon icon) {
		super(text, icon);
		setOpaque(false);
		setForeground(Color.BLUE);
	}

	@Override
	public ButtonUI getUI() {
		return (ButtonUI) LinkButtonUI.createUI(this);
	}

	@Override
	public void updateUI() {
		super.updateUI();
		if (UIManager.get(uiClassID) != null) {
			setUI((LinkButtonUI) UIManager.getUI(this));
		} else {
			setUI(LinkButtonUI.createUI(this));
		}
		setBorder(BorderFactory.createEmptyBorder(0, 0, 2, 0));
		setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}

	private static final class LinkButtonUI extends BasicButtonUI {
		private static final Rectangle viewRect = new Rectangle();
		private static final Rectangle iconRect = new Rectangle();
		private static final Rectangle textRect = new Rectangle();
		private static Dimension size;

		private LinkButtonUI() {
			super();
		}

		@Override
		public synchronized void paint(Graphics g, JComponent c) {
			var button = (AbstractButton) c;
			var model = button.getModel();
			var met = button.getFontMetrics(button.getFont());
			g.setFont(button.getFont());
			var is = button.getInsets();
			size = button.getSize(size);
			viewRect.x = is.left;
			viewRect.y = is.top;
			viewRect.width = size.width - (is.left + is.right);
			viewRect.height = size.height - (is.top + is.bottom);
			iconRect.setBounds(0, 0, 0, 0);
			textRect.setBounds(0, 0, 0, 0);
			var text = SwingUtilities.layoutCompoundLabel(c, met, button.getText(), null, button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, 0);
			if (button.isOpaque()) {
				g.setColor(button.getBackground());
				g.fillRect(0, 0, size.width, size.height);
			}
			if (text == null) return;
			if (!model.isSelected() && !model.isPressed() && !model.isArmed() && button.isRolloverEnabled() && model.isRollover() && model.isEnabled()) {
				g.setColor(button.getForeground());
				g.drawLine(textRect.x, textRect.y + textRect.height, textRect.x + textRect.width, textRect.y + textRect.height);
			}
			var view = (View) c.getClientProperty(BasicHTML.propertyKey);
			if (view != null) view.paint(g, textRect);
			else paintText(g, button, textRect, text);
		}

		public static ComponentUI createUI(AbstractButton button) {
			return buttonUI;
		}
	}

	/**
	 * ブラウザを起動するURIを表示する{@link Action}です。
	 *
	 * @author 無線部開発班
	 * @since 2013年1月20日
	 */
	public static class BrowseAction extends AbstractAction {
		private static final long serialVersionUID = 1L;
		private final Desktop desktop;
		private final String uri;

		/**
		 * 表示するテキストとURLを指定してActionを構築します。
		 *
		 * @param text 文字列
		 * @param url  URL
		 */
		public BrowseAction(String text, URL url) {
			this(text, url.toExternalForm());
		}

		/**
		 * 表示するテキストとURIを指定してActionを構築します。
		 *
		 * @param text 文字列
		 * @param uri  URI
		 */
		public BrowseAction(String text, URI uri) {
			this(text, uri.toString());
		}

		/**
		 * 表示するテキストとURI文字列を指定してActionを構築します。
		 *
		 * @param text 文字列
		 * @param uri  URI
		 */
		public BrowseAction(String text, String uri) {
			super(text);
			this.uri = uri;
			if (Desktop.isDesktopSupported()) {
				desktop = Desktop.getDesktop();
				setEnabled(true);
			} else {
				desktop = null;
				setEnabled(false);
			}
			putValue(Action.SHORT_DESCRIPTION, uri);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			try {
				desktop.browse(new URI(uri));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
