/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.app;

/**
 * tseditを起動します。
 *
 * @author 無線部開発班
 * @since 2013/01/03
 */
public class Startup {

	/**
	 * フレームワークを起動してからアプリケーションを起動します。
	 *
	 * @param args コマンドライン引数
	 */
	public static void main(String[] args) {
		Framework.startup(TsEditApp.class);
	}

}
