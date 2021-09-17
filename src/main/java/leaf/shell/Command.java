/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.shell;

/**
 * シェルに統合される各種コマンドの基底クラスです。
 *
 * @author 無線部開発班
 * @since 2011年8月31日
 */
public abstract class Command {
	/**
	 * このオブジェクトが担当するコマンドの名前を返します。
	 * デフォルトでは、クラスの単純名をコマンド名とします。
	 *
	 * @return コマンド名(クラスの単純名)
	 */
	public String getName() {
		return getClass().getSimpleName();
	}

	/**
	 * このオブジェクトが担当するコマンドを処理します。
	 *
	 * @param args コマンドへの引数
	 *
	 * @throws Exception この処理が発生しうる例外
	 */
	public abstract void process(Object... args) throws Exception;
}
