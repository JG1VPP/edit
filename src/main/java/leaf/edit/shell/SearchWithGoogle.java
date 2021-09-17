/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.SelectionCommand;
import leaf.util.BrowseUtils;
import leaf.util.GoogleUtils;

/**
 * 選択文字列をGoogleで検索するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SearchWithGoogle extends SelectionCommand {

	@Override
	public void process(Object... args) {
		BrowseUtils.browse(GoogleUtils.createSearchURI(getEditor().getSelectedText()));
	}
}
