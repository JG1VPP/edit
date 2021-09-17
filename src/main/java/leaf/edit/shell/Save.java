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
 * エディタの内容をファイルに上書き保存するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Save extends EditorCommand {
	private static Save instance;
	private final LocalizeManager localize;

	public Save() {
		instance = this;
		localize = LocalizeManager.get(Save.class);
	}

	@Override
	public void process(Object... args) {
		save();
	}

	private boolean saveFile() {
		final var editor = getEditor();
		final var file = editor.getFile();
		if (file == null) return SaveAs.save();
		try {
			editor.write(file);
			return true;
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(getFrame(), localize.translate("failed_to_save_msg", file), localize.translate("command_name"), JOptionPane.WARNING_MESSAGE);
			return false;
		}
	}

	/**
	 * エディタのファイルを上書き保存します。
	 *
	 * @return ファイルの保存が完了した場合true
	 */
	public static boolean save() {
		return instance.saveFile();
	}
}
