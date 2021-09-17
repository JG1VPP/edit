/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * 現在キャレットがある行の末尾まで選択するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SelectToEOL extends EditorCommand {
	@Override
	public void process(Object... args) {
		var textpane = getEditor().getTextPane();
		var root = textpane.getDocument().getDefaultRootElement();
		var line = root.getElementIndex(textpane.getCaretPosition());
		var end = root.getElement(line).getEndOffset();
		textpane.setSelectionEnd(end - 1);
	}
}
