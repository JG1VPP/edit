/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;

/**
 * ニュースリーダーの設定画面の実装です。
 *
 * @author 無線部開発班
 * @since 2011年5月4日
 */
@SuppressWarnings("serial")
final class ConfigBar extends JComponent {
	private final JTextField urlfld;
	private final JToolBar box;
	private boolean isExpanded = false;
	private Timer animator;
	private URL url;

	public ConfigBar(final LeafNewsBar bar) {
		super();
		setLayout(new ExpandLayout());
		box = new JToolBar();
		box.setFloatable(false);
		box.setLayout(new ToolBarLayout());
		add(box, BorderLayout.CENTER);
		box.add(new JLabel(" URL "));
		box.add(urlfld = new JTextField());
		JButton bok;
		box.add(bok = new NotBorderedButton("button_ok"));
		bok.addActionListener(e -> {
			expand(false);
			try {
				var url = urlfld.getText();
				bar.setURL(url.isEmpty() ? null : url);
				bar.update();
			} catch (MalformedURLException ex) {
			}
		});
		JButton bcancel;
		box.add(bcancel = new NotBorderedButton("button_cancel"));
		bcancel.addActionListener(e -> {
			expand(false);
			bar.setURL(url);
		});
	}

	public void expand(boolean exp) {
		if (isExpanded == exp) return;
		if (animator != null && animator.isRunning()) return;
		isExpanded = getHeight() != 0;
		animator = new Timer(5, e -> revalidate());
		animator.start();
	}

	public void setURL(URL url) {
		urlfld.setText(url != null ? url.toString() : "");
		this.url = url;
	}

	private class ToolBarLayout extends BoxLayout {
		public ToolBarLayout() {
			super(box, BoxLayout.X_AXIS);
		}

		@Override
		public Dimension maximumLayoutSize(Container target) {
			return target.getMaximumSize();
		}
	}

	private class ExpandLayout extends BorderLayout {
		private int h = 0;

		public ExpandLayout() {
			super(2, 2);
		}

		@Override
		public Dimension preferredLayoutSize(Container target) {
			var ps = super.preferredLayoutSize(target);
			var ph = ps.height;
			if (animator != null) {
				if (isExpanded && getHeight() > 0) h -= 2;
				if (!isExpanded && getHeight() < ph) h += 2;
				if (h <= 0 || h >= ph) {
					animator.stop();
					animator = null;
					if (h <= 0) h = 0;
					else {
						h = ph;
						urlfld.requestFocusInWindow();
					}
					isExpanded = h != 0;
				}
			}
			ps.height = h;
			return ps;
		}
	}
}
