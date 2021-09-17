/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

/**
 * 取り外し可能なモジュールが実装するインターフェースです。
 *
 * @author 無線部開発班
 * @since 2012/12/28
 */
public interface Removable {
	/**
	 * このモジュールを終了します。
	 *
	 * @return 終了準備が整った場合true
	 */
	boolean shutdown();

	/**
	 * このモジュールが開始する時に呼び出されます。
	 */
	void start();

}
