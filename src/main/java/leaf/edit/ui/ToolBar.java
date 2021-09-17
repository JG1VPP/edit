/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import leaf.swing.XMLToolBar;

/**
 * テキストエディタのメインウィンドウに表示されるツールバーです。
 *
 * @author 無線部開発班
 * @since 2012/04/01
 */
public final class ToolBar extends XMLToolBar {
	private static ToolBar instance;

	private ToolBar() {
		super("toolbar");
		setFloatable(false);
	}

	/**
	 * ツールバーのインスタンスを返します。
	 *
	 * @return ツールバー
	 */
	public static ToolBar getInstance() {
		if (instance == null) instance = new ToolBar();
		return instance;
	}

}
