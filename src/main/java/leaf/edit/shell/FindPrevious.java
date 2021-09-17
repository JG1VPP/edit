/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;
import leaf.swing.LeafSearchDialog;

/**
 * キャレット位置よりも前で文字列を検索するコマンドです。
 *
 * @author 無線部開発班
 */
public final class FindPrevious extends EditorCommand {
	private final LeafSearchDialog dialog;

	public FindPrevious(Find find) {
		this.dialog = find.dialog;
	}

	@Override
	public void process(Object... args) {
		dialog.search(LeafSearchDialog.Ward.SEARCH_UPWARD);
	}

}
