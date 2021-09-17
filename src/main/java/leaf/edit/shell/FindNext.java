/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;
import leaf.swing.LeafSearchDialog;

/**
 * キャレット位置より後ろで文字列を検索するコマンドです。
 *
 * @author 無線部開発班
 */
public final class FindNext extends EditorCommand {
	private final LeafSearchDialog dialog;

	public FindNext(Find find) {
		this.dialog = find.dialog;
	}

	@Override
	public void process(Object... args) {
		dialog.search(LeafSearchDialog.Ward.SEARCH_DOWNWARD);
	}

}
