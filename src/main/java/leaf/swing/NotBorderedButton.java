/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import javax.swing.*;

import leaf.util.LocalizeManager;

/**
 * ボーダーを描画しないボタンです。
 *
 * @author 無線部開発班
 * @since 2011年5月4日
 */
final class NotBorderedButton extends JButton {
	private final LocalizeManager localize = LocalizeManager.get(getClass());

	public NotBorderedButton(String key) {
		super();
		setText(key);
		setBorderPainted(false);
		setFocusPainted(false);
		setFocusable(false);
		setRequestFocusEnabled(false);
	}

	@Override
	public void setText(String key) {
		super.setText(localize.translate(key));
	}

}
