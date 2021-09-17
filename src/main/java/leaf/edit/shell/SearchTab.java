/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.edit.ui.TabFindDialog;
import leaf.edit.ui.TextEditorUtils;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;

/**
 * タブを検索するダイアログを表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SearchTab extends Command implements LocaleListener {
	private TabFindDialog dialog;

	@Override
	public void localeChanged(LocaleEvent e) {
		if (dialog != null) dialog.initialize();
	}

	@Override
	public void process(Object... args) {
		if (dialog == null) {
			dialog = new TabFindDialog(getFrame(), TextEditorUtils.getTabbedPane());
		}
		dialog.setVisible(true);
	}
}
