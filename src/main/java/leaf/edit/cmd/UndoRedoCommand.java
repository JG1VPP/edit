/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.cmd;

import leaf.edit.shell.Redo;
import leaf.edit.shell.Undo;
import leaf.edit.ui.TextEditorUtils;

/**
 * エディタの内容を元に戻す、またはやり直すコマンドです。
 *
 * @author 無線部開発班
 * @since 2012/03/31
 */
public abstract class UndoRedoCommand extends EditorCommand {
	private static Undo undo;
	private static Redo redo;

	public UndoRedoCommand() {
		if (this instanceof Undo) undo = (Undo) this;
		if (this instanceof Redo) redo = (Redo) this;
	}


	public static synchronized void updateUndoRedoEnabled() {
		var editor = TextEditorUtils.getSelectedEditor();
		if (undo != null) undo.setEnabled(editor.canUndo());
		if (redo != null) redo.setEnabled(editor.canRedo());
	}

}
