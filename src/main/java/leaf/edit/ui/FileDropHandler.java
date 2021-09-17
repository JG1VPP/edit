/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import leaf.edit.shell.Open;

import static java.awt.datatransfer.DataFlavor.javaFileListFlavor;


/**
 * tseditのエディタに対するファイルのドロップを検出します。
 *
 * @author 無線部開発班
 */
@SuppressWarnings("serial")
public class FileDropHandler extends StringDropHandler {

	@Override
	public boolean canImport(TransferSupport support) {
		if (super.canImport(support)) return true;
		return support.isDataFlavorSupported(javaFileListFlavor);
	}

	@Override
	public boolean importData(TransferSupport support) {
		if (support.isDataFlavorSupported(javaFileListFlavor)) {
			return importFileListData(support);
		}
		return super.importData(support);
	}

	private boolean importFileListData(TransferSupport support) {
		var transferable = support.getTransferable();
		if (support.isDrop()) try {
			var data = transferable.getTransferData(javaFileListFlavor);
			for (Object file : (List<?>) data) {
				if (file instanceof File) Open.open((File) file);
			}
			return false;
		} catch (UnsupportedFlavorException | IOException ex) {
			return false;
		}
		else return false;
	}
}
