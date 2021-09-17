/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.util.EventObject;

/**
 * {@link LeafExpandPane}の展開状態の変更を表現するイベントクラスです。
 *
 * @author 無線部開発班
 * @since 2010年7月10日
 */
public class ExpandEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	/**
	 * イベントの発生元のオブジェクトを指定してイベントを構築します。
	 *
	 * @param source
	 */
	public ExpandEvent(Object source) {
		super(source);
	}

}
