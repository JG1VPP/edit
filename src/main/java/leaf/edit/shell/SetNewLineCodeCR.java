/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import javax.swing.*;

import leaf.edit.cmd.Command;
import leaf.edit.ui.TextEditorUtils;
import leaf.main.Shell;

/**
 * 保存時の改行コードをCRに設定するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SetNewLineCodeCR extends Command {
	@Override
	public JMenuItem createMenuItem(JMenuItem item) {
		item = new JRadioButtonMenuItem("CR");
		item.addActionListener(Shell.getInstance());
		item.setActionCommand(getClass().getSimpleName());
		item.setSelected(TextEditorUtils.getLineSeparator().equals("\r"));
		return item;
	}

	@Override
	public void process(Object... args) {
		TextEditorUtils.setLineSeparator("\r");
	}
}
