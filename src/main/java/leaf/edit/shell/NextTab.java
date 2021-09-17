/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.edit.ui.TextEditorUtils;

/**
 * 次のタブを選択するコマンドです。
 *
 * @author 無線部開発班
 * @since 2013/02/26
 */
public final class NextTab extends Command {
	@Override
	public void process(Object... args) {
		var tab = TextEditorUtils.getTabbedPane();
		var index = tab.getSelectedIndex() + 1;
		if (index >= tab.getTabCount()) index = 0;
		tab.setSelectedIndex(index);
	}

}
