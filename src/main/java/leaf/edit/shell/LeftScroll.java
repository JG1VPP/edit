/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.*;

import leaf.swing.LeafIcons;
import leaf.swing.LeafCellTablePane;
import leaf.util.Properties;
import leaf.main.Command;
import leaf.util.WindowUtils;

/**
 * 左スクロール電光掲示板を表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class LeftScroll extends Command {

	private final Properties properties;
	private LeftScrollFrame frame = null;

	public LeftScroll() {
		properties = Properties.getInstance(getClass());
	}

	@Override
	public void process(Object... args) {
		if (frame == null) frame = new LeftScrollFrame();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		frame.toFront();
	}

	private class LeftScrollFrame extends JFrame {
		private final LeafCellTablePane panel;

		/**
		 * ウィンドウを構築します。
		 */
		public LeftScrollFrame() {
			super("LeftScroll");
			setIconImage(LeafIcons.getImage("LUNA"));
			setLayout(null);
			int w = properties.get("width", Integer.class, 120);
			int h = properties.get("height", Integer.class, 40);
			var scroll = new leaf.swing.LeftScroll(w, h);
			scroll.addColor(Color.YELLOW);
			panel = new LeafCellTablePane();
			panel.setCellAutomata(scroll);
			panel.setLocation(10, 10);
			panel.setSize(panel.getPreferredSize());
			add(panel);
			var size = new Dimension(panel.getSize());
			var insets = new Insets(10, 10, 10, 10);
			size.width += insets.left + insets.right;
			size.height += insets.top + insets.bottom;
			WindowUtils.setContentSize(this, size);
			addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosing(WindowEvent e) {
					panel.setAutoUpdateEnabled(false);
				}
			});
		}
	}
}
