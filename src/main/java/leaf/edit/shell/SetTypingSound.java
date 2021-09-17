/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.File;
import java.io.IOException;

import leaf.edit.cmd.Command;
import leaf.edit.ui.BasicTextEditor;
import leaf.swing.LeafDialog;
import leaf.util.Properties;
import leaf.swing.TypingSoundDialog;
import leaf.swing.TypingSoundPlayer;

/**
 * テキストエディタでキーをタイプした際に流れるメロディを設定するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SetTypingSound extends Command {
	private static final TypingSoundPlayer player;
	private static final Properties properties;

	static {
		player = new TypingSoundPlayer();
		properties = Properties.getInstance(SetTypingSound.class);
	}

	/**
	 * コマンドを初期化します。
	 */
	public SetTypingSound() {
		try {
			player.load(getFile());
		} catch (IOException ex) {
		}
	}

	@Override
	public void process(Object... args) throws Exception {
		var dialog = new TypingSoundDialog(getFrame());
		dialog.setSelectedFile(getFile());
		if (dialog.showDialog() == LeafDialog.OK_OPTION) {
			var file = dialog.getSelectedFile();
			properties.put("file", file != null ? file.getPath() : null);
			player.load(file);
		}
	}

	/**
	 * tseditで使用するタイピング音のファイルを返します。
	 *
	 * @return 読み込むファイル
	 */
	public static File getFile() {
		var path = properties.get("file", String.class, null);
		return path != null ? new File(path) : null;
	}

	/**
	 * 指定されたエディタにタイピング音を適用します。
	 *
	 * @param editor タイピング音を再生するエディタ
	 */
	public static void install(BasicTextEditor editor) {
		editor.addKeyListener(player);
	}
}
