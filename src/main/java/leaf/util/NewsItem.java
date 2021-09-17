/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * フィードに含まれるヘッドラインを表現します。
 *
 * @author 無線部開発班
 * @since 2011年5月2日
 */
public class NewsItem implements Serializable {
	private static final long serialVersionUID = 1L;
	private String title, description;
	private URL link;
	private Date date;

	/**
	 * 空のアイテムを生成します。
	 */
	public NewsItem() {
	}

	/**
	 * アイテムの発行日付を返します。
	 *
	 * @return 日付
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * アイテムの発行日付を設定します。
	 *
	 * @param date 日付
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * アイテムの説明を返します。
	 *
	 * @return 説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * アイテムの説明を設定します。
	 *
	 * @param desc 説明
	 */
	public void setDescription(String desc) {
		this.description = desc;
	}

	/**
	 * アイテムのリンクを返します。
	 *
	 * @return リンク
	 */
	public URL getLink() {
		return link;
	}

	/**
	 * アイテムのリンクを設定します。
	 *
	 * @param link リンク
	 */
	public void setLink(URL link) {
		this.link = link;
	}

	/**
	 * アイテムのタイトルを返します。
	 *
	 * @return タイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * アイテムのタイトルを設定します。
	 *
	 * @param title タイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * アイテムの文字列化表現を返します。
	 *
	 * @return アイテムを表す文字列
	 */
	public String toString() {
		return title;
	}

}
