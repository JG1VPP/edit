/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.File;
import java.io.IOException;
import javax.swing.*;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.TextEditorUtils;
import leaf.util.Find;
import leaf.util.LocalizeManager;

/**
 * エディタの内容を名前を付けて保存するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SaveAs extends EditorCommand {
	private static SaveAs instance;
	private final LocalizeManager localize;

	public SaveAs() {
		instance = this;
		localize = LocalizeManager.get(SaveAs.class);
	}

	@Override
	public void process(Object... args) {
		save();
	}

	private boolean saveFile() {
		var file = getEditor().getFile();
		if (file == null) {
			var tab = TextEditorUtils.getTabbedPane();
			var title = tab.getTitleAt(tab.getSelectedIndex());
			file = new File(getFileChooser().getCurrentDirectory(), title);
			if (!Find.hasSuffix(file)) file = new File(file + ".txt");
		}
		return showSaveDialog(file);
	}

	private boolean showSaveDialog(File file) {
		var chooser = getFileChooser();
		chooser.setSelectedFile(file);
		chooser.setCurrentDirectory(file.getParentFile());
		if (chooser.showSaveDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			if ((file = chooser.getSelectedFile()).canWrite()) {
				var opt = JOptionPane.showConfirmDialog(getFrame(), localize.translate("overwrite_confirm", file), localize.translate("command_name"), JOptionPane.YES_NO_OPTION);
				if (opt != JOptionPane.YES_OPTION) return false;
			}
			var editor = getEditor();
			editor.setEncoding(chooser.getSelectedCharset());
			TextEditorUtils.updateMainFrameTitle();
			TextEditorUtils.updateTabTitle();
			History.addRecentFile(file.getPath());
			try {
				getEditor().write(file);
				return true;
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(getFrame(), localize.translate("failed_to_save_msg", file), localize.translate("command_name"), JOptionPane.WARNING_MESSAGE);
			}
		}
		return false;
	}

	/**
	 * ファイル選択画面を表示して名前を付けて保存します。
	 *
	 * @return ファイルの保存が完了した場合true
	 */
	public static boolean save() {
		return instance.saveFile();
	}
}
