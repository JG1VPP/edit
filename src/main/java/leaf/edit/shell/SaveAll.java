/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.EditorListTask;
import leaf.edit.ui.TextEditorUtils;

/**
 * 現在開かれている全てのエディタの内容を保存するコマンドです。
 *
 * @author 無線部開発班
 * @since 2012/03/28
 */
public final class SaveAll extends Command {
	@Override
	public void process(Object... args) {
		final var tab = TextEditorUtils.getTabbedPane();
		new EditorListTask<>(BasicTextEditor.class) {
			@Override
			public boolean process(BasicTextEditor editor) {
				tab.setSelectedComponent(editor);
				Save.save();
				return false;
			}
		}.start();
	}

}
