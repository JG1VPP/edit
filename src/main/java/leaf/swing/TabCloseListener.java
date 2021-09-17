/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.util.EventListener;

/**
 * {@link LeafTabbedPane}のタブが閉じられる時に呼び出されます。
 *
 * @author 無線部開発班
 * @since 2010年3月12日
 */
public interface TabCloseListener extends EventListener {
	/**
	 * タブ項目の「閉じる」ボタンが押されたことを通知します。
	 *
	 * @param e イベント
	 *
	 * @return 閉じても良い場合はtrue
	 */
	boolean tabClosing(TabCloseEvent e);

}
