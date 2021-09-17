/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.IOException;
import java.nio.charset.Charset;
import javax.swing.*;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.TextEditorUtils;
import leaf.util.LocalizeManager;

/**
 * ファイルを開く直すコマンドです。
 *
 * @author 無線部開発班
 */
public final class Reopen extends EditorCommand {
	private final LocalizeManager localize;

	public Reopen() {
		localize = LocalizeManager.get(getClass());
	}

	@Override
	public void process(Object... args) throws IOException {
		var frame = getFrame();
		var editor = getEditor();
		if (!CloseTab.prepareToClose(editor)) return;
		var chsets = TextEditorUtils.getCharsets();
		var chset = (Charset) JOptionPane.showInputDialog(frame, localize.translate("select_charset_input"), localize.translate("command_name"), JOptionPane.QUESTION_MESSAGE, null, chsets, chsets[0]);
		if (chset != null) {
			final var file = editor.getFile();
			editor.setEncoding(chset);
			try {
				editor.read(file);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(frame, localize.translate("failed_to_open_msg", file), localize.translate("command_name"), JOptionPane.WARNING_MESSAGE);
				throw ex;
			}
		}
	}
}
