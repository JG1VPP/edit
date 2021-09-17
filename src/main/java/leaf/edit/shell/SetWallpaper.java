/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.IOException;

import leaf.edit.cmd.Command;
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.EditorListTask;
import leaf.swing.LeafDialog;
import leaf.util.Properties;
import leaf.swing.Wallpaper;
import leaf.swing.WallpaperDialog;
import leaf.swing.WallpaperSettings;

/**
 * テキストエディタに表示する壁紙を設定するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SetWallpaper extends Command {
	private static final Properties properties;
	private static WallpaperSettings ws = null;
	private static Wallpaper paper = null;

	static {
		properties = Properties.getInstance(SetWallpaper.class);
	}

	@Override
	public void process(Object... args) throws IOException {
		var frame = getFrame();
		var dialog = new WallpaperDialog(frame);
		if (dialog.showDialog(ws) == LeafDialog.OK_OPTION) {
			properties.put("settings", ws = dialog.getSettings());
			paper = new Wallpaper(ws);
			new EditorListTask<>(BasicTextEditor.class) {
				@Override
				public boolean process(BasicTextEditor editor) {
					editor.setViewportBorder(paper);
					return false;
				}
			}.start();
		}
	}

	/**
	 * エディタに壁紙を設定します。
	 *
	 * @param editor 壁紙を表示するエディタ
	 */
	public static void install(BasicTextEditor editor) {
		ws = new WallpaperSettings();
		ws = properties.get("settings", WallpaperSettings.class, ws);
		if (paper == null) try {
			paper = new Wallpaper(ws);
		} catch (IOException ex) {
		}
		editor.setViewportBorder(paper);
	}

}
