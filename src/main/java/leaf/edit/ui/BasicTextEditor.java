/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.swing.undo.UndoManager;

import leaf.edit.app.TsEditApp;
import leaf.edit.shell.CloseTab;
import leaf.edit.shell.SetFont;
import leaf.edit.shell.SetTabSize;

/**
 * tseditのテキストエディタの実装です。
 *
 * @author 無線部開発班
 * @since 2010年3月16日
 */
public class BasicTextEditor extends TextEditor {
	private final UndoManager undoManager;
	private final String title;
	protected boolean isEdited = false;
	private File file = null;
	private Charset encode = null;

	/**
	 * エディタを生成します。
	 */
	public BasicTextEditor() {
		this(null);
	}

	/**
	 * タイトルを指定してエディタを生成します。
	 *
	 * @param title
	 */
	public BasicTextEditor(String title) {
		super();
		this.title = title;
		setEncoding(TextEditorUtils.getPrimaryCharset());
		setTabSize(SetTabSize.getTabSize());
		setFont(SetFont.getFont());
		var popup = PopupMenu.getInstance();
		setComponentPopupMenu(popup);
		undoManager = new TextUndoManager(this);
		undoManager.setLimit(2000);
		getDocument().addUndoableEditListener(undoManager);
	}

	/**
	 * やり直しうる編集操作があるか返します。
	 *
	 * @return やり直せる場合true
	 */
	public boolean canRedo() {
		return undoManager.canRedo();
	}

	/**
	 * 元に戻しうる編集操作があるか返します。
	 *
	 * @return 元に戻せる場合true
	 */
	public boolean canUndo() {
		return undoManager.canUndo();
	}

	/**
	 * エディタの終了処理を行います。
	 *
	 * @return このエディタを閉じる準備ができた場合
	 */
	public boolean close() {
		return CloseTab.prepareToClose(this);
	}

	/**
	 * カーソル位置の桁番号を返します。
	 *
	 * @return カーソルのある桁
	 */
	public int getColumnNumber() {
		return getScrollPane().getColumnNumber();
	}

	/**
	 * エディタの文字コード名を返します。
	 *
	 * @return 文字コード名
	 */
	public String getEncoding() {
		return encode.name();
	}

	/**
	 * エディタの文字コードを設定します。
	 *
	 * @param encode 文字コード名
	 */
	public void setEncoding(String encode) {
		try {
			this.encode = Charset.forName(encode);
		} catch (Exception ex) {
			this.encode = StandardCharsets.UTF_8;
		}
	}

	/**
	 * エディタの文字セットを設定します。
	 *
	 * @param encode 文字セット
	 */
	public void setEncoding(Charset encode) {
		this.encode = encode;
	}

	/**
	 * このエディタが閲覧しているファイルを返します。
	 *
	 * @return ファイル
	 */
	public File getFile() {
		return file;
	}

	/**
	 * エディタで編集中のファイルの絶対パスを返します。
	 *
	 * @return 絶対パス
	 */
	public String getFilePath() {
		return (file != null) ? file.getAbsolutePath() : null;
	}

	/**
	 * カーソル位置の行番号を返します。
	 *
	 * @return カーソルのある行
	 */
	public int getLineNumber() {
		return getScrollPane().getLineNumber();
	}

	/**
	 * タブに表示するためのエディタのタイトルを返します。
	 *
	 * @return タイトル
	 */
	public String getTitle() {
		return file != null ? file.getName() : title;
	}

	/**
	 * エディタの内容が編集されているか返します。
	 *
	 * @return 編集されている場合true
	 */
	public boolean isEdited() {
		return isEdited;
	}

	/**
	 * このエディタを終了処理することなく閉じることができるか確認します。
	 *
	 * @return すぐに閉じることができる場合true
	 */
	public boolean isReadyToClose() {
		return !isEdited;
	}

	/**
	 * ファイルから文字列を読み込んでエディタを初期化します。
	 *
	 * @param file 読み込むファイル
	 *
	 * @throws IOException 入出力エラーの場合
	 */
	public void read(File file) throws IOException {
		try (var stream = new FileInputStream(this.file = file)) {
			read(new InputStreamReader(stream, encode));
			var frame = TsEditApp.getMainFrame();
			frame.getStatusBar().setText(getEncoding(), 1);

		}
	}

	/**
	 * リーダーから文字列を読み込んでエディタを初期化します。
	 *
	 * @param reader 読み込むリーダー
	 *
	 * @throws IOException 入出力エラーの場合
	 */
	public void read(Reader reader) throws IOException {
		super.read(reader);
		SyntaxHighlight.update(this);
		undoManager.discardAllEdits();
		getDocument().addUndoableEditListener(undoManager);
	}

	/**
	 * エディタの内容を指定されたWriterに出力します。
	 *
	 * @param writer 出力先
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	@Override
	public void write(Writer writer) throws IOException {
		super.write(writer);
		this.isEdited = false;
	}

	/**
	 * 指定されたファイルから選択領域にテキストを読み込みます。
	 *
	 * @param file  読み込み元ファイル
	 * @param chset 文字セット
	 *
	 * @throws IOException 読み込みに失敗した場合
	 */
	public void readIntoSelection(File file, Charset chset) throws IOException {
		try (var stream = new FileInputStream(file)) {
			readIntoSelection(new InputStreamReader(stream, chset));
		}
	}

	/**
	 * 直前に元に戻した編集操作をやり直します。
	 */
	public void redo() {
		if (undoManager.canRedo()) {
			undoManager.redo();
		}
	}

	/**
	 * 指定した行を選択します。
	 *
	 * @param line 1以上の行番号
	 */
	public void selectLine(int line) {
		var root = getDocument().getDefaultRootElement();
		var elem = root.getElement(line - 1);
		getTextPane().select(elem.getStartOffset(), elem.getEndOffset() - 1);
	}

	/**
	 * 直前の編集操作を元に戻します。
	 */
	public synchronized void undo() {
		if (undoManager.canUndo()) {
			undoManager.undo();
		}
	}

	/**
	 * 指定されたファイルにエディタの内容を保存します。
	 *
	 * @param file  書き込むファイル
	 * @param chset 文字セット
	 *
	 * @throws IOException 入出力エラーの場合
	 */
	public void write(File file, Charset chset) throws IOException {
		try (var stream = new FileOutputStream(file)) {
			write(new OutputStreamWriter(stream, chset));
		}
	}

	/**
	 * 指定されたファイルにエディタの内容を保存します。
	 *
	 * @param file 書き込むファイル
	 *
	 * @throws IOException 入出力エラーの場合
	 */
	public void write(File file) throws IOException {
		write(this.file = file, encode);
		var frame = TsEditApp.getMainFrame();
		frame.getStatusBar().setText(getEncoding(), 1);
		SyntaxHighlight.update(this);
	}

	/**
	 * 選択領域の文字列をファイルに保存します。
	 *
	 * @param file  書き込むファイル
	 * @param chset 文字コード名
	 *
	 * @throws IOException 書き込みに失敗した場合
	 */
	public void writeFromSelection(File file, Charset chset) throws IOException {
		try (var stream = new FileOutputStream(file)) {
			writeFromSelection(new OutputStreamWriter(stream, chset));
		}
	}
}
