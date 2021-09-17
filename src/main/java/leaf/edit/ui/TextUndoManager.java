/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import javax.swing.event.DocumentEvent;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import leaf.edit.cmd.UndoRedoCommand;


/**
 * エディタの編集履歴を保管するマネージャです。
 *
 * @author 無線部開発班
 */
@SuppressWarnings("serial")
public class TextUndoManager extends UndoManager {
	private final BasicTextEditor editor;

	public TextUndoManager(BasicTextEditor editor) {
		this.editor = editor;
	}

	@Override
	public synchronized boolean addEdit(UndoableEdit edit) {
		var retval = super.addEdit(edit);
		if (edit instanceof DocumentEvent) {
			var ed = (DocumentEvent) edit;
			if (ed.getType() == DocumentEvent.EventType.CHANGE) {
				return retval;
			}
		}
		editor.isEdited = true;
		UndoRedoCommand.updateUndoRedoEnabled();
		return retval;
	}
}
