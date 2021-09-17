/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * カーソル前を削除するコマンドを処理します。
 */
public final class Backspace extends EditorCommand {
	@Override
	public void process(Object... args) {
		var textpane = getEditor().getTextPane();
		var pos = textpane.getCaretPosition();
		if (textpane.getSelectedText() == null) {
			textpane.select(pos - 1, pos);
		}
		textpane.replaceSelection("");
	}
}
