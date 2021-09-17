/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

/**
 * 行カーソルの表示機能を持つテキストエリアです。
 *
 * @author 無線部開発班
 * @since 2011年7月28日
 */
public class LeafTextArea extends JTextArea {
	private static final long serialVersionUID = 1L;
	private final Color color = Color.BLUE;
	private LeafCaret caret;
	private int maxRows = 0;
	private boolean isLineCursorVisible = true;

	/**
	 * テキストエリアを生成します。
	 */
	public LeafTextArea() {
		super();
		initialize(getDocument());
	}

	/**
	 * 文字列を指定してテキストエリアを生成します。
	 *
	 * @param text 初期表示文字列
	 */
	public LeafTextArea(String text) {
		super(text);
		initialize(getDocument());
	}

	/**
	 * ドキュメントを指定してテキストエリアを生成します。
	 *
	 * @param doc ドキュメント
	 */
	public LeafTextArea(Document doc) {
		super(doc);
		initialize(doc);
	}

	/**
	 * 行数と桁数を指定してテキストエリアを生成します。
	 *
	 * @param rows 行数
	 * @param cols 桁数
	 */
	public LeafTextArea(int rows, int cols) {
		super(rows, cols);
		this.maxRows = rows;
		initialize(getDocument());
	}

	/**
	 * 文字列と行数、桁数を指定してテキストエリアを生成します。
	 *
	 * @param text 初期表示文字列
	 * @param rows 行数
	 * @param cols 桁数
	 */
	public LeafTextArea(String text, int rows, int cols) {
		super(text, rows, cols);
		this.maxRows = rows;
		initialize(getDocument());
	}

	/**
	 * ドキュメントと文字列、行数、桁数を指定してテキストエリアを生成します。
	 *
	 * @param doc  ドキュメント
	 * @param text 初期表示文字列
	 * @param rows 行数
	 * @param cols 桁数
	 */
	public LeafTextArea(Document doc, String text, int rows, int cols) {
		super(doc, text, rows, cols);
		this.maxRows = rows;
		initialize(doc);
	}

	/**
	 * 最大表示行数を返します。
	 *
	 * @return 最大行数
	 */
	public int getMaxRowCount() {
		return maxRows;
	}

	/**
	 * 最大表示行数を設定します。行数を超えた行は先頭から順に削除されます。
	 *
	 * @param rows 最大行数 制限しない場合は0
	 */
	public void setMaxRowCount(int rows) {
		this.maxRows = rows;
	}

	/**
	 * 改行コードをLFに統一してテキストを返します。
	 *
	 * @return テキスト
	 */
	public String getText() {
		return super.getText().replaceAll("(\r\n|\r)", "\n");
	}

	/**
	 * 改行コードをLFに統一して選択文字列を返します。
	 *
	 * @return 選択されたテキスト
	 */
	public String getSelectedText() {
		try {
			return super.getSelectedText().replaceAll("(\r\n|\r)", "\n");
		} catch (NullPointerException ex) {
			return null;
		}
	}

	/**
	 * 指定されたドキュメントモデルでテキストエリアを初期化します。
	 *
	 * @param doc ドキュメント
	 */
	private void initialize(Document doc) {
		setSelectionColor(Color.BLACK);
		setSelectedTextColor(Color.WHITE);
		var blink = getCaret().getBlinkRate();
		setCaret(caret = new LeafCaret(this));
		caret.setBlinkRate(blink);
		doc.addDocumentListener(new RowLimitter(doc));
	}

	/**
	 * 行カーソルが表示されているか返します。
	 *
	 * @return 行カーソル表示の場合true
	 */
	public boolean isLineCursorVisible() {
		return isLineCursorVisible;
	}

	/**
	 * 行カーソルを表示するか設定します。
	 *
	 * @param visible 行カーソル表示の場合true
	 */
	public void setLineCursorVisible(boolean visible) {
		isLineCursorVisible = visible;
	}

	/**
	 * コンポーネントを描画します。
	 *
	 * @param g グラフィックス
	 */
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		paintLineCursor(g);
	}

	/**
	 * 行カーソルを描画します。
	 *
	 * @param g グラフィックス
	 */
	protected void paintLineCursor(Graphics g) {
		if (!isLineCursorVisible) return;
		g.setColor(color);
		var insets = getInsets();
		var cy = caret.y + caret.height;
		var width = getSize().width - insets.left - insets.right;
		g.drawLine(insets.left, cy, width, cy);
	}

	private final class RowLimitter implements DocumentListener {
		private final Document doc;

		public RowLimitter(Document doc) {
			this.doc = doc;
		}

		@Override
		public void insertUpdate(DocumentEvent e) {
			if (maxRows <= 0) return;
			final var root = doc.getDefaultRootElement();
			if (root.getElementCount() > maxRows) {
				EventQueue.invokeLater(() -> removeLines(root));
			}
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		private void removeLines(Element root) {
			var first = root.getElement(0);
			try {
				doc.remove(0, first.getEndOffset());
			} catch (BadLocationException ex) {
			}
		}
	}

}
