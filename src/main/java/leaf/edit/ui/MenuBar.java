/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import leaf.swing.XMLMenuBar;

/**
 * tseditのメインウィンドウに表示されるメニューバーです。
 *
 * @author 無線部開発班
 * @since 2012/04/01
 */
public final class MenuBar extends XMLMenuBar {
	private static MenuBar instance;

	private MenuBar() {
		super("menubar");
	}

	/**
	 * メニューバーのインスタンスを返します。
	 *
	 * @return メニューバー
	 */
	public static MenuBar getInstance() {
		if (instance == null) instance = new MenuBar();
		return instance;
	}

}
