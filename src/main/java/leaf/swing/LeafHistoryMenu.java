/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.swing.*;

import leaf.util.LocalizeManager;

/**
 * 「最近使ったファイル」など、履歴を表示するメニューです。
 *
 * @author 無線部開発班
 * @since 2010年6月8日
 */
public class LeafHistoryMenu extends JMenu {
	/**
	 * 同時に表示される履歴の最大数です。
	 */
	public static final int HISTORY_MAX = 16;
	private static final long serialVersionUID = 1L;
	private final LocalizeManager localize;
	private final List<Object> list;

	/**
	 * 空の履歴メニューを生成します。
	 */
	public LeafHistoryMenu() {
		this(null);
	}

	/**
	 * 初期履歴を指定して履歴メニューを生成します。
	 *
	 * @param arr 履歴
	 */
	public LeafHistoryMenu(Object[] arr) {
		super("History");
		localize = LocalizeManager.get(LeafHistoryMenu.class);
		setText(localize.translate("menu_text"));
		setMnemonic(KeyEvent.VK_H);
		list = new ArrayList<>();
		addAll(arr);
	}

	/**
	 * 履歴リストを設定します。
	 *
	 * @param arr 履歴の配列
	 */
	public void addAll(Object[] arr) {
		list.clear();
		if (arr != null) Collections.addAll(list, arr);
		update();
	}

	/**
	 * HistoryMenuListenerを追加します。
	 *
	 * @param listener リスナー
	 */
	public void addHistoryMenuListener(HistoryMenuListener listener) {
		listenerList.add(HistoryMenuListener.class, listener);
	}

	/**
	 * 履歴の末尾にアイテムを追加します。
	 *
	 * @param item 追加履歴
	 */
	public void addItem(Object item) {
		list.remove(item);
		list.add(0, item);
		if (list.size() > HISTORY_MAX) list.remove(HISTORY_MAX);
		update();
	}

	/**
	 * 履歴を全て消去します。
	 */
	public void clear() {
		list.clear();
		update();
	}

	/**
	 * 履歴消去のメニューアイテムを生成します。
	 *
	 * @return アイテム
	 */
	private JMenuItem createClearMenuItem() {
		var item = new JMenuItem(localize.translate("clear_text"));
		item.setMnemonic(KeyEvent.VK_C);
		item.addActionListener(e -> {
			list.clear();
			update();
		});
		return item;
	}

	/**
	 * 各履歴に対応するメニューアイテムを生成します。
	 *
	 * @param item 対応するアイテム
	 * @param num  アイテムの番号
	 */
	private JMenuItem createHistoryMenuItem(final Object item, int num) {
		var text = String.valueOf(item);
		var index = Integer.toHexString(num).toUpperCase();
		var menuItem = new JMenuItem(index + "  " + text);
		menuItem.setMnemonic(index.charAt(0));
		menuItem.addActionListener(e -> fireHistoryClicked(item));
		return menuItem;
	}

	/**
	 * HistoryMenuListenerを呼び出します。
	 *
	 * @param item クリックされたアイテム
	 */
	private void fireHistoryClicked(Object item) {
		final var e = new HistoryMenuEvent(this, item);
		var listeners = listenerList.getListeners(HistoryMenuListener.class);
		for (var l : listeners) l.historyClicked(e);
	}

	/**
	 * 履歴を返します。
	 *
	 * @return 履歴
	 */
	public Object[] getAll() {
		return list.toArray();
	}

	/**
	 * HistoryMenuListenerを削除します。
	 *
	 * @param listener 削除するリスナー
	 */
	public void removeHistoryMenuListener(HistoryMenuListener listener) {
		listenerList.remove(HistoryMenuListener.class, listener);
	}

	/**
	 * 履歴の表示を更新します。
	 */
	private void update() {
		var size = Math.min(HISTORY_MAX, list.size());
		removeAll();
		for (var i = 0; i < size; i++) add(createHistoryMenuItem(list.get(i), i));
		addSeparator();
		add(createClearMenuItem());
		setEnabled(list.size() > 0);
	}

}
