/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;

/**
 * 行番号と桁ルーラを表示する{@link JTextComponent}用スクロールコンテナです。
 *
 * @author 無線部開発班
 * @since 2010年5月22日
 */
public class LeafTextScrollPane extends JScrollPane {
	private static final long serialVersionUID = 1L;
	private final FontHandler fontHandler;
	private final ArrayList<BookMark> marks;
	private final JTextComponent comp;
	private LeafLineNumberPane lineNumber;
	private LeafColumnRulerPane columnRuler;
	private Element root;
	private boolean isLineNumberVisible;
	private boolean isColumnRulerVisible;
	private FontMetrics fontMetrics;
	private int fontWidth;

	/**
	 * テキストコンポーネントを指定してLeafTextScrollPaneを生成します。
	 *
	 * @param comp テキストコンポーネント
	 */
	public LeafTextScrollPane(JTextComponent comp) {
		this(comp, true, true);
	}

	/**
	 * テキストコンポーネントと表示設定を指定してLeafTextScrollPaneを構築します。
	 *
	 * @param comp テキストコンポーネント
	 * @param ln   行番号を表示する場合true
	 * @param cr   桁ルーラを表示する場合true
	 */
	public LeafTextScrollPane(JTextComponent comp, boolean ln, boolean cr) {
		super(comp);
		this.comp = comp;
		isLineNumberVisible = ln;
		isColumnRulerVisible = cr;
		fontHandler = new FontHandler();
		initialize();
		setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_AS_NEEDED);
		setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, new JPanel());
		setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, new JPanel());
		setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new JPanel());
		setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, new JPanel());
		marks = new ArrayList<>();
	}

	private BookMark getBookMark(int line) {
		for (var mark : marks) {
			if (mark.getLine() == line) {
				return mark;
			}
		}
		return null;
	}

	/**
	 * 現在カーソルが表示されている桁の行番号を返します。
	 *
	 * @return 1以上の桁番号
	 */
	public int getColumnNumber() {
		var caret = comp.getCaretPosition();
		var elem = root.getElement(root.getElementIndex(caret));
		return caret - elem.getStartOffset() + 1;
	}

	/**
	 * 現在カーソルが表示されている行の行番号を返します。
	 *
	 * @return 1以上の行番号
	 */
	public int getLineNumber() {
		return root.getElementIndex(comp.getCaretPosition()) + 1;
	}

	/**
	 * LeafTextScrollPaneの表示を最新の状態に更新します。
	 */
	public final void initialize() {
		fontMetrics = getFontMetrics(comp.getFont());
		fontWidth = fontMetrics.charWidth('m');
		comp.addCaretListener(new ExCaretListener());
		root = comp.getDocument().getDefaultRootElement();
		comp.getDocument().addDocumentListener(new ExDocumentListener());
		lineNumber = isLineNumberVisible ? new LeafLineNumberPane() : null;
		columnRuler = isColumnRulerVisible ? new LeafColumnRulerPane() : null;
		setRowHeaderView(lineNumber);
		setColumnHeaderView(columnRuler);
		comp.removePropertyChangeListener("font", fontHandler);
		comp.addPropertyChangeListener("font", fontHandler);
	}

	/**
	 * 桁ルーラ—を表示するか表示しないか返します。
	 *
	 * @return 桁ルーラ—を表示する場合true
	 */
	public boolean isColumnRulerVisible() {
		return isColumnRulerVisible;
	}

	/**
	 * 桁ルーラーを表示するか表示しないか設定します。
	 * 直後に{@link #initialize()}を実行すると表示上反映されます。
	 *
	 * @param visible 桁ルーラ—を表示する場合true
	 */
	public void setColumnRulerVisible(boolean visible) {
		final var old = isColumnRulerVisible;
		isColumnRulerVisible = visible;
		initialize();
		firePropertyChange("columnRulerVisible", old, visible);
	}

	/**
	 * 行番号を表示するか表示しないか返します。
	 *
	 * @return 行番号を表示する場合true
	 */
	public boolean isLineNumberVisible() {
		return isLineNumberVisible;
	}

	/**
	 * 行番号を表示するか表示しないか設定します。
	 * 直後に{@link #initialize()}を実行すると表示上反映されます。
	 *
	 * @param visible 行番号を表示する場合true
	 */
	public void setLineNumberVisible(boolean visible) {
		final var old = isLineNumberVisible;
		isLineNumberVisible = visible;
		initialize();
		firePropertyChange("lineNumberVisible", old, visible);
	}

	/**
	 * このスクロールコンポーネントに、行番号の速やかな再描画を要求します。
	 */
	public void repaintLineNumber() {
		SwingUtilities.invokeLater(() -> {
			if (lineNumber != null) lineNumber.repaint();
			if (columnRuler != null) columnRuler.repaint();
		});
	}

	/**
	 * 指定行にキャレットを移動します。行番号は1から始まります。
	 *
	 * @param line 移動先の行
	 */
	public void scrollToLine(int line) {
		line = Math.max(0, Math.min(root.getElementCount() - 1, line - 1));
		try {
			var l = root.getElement(line);
			var d = comp.modelToView(l.getStartOffset());
			var c = getViewport().getViewRect();
			new Timer(15, new ScrollToLineTask(l, d, c)).start();
		} catch (BadLocationException ex) {
			Toolkit.getDefaultToolkit().beep();
		}
	}

	private class FontHandler implements PropertyChangeListener {
		@Override
		public void propertyChange(PropertyChangeEvent arg0) {
			initialize();
		}
	}

	private class ExCaretListener implements CaretListener {
		@Override
		public void caretUpdate(CaretEvent e) {
			if (columnRuler != null) columnRuler.repaint();
		}
	}

	private class ExDocumentListener implements DocumentListener {
		@Override
		public void insertUpdate(DocumentEvent e) {
			this.update();
			repaintLineNumber();
		}

		@Override
		public void removeUpdate(DocumentEvent e) {
			this.update();
			repaintLineNumber();
		}

		@Override
		public void changedUpdate(DocumentEvent e) {
		}

		private void update() {
			for (var i = marks.size() - 1; i >= 0; i--) {
				marks.get(i).update();
			}
		}
	}

	private class ScrollToLineTask implements ActionListener {
		private final Rectangle dest, cur;
		private final Element line;

		public ScrollToLineTask(Element l, Rectangle d, Rectangle c) {
			this.line = l;
			this.dest = d;
			this.cur = c;
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			var timer = (Timer) e.getSource();
			if (dest.y < cur.y && timer.isRunning()) {
				cur.y -= Math.max(1, (cur.y - dest.y) / 2);
				comp.scrollRectToVisible(cur);
			} else if (dest.y > cur.y && timer.isRunning()) {
				cur.y += Math.max(1, (dest.y - cur.y) / 2);
				comp.scrollRectToVisible(cur);
			} else {
				comp.setCaretPosition(line.getStartOffset());
				timer.stop();
			}
		}
	}

	private class BookMark {
		private final Element elem;

		public BookMark(Element elem) {
			this.elem = elem;
		}

		public int getLine() {
			return root.getElementIndex(elem.getStartOffset());
		}

		public void update() {
			if (root.getElement(getLine()) != elem) {
				marks.remove(this);
			}
		}
	}

	// 行番号を表示するコンポーネント
	private class LeafLineNumberPane extends JComponent {
		private static final long serialVersionUID = 1L;
		private static final int MARGIN = 8;
		private final int topInset;
		private final int fontAscent;
		private final int fontHeight;
		private int start = 0; //選択開始行

		public LeafLineNumberPane() {
			setFont(comp.getFont());
			fontHeight = fontMetrics.getHeight();
			fontAscent = fontMetrics.getAscent();
			topInset = comp.getInsets().top;
			setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK));
			addMouseListener(new MouseClickListener());
			addMouseMotionListener(new MouseDragListener());
		}

		private int getComponentWidth() {
			var doc = comp.getDocument();
			var lineCount = root.getElementIndex(doc.getLength());
			var maxDigits = Math.max(4, String.valueOf(lineCount).length());
			return maxDigits * fontMetrics.stringWidth("0") + MARGIN * 2;
		}

		private int getHeightAtPoint(int y) {
			try {
				var pos = comp.viewToModel(new Point(0, y));
				return comp.modelToView(pos).y;
			} catch (BadLocationException ex) {
				return 0;
			}
		}

		private int getLineAtPoint(int y) {
			var pos = comp.viewToModel(new Point(0, y));
			return root.getElementIndex(pos);
		}

		@Override
		protected void paintComponent(Graphics g) {
			var clip = g.getClipBounds();
			g.setColor(getBackground());
			g.fillRect(clip.x, clip.y, clip.width, clip.height);
			g.setColor(getForeground());
			final var width = getComponentWidth();
			final var base = clip.y - topInset;
			final var start = getLineAtPoint(base);
			final var end = getHeightAtPoint(base + clip.height);
			var y = topInset + fontAscent + (start - 1) * fontHeight;
			for (var i = start + 1; y <= end; i++) {
				var text = String.valueOf(i);
				var x = width - MARGIN - fontMetrics.stringWidth(text);
				y += fontHeight;
				g.drawString(text, x, y);
			}
			g.setXORMode(Color.WHITE);
			for (var mark : marks) {
				y = topInset + mark.getLine() * fontHeight;
				g.fillRect(0, y, width - 1, fontHeight);
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(getComponentWidth(), comp.getHeight());
		}

		private class MouseClickListener extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				start = (e.getY() - topInset) / fontHeight;
				try {
					var elem = comp.getDocument().getDefaultRootElement().getElement(start);
					if (e.getClickCount() == 1) {
						comp.select(elem.getStartOffset(), elem.getEndOffset() - 1);
						comp.requestFocusInWindow();
					} else if (elem != null) {
						var mark = getBookMark(start);
						if (mark != null) marks.remove(mark);
						else marks.add(new BookMark(elem));
						repaint();
					}
				} catch (Exception ex) {
				}
			}
		}

		private class MouseDragListener extends MouseMotionAdapter {
			@Override
			public void mouseDragged(MouseEvent e) {
				var end = (e.getY() - topInset) / fontHeight;
				if (start <= end) {
					selectStartToEnd(end);
				} else {
					selectEndToStart(end);
				}
				comp.requestFocusInWindow();
			}

			private void selectEndToStart(int end) {
				var s = root.getElement(end).getStartOffset();
				var e = root.getElement(start).getEndOffset();
				comp.select(s, e - 1);
			}

			private void selectStartToEnd(int end) {
				var s = root.getElement(start).getStartOffset();
				var e = root.getElement(end).getEndOffset();
				comp.select(s, e - 1);
			}
		}
	}

	// 桁ルーラを表示するコンポーネント
	private class LeafColumnRulerPane extends JComponent {
		private static final long serialVersionUID = 1L;
		private final int leftInset;
		private int start = 0; // 選択開始桁

		public LeafColumnRulerPane() {
			setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
			leftInset = comp.getInsets().left;
			setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK));
			addMouseListener(new MouseClickListener());
			addMouseMotionListener(new MouseDragListener());
		}

		@Override
		protected void paintComponent(Graphics g) {
			var clip = g.getClipBounds();
			g.setColor(getBackground());
			g.fillRect(clip.x, clip.y, clip.width, clip.height);
			g.setColor(getForeground());
			final var end = getWidth() / fontWidth;
			final var y1_5 = getHeight() / 3;
			final var y1_e = (int) (getHeight() * 0.75f);
			final var y2 = getHeight() - 2;
			var x = leftInset;
			for (var col = 0; col <= end; col++, x += fontWidth) {
				if (col % 5 == 0) g.drawLine(x, y1_5, x, y2);
				else g.drawLine(x, y1_e, x, y2);
				if (col % 10 == 0) g.drawString(String.valueOf(col / 10), x + 2, y2);
			}
			g.setXORMode(Color.WHITE);
			try {
				var rect = comp.modelToView(comp.getCaretPosition());
				g.fillRect(rect.x + 1, 0, fontWidth - 1, getHeight() + 1);
			} catch (BadLocationException ex) {
			}
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(comp.getWidth(), getFontMetrics(getFont()).getHeight() - 2);
		}

		@Override
		public Dimension getMaximumSize() {
			return getPreferredSize();
		}

		private class MouseClickListener extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				try {
					var point = new Point(e.getPoint());
					point.translate(-leftInset, comp.modelToView(comp.getCaretPosition()).y - point.y);
					start = comp.viewToModel(point);
					comp.setCaretPosition(start);
					comp.requestFocusInWindow();
				} catch (BadLocationException ex) {
				}
			}
		}

		private class MouseDragListener extends MouseMotionAdapter {
			@Override
			public void mouseDragged(MouseEvent e) {
				try {
					var point = new Point(e.getPoint());
					point.translate(-leftInset, comp.modelToView(comp.getCaretPosition()).y - point.y);
					var end = comp.viewToModel(point);
					comp.select(Math.min(start, end), Math.max(start, end));
					comp.requestFocusInWindow();
				} catch (BadLocationException ex) {
				}
			}
		}
	}
}
