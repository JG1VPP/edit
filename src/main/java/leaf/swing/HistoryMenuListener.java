/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.util.EventListener;

/**
 * 履歴が選択された時に呼び出される専用のリスナーです。
 *
 * @author 無線部開発班
 * @since 2010年5月6日
 */
public interface HistoryMenuListener extends EventListener {
	/**
	 * 履歴が選択されたときに呼び出されます。
	 *
	 * @param e 通知内容を表すイベント
	 */
	void historyClicked(HistoryMenuEvent e);
}
