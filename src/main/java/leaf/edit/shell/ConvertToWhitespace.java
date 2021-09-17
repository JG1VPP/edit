/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.SelectionCommand;

/**
 * 選択文字列のタブを空白文字に変換するコマンドです。
 *
 * @author 無線部開発班
 */
public final class ConvertToWhitespace extends SelectionCommand {
	@Override
	public void process(Object... args) {
		var editor = getEditor();
		var selected = editor.getSelectedText();
		if (selected == null) return;
		var size = SetTabSize.getTabSize();
		var space = String.format("%" + size + "s", "");
		editor.replaceSelection(selected.replaceAll("\t", space));
	}
}
