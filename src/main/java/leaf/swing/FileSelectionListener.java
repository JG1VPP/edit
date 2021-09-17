/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.util.EventListener;

/**
 * {@link LeafFileTree}のファイル選択イベントを受信するリスナーです。
 *
 * @author 無線部開発班
 * @since 2010年7月10日
 */
public interface FileSelectionListener extends EventListener {
	/**
	 * ファイル選択時に呼び出されます。
	 *
	 * @param e ファイル選択イベント
	 */
	void fileSelected(FileSelectionEvent e);

}
