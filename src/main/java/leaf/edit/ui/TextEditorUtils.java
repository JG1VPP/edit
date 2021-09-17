/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Map;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import leaf.edit.app.TsEditApp;
import leaf.edit.cmd.Command;
import leaf.edit.cmd.EditorCommand;
import leaf.edit.cmd.SelectionCommand;
import leaf.edit.cmd.UndoRedoCommand;
import leaf.edit.shell.SetCursorImage;
import leaf.edit.shell.SetTypingSound;
import leaf.edit.shell.SetWallpaper;
import leaf.swing.LeafTabbedPane;
import leaf.util.Properties;
import leaf.util.LocalizeManager;

/**
 * テキストエディタに関するユーティリティを提供します。
 *
 * @author 無線部開発班
 * @since 2012/03/28
 */
public final class TextEditorUtils {
	private static final LocalizeManager localize;
	private static final Properties properties;
	private static final TextCaretListener caretListener;
	private static LeafTabbedPane tabbedpane;

	static {
		localize = LocalizeManager.get(TextEditorUtils.class);
		properties = Properties.getInstance(TextEditorUtils.class);
		caretListener = new TextCaretListener();
	}

	/**
	 * テキストエディタを、メインウィンドウ内の指定されたタブ位置に追加します。
	 *
	 * @param editor 追加するテキストエディタ
	 * @param index  タブ番号 負整数を指定すると最後尾に追加する
	 *
	 * @return 追加されたテキストエディタ
	 */
	public static BasicTextEditor addTab(BasicTextEditor editor, int index) {
		var title = editor.getTitle();
		if (title == null) title = localize.translate("untitled");
		index = index >= 0 ? index : getTabbedPane().getTabCount();
		getTabbedPane().add(editor, title, index);
		editor.addCaretListener(caretListener);
		SetTypingSound.install(editor);
		SetCursorImage.install(editor);
		SetWallpaper.install(editor);
		return editor;
	}

	/**
	 * 指定された名前のテキストエディタを、ウィンドウ内の指定の位置に追加します。
	 *
	 * @param title タブに表示されるタイトル
	 * @param index タブ番号 負整数を指定すると最後尾に追加する
	 *
	 * @return 追加されたテキストエディタ
	 */
	public static BasicTextEditor addTab(String title, int index) {
		return addTab(new BasicTextEditor(title), index);
	}

	/**
	 * 指定された名前のテキストエディタを最後尾に追加します。
	 *
	 * @param title タブに表示されるタイトル
	 *
	 * @return 追加されたテキストエディタ
	 */
	public static BasicTextEditor addTab(String title) {
		return addTab(new BasicTextEditor(title), -1);
	}

	/**
	 * タイトルが「untitled」となるテキストエディタを最後尾に追加します。
	 *
	 * @return 追加されたテキストエディタ
	 */
	public static BasicTextEditor addTab() {
		return addTab(new BasicTextEditor(null), -1);
	}

	/**
	 * エディタで使用可能な文字セットを配列で返します。
	 *
	 * @return 文字セットの配列
	 */
	public static Charset[] getAvailableCharsets() {
		var list = new ArrayList<Charset>();
		Map<String, Charset> chsets = Charset.availableCharsets();
		for (var s : chsets.keySet()) list.add(chsets.get(s));
		return list.toArray(new Charset[0]);
	}

	/**
	 * エディタで使用する文字セットの配列を返します。
	 *
	 * @return 文字セットの配列
	 */
	public static Charset[] getCharsets() {
		var names = new String[]{"UTF-8", "UTF-16"};
		names = properties.get("charsets", String[].class, names);
		var chsets = new Charset[names.length];
		for (var i = 0; i < names.length; i++) {
			chsets[i] = Charset.forName(names[i]);
		}
		return chsets;
	}

	/**
	 * エディタで使用する文字セットの配列を設定します。
	 *
	 * @param chsets 文字セットの配列
	 */
	public static void setCharsets(Charset[] chsets) {
		var names = new String[chsets.length];
		for (var i = 0; i < chsets.length; i++) {
			names[i] = chsets[i].name();
		}
		properties.put("charsets", names);
	}

	/**
	 * エディタで改行を出力するのに用いる改行コードを返します。
	 *
	 * @return 改行コード
	 */
	public static String getLineSeparator() {
		var defval = System.getProperty("line.separator");
		return properties.get("line.separator", defval);
	}

	/**
	 * エディタで改行を出力するのに用いる改行コードを設定します。
	 *
	 * @param ls 改行コード
	 */
	public static void setLineSeparator(String ls) {
		properties.put("line.separator", ls);
	}

	/**
	 * エディタで使用する文字セットのうち最も優先度の高いものを返します。
	 *
	 * @return 文字セット
	 */
	public static Charset getPrimaryCharset() {
		var chsets = getCharsets();
		if (chsets.length > 0) return chsets[0];
		return StandardCharsets.UTF_8;
	}

	/**
	 * メインウィンドウ内で現在表示されているタブ内のコンポーネントを返します。
	 *
	 * @return タブ内のコンポーネント(通常はエディタ)
	 */
	public static Component getSelectedComponent() {
		return getTabbedPane().getSelectedComponent();
	}

	/**
	 * メインウィンドウ内で現在表示されているテキストエディタを返します。
	 *
	 * @return エディタ
	 *
	 * @throws IllegalStateException このプラグインが無効である場合
	 * @throws ClassCastException    テキストエディタでない場合
	 */
	public static BasicTextEditor getSelectedEditor() {
		return (BasicTextEditor) getSelectedComponent();
	}

	/**
	 * メインウィンドウ内で表示するテキストエディタを設定します。
	 *
	 * @param editor エディタ
	 */
	public static void setSelectedEditor(TextEditor editor) {
		getTabbedPane().setSelectedComponent(editor);
	}

	/**
	 * このアプリケーションが関連付けられている{@link LeafTabbedPane}を返します。
	 *
	 * @return タブ付きコンポーネント
	 *
	 * @throws IllegalStateException このアプリケーションが無効である場合
	 */
	public static LeafTabbedPane getTabbedPane() throws IllegalStateException {
		if (tabbedpane == null) {
			tabbedpane = new LeafTabbedPane();
			var centerpane = TsEditApp.getMainFrame().getCenterPane();
			centerpane.add(tabbedpane, BorderLayout.CENTER);
		}
		return tabbedpane;
	}

	/**
	 * タブ変更監視のためのリスナーを追加します。
	 */
	public static void setTabChangeListener() {
		getTabbedPane().addChangeListener(new TabChangeListener());
	}

	/**
	 * メインウィンドウのタイトルを適切な文字列に更新します。
	 */
	public static void updateMainFrameTitle() {
		var title = getSelectedEditor().getTitle();
		if (title == null) title = localize.translate("untitled");
		var frame = TsEditApp.getMainFrame();
		frame.setTitle(localize.translate("tsedit_localized_title", title));

	}

	/**
	 * ステータスバーの表示を適切な文字列に設定します。
	 */
	public static void updateStatusBar() {
		var editor = TextEditorUtils.getSelectedEditor();
		final var ln = editor.getLineNumber();
		final var cn = editor.getColumnNumber();
		var statusbar = TsEditApp.getMainFrame().getStatusBar();
		statusbar.setText(localize.translate("statusbar_text", ln, cn), 0);
		statusbar.setText(editor.getEncoding(), 1);
	}

	/**
	 * タブのタイトルを適切な文字列に更新します。
	 */
	public static void updateTabTitle() {
		final var index = getTabbedPane().getSelectedIndex();
		var title = getSelectedEditor().getTitle();
		var file = getSelectedEditor().getFile();
		if (title == null) title = localize.translate("untitled");
		if (file != null) {
			var path = file.getAbsolutePath();
			getTabbedPane().setTitleAt(index, title, path);
		} else getTabbedPane().setTitleAt(index, title);

	}

	private static final class TextCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			SwingUtilities.invokeLater(new Updator(e.getDot() != e.getMark()));
		}

		private static class Updator implements Runnable {
			private final boolean selected;

			public Updator(boolean selected) {
				this.selected = selected;
			}

			@Override
			public void run() {
				updateStatusBar();
				Command.setAllEnabled(SelectionCommand.class, selected);
			}
		}
	}

	static final class TabChangeListener implements ChangeListener {
		@Override
		public void stateChanged(ChangeEvent e) {
			if (getTabbedPane().getTabCount() == 0) addTab();
			var tab = getSelectedComponent();
			if (tab instanceof BasicTextEditor) {
				var editor = (BasicTextEditor) tab;
				var pane = editor.getTextPane();
				final var from = pane.getSelectionStart();
				final var to = pane.getSelectionEnd();
				final var selected = from != to;
				Command.setAllEnabled(EditorCommand.class, true);
				Command.setAllEnabled(SelectionCommand.class, selected);
				UndoRedoCommand.updateUndoRedoEnabled();
				updateMainFrameTitle();
				updateStatusBar();
			} else Command.setAllEnabled(EditorCommand.class, false);
		}
	}
}
