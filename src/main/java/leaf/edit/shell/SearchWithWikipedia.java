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
import java.util.regex.Pattern;

import leaf.edit.cmd.SelectionCommand;
import leaf.util.LocalizeManager;
import leaf.util.BrowseUtils;

/**
 * 選択文字列をWikipediaで検索するコマンドです。
 *
 * @author 無線部開発班
 */
public final class SearchWithWikipedia extends SelectionCommand {
	private final Pattern dom = Pattern.compile("[dom]", Pattern.LITERAL);

	@Override
	public void process(Object... args) throws URISyntaxException {
		var keyword = getEditor().getSelectedText();
		var template = "http://[dom].wikipedia.org/wiki/";
		var url = dom.matcher(template).replaceFirst(LocalizeManager.getLocale().getLanguage());
		BrowseUtils.browse(new URI(url + URLEncoder.encode(keyword, StandardCharsets.UTF_8)));
	}
}
