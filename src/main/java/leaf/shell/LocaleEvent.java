/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.shell;

import java.util.EventObject;
import java.util.Locale;

/**
 * シェルのロケールが変更された場合に通知されるイベントです。
 *
 * @author 無線部開発班
 * @since 2011年12月11日
 */
public class LocaleEvent extends EventObject {
	private static final long serialVersionUID = 1L;
	private final Locale locale;

	/**
	 * イベントの発生源とロケールを指定してイベントを構築します。
	 *
	 * @param source イベントの発生源、つまりシェル本体
	 * @param locale 新しく適用されるロケール
	 */
	public LocaleEvent(Object source, Locale locale) {
		super(source);
		this.locale = locale;
	}

	/**
	 * イベントが通知するロケールを返します。
	 *
	 * @return 新しく適用されるロケール
	 */
	public Locale getLocale() {
		return locale;
	}

}
