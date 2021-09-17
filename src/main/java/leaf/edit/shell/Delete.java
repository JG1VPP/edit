/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.SelectionCommand;

/**
 * 削除コマンドを処理します。
 */
public final class Delete extends SelectionCommand {
	@Override
	public void process(Object... args) {
		getEditor().getTextPane().replaceSelection("");
	}
}
