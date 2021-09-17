/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.UndoRedoCommand;

/**
 * 前回元に戻した編集内容を再現するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Redo extends UndoRedoCommand {
	@Override
	public void process(Object... args) {
		getEditor().redo();
		updateUndoRedoEnabled();
	}
}
