/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * エディタ全体の文字列を選択するコマンドです。
 */
public final class SelectAll extends EditorCommand {
	@Override
	public void process(Object... args) {
		getEditor().getTextPane().selectAll();
	}
}
