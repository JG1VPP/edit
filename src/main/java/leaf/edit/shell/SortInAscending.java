/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import javax.swing.text.BadLocationException;

import leaf.edit.cmd.EditorCommand;

/**
 * 選択行を昇順ソートするコマンドです。
 *
 * @author 無線部開発班
 */
public final class SortInAscending extends Sort {
	@Override
	public void process(Object... args) throws BadLocationException {
		var textpane = EditorCommand.getEditor().getTextPane();
		var root = EditorCommand.getDocument().getDefaultRootElement();
		var start = root.getElementIndex(textpane.getSelectionStart());
		var end = root.getElementIndex(textpane.getSelectionEnd() - 1);
		var lines = sort(start, end);
		if (lines != null) {
			var sb = new StringBuilder(lines[0]);
			for (var i = 1; i < lines.length; i++) {
				sb.append("\n").append(lines[i]);
			}
			textpane.select(root.getElement(start).getStartOffset(), root.getElement(end).getEndOffset() - 1);
			textpane.replaceSelection(sb.toString());
		}
	}
}
