/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.swing.BlueScreen;

/**
 * 緊急時に疑似的に青画面を全画面表示してウィンドウを隠します。
 *
 * @author 無線部開発班
 * @since 2013/03/17
 */
public class HideWindows extends Command {
	@Override
	public void process(Object... args) {
		new BlueScreen("Press ESC key", "");
	}

}
