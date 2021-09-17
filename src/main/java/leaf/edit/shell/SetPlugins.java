/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.app.TsEditApp;
import leaf.edit.cmd.Command;
import leaf.plugin.ManagerDialog;
import leaf.plugin.ModuleManager;

/**
 * ガジェットの設定を行うコマンドです。
 *
 * @author 無線部開発班
 * @since 2012/09/27
 */
public final class SetPlugins extends Command {
	@Override
	public void process(Object... args) {
		var frame = TsEditApp.getMainFrame();
		var manager = ModuleManager.getInstance();
		new ManagerDialog(frame, manager).setVisible(true);
	}
}
