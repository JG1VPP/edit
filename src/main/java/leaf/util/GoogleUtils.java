/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Googleによる文字列検索を行うためのURIを生成します。また、
 * Google News英語版のURLを返します。
 *
 * @author 無線部開発班
 * @since 2012/04/30
 */
public final class GoogleUtils {
	private static final String template = "http://www.google.com/search?q=";

	public static URI createSearchURI(String query) {
		try {
			return new URI(template + URLEncoder.encode(query, StandardCharsets.UTF_8));
		} catch (URISyntaxException ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public static String getNewsURL() {
		return "https://news.google.com/news/rss/headlines/";
	}
}
