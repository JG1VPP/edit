/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.main.Command;
import leaf.edit.app.Framework;

/**
 * テキストエディタと実行環境を終了するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Exit extends Command {
	@Override
	public void process(Object... args) {
		Exit.exit();
	}

	/**
	 * エディタとシステム全体を終了するよう試みます。
	 */
	public static void exit() {
		if (CloseAllTabs.closeAllTabs()) Framework.shutdown(0);
	}

}
