/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import javax.swing.*;

import leaf.edit.cmd.EditorCommand;
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.TextEditorUtils;
import leaf.util.LocalizeManager;

/**
 * 現在開いているタブを閉じようと試みるコマンドです。
 *
 * @author 無線部開発班
 * @since 2013/02/26
 */
public final class CloseTab extends EditorCommand {
	private static final LocalizeManager localize;

	static {
		localize = LocalizeManager.get(CloseTab.class);
	}

	@Override
	public void process(Object... args) {
		close(TextEditorUtils.getTabbedPane().getSelectedIndex());
	}

	/**
	 * 指定されたタブ番号のタブを閉じようと試みます。
	 *
	 * @param index タブ番号
	 *
	 * @return ユーザーによって操作が完全に許可された場合true
	 */
	public static boolean close(int index) {
		var tab = TextEditorUtils.getTabbedPane();
		return close((BasicTextEditor) tab.getComponentAt(index));
	}

	/**
	 * 指定されたエディタのタブを閉じようと試みます。
	 *
	 * @param editor エディタ
	 *
	 * @return ユーザーによって操作が完全に許可された場合true
	 */
	public static boolean close(BasicTextEditor editor) {
		if (!editor.close()) return false;
		TextEditorUtils.getTabbedPane().remove(editor);
		return true;
	}

	public static boolean prepareToClose(BasicTextEditor editor) {
		if (editor.isEdited()) {
			var opt = JOptionPane.showConfirmDialog(editor, localize.translate("file_is_edited_confirm"), localize.translate("command_name"), JOptionPane.YES_NO_CANCEL_OPTION);
			if (opt == JOptionPane.CANCEL_OPTION) return false;
			if (opt == JOptionPane.YES_OPTION && !Save.save()) return false;
		}
		if (editor.getFile() != null) {
			History.addRecentFile(editor.getFilePath());
		}
		return true;
	}

}
