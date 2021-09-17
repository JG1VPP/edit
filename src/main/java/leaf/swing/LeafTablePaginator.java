/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;
import javax.swing.plaf.basic.BasicHTML;
import javax.swing.plaf.basic.BasicRadioButtonUI;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.text.View;

/**
 * 巨大な{@link JTable}をページ分割して表示する操作を提供します。
 *
 * @author 無線部開発班
 * @since 2010年8月6日
 */
public class LeafTablePaginator extends JComponent {
	private static final long serialVersionUID = 1L;
	private static final int WING_PAGE_SIZE = 6;
	private final PageButtonUI ui = new PageButtonUI();
	private final TableRowSorter<TableModel> sorter;
	private final JTable table;
	private int visibleRowCount = 100;
	private int currentPage = 0;
	private Color linkColor = Color.BLUE;

	/**
	 * テーブルを指定してページ分割コンポーネントを構築します。
	 *
	 * @param table 操作対象のテーブル
	 */
	public LeafTablePaginator(JTable table) {
		super();
		this.table = table;
		sorter = new TableRowSorter<>(table.getModel()) {
			@Override
			public boolean isSortable(int column) {
				return true;
			}
		};
		table.setRowSorter(sorter);
		setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
		setPreferredSize(new Dimension(200, 30));
		update(0);
	}

	/**
	 * 指定したページへのリンクボタンを生成します。
	 *
	 * @param to リンクするページ
	 *
	 * @return 生成したリンクボタン
	 */
	private JRadioButton createLinkButton(final int to) {
		var button = new JRadioButton(String.valueOf(to + 1));
		button.setForeground(to == currentPage ? getForeground() : linkColor);
		button.setSelected(to == currentPage);
		button.setUI(ui);
		button.setOpaque(false);
		button.addActionListener(e -> update(to));
		return button;
	}

	/**
	 * 現在表示されているページの番号を返します。
	 *
	 * @return 現在のページ番号
	 */
	public int getCurrentPage() {
		return currentPage;
	}

	/**
	 * 表示するページを設定します。
	 *
	 * @param page 表示するページの番号
	 */
	public void setCurrentPage(int page) {
		final var old = currentPage;
		update(currentPage = page);
		firePropertyChange("currentPage", old, page);
	}

	/**
	 * リンクボタンを表示するのに用いる色を返します。
	 *
	 * @return リンクボタンの色
	 */
	public Color getLinkColor() {
		return linkColor;
	}

	/**
	 * リンクボタンを表示するのに用いる色を設定します。
	 *
	 * @param color リンクボタンの色
	 */
	public void setLinkColor(Color color) {
		final var old = linkColor;
		if (!linkColor.equals(color)) {
			linkColor = (color != null ? color : Color.WHITE);
			update(currentPage);
		}
		firePropertyChange("linkColor", old, linkColor);
	}

	/**
	 * 1ページあたりに表示する最大アイテム数を返します。
	 *
	 * @return 1ページに同時に表示するアイテム数
	 */
	public int getVisibleRowCount() {
		return visibleRowCount;
	}

	/**
	 * 1ページあたりに表示する最大アイテム数を設定します。
	 *
	 * @param count 1ページに同時に表示するアイテム数
	 */
	public void setVisibleRowCount(int count) {
		final var old = visibleRowCount;
		if (visibleRowCount != count) {
			visibleRowCount = count;
			update(currentPage);
		}
		firePropertyChange("visibleRowCount", old, count);
	}

	/**
	 * ページ分割コンポーネントを初期化します。
	 *
	 * @param page 表示するページ番号
	 */
	private void update(int page) {
		this.currentPage = page;
		sorter.setRowFilter(new RowFilter<>() {
			@Override
			public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
				int id = entry.getIdentifier();
				var min = currentPage * visibleRowCount;
				return min <= id && id < min + visibleRowCount;
			}
		});
		final var rowCount = table.getModel().getRowCount();
		var lowest = Math.max(page - WING_PAGE_SIZE, 0);
		var uppest = Math.min(page + WING_PAGE_SIZE, (rowCount - 1) / visibleRowCount);
		final var group = new ButtonGroup();
		removeAll();
		add(Box.createHorizontalGlue());
		for (var i = lowest; i <= uppest; i++) {
			var link = createLinkButton(i);
			group.add(link);
			add(link);
		}
		add(Box.createHorizontalGlue());
		revalidate();
		repaint();
	}

	private static class PageButtonUI extends BasicRadioButtonUI {
		private final Rectangle viewRect = new Rectangle();
		private final Rectangle iconRect = new Rectangle();
		private final Rectangle textRect = new Rectangle();
		private Dimension size = new Dimension();

		@Override
		public Icon getDefaultIcon() {
			return null;
		}

		@Override
		public synchronized void paint(Graphics g, JComponent c) {
			var button = (AbstractButton) c;
			var model = button.getModel();
			g.setFont(button.getFont());
			var met = button.getFontMetrics(button.getFont());
			var ins = button.getInsets();
			size = button.getSize(size);
			viewRect.x = ins.left;
			viewRect.y = ins.top;
			viewRect.width = size.width - (ins.right + viewRect.x);
			viewRect.height = size.height - (ins.bottom + viewRect.y);
			iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;
			textRect.x = textRect.y = textRect.width = textRect.height = 0;
			var text = SwingUtilities.layoutCompoundLabel(button, met, button.getText(), null, button.getVerticalAlignment(), button.getHorizontalAlignment(), button.getVerticalTextPosition(), button.getHorizontalTextPosition(), viewRect, iconRect, textRect, 0);
			if (button.isOpaque()) {
				g.setColor(button.getBackground());
				g.fillRect(0, 0, size.width, size.height);
			}
			if (text == null) return;
			g.setColor(button.getForeground());
			if (!model.isSelected() && !model.isPressed() && !model.isArmed() && button.isRolloverEnabled() && model.isRollover()) {
				g.drawLine(viewRect.x, viewRect.y + viewRect.height, viewRect.x + viewRect.width, viewRect.y + viewRect.height);
			}
			var view = (View) c.getClientProperty(BasicHTML.propertyKey);
			if (view != null) view.paint(g, textRect);
			else paintText(g, button, textRect, text);
		}
	}

}
