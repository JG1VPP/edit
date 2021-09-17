/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.EditorCommand;

/**
 * エディタにOSの名前とバージョンを挿入するコマンドです。
 *
 * @author 無線部開発班
 */
public final class InsertOSVersion extends EditorCommand {
	@Override
	public void process(Object... args) {
		var name = System.getProperty("os.name");
		var version = System.getProperty("os.version");
		getEditor().replaceSelection(name + ' ' + version);
	}
}
