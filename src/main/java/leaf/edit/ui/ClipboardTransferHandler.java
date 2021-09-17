/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;

import static java.awt.datatransfer.DataFlavor.stringFlavor;

/**
 * tseditのエディタに対するクリップボード操作を検出します。
 *
 * @author 無線部開発班
 */
@SuppressWarnings("serial")
public class ClipboardTransferHandler extends TransferHandler {

	public ClipboardTransferHandler() {
		super("text");
	}

	@Override
	public void exportToClipboard(JComponent comp, Clipboard clip, int action) throws IllegalStateException {
		if (comp instanceof JTextComponent) try {
			var textComp = (JTextComponent) comp;
			var doc = textComp.getDocument();
			var s = textComp.getSelectionStart();
			var e = textComp.getSelectionEnd();
			if (s == e) {
				var root = doc.getDefaultRootElement();
				var line = root.getElement(root.getElementIndex(s));
				s = line.getStartOffset();
				e = line.getEndOffset() - 1;
			}
			var data = doc.getText(s, e - s);
			clip.setContents(new StringSelection(data), null);
			if (action == TransferHandler.MOVE) doc.remove(s, e - s);

		} catch (BadLocationException ex) {
		}
	}

	@Override
	public boolean importData(TransferSupport support) {
		var comp = (JTextComponent) support.getComponent();
		var ic = comp.getInputContext();
		if (ic != null) ic.endComposition();
		var transferable = support.getTransferable();
		try {
			var data = transferable.getTransferData(stringFlavor);
			comp.replaceSelection(String.valueOf(data));
			return true;
		} catch (UnsupportedFlavorException | IOException ex) {
			return false;
		}
	}

	@Override
	public boolean canImport(TransferSupport support) {
		if (support.isDataFlavorSupported(stringFlavor)) {
			if (support.getComponent() instanceof JTextComponent) {
				var comp = (JTextComponent) support.getComponent();
				return comp.isEditable() && comp.isEnabled();
			}
		}
		return false;
	}

	@Override
	public int getSourceActions(JComponent comp) {
		return TransferHandler.COPY_OR_MOVE;
	}
}
