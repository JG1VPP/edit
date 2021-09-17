/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.SelectionCommand;

/**
 * 選択文字列を半角英字に変換するコマンドです。
 *
 * @author 無線部開発班
 */
public final class ConvertToEnAlphabet extends SelectionCommand {
	@Override
	public void process(Object... args) {
		var editor = getEditor();
		var selected = editor.getSelectedText();
		if (selected == null) return;
		var sb = new StringBuilder(selected);
		for (var i = 0; i < sb.length(); i++) {
			var ch = sb.charAt(i);
			if (ch >= 'ａ' && ch <= 'ｚ') {
				sb.setCharAt(i, (char) (ch - 'ａ' + 'a'));
			} else if (ch >= 'Ａ' && ch <= 'Ｚ') {
				sb.setCharAt(i, (char) (ch - 'Ａ' + 'A'));
			}
		}
		editor.replaceSelection(sb.toString());
	}
}
