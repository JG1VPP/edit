/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.awt.*;

import leaf.edit.app.TsEditApp;
import leaf.edit.cmd.Command;
import leaf.edit.ui.EditorListTask;
import leaf.edit.ui.TextEditor;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;
import leaf.swing.LeafFontDialog;
import leaf.util.Properties;

/**
 * テキストエディタの表示に用いられるフォントを設定します。
 *
 * @author 無線部開発班
 */
public final class SetFont extends Command implements LocaleListener {
	private static final Font DEFAULT;
	private static final Properties properties;

	static {
		DEFAULT = new Font(Font.MONOSPACED, Font.PLAIN, 12);
		properties = Properties.getInstance(SetFont.class);
	}

	private LeafFontDialog dialog;

	@Override
	public void localeChanged(LocaleEvent e) {
		if (dialog != null) dialog.initialize();
	}

	@Override
	public void process(Object... args) {
		if (dialog == null) {
			var frame = TsEditApp.getMainFrame();
			dialog = new LeafFontDialog(frame);
		}
		var font = dialog.showDialog(getFont());
		if (font != null) setFont(font);
	}

	/**
	 * エディタが使用する表示フォントを返します。
	 *
	 * @return 表示フォント
	 */
	public static Font getFont() {
		return properties.get("font", Font.class, DEFAULT);
	}

	/**
	 * エディタの表示フォントを新しいフォントで代替します。
	 *
	 * @param font 新しく適用するフォント
	 */
	public static void setFont(final Font font) {
		properties.put("font", font);
		new EditorListTask<>(TextEditor.class) {
			@Override
			public boolean process(TextEditor editor) {
				editor.setFont(font);
				return false;
			}
		}.start();
	}

}
