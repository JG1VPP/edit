/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;
import leaf.swing.LeafSearchDialog;

/**
 * 文字列を検索するダイアログを表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Find extends EditorCommand implements LocaleListener {
	public final LeafSearchDialog dialog;

	public Find() {
		dialog = new LeafSearchDialog(getFrame());
	}

	@Override
	public void localeChanged(LocaleEvent e) {
		dialog.initialize();
	}

	@Override
	public void process(Object... args) {
		var editor = getEditor();
		if (editor != null) {
			dialog.setTextComponent(editor.getTextPane());
		}
		dialog.setVisible(true);
	}

}
