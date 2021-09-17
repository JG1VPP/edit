/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;
import javax.swing.table.JTableHeader;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

/**
 * {@link JTable}に行番号を表示する{@link JScrollPane}です。
 *
 * @author 無線部開発班
 * @since 2010年6月8日
 */
public class LeafTableScrollPane extends JScrollPane {
	private final DefaultListModel<String> listmodel;

	/**
	 * テーブルを指定してスクロールコンテナを構築します。
	 *
	 * @param table 内部に配置するテーブル
	 */
	public LeafTableScrollPane(JTable table) {
		super(table);
		setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		// set corner opaque
		setCorner(ScrollPaneConstants.LOWER_LEFT_CORNER, new JPanel());
		setCorner(ScrollPaneConstants.LOWER_RIGHT_CORNER, new JPanel());
		setCorner(ScrollPaneConstants.UPPER_LEFT_CORNER, new JPanel());
		setCorner(ScrollPaneConstants.UPPER_RIGHT_CORNER, new JPanel());
		// row numbers
		listmodel = new DefaultListModel<>();
		setRowHeaderView(new RowNumberView(table, listmodel));
		getRowHeader().addChangeListener(e -> {
			var viewport = (JViewport) e.getSource();
			getVerticalScrollBar().setValue(viewport.getViewPosition().y);
		});
		final var tablemodel = table.getModel();
		final var last = tablemodel.getRowCount();
		final var now = listmodel.getSize();
		for (var i = now; i < last; i++) {
			listmodel.addElement(String.valueOf(i + 1));
		}
		// auto update
		tablemodel.addTableModelListener(e -> {
			final var last1 = tablemodel.getRowCount();
			final var now1 = listmodel.getSize();
			for (var i = now1; i < last1; i++) {
				listmodel.addElement(String.valueOf(i + 1));
			}
			for (var i = last1; i < now1; i++) {
				listmodel.removeElementAt(i);
			}
		});
	}

	// implementation of header which show row number
	private static class RowNumberView extends JList<String> {
		private final JTable table;
		private final ListSelectionModel tablemodel;
		private final ListSelectionModel rowmodel;

		private int rollOveredRowIndex = -1;
		private int pressedRowIndex = -1;

		public RowNumberView(JTable table, ListModel<String> model) {
			super(model);
			setFixedCellHeight((this.table = table).getRowHeight());
			setFixedCellWidth(getFontMetrics(getFont()).stringWidth("000000"));
			setCellRenderer(new HeaderRenderer(table.getTableHeader()));
			var listener = new RollOverListener();
			addMouseListener(listener);
			addMouseMotionListener(listener);
			tablemodel = table.getSelectionModel();
			rowmodel = getSelectionModel();
		}

		class HeaderRenderer extends JLabel implements ListCellRenderer<String> {
			private final JTableHeader header;

			public HeaderRenderer(JTableHeader header) {
				this.header = header;
				setOpaque(true);
				setBackground(header.getBackground());
				setForeground(header.getForeground());
				setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, getForeground()));
				setHorizontalAlignment(CENTER);
			}

			@Override
			public Component getListCellRendererComponent(JList<? extends String> list, String value, int index, boolean isSelected, boolean cellHasFocus) {
				if (index == rollOveredRowIndex) {
					setBackground(header.getBackground().brighter());
				} else {
					setBackground(header.getBackground());
				}
				setText((value != null) ? value : "");
				return this;
			}
		}

		class RollOverListener extends MouseAdapter {
			@Override
			public void mousePressed(MouseEvent e) {
				var row = RowNumberView.this.locationToIndex(e.getPoint());
				if (row != pressedRowIndex) {
					rowmodel.clearSelection();
					table.changeSelection(row, 0, false, false);
					table.changeSelection(row, table.getColumnModel().getColumnCount() - 1, false, true);
					pressedRowIndex = row;
				}
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				rowmodel.clearSelection();
				pressedRowIndex = -1;
				rollOveredRowIndex = -1;
				repaint();
			}

			@Override
			public void mouseExited(MouseEvent e) {
				if (pressedRowIndex < 0) {
					rollOveredRowIndex = -1;
					repaint();
				}
			}

			@Override
			public void mouseDragged(MouseEvent e) {
				if (pressedRowIndex >= 0) {
					var row = RowNumberView.this.locationToIndex(e.getPoint());
					final var start = Math.min(row, pressedRowIndex);
					final var end = Math.max(row, pressedRowIndex);
					tablemodel.clearSelection();
					rowmodel.clearSelection();
					if (tablemodel.getSelectionMode() == SINGLE_SELECTION) {
						tablemodel.setSelectionInterval(row, row);
						rowmodel.setSelectionInterval(row, row);
					} else {
						tablemodel.addSelectionInterval(start, end);
						rowmodel.addSelectionInterval(start, end);
					}
					rollOveredRowIndex = row;
					repaint();
				}
			}

			@Override
			public void mouseMoved(MouseEvent e) {
				var row = RowNumberView.this.locationToIndex(e.getPoint());
				if (row != rollOveredRowIndex) {
					rollOveredRowIndex = row;
					repaint();
				}
			}
		}
	}

}
