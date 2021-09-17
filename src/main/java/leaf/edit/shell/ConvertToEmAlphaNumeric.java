/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.SelectionCommand;

/**
 * 選択文字列を全角英数字に変換するコマンドです。
 *
 * @author 無線部開発班
 */
public final class ConvertToEmAlphaNumeric extends SelectionCommand {
	@Override
	public void process(Object... args) {
		var editor = getEditor();
		var selected = editor.getSelectedText();
		if (selected == null) return;
		var sb = new StringBuilder(selected);
		for (var i = 0; i < sb.length(); i++) {
			var ch = sb.charAt(i);
			if (ch >= 'a' && ch <= 'z') {
				sb.setCharAt(i, (char) (ch - 'a' + 'ａ'));
			} else if (ch >= 'A' && ch <= 'Z') {
				sb.setCharAt(i, (char) (ch - 'A' + 'Ａ'));
			} else if (ch >= '0' && ch <= '9') {
				sb.setCharAt(i, (char) (ch - '0' + '０'));
			}
		}
		editor.replaceSelection(sb.toString());
	}
}
