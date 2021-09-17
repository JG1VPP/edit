/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.awt.*;

import leaf.edit.cmd.EditorCommand;

/**
 * 次のページにキャレットを移動するコマンドです。
 *
 * @author 無線部開発班
 * @since 2013/01/02
 */
public final class MoveToNextPage extends EditorCommand {
	@Override
	public void process(Object... args) {
		var editor = getEditor();
		var scroll = editor.getScrollPane();
		var root = editor.getDocument().getDefaultRootElement();
		var now = scroll.getVerticalScrollBar().getValue();
		var height = scroll.getViewport().getHeight() + 5;
		var position = editor.getTextPane().viewToModel(new Point(0, now + height));
		scroll.scrollToLine(1 + root.getElementIndex(position));
	}
}
