/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.LinkedList;
import javax.swing.*;
import javax.swing.event.ListDataEvent;
import javax.swing.event.ListDataListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import static javax.swing.SwingConstants.VERTICAL;

/**
 * チェックボックスによる選択・選択解除操作を可能にするリストの実装です。
 *
 * @author 無線部開発班
 * @since 2011/09/12
 */
public class LeafCheckList<E> extends JComponent implements Scrollable {
	private static final long serialVersionUID = 1L;
	private final Box box;
	private final CheckItemListener itemListener;
	private final ModelObserver modelObserver;
	private ListModel<E> data;
	private ListSelectionHandler handler;
	private ListSelectionModel selection;
	private LinkedList<CheckBoxItem> items;
	private int visibleRowCount = 8;

	/**
	 * 空のリストモデルでリストを構築します。
	 */
	public LeafCheckList() {
		this(new DefaultListModel<>());
	}

	/**
	 * リストのモデルを指定してリストを構築します。
	 *
	 * @param model モデル
	 */
	public LeafCheckList(ListModel<E> model) {
		setLayout(new BorderLayout());
		box = Box.createVerticalBox();
		add(box, BorderLayout.CENTER);
		box.setBackground(Color.WHITE);
		box.setOpaque(true);
		this.modelObserver = new ModelObserver();
		this.itemListener = new CheckItemListener();
		setSelectionModel(new DefaultListSelectionModel());
		setModel(model);
	}

	/**
	 * 選択状態が変更されるたびに通知されるリストにリスナーを追加します。
	 *
	 * @param listener 追加するリスナー
	 */
	public void addListSelectionListener(ListSelectionListener listener) {
		listenerList.add(ListSelectionListener.class, listener);
	}

	/**
	 * リスト内の指定されたインデックスが可視範囲に入るようにスクロールします。
	 *
	 * @param index 見えるようにするインデックス
	 */
	public void ensureIndexIsVisible(int index) {
		super.scrollRectToVisible(getCellBounds(index));
	}

	/**
	 * データの選択状態に変更が加えられたことを通知します。
	 *
	 * @param first 変更開始位置
	 * @param last  変更終了位置
	 * @param adj   このイベントが一連の変更操作の一部である場合
	 */
	protected void fireSelectionValueChanged(int first, int last, boolean adj) {
		var listeners = listenerList.getListeners(ListSelectionListener.class);
		ListSelectionEvent e = null;
		for (var i = listeners.length - 1; i >= 0; i--) {
			if (e == null) e = new ListSelectionEvent(this, first, last, adj);
			listeners[i].valueChanged(e);
		}
	}

	/**
	 * リスト内の指定されたインデックスのセルの座標範囲を返します。
	 *
	 * @return セルの矩形範囲
	 */
	public Rectangle getCellBounds(int index) {
		if (index < 0) index = 0;
		if (index >= items.size()) index = items.size() - 1;
		return items.isEmpty() ? null : items.get(index).getBounds();
	}

	/**
	 * このリストに関連付けられたデータモデルを返します。
	 *
	 * @return データモデル
	 */
	public ListModel<E> getModel() {
		return data;
	}

	/**
	 * このリストにデータモデルを関連付けます。
	 *
	 * @param model データモデル
	 */
	public void setModel(ListModel<E> model) {
		var old = this.data;
		items = new LinkedList<>();
		model.removeListDataListener(modelObserver);
		model.addListDataListener(modelObserver);
		box.removeAll();
		final var size = (this.data = model).getSize();
		for (var i = 0; i < size; i++) {
			Object value = model.getElementAt(i);
			var cb = new CheckBoxItem(value);
			cb.setSelected(selection.isSelectedIndex(i));
			box.add(cb);
			items.add(cb);
		}
		firePropertyChange("model", old, model);
	}

	/**
	 * ビューポートの推奨されるサイズを計算します。
	 *
	 * @return ビューポートのサイズ
	 */
	@Override
	public Dimension getPreferredScrollableViewportSize() {
		final var insets = getInsets();
		final var dy = insets.top + insets.bottom;
		final var visibleRowCount = getVisibleRowCount();
		if (getModel().getSize() > 0) {
			var dim = new Dimension(getPreferredSize());
			var rect = getCellBounds(0);
			dim.height = (visibleRowCount * rect.height) + dy;
			return dim;
		} else return getPreferredSize();
	}

	/**
	 * 次または前の行を表示するためにスクロールする距離を返します。
	 *
	 * @param rect   ビューポート内の可視範囲
	 * @param orient {@link javax.swing.SwingConstants#VERTICAL}
	 * @param direct 上に移動する場合は負、下に移動する場合は正
	 *
	 * @return ユニット増分値
	 */
	@Override
	public int getScrollableUnitIncrement(Rectangle rect, int orient, int direct) {
		assert (orient != VERTICAL) : "illegal scroll orientation";
		if (orient == VERTICAL) {
			var row = locationToIndex(rect.getLocation());
			if (row == -1) return 0;
			if (direct > 0) { // Scroll Down
				var bounds = getCellBounds(row);
				return bounds.height - (rect.y - bounds.y);
			} else { // Scroll Up
				var bounds = getCellBounds(row - 1);
				if ((bounds.y == rect.y) && (row == 0)) return 0;
				if (bounds.y == rect.y) {
					var prevRect = getCellBounds(row - 1);
					return (prevRect.y >= bounds.y) ? 0 : prevRect.height;
				} else return rect.y - bounds.y;
			}
		} else return (getFont() != null) ? getFont().getSize() : 1;
	}

	/**
	 * 次または前のブロックを表示するためにスクロールする距離を返します。
	 *
	 * @param rect   ビューポート内の可視範囲
	 * @param orient {@link javax.swing.SwingConstants#VERTICAL}
	 * @param direct 上に移動する場合は負、下に移動する場合は正
	 *
	 * @return ブロック増分値
	 */
	@Override
	public int getScrollableBlockIncrement(Rectangle rect, int orient, int direct) {
		assert (orient != VERTICAL) : "illegal scroll orientation";
		if (orient == VERTICAL) {
			var inc = rect.height;
			if (direct > 0) { // Scroll Down
				var p = new Point(rect.x, rect.y + rect.height - 1);
				var last = locationToIndex(p);
				if (last != -1) {
					var lastRect = getCellBounds(last);
					inc = lastRect.height;
				}
			} else { // Scroll Up
				var p = new Point(rect.x, rect.y - rect.height);
				var newFirst = locationToIndex(p);
				var oldFirst = locationToIndex(getVisibleRect().getLocation());
				if (newFirst != -1) {
					if (oldFirst == -1) {
						oldFirst = locationToIndex(rect.getLocation());
					}
					var newFirstRect = getCellBounds(newFirst);
					var oldFirstRect = getCellBounds(oldFirst);
					while ((newFirstRect.y + rect.height < oldFirstRect.y + oldFirstRect.height) && (newFirstRect.y < oldFirstRect.y)) {
						newFirstRect = getCellBounds(++newFirst);
					}
					inc = rect.y - newFirstRect.y;
					if ((inc <= 0) && (newFirstRect.y > 0)) {
						newFirstRect = getCellBounds(--newFirst);
						inc = rect.y - newFirstRect.y;
					}
				}
			}
			return inc;
		} else return rect.width;
	}

	/**
	 * リストコンポーネントの幅をビューポートの幅に強制一致させるか返します。
	 *
	 * @return 強制一致させる場合trueを返す
	 */
	@Override
	public boolean getScrollableTracksViewportWidth() {
		if (getParent() instanceof JViewport) {
			var vp = (JViewport) getParent();
			return (vp.getWidth() > getPreferredSize().width);
		}
		return false;
	}

	/**
	 * リストコンポーネントの高さをビューポートの高さに強制一致させるか返します。
	 *
	 * @return 強制一致させる場合trueを返す
	 */
	@Override
	public boolean getScrollableTracksViewportHeight() {
		if (getParent() instanceof JViewport) {
			var vp = (JViewport) getParent();
			return (vp.getHeight() > getPreferredSize().height);
		}
		return false;
	}

	/**
	 * リストの選択モデルを返します。
	 *
	 * @return 選択モデル
	 */
	public ListSelectionModel getSelectionModel() {
		return selection;
	}

	/**
	 * リストに選択モデルを設定します。
	 *
	 * @param newModel 選択モデル
	 */
	public void setSelectionModel(ListSelectionModel newModel) {
		var oldModel = this.selection;
		if (oldModel == null) {
			this.handler = new ListSelectionHandler();
		} else {
			oldModel.removeListSelectionListener(this.handler);
		}
		newModel.addListSelectionListener(this.handler);
		this.selection = newModel;
		firePropertyChange("selectionModel", oldModel, newModel);
	}

	/**
	 * スクロールしないで可視範囲に表示可能な最大行数を返します。
	 *
	 * @return 最大行数
	 */
	public int getVisibleRowCount() {
		return visibleRowCount;
	}

	/**
	 * スクロールしないで可視範囲に表示可能な最大行数を指定します。
	 *
	 * @param visibleRowCount 最大行数
	 */
	public void setVisibleRowCount(int visibleRowCount) {
		final var old = this.visibleRowCount;
		this.visibleRowCount = visibleRowCount;
		firePropertyChange("visibleRowCount", old, visibleRowCount);
	}

	/**
	 * リスト内の指定されたインデックスのセルの原点座標を返します。
	 *
	 * @param index インデックス
	 */
	public Point indexToLocation(int index) {
		return items.get(index).getLocation();
	}

	/**
	 * リスト内の指定された座標に最も近いセルのインデックスを返します。
	 *
	 * @param location 座標
	 */
	public int locationToIndex(Point location) {
		final var size = data.getSize();
		var now = getInsets().top;
		var max = location.y;
		for (var i = 0; i < size; i++) {
			if (now >= max) return i;
			now += items.get(i).getHeight();
		}
		return size - 1;
	}

	/**
	 * リストから選択リスナーを削除します。
	 *
	 * @param listener 削除するリスナー
	 */
	public void removeListSelectionListener(ListSelectionListener listener) {
		listenerList.remove(ListSelectionListener.class, listener);
	}

	private class CheckBoxItem extends JCheckBox {
		private static final long serialVersionUID = 1L;

		public CheckBoxItem(Object item) {
			super(String.valueOf(item));
			setOpaque(false);
			setBorderPainted(false);
			setFocusPainted(false);
			setFocusable(false);
			setRequestFocusEnabled(false);
			addItemListener(LeafCheckList.this.itemListener);
		}

		public void setValue(E value) {
			setText(String.valueOf(value));
		}
	}

	private class CheckItemListener implements ItemListener {
		@Override
		public void itemStateChanged(ItemEvent e) {
			var cb = (JCheckBox) e.getItemSelectable();
			var index = items.indexOf(cb);
			if (cb.isSelected()) {
				selection.addSelectionInterval(index, index);
			} else {
				selection.removeSelectionInterval(index, index);
			}
		}
	}

	private class ModelObserver implements ListDataListener {
		@Override
		public void intervalAdded(ListDataEvent e) {
			final var index0 = e.getIndex0();
			final var index1 = e.getIndex1();
			for (var i = index0; i <= index1; i++) {
				var val = data.getElementAt(i);
				var cb = new CheckBoxItem(val);
				box.add(cb, i);
				items.add(i, cb);
			}
			box.revalidate();
		}

		@Override
		public void intervalRemoved(ListDataEvent e) {
			final var index0 = e.getIndex0();
			final var index1 = e.getIndex1();
			for (var i = index0; i <= index1; i++) {
				box.remove(items.remove(index0));
			}
			box.revalidate();
		}

		@Override
		public void contentsChanged(ListDataEvent e) {
			final var index0 = e.getIndex0();
			final var index1 = e.getIndex1();
			for (var i = index0; i <= index1; i++) {
				var val = data.getElementAt(i);
				items.get(i).setValue(val);
			}
		}
	}

	private class ListSelectionHandler implements ListSelectionListener {
		@Override
		public void valueChanged(ListSelectionEvent e) {
			final var first = e.getFirstIndex();
			final var last = e.getLastIndex();
			final var adj = e.getValueIsAdjusting();
			for (var i = first; i <= last; i++) {
				items.get(i).setSelected(selection.isSelectedIndex(i));
			}
			fireSelectionValueChanged(first, last, adj);
		}
	}

}
