/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.TextEditorUtils;

/**
 * エディタの内容を上書き保存してからタブを閉じるコマンドです。
 *
 * @author 無線部開発班
 */
public final class SaveAndCloseTab extends EditorCommand {
	@Override
	public void process(Object... args) {
		if (Save.save()) CloseTab.close(TextEditorUtils.getSelectedEditor());
	}
}
