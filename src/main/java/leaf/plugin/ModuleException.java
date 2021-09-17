/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

/**
 * モジュールのロードに失敗した場合にスローされます。
 *
 * @author 無線部開発班
 * @since 2012/06/16
 */
public class ModuleException extends Exception {
	private static final long serialVersionUID = 1L;

	/**
	 * 指定されたメッセージを持つ例外を構築します。
	 *
	 * @param msg 例外の内容を説明する文字列
	 */
	public ModuleException(String msg) {
		super(msg);
	}

}
