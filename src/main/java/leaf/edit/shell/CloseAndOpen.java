/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.IOException;
import javax.swing.*;

import leaf.edit.ui.TextEditorUtils;

/**
 * ファイルを閉じてから開くコマンドです。
 *
 * @author 無線部開発班
 */
public final class CloseAndOpen extends Open {
	@Override
	public void process(Object... args) throws IOException {
		// close
		final var old = TextEditorUtils.getTabbedPane().getSelectedIndex();
		CloseTab.close(getEditor());
		// open
		var file = getEditor().getFile();
		if (file == null) file = History.getLastFile();
		var chooser = getFileChooser();
		chooser.setSelectedFile(file);
		chooser.setCurrentDirectory(file.getParentFile());
		if (chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			open(chooser.getSelectedFile(), old);
		}
	}

}
