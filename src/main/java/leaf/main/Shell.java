/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * コマンドを管理・処理するシェルです。
 */
public final class Shell extends leaf.shell.Shell implements ActionListener {
	private static final Shell instance = new Shell();

	/**
	 * シェルを構築します。
	 */
	private Shell() {
		super();
	}

	/**
	 * ボタンやメニューアイテムが選択された時にコマンドを呼び出します。
	 *
	 * @param e メソッドが受け取るイベント
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		call(e.getActionCommand());
	}

	/**
	 * シェルのインスタンスを返します。
	 *
	 * @return このクラスのインスタンス
	 */
	public static Shell getInstance() {
		return instance;
	}

}
