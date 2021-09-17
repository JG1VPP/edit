/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.util.Arrays;
import javax.swing.text.BadLocationException;

import leaf.edit.cmd.SelectionCommand;

/**
 * 選択領域の文字列を行ごとにソートするコマンドです。
 *
 * @author 無線部開発班
 */
abstract class Sort extends SelectionCommand {

	protected String[] sort(int start, int end) throws BadLocationException {
		if (start > end) throw new IllegalArgumentException();
		var doc = getDocument();
		var root = doc.getDefaultRootElement();
		var lines = new String[end - start + 1];
		for (var i = start; i <= end; i++) {
			var elem = root.getElement(i);
			var s = elem.getStartOffset();
			var e = elem.getEndOffset() - 1;
			lines[i - start] = doc.getText(s, e - s);
		}
		Arrays.sort(lines);
		return lines;
	}
}
