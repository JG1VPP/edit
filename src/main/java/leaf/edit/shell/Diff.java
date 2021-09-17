/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.StringReader;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.DiffDialog;
import leaf.edit.ui.TextEditorUtils;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;

public final class Diff extends EditorCommand implements LocaleListener {
	private DiffDialog dialog;

	@Override
	public void localeChanged(LocaleEvent e) {
		if (dialog != null) dialog.initialize();
	}

	@Override
	public void process(Object... args) throws Exception {
		if (dialog == null) dialog = new DiffDialog(getFrame());
		var editor = getEditor();
		if (dialog.showDialog()) {
			editor = TextEditorUtils.addTab("DIFF");
			var result = dialog.getResult();
			editor.read(new StringReader(result.toString()));
		}
	}
}
