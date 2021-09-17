/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.edit.ui.SyntaxHighlight;

/**
 * 構文強調キーワードと配色を設定するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SetSyntaxHighlight extends Command {
	@Override
	public void process(Object... args) {
		SyntaxHighlight.showConfigurationDialog();
	}
}
