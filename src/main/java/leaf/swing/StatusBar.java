/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;

/**
 * ステータスバーの実装です。
 *
 * @author 無線部開発班
 * @since 2013/02/09
 */
@SuppressWarnings("serial")
public class StatusBar extends JComponent {
	private final JLabel icon;
	private Component glue;

	/**
	 * ステータスバーを構築します。
	 */
	public StatusBar() {
		setPreferredSize(new Dimension(640, 24));
		setLayout(new StatusBarLayout());
		add(glue = Box.createGlue());
		add(icon = new JLabel(new CornerIcon()));
	}

	/**
	 * ステータスバーの右端にコンポーネントを追加します。
	 *
	 * @param comp 追加するコンポーネント
	 */
	public void addComp(Component comp) {
		remove(icon);
		add(new JSeparator(JSeparator.VERTICAL));
		add(comp);
		add(icon);
	}

	/**
	 * ステータスバーの左端にグル—を追加します。
	 *
	 * @param comp 追加するグル—
	 */
	public void setGlue(Component comp) {
		remove(glue);
		add(comp, 0);
		glue = comp;
	}

	private class StatusBarLayout extends BoxLayout {
		public StatusBarLayout() {
			super(StatusBar.this, BoxLayout.X_AXIS);
		}

		@Override
		public Dimension maximumLayoutSize(Container target) {
			return target.getMaximumSize();
		}
	}

}
