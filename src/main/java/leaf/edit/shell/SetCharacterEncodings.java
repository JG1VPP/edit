/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.edit.ui.CharsetDialog;
import leaf.edit.ui.TextEditorUtils;

/**
 * テキストエディタの入出力で用いる文字セットを設定するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SetCharacterEncodings extends Command {
	@Override
	public void process(Object... args) {
		var dialog = new CharsetDialog(getFrame());
		dialog.setSelectedCharsets(TextEditorUtils.getCharsets());
		if (dialog.showDialog()) {
			var chsets = dialog.getSelectedCharsets();
			TextEditorUtils.setCharsets(chsets);
			Grep.setCharsets(chsets);
			getFileChooser().initialize();
			TextEditorUtils.setCharsets(chsets);
		}
	}
}
