/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.io.File;
import java.io.IOException;
import javax.swing.*;

import leaf.edit.cmd.Command;
import leaf.swing.HistoryMenuEvent;
import leaf.swing.HistoryMenuListener;
import leaf.swing.LeafHistoryMenu;
import leaf.util.Properties;

/**
 * ファイル履歴機能を提供するコマンドです。
 *
 * @author 無線部開発班
 * @since 2013/02/01
 */
public final class History extends Command {
	private static final History instance = new History();
	private final Properties prop;
	private LeafHistoryMenu menu;

	private History() {
		prop = Properties.getInstance(getClass());
	}

	@Override
	public JMenuItem createMenuItem(JMenuItem item) {
		menu = new LeafHistoryMenu();
		menu.addHistoryMenuListener(new MenuHandler());
		menu.addAll(prop.get("files", Object[].class, null));
		return menu;
	}

	@Override
	public void process(Object... args) {
	}

	/**
	 * 新しいファイルを履歴に追加します。
	 *
	 * @param path 追加するパス
	 */
	public static void addRecentFile(String path) {
		instance.menu.addItem(path);
		instance.prop.put("files", getRecentFiles());
	}

	/**
	 * コマンドのインスタンスを返します。
	 *
	 * @return インスタンス
	 */
	public static History getInstance() {
		return instance;
	}

	/**
	 * 一番最後に履歴に追加されたファイルを返します。
	 *
	 * @return 最後のファイル
	 */
	public static File getLastFile() {
		var files = instance.menu.getAll();
		if (files.length == 0) return new File("");
		return new File(String.valueOf(files[0]));
	}

	/**
	 * ファイル履歴を返します。
	 *
	 * @return ファイル履歴
	 */
	public static Object[] getRecentFiles() {
		return instance.menu.getAll();
	}

	private static class MenuHandler implements HistoryMenuListener {
		@Override
		public void historyClicked(HistoryMenuEvent e) {
			try {
				Open.open(new File(e.getItem().toString()));
			} catch (IOException ex) {
			}
		}
	}

}
