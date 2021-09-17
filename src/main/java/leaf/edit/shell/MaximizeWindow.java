/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import javax.swing.*;

import leaf.edit.cmd.Command;

/**
 * メインウィンドウを最大化するコマンドです。
 *
 * @author 無線部開発班
 */
public final class MaximizeWindow extends Command {
	@Override
	public void process(Object... args) {
		getFrame().setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
}
