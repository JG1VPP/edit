/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.shell;

import javax.xml.namespace.QName;

/**
 * ビルド文書の要素や属性がこのパッケージで未定義である場合にスローされます。
 *
 * @author 無線部開発班
 * @since 2011年12月11日
 */
public class UnknownNameException extends Exception {
	private static final long serialVersionUID = 1L;
	private final String name;

	/**
	 * 指定された詳細メッセージを持つ例外を生成します。
	 *
	 * @param name 例外の原因となった名前
	 */
	public UnknownNameException(String name) {
		super(name);
		this.name = name;
	}

	/**
	 * 指定された原因を詳細メッセージに持つ例外を生成します。
	 *
	 * @param qname 例外の原因となった名前
	 */
	public UnknownNameException(QName qname) {
		this(qname.toString());
	}

	/**
	 * この例外の原因となった名前を返します。
	 *
	 * @return 例外の原因となった名前
	 */
	public String getName() {
		return name;
	}

}
