/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * 現在キャレットがある行全体の文字列を削除します。
 *
 * @author 無線部開発班
 * @since 2013/01/02
 */
public final class DeleteLine extends EditorCommand {
	@Override
	public void process(Object... args) {
		getEditor().selectLine(getEditor().getLineNumber());
		getEditor().replaceSelection("");
	}
}
