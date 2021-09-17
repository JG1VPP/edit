/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.SelectionCommand;

/**
 * 切り取りコマンドを処理します。
 */
public final class Cut extends SelectionCommand {
	@Override
	public void process(Object... args) {
		getEditor().cut();
	}
}
