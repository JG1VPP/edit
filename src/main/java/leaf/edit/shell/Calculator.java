/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;
import leaf.swing.LeafCalcDialog;

/**
 * 高精度電卓を表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class Calculator extends Command implements LocaleListener {
	private LeafCalcDialog dialog;

	@Override
	public void localeChanged(LocaleEvent e) {
		if (dialog != null) dialog.initialize();
	}

	@Override
	public void process(Object... args) {
		if (dialog == null) dialog = new LeafCalcDialog(getFrame());
		dialog.setVisible(true);
	}

}
