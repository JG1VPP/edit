/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;

/**
 * テキストエディタのフォントを小に設定します。
 *
 * @author 無線部開発班
 */
public final class SetFontSizeSmall extends Command {
	@Override
	public void process(Object... args) {
		SetFont.setFont(SetFont.getFont().deriveFont(12f));
	}
}