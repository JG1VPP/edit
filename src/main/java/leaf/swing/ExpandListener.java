/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.util.EventListener;

/**
 * {@link LeafExpandPane}の展開/折りたたみのイベントを受信するリスナーです。
 *
 * @author 無線部開発班
 * @since 2010年7月10日
 */
public interface ExpandListener extends EventListener {
	/**
	 * 展開状態の変更時に呼び出されます。
	 *
	 * @param e 変更内容を表すExpandEvent
	 */
	void stateChanged(ExpandEvent e);
}
