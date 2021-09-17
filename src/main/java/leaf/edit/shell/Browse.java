/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.TextEditorUtils;
import leaf.util.BrowseUtils;

/**
 * ファイルを閲覧するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Browse extends EditorCommand {
	@Override
	public void process(Object... args) {
		var file = TextEditorUtils.getSelectedEditor().getFile();
		if (file != null) BrowseUtils.browse(file.toURI());
	}
}
