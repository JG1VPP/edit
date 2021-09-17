/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.IOException;
import java.nio.charset.Charset;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.TextEditorUtils;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;
import leaf.swing.LeafDialog;
import leaf.swing.LeafGrepDialog;

/**
 * GREP検索用ダイアログを表示するコマンドです。
 *
 * @author 無線部開発班
 * @since 2013/02/26
 */
public final class Grep extends EditorCommand implements LocaleListener {
	private static LeafGrepDialog dialog;

	@Override
	public void localeChanged(LocaleEvent e) {
		if (dialog != null) dialog.initialize();
	}

	@Override
	public void process(Object... args) throws IOException {
		if (dialog == null) {
			dialog = new LeafGrepDialog(getFrame());
			dialog.setCharsetList(TextEditorUtils.getCharsets());
		}
		var file = TextEditorUtils.getSelectedEditor().getFile();
		dialog.setDirectory(file);
		if (dialog.showDialog() == LeafDialog.OK_OPTION) {
			TextEditorUtils.addTab("GREP").read(dialog.getResult());
		}
	}

	public static void setCharsets(Charset[] chsets) {
		if (dialog != null) dialog.setCharsetList(chsets);
	}

}
