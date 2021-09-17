/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.swing.*;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.TextEditorUtils;
import leaf.util.Dump;
import leaf.util.LocalizeManager;

/**
 * 16進ダンプを表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class HexDump extends EditorCommand {
	private static final LocalizeManager localize = LocalizeManager.get(HexDump.class);

	@Override
	public void process(Object... args) throws IOException {
		var file = getEditor().getFile();
		if (file == null) file = History.getLastFile();
		var chooser = getFileChooser();
		chooser.setSelectedFile(file);
		chooser.setCurrentDirectory(file.getParentFile());
		if (chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			file = chooser.getSelectedFile();
			History.addRecentFile(file.getAbsolutePath());
			dump(file);
		}
	}

	/**
	 * 指定されたファイルをダンプしてエディタに表示します。
	 *
	 * @param file ダンプするファイル
	 *
	 * @throws IOException ダンプに失敗した場合
	 */
	public static void dump(File file) throws IOException {
		var editor = TextEditorUtils.addTab(file.getName() + ".dump");
		try {
			editor.read(new Dump((new FileInputStream(file))));
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(getFrame(), localize.translate("failed_to_dump", file), localize.translate("command_name"), JOptionPane.WARNING_MESSAGE);
			throw ex;
		}
	}
}
