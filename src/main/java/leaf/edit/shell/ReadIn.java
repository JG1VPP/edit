/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.IOException;
import javax.swing.*;

import leaf.edit.cmd.EditorCommand;
import leaf.util.LocalizeManager;

/**
 * ファイルを選択部分に読み込むコマンドです。
 *
 * @author 無線部開発班
 */
public final class ReadIn extends EditorCommand {
	private final LocalizeManager localize = LocalizeManager.get(ReadIn.class);

	@Override
	public void process(Object... args) throws IOException {
		var file = getEditor().getFile();
		if (file == null) file = History.getLastFile();
		var chooser = getFileChooser();
		chooser.setSelectedFile(file);
		chooser.setCurrentDirectory(file.getParentFile());
		if (chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			if (!file.canRead()) {
				var opt = JOptionPane.showConfirmDialog(getFrame(), localize.translate("file_not_found_msg", file), localize.translate("command_name"), JOptionPane.YES_NO_OPTION);
				if (opt == JOptionPane.YES_OPTION) process(args);
				return;
			}
			try {
				var chset = chooser.getSelectedCharset();
				getEditor().readIntoSelection(file, chset);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(getFrame(), localize.translate("failed_to_open_msg", file), localize.translate("command_name"), JOptionPane.WARNING_MESSAGE);
				throw ex;
			}
		}
	}
}
