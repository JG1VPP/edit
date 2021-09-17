/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.shell;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import leaf.edit.cmd.SelectionCommand;
import leaf.util.BrowseUtils;

/**
 * 選択文字列をYahooで検索するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SearchWithYahoo extends SelectionCommand {

	@Override
	public void process(Object... args) throws URISyntaxException {
		var keyword = getEditor().getSelectedText();
		var template = "http://search.yahoo.com/search?p=";
		BrowseUtils.browse(new URI(template + URLEncoder.encode(keyword, StandardCharsets.UTF_8)));
	}
}
