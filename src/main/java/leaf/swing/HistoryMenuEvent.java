/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.util.EventObject;

/**
 * 履歴がクリックされた時に通知されるイベントです。
 *
 * @author 無線部開発班
 * @since 2010年5月6日
 */
public class HistoryMenuEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private final Object item;

	/**
	 * イベント発生源と対応するアイテムを指定してイベントを発行します。
	 *
	 * @param source イベントの発生源
	 * @param item   イベントに対応する履歴アイテム
	 */
	public HistoryMenuEvent(Object source, Object item) {
		super(source);
		this.item = item;
	}

	/**
	 * ユーザーが選択した履歴アイテムを返します。
	 *
	 * @return イベントに対応する履歴アイテム
	 */
	public Object getItem() {
		return item;
	}

}
