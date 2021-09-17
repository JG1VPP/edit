/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * エディタに編集中の文字列の長さを挿入するコマンドです。
 *
 * @author 無線部開発班
 * @since 2013/01/02
 */
public final class InsertTextLength extends EditorCommand {
	@Override
	public void process(Object... args) {
		var editor = getEditor();
		editor.replaceSelection(String.valueOf(editor.getText().length()));
	}
}
