/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Logger;

import leaf.edit.app.TsEditApp;
import leaf.edit.shell.SetSyntaxHighlight;
import leaf.swing.SyntaxManager;
import leaf.util.Find;
import leaf.util.Properties;

/**
 * エディタに構文強調を適用します。
 */
public final class SyntaxHighlight {
	private static final Properties properties;
	private static SyntaxManager manager = null;

	static {
		properties = Properties.getInstance(SetSyntaxHighlight.class);
	}

	/**
	 * 構文強調管理オブジェクトを返します。
	 *
	 * @return 構文強調管理オブジェクト
	 */
	public static SyntaxManager getSyntaxManager() {
		return manager;
	}

	/**
	 * 構文強調管理オブジェクトを初期化します。
	 */
	public static void load() {
		manager = new SyntaxManager();
		try {
			manager.load(new File("syntax.xml"));
		} catch (IOException ex) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(ex.toString());
		}
		loadColor("normal", Color.BLACK);
		loadColor("keyword", Color.BLUE);
		loadColor("quote", Color.RED);
		loadColor("comment", new Color(0xff666666));
	}

	private static void loadColor(String key, Color defaultColor) {
		var color = properties.get(key, Color.class, defaultColor);
		SyntaxManager.putColor(key, color);
	}

	/**
	 * 構文強調設定をローカルに保存します。
	 */
	public static void save() {
		try {
			manager.save(new File("syntax.xml"));
		} catch (FileNotFoundException ex) {
			Logger.getLogger(Logger.GLOBAL_LOGGER_NAME).warning(ex.toString());
		}
	}

	private static void saveColor(String key) {
		properties.put(key, SyntaxManager.getColor(key));
	}

	/**
	 * 構文強調設定の管理画面を表示します。
	 *
	 * @since 2012/12/29
	 */
	public static void showConfigurationDialog() {
		var manager = SyntaxHighlight.getSyntaxManager();
		var frame = TsEditApp.getMainFrame();
		if (!new SyntaxDialog(frame, manager).showDialog()) return;
		new EditorListTask<>(BasicTextEditor.class) {
			@Override
			public boolean process(BasicTextEditor editor) {
				SyntaxHighlight.update(editor);
				return false;
			}
		}.start();
		saveColor("normal");
		saveColor("quote");
		saveColor("keyword");
		saveColor("comment");
		save();
	}

	/**
	 * エディタに構文強調を適用します。
	 *
	 * @param editor 構文強調するエディタ
	 *
	 * @throws IOException 構文強調設定の読み込みに失敗した場合
	 */
	public static void update(BasicTextEditor editor) {
		var manager = getSyntaxManager();
		var doc = editor.getDocument();
		var suffix = Find.getSuffix(editor.getFile());
		doc.setKeywordSet(manager.getKeywordSetByExtension(suffix));
		doc.update();
	}
}
