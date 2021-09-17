/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.EditorListTask;

/**
 * 開かれている全てのタブを閉じるコマンドです。
 *
 * @author 無線部開発班
 */
public final class CloseAllTabs extends Command {
	@Override
	public void process(Object... args) {
		closeAllTabs();
	}

	/**
	 * エディタの全てのタブを閉じようと試みます。
	 *
	 * @return 全てのタブが閉じられた場合true
	 */
	public static boolean closeAllTabs() {
		return !new EditorListTask<>(BasicTextEditor.class) {
			@Override
			public boolean process(BasicTextEditor editor) {
				return !CloseTab.close(editor);
			}
		}.start();
	}
}
