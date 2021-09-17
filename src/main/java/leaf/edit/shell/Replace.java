/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;
import leaf.swing.LeafReplaceDialog;

/**
 * 文字列を検索・置換するダイアログを表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Replace extends EditorCommand implements LocaleListener {
	private final LeafReplaceDialog dialog;

	public Replace() {
		dialog = new LeafReplaceDialog(getFrame());
	}

	@Override
	public void localeChanged(LocaleEvent e) {
		dialog.initialize();
	}

	@Override
	public void process(Object... args) {
		var editor = getEditor();
		if (editor != null) dialog.setTextComponent(editor.getTextPane());
		dialog.setVisible(true);
	}
}
