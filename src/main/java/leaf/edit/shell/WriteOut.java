/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.File;
import java.io.IOException;
import javax.swing.*;

import leaf.edit.cmd.SelectionCommand;
import leaf.util.LocalizeManager;

/**
 * 選択文字列をファイルに保存するコマンドです。
 *
 * @author 無線部開発班
 */
public final class WriteOut extends SelectionCommand {
	private final LocalizeManager localize = LocalizeManager.get(WriteOut.class);

	@Override
	public void process(Object... args) {
		var file = getEditor().getFile();
		var chooser = getFileChooser();
		if (file != null) {
			chooser.setSelectedFile(file);
			chooser.setCurrentDirectory(file.getParentFile());

		} else {
			file = chooser.getCurrentDirectory();
			var untitled = localize.translate("untitled_filename");
			chooser.setSelectedFile(new File(file, untitled));
		}
		if (chooser.showSaveDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			if (file.exists()) {
				var opt = JOptionPane.showConfirmDialog(getFrame(), localize.translate("overwrite_confirm", file), localize.translate("command_name"), JOptionPane.YES_NO_OPTION);
				if (opt != JOptionPane.YES_OPTION) return;
			}
			try {
				var chset = chooser.getSelectedCharset();
				getEditor().writeFromSelection(file, chset);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(getFrame(), localize.translate("failed_to_save_msg", file), localize.translate("command_name"), JOptionPane.WARNING_MESSAGE);
			}
		}
	}
}
