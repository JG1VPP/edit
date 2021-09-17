/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import javax.swing.*;

import leaf.edit.cmd.Command;
import leaf.main.Shell;

/**
 * 常に最前面に表示するコマンドを処理します。
 */
public final class AlwaysOnTop extends Command {

	@Override
	public JMenuItem createMenuItem(JMenuItem item) {
		item = new JCheckBoxMenuItem();
		item.addActionListener(Shell.getInstance());
		item.setActionCommand(getClass().getSimpleName());
		return item;
	}

	@Override
	public void process(Object... args) {
		getFrame().setAlwaysOnTop(!getFrame().isAlwaysOnTop());
	}
}
