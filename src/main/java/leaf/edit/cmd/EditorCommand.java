/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.cmd;

import javax.swing.text.Document;

import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.TextEditorUtils;

/**
 * エディタに対する操作を行うコマンドの基底クラスです。
 *
 * @author 無線部開発班
 * @since 2012/12/21
 */
public abstract class EditorCommand extends Command {
	/**
	 * このコマンドを処理する時のドキュメントを返します。
	 *
	 * @return ドキュメント
	 *
	 * @throws ClassCastException テキストエディタでない場合
	 */
	public static final Document getDocument() {
		return getEditor().getDocument();
	}

	/**
	 * このコマンドを処理する時のエディタを返します。
	 *
	 * @return エディタ
	 *
	 * @throws ClassCastException テキストエディタでない場合
	 */
	public static final BasicTextEditor getEditor() {
		return TextEditorUtils.getSelectedEditor();
	}

}
