/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.net.URI;
import java.net.URISyntaxException;

import leaf.edit.cmd.Command;
import leaf.util.BrowseUtils;

/**
 * プロジェクトのウェブページを表示するコマンドです。
 *
 * @author 無線部開発班
 */
public final class HomePage extends Command {
	@Override
	public void process(Object... args) {
		try {
			BrowseUtils.browse(new URI("https://github.com/jg1vpp/edit"));
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
		}
	}
}
