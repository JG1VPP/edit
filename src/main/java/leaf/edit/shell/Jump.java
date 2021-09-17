/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import javax.swing.*;

import leaf.edit.cmd.EditorCommand;
import leaf.util.LocalizeManager;

/**
 * テキストエディタ内の指定行にキャレットを移動させるコマンドです。
 *
 * @author 無線部開発班
 */
public final class Jump extends EditorCommand {
	private final LocalizeManager localize;

	public Jump() {
		localize = LocalizeManager.get(getClass());
	}

	@Override
	public void process(Object... args) {
		var input = JOptionPane.showInputDialog(getFrame(), localize.translate("line_number"), localize.translate("command_name"), JOptionPane.PLAIN_MESSAGE);
		if (input != null && !input.isEmpty()) {
			try {
				getEditor().getScrollPane().scrollToLine(Integer.parseInt(input.trim()));
			} catch (NumberFormatException ex) {
				JOptionPane.showMessageDialog(getFrame(), localize.translate("illegal_number_format", input), localize.translate("command_name"), JOptionPane.INFORMATION_MESSAGE);
			}
		}
	}
}
