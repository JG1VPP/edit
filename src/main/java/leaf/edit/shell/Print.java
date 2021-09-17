/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.text.MessageFormat;
import javax.swing.*;

import leaf.edit.cmd.EditorCommand;

/**
 * エディタの内容を印刷するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Print extends EditorCommand {
	@Override
	public void process(Object... args) {
		new PrintWorker().execute();
	}

	private static class PrintWorker extends SwingWorker<String, String> {
		@Override
		protected String doInBackground() throws Exception {
			var editor = getEditor();
			var file = editor.getFile();
			var title = file != null ? file.getAbsolutePath() : "untitled";
			editor.getTextPane().print(new MessageFormat(title), new MessageFormat("{0}"));
			return "Done";
		}
	}
}
