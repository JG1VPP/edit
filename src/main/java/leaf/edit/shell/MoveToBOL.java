/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * 行の先頭にキャレットを移動するコマンドです。
 *
 * @author 無線部開発班
 */
public final class MoveToBOL extends EditorCommand {
	@Override
	public void process(Object... args) {
		var textpane = getEditor().getTextPane();
		var root = textpane.getDocument().getDefaultRootElement();
		var index = root.getElementIndex(textpane.getCaretPosition());
		var offset = root.getElement(index).getStartOffset();
		textpane.setCaretPosition(offset);
	}
}
