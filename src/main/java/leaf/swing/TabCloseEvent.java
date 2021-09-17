/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.util.EventObject;

public class TabCloseEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private final Component component;

	/**
	 * イベントの発生源と閉じられるタブを指定してイベントを構築します。
	 *
	 * @param source イベントの発生源
	 * @param comp   このイベントの後に閉じられるコンポーネント
	 */
	public TabCloseEvent(Object source, Component comp) {
		super(source);
		this.component = comp;
	}

	/**
	 * このイベントの後に閉じられるコンポーネントを返します。
	 *
	 * @return コンポーネント
	 */
	public Component getComponent() {
		return component;
	}

}
