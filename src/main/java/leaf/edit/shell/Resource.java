/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import leaf.edit.cmd.Command;
import leaf.shell.LocaleEvent;
import leaf.shell.LocaleListener;
import leaf.swing.LeafResourceMonitor;

/**
 * リソースモニタを表示するコマンドです。
 *
 * @author 無線部開発班
 * @since 2012/09/25
 */
public class Resource extends Command implements LocaleListener {
	private LeafResourceMonitor monitor = null;

	@Override
	public void localeChanged(LocaleEvent arg0) {
		if (monitor != null) monitor.initialize();
	}

	@Override
	public void process(Object... args) {
		if (monitor == null) monitor = new LeafResourceMonitor(getFrame());
		monitor.setVisible(true);
	}

}
