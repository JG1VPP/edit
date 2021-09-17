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
import leaf.edit.ui.BasicTextEditor;
import leaf.edit.ui.EditorListTask;
import leaf.edit.ui.TextEditorUtils;
import leaf.util.LocalizeManager;

/**
 * ファイルを開くコマンドの共通のスーパークラスです。
 *
 * @author 無線部開発班
 */
public class Open extends EditorCommand {
	private static final LocalizeManager localize;

	static {
		localize = LocalizeManager.get(Open.class);
	}

	@Override
	public void process(Object... args) throws Exception {
		open();
	}

	/**
	 * 指定されたファイルを開きます。
	 *
	 * @param file 開くファイル
	 *
	 * @throws IOException ファイルを開くのに失敗した場合
	 */
	public static final void open(File file) throws IOException {
		open(file, -1);
	}

	/**
	 * 指定したファイルを開きます。
	 *
	 * @param file  開くファイル
	 * @param index タブに追加する位置
	 *
	 * @throws IOException ファイルを開くのに失敗した場合
	 */
	public static final void open(File file, int index) throws IOException {
		if (!(file = file.getAbsoluteFile()).exists()) {
			var opt = JOptionPane.showConfirmDialog(getFrame(), localize.translate("file_not_found_msg", file), localize.translate("command_name"), JOptionPane.YES_NO_OPTION);
			if (opt == JOptionPane.YES_OPTION) open();
		} else {
			History.addRecentFile(file.getPath());
			switch (getFileChooser().getFileType(file)) {
				case TEXT:
					openAsTextFile(file, index);
					break;
				case IMAGE:
					Picture.view(file);
					break;
				case BINARY:
					HexDump.dump(file);
					break;
			}
		}
	}

	/**
	 * ファイル選択画面を表示します。
	 *
	 * @throws IOException ファイルを開くのに失敗した場合
	 */
	public static final void open() throws IOException {
		var file = getEditor().getFile();
		if (file == null) file = History.getLastFile();
		var chooser = getFileChooser();
		chooser.setSelectedFile(file);
		chooser.setCurrentDirectory(file.getParentFile());
		if (chooser.showOpenDialog(getFrame()) == JFileChooser.APPROVE_OPTION) {
			open(chooser.getSelectedFile(), -1);
		}
	}

	/**
	 * 指定したファイルをテキストファイルとして開きます。
	 *
	 * @param file  エディタで開くファイル
	 * @param index タブに追加する位置
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public static void openAsTextFile(File file, int index) throws IOException {
		if (!searchTab(file)) {
			var task = new SearchEmptyEditorTask();
			BasicTextEditor editor;
			if (task.start()) editor = task.getResult();
			else editor = TextEditorUtils.addTab((String) null, index);
			TextEditorUtils.setSelectedEditor(editor);
			editor.setEncoding(getFileChooser().getSelectedCharset());
			try {
				editor.read(file);
			} catch (IOException ex) {
				JOptionPane.showMessageDialog(getFrame(), localize.translate("failed_to_open_msg", file), localize.translate("command_name"), JOptionPane.WARNING_MESSAGE);
				throw ex;
			} finally {
				TextEditorUtils.updateMainFrameTitle();
				TextEditorUtils.updateTabTitle();
			}
		}
	}

	private static final boolean searchTab(final File file) {
		return new EditorListTask<>(BasicTextEditor.class) {
			@Override
			public boolean process(BasicTextEditor editor) {
				if (file.equals(editor.getFile())) {
					TextEditorUtils.getTabbedPane().setSelectedComponent(editor);
					return true;
				}
				return false;
			}
		}.start();
	}

	private static class SearchEmptyEditorTask extends EditorListTask<BasicTextEditor> {
		private BasicTextEditor result;

		public SearchEmptyEditorTask() {
			super(BasicTextEditor.class);
		}

		public BasicTextEditor getResult() {
			return result;
		}

		@Override
		public boolean process(BasicTextEditor ed) {
			if (ed.getFile() == null && ed.isReadyToClose()) {
				result = ed;
				return true;
			}
			return false;
		}
	}

}
