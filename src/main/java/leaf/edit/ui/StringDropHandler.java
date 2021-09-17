/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import static java.awt.datatransfer.DataFlavor.stringFlavor;


/**
 * tseditのエディタに対する文字列のドロップを検出します。
 *
 * @author 無線部開発班
 */
@SuppressWarnings("serial")
public class StringDropHandler extends ClipboardTransferHandler {
	private int selectionStart, selectionEnd;

	@Override
	protected Transferable createTransferable(JComponent comp) {
		var jTextComponent = ((JTextComponent) comp);
		var selected = jTextComponent.getSelectedText();
		selectionStart = jTextComponent.getSelectionStart();
		selectionEnd = jTextComponent.getSelectionEnd();
		return new StringSelection(selected);
	}

	@Override
	public boolean importData(TransferSupport support) {
		if (support.isDataFlavorSupported(stringFlavor)) {
			return importStringData(support);
		}
		return false;
	}

	private boolean importDroppedStringData(TransferSupport support) {
		var transferable = support.getTransferable();
		try {
			var data = String.valueOf(transferable.getTransferData(stringFlavor));
			var jTextComponent = (JTextComponent) support.getComponent();
			var insertTo = jTextComponent.getDropLocation().getIndex();
			if (insertTo < selectionStart) {
				var doc = jTextComponent.getDocument();
				doc.remove(selectionStart, selectionEnd - selectionStart);
				doc.insertString(insertTo, data, null);
				jTextComponent.select(insertTo, insertTo + data.length());
				return true;
			}
			if (insertTo > selectionEnd) {
				var doc = jTextComponent.getDocument();
				doc.insertString(insertTo, data, null);
				jTextComponent.select(insertTo, insertTo + data.length());
				doc.remove(selectionStart, selectionEnd - selectionStart);
				return true;
			}
			return false;
		} catch (Exception ex) {
			return false;
		}
	}

	private boolean importStringData(TransferSupport support) {
		if (!support.isDrop()) return super.importData(support);
		return importDroppedStringData(support);
	}
}
