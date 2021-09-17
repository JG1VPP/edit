/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.io.Serializable;
import java.util.List;

/**
 * キーワード強調設定を保存するためのクラスです。
 *
 * @author 無線部開発班
 * @since 2010年9月16日
 */
public class SyntaxSaveData implements Serializable {
	private static final long serialVersionUID = 1L;
	private List<KeywordSet> data;

	/**
	 * 空の保存データを持つインスタンスを構築します。
	 */
	public SyntaxSaveData() {
		data = null;
	}

	/**
	 * 保存データを指定して保存クラスを生成します。
	 *
	 * @param data 保存するキーワードセットのリスト
	 */
	public SyntaxSaveData(List<KeywordSet> data) {
		this.data = data;
	}

	/**
	 * 保存データを返します。
	 *
	 * @return 保存するキーワードセットのリスト
	 */
	public List<KeywordSet> getData() {
		return data;
	}

	/**
	 * 保存データを設定します。
	 *
	 * @param data 保存するキーワードセットのリスト
	 */
	public void setData(List<KeywordSet> data) {
		this.data = data;
	}

}
