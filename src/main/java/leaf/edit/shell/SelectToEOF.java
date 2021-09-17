/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * ファイル末尾まで選択するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SelectToEOF extends EditorCommand {
	@Override
	public void process(Object... args) {
		getEditor().getTextPane().setSelectionEnd(getDocument().getLength());
	}
}
