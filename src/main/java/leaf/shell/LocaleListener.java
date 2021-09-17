/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.shell;

/**
 * {@link Command}継承クラスが{@link LocaleEvent}を受け取る場合に実装されます。
 *
 * @author 無線部開発班
 * @since 2011年12月11日
 */
public interface LocaleListener {
	/**
	 * ロケール設定の変更を受け取ります。
	 *
	 * @param e 新しいロケールのイベント
	 */
	void localeChanged(LocaleEvent e);
}
