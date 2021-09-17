/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.ui;

import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.LinkedList;
import java.util.List;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretListener;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;

import leaf.swing.SyntaxDocument;
import leaf.swing.LeafTextPane;
import leaf.swing.LeafTextScrollPane;
import leaf.swing.MultiSplit;

/**
 * 多重分割機能を有するエディタコンポーネントです。
 *
 * @author 無線部開発班
 * @since 2012年03月26日
 */
public class TextEditor extends MultiSplit {
	private static final long serialVersionUID = 1L;
	private LinkedList<LeafTextPane> textpanes;
	private LinkedList<LeafTextScrollPane> scrollpanes;

	private LeafTextPane textpane;
	private LeafTextScrollPane scrollpane;

	private Border border = null;

	private EditorKit editorkit;
	private boolean isEditable;
	private boolean isAutoIndentEnabled;

	/**
	 * エディタを構築します。
	 */
	public TextEditor() {
		super(true);
		textpane = textpanes.get(0);
		scrollpane = scrollpanes.get(0);
	}

	/**
	 * エディタに{@link CaretListener}を追加します。
	 *
	 * @param listener 追加するリスナー
	 */
	public void addCaretListener(CaretListener listener) {
		listenerList.add(CaretListener.class, listener);
		for (var textpane : textpanes) {
			textpane.addCaretListener(listener);
		}
	}

	/**
	 * エディタの末尾に文字列を追加します。
	 *
	 * @param str 追加する文字列
	 */
	public void append(String str) {
		textpane.append(str);
	}

	/**
	 * 選択領域の文字列をクリップボードにコピーします。
	 */
	public void copy() {
		textpane.copy();
	}

	/**
	 * エディタ分割時に呼び出されます。
	 *
	 * @return 新たに配置されるテキストコンポーネント
	 */
	@Override
	protected Component createComponent() {
		var cursor = textpane.getCursor();
		var tabsize = textpane.getTabSize();
		var font = textpane.getFont();
		Document doc = getDocument();
		scrollpane = createScrollPane();
		scrollpane.setViewportBorder(border);
		scrollpane.getViewport().setOpaque(border == null);
		textpane.setDocument(doc);
		textpane.setComponentPopupMenu(getComponentPopupMenu());
		textpane.setCursor(cursor);
		textpane.setFont(font);
		textpane.setTabSize(tabsize);
		return scrollpane;
	}

	/**
	 * 最初のテキストコンポーネントを構築して返します。
	 *
	 * @return 最初に配置されるテキストコンポーネント
	 */
	@Override
	protected Component createFirstComponent() {
		setTransferHandler(new FileDropHandler());
		editorkit = SyntaxDocument.getEditorKit();
		textpanes = new LinkedList<>();
		scrollpanes = new LinkedList<>();
		isEditable = true;
		// before createScrollPane
		scrollpane = createScrollPane();
		// after  createScrollPane
		var doc = getDocument();
		doc.setAutoIndentEnabled(isAutoIndentEnabled = true);
		return scrollpane;
	}

	/**
	 * エディタ分割を1段階解除します。
	 *
	 * @return 削除されたコンポーネント
	 */
	@Override
	public Component[] merge() {
		var comps = super.merge();
		for (var comp : comps) {
			var scroll = (LeafTextScrollPane) comp;
			var view = scroll.getViewport().getView();
			textpanes.remove((LeafTextPane) view);
			scrollpanes.remove(scroll);
		}
		return comps;
	}

	protected LeafTextScrollPane createScrollPane() {
		final var pane = new LeafTextPane();
		final var scroll = new LeafTextScrollPane(pane);
		pane.setEditorKit(editorkit);
		pane.setEditable(isEditable);
		pane.setTransferHandler(getTransferHandler());
		pane.setOpaque(false);
		pane.setBackground(new Color(255, 255, 255, 0)); // Nimbus LnF
		scroll.getViewport().setBackground(Color.WHITE);
		pane.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				textpane = pane;
				scrollpane = scroll;
			}
		});
		for (var listener : listenerList.getListenerList()) {
			if (listener instanceof CaretListener) {
				pane.addCaretListener((CaretListener) listener);
			}
			if (listener instanceof KeyListener) {
				pane.addKeyListener((KeyListener) listener);
			}
		}
		textpanes.add(textpane = pane);
		scrollpanes.add(scrollpane = scroll);
		return scroll;
	}

	/**
	 * 選択領域の文字列を切り取ってクリップボードにコピーします。
	 */
	public void cut() {
		textpane.cut();
	}

	/**
	 * このエディタのドキュメントモデルを返します。
	 *
	 * @return ドキュメント
	 */
	public SyntaxDocument getDocument() {
		return (SyntaxDocument) textpane.getDocument();
	}

	/**
	 * エディタの表示フォントを返します。
	 *
	 * @return フォント
	 */
	@Override
	public Font getFont() {
		if (textpane != null) return textpane.getFont();
		return super.getFont();
	}

	/**
	 * エディタに{@link KeyListener}を追加します。
	 *
	 * @param listener 追加するリスナー
	 */
	public void addKeyListener(KeyListener listener) {
		listenerList.add(KeyListener.class, listener);
		for (var textpane : textpanes) {
			textpane.addKeyListener(listener);
		}
	}

	/**
	 * エディタから{@link KeyListener}を削除します。
	 *
	 * @param listener 削除するリスナー
	 */
	public void removeKeyListener(KeyListener listener) {
		listenerList.remove(KeyListener.class, listener);
		for (var textpane : textpanes) {
			textpane.removeKeyListener(listener);
		}
	}

	/**
	 * エディタの表示フォントを設定します。
	 *
	 * @param font フォント
	 */
	@Override
	public void setFont(Font font) {
		super.setFont(font);
		for (var textpane : textpanes) {
			textpane.setFont(font);
			textpane.setTabSize(textpane.getTabSize());
		}
	}

	/**
	 * {@link #getTextPane()}の親であるLeafTextScrollPaneを返します。
	 *
	 * @return LeafTextScrollPane
	 */
	public LeafTextScrollPane getScrollPane() {
		return scrollpane;
	}

	/**
	 * このエディタに含まれるLeafTextScrollPaneのリストを返します。
	 *
	 * @return LeafTextScrollPaneのリスト
	 */
	@SuppressWarnings("unchecked")
	public List<LeafTextScrollPane> getScrollPaneList() {
		return (List<LeafTextScrollPane>) scrollpanes.clone();
	}

	/**
	 * 改行コードをLFに統一して選択文字列を返します。
	 *
	 * @return 選択部分の文字列
	 */
	public String getSelectedText() {
		return textpane.getSelectedText();
	}

	/**
	 * エディタで水平タブを表示する際の最大スペース数を返します。
	 *
	 * @return タブの大きさ
	 */
	public int getTabSize() {
		return textpane.getTabSize();
	}

	/**
	 * エディタで水平タブを表示する際の最大スペース数を設定します。
	 *
	 * @param size タブの大きさ
	 */
	public void setTabSize(int size) {
		for (var textpane : textpanes) {
			textpane.setTabSize(size);
		}
	}

	/**
	 * 改行コードをLFに統一してエディタの内容を返します。
	 *
	 * @return エディタの文字列
	 */
	public String getText() {
		return textpane.getText();
	}

	/**
	 * 現在フォーカスを持っているテキストコンポーネントを返します。
	 *
	 * @return フォーカスのあるLeafTextPane
	 */
	public LeafTextPane getTextPane() {
		return textpane;
	}

	/**
	 * このエディタに含まれるLeafTextPaneのリストを返します。
	 *
	 * @return LeafTextPaneのリスト
	 */
	@SuppressWarnings("unchecked")
	public List<LeafTextPane> getTextPaneList() {
		return (List<LeafTextPane>) textpanes.clone();
	}

	/**
	 * このエディタで自動字下げ機能が有効になっているか返します。
	 *
	 * @return 有効である場合true
	 */
	public boolean isAutoIndentEnabled() {
		return isAutoIndentEnabled;
	}

	/**
	 * このエディタで自動字下げ機能を有効にするか設定します。
	 *
	 * @param b 有効にする場合true
	 */
	public void setAutoIndentEnabled(boolean b) {
		final var old = isAutoIndentEnabled;
		isAutoIndentEnabled = b;
		firePropertyChange("autoIndentEnabled", old, b);
	}

	/**
	 * エディタが編集可能であるか返します。
	 *
	 * @return 編集可能である場合true
	 */
	public boolean isEditable() {
		return isEditable;
	}

	/**
	 * エディタが編集可能であるか設定します。
	 *
	 * @param b 編集可能にする場合true
	 */
	public void setEditable(boolean b) {
		if (isEditable == b) return;
		for (var textpane : textpanes) {
			textpane.setEditable(b);
		}
		isEditable = b;
	}

	/**
	 * 選択領域にクリップボードから文字列を貼り付けます。
	 */
	public void paste() {
		textpane.paste();
	}

	/**
	 * リーダーからテキストを読み込んでエディタを初期化します。
	 *
	 * @param reader 読み込み元
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void read(Reader reader) throws IOException {
		getDocument().setAutoIndentEnabled(false);
		try {
			textpane.read(reader, null);
			var doc = getDocument();
			for (var textpane : textpanes) {
				textpane.setDocument(doc);
			}
			doc.setAutoIndentEnabled(isAutoIndentEnabled);
			for (var scroll : scrollpanes) {
				scroll.initialize();
			}
		} finally {
			if (reader != null) reader.close();
		}
	}

	/**
	 * 選択領域に文字列を読み込みます。
	 *
	 * @param reader 読み込み元
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void readIntoSelection(Reader reader) throws IOException {
		try (var breader = new BufferedReader(reader)) {
			var sb = new StringBuilder();
			String line;
			while ((line = breader.readLine()) != null) {
				sb.append(line);
				sb.append("\n");
			}
			textpane.replaceSelection(sb.substring(0, sb.length() - 1));
		} finally {
			if (reader != null) reader.close();
		}
	}

	/**
	 * エディタから{@link CaretListener}を削除します。
	 *
	 * @param listener 削除するリスナー
	 */
	public void removeCaretListener(CaretListener listener) {
		listenerList.remove(CaretListener.class, listener);
		for (var textpane : textpanes) {
			textpane.removeCaretListener(listener);
		}
	}

	/**
	 * 選択領域の文字列を指定された文字列で置換します。
	 *
	 * @param to 置換後の文字列
	 */
	public void replaceSelection(String to) {
		var start = textpane.getSelectionStart();
		textpane.replaceSelection(to);
		textpane.select(start, start + to.length());
	}

	/**
	 * このエディタにポップアップメニューを登録します。
	 *
	 * @param popup 登録するポップアップメニュー
	 */
	@Override
	public void setComponentPopupMenu(JPopupMenu popup) {
		super.setComponentPopupMenu(popup);
		if (textpanes != null) {
			for (var textpane : textpanes) {
				textpane.setComponentPopupMenu(popup);
			}
		}
	}

	/**
	 * フォーカスをテキストコンポーネントに要求します。
	 */
	@Override
	public boolean requestFocusInWindow() {
		return textpane.requestFocusInWindow();
	}

	/**
	 * このエディタのJScrollPaneにボーダーを設定します。
	 *
	 * @param border ボーダー
	 */
	public void setViewportBorder(Border border) {
		this.border = border;
		for (var scroll : scrollpanes) {
			scroll.setViewportBorder(border);
			var viewport = scroll.getViewport();
			viewport.setOpaque(border == null);
			viewport.setBackground(Color.WHITE);
		}
	}

	/**
	 * エディタの内容を指定されたWriterに出力します。
	 *
	 * @param writer 出力先
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void write(Writer writer) throws IOException {
		try (var bwriter = new BufferedWriter(writer)) {
			int start = 0, end;
			var text = getText();
			var ls = TextEditorUtils.getLineSeparator();
			while ((end = text.indexOf("\n", start)) >= 0) {
				bwriter.write(text.substring(start, end) + ls);
				bwriter.flush();
				start = end + 1;
			}
			if (start < text.length()) bwriter.write(text.substring(start));
		} finally {
			if (writer != null) writer.close();
		}
	}

	/**
	 * 選択領域の文字列をWriterに出力します。
	 *
	 * @param writer 出力先
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public void writeFromSelection(Writer writer) throws IOException {
		try (var bwriter = new BufferedWriter(writer)) {
			int start = 0, end;
			var text = getSelectedText();
			var ls = TextEditorUtils.getLineSeparator();
			while ((end = text.indexOf("\n", start)) >= 0) {
				bwriter.write(text.substring(start, end) + ls);
				bwriter.flush();
				start = end + 1;
			}
			if (start < text.length()) bwriter.write(text.substring(start));
		}
	}
}
