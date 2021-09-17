/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.main;

import javax.swing.*;

import leaf.swing.MainFrame;

/**
 * フレームワークが起動するアプリケーションは必ずこのクラスを継承します。
 *
 * @author 無線部開発班
 * @since 2012/03/27
 */
public abstract class Application {
	/**
	 * このアプリケーションが提供するメニューバーを構築します。
	 *
	 * @return メイン画面に表示するメニューバー
	 */
	public abstract JMenuBar createMenuBar();

	/**
	 * このアプリケーションが提供するツールバーを構築します。
	 *
	 * @return メイン画面に表示するツールバー
	 */
	public abstract JToolBar createToolBar();

	/**
	 * このアプリケーションが提供するコマンドをシェルに登録します。
	 * <p>
	 * {@link #createMenuBar()} {@link #createToolBar()}の前に呼び出されます。
	 *
	 * @param shell 対象となるシェル
	 */
	public abstract void installCommands(Shell shell);

	/**
	 * このアプリケーションのインストールが完全に終了した時に呼び出されます。
	 */
	public abstract void installFinished();

	/**
	 * アプリケーションは必ずこのメソッドで起動されます。
	 *
	 * @param frame アプリケーションが適用されるメイン画面
	 *
	 * @return 起動直後のアプリケーション
	 */
	public static Application newInstance(MainFrame frame) {
		return null;
	}

}
