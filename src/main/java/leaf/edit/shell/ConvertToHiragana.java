/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.SelectionCommand;

/**
 * 選択文字列を全角ひらがなに変換するコマンドです。
 *
 * @author 無線部開発班
 * @since 2013/01/02
 */
public final class ConvertToHiragana extends SelectionCommand {
	@Override
	public void process(Object... args) {
		var editor = getEditor();
		var selected = editor.getSelectedText();
		if (selected == null) return;
		var sb = new StringBuilder(selected);
		for (var i = 0; i < sb.length(); i++) {
			var ch = sb.charAt(i);
			if (ch >= 'ァ' && ch <= 'ン') {
				sb.setCharAt(i, (char) (ch - 'ァ' + 'ぁ'));
			} else if (ch == 'ヵ') sb.setCharAt(i, 'か');
			else if (ch == 'ヶ') sb.setCharAt(i, 'け');
			else if (ch == 'ヴ') {
				sb.setCharAt(i, 'う');
				sb.insert(++i, '゛');
			}
		}
		editor.replaceSelection(sb.toString());
	}
}
