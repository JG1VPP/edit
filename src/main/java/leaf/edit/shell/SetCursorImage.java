/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.awt.*;

import leaf.edit.cmd.Command;
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.EditorListTask;
import leaf.swing.LeafDialog;
import leaf.util.Properties;
import leaf.swing.CursorDialog;
import leaf.swing.CursorFactory;
import leaf.swing.CursorSettings;

/**
 * カーソルに表示する画像を設定するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SetCursorImage extends Command {
	private static final Properties properties = Properties.getInstance(SetCursorImage.class);
	private static CursorSettings cs = null;
	private static Cursor cursor = null;

	@Override
	public void process(Object... args) {
		var frame = getFrame();
		var dialog = new CursorDialog(frame);
		if (dialog.showDialog(cs) == LeafDialog.OK_OPTION) {
			properties.put("settings", cs = dialog.getSettings());
			cursor = CursorFactory.createCursor(cs);
			new EditorListTask<>(BasicTextEditor.class) {
				@Override
				public boolean process(BasicTextEditor editor) {
					setTextCursor(editor, cursor);
					return false;
				}
			}.start();
		}
	}

	/**
	 * カーソルをエディタに設定します。
	 *
	 * @param editor カーソルを表示するエディタ
	 */
	public static void install(BasicTextEditor editor) {
		if (cursor == null) {
			(cs = new CursorSettings()).setType(Cursor.TEXT_CURSOR);
			cs = properties.get("settings", CursorSettings.class, cs);
			if (cursor == null) cursor = CursorFactory.createCursor(cs);
		}
		setTextCursor(editor, cursor);
	}

	private static void setTextCursor(BasicTextEditor editor, Cursor cursor) {
		for (var textpane : editor.getTextPaneList()) {
			textpane.setCursor(cursor);
		}
	}
}
