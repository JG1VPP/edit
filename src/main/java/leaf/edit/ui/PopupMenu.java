/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import leaf.swing.XMLPopupMenu;

/**
 * 月白エディタの右クリックメニューの実装です。
 *
 * @author 無線部開発班
 */
public final class PopupMenu extends XMLPopupMenu {
	private static PopupMenu instance;

	private PopupMenu() {
		super("popup");
	}

	/**
	 * ポップアップメニューのインスタンスを返します。
	 *
	 * @return ポップアップメニュー
	 */
	public static PopupMenu getInstance() {
		if (instance == null) instance = new PopupMenu();
		return instance;
	}
}
