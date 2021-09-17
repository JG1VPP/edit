/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * ファイルのバイトサイズを挿入するコマンドです。
 *
 * @author 無線部開発班
 */
public final class InsertByteLength extends EditorCommand {
	@Override
	public void process(Object... args) {
		var size = getEditor().getText().getBytes().length;
		getEditor().replaceSelection(String.valueOf(size));
	}
}
