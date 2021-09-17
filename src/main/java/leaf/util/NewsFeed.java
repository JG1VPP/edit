/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.Serializable;
import java.net.URL;
import java.util.*;

/**
 * リーダーで受信したフィードを表現します。
 *
 * @author 無線部開発班
 * @since 2011年5月2日
 */
public class NewsFeed implements Serializable {
	private static final long serialVersionUID = 1L;
	private final List<NewsItem> items;
	private final Map<String, List<NewsItem>> categories;
	private URL link;
	private Date date;
	private Locale language;
	private String copyright, description;
	private String generator, title;

	/**
	 * 空のフィードを生成します。
	 */
	public NewsFeed() {
		items = new ArrayList<>();
		categories = new HashMap<>();
	}

	/**
	 * カテゴリを指定しないでフィードにアイテムを追加します。
	 *
	 * @param item 追加するアイテム
	 */
	public void addItem(NewsItem item) {
		if (!items.contains(item)) items.add(item);
	}

	/**
	 * 指定されたカテゴリでフィードにアイテムを追加します。
	 *
	 * @param category アイテムのカテゴリ
	 * @param item     追加するアイテム
	 */
	public void addItem(String category, NewsItem item) {
		addItem(item);
		if (!categories.containsKey(category)) {
			categories.put(category, new ArrayList<>());
		}
		categories.get(category).add(item);
	}

	/**
	 * フィードに含まれる全てのカテゴリを返します。
	 *
	 * @return カテゴリの一覧
	 */
	public String[] getCategories() {
		return categories.keySet().toArray(new String[0]);
	}

	/**
	 * フィードの著作権表示を返します。
	 *
	 * @return 著作権表示
	 */
	public String getCopyright() {
		return copyright;
	}

	/**
	 * フィードに著作権表示を設定します。
	 *
	 * @param copyright 著作権表示
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}

	/**
	 * フィードの発行された日時を返します。
	 *
	 * @return 発行日時
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * フィードが発行された日時を設定します。
	 *
	 * @param date 発行日時
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * フィードの内容を説明する文字列を返します。
	 *
	 * @return フィードの説明
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * フィードの内容を説明する文字列を設定します。
	 *
	 * @param description フィードの説明
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * フィードを生成したソフトウェアの名前を返します。
	 *
	 * @return ジェネレータの名前
	 */
	public String getGenerator() {
		return generator;
	}

	/**
	 * フィードを生成したソフトウェアの名前を設定します。
	 *
	 * @param generator ジェネレータの名前
	 */
	public void setGenerator(String generator) {
		this.generator = generator;
	}

	/**
	 * 全てのカテゴリのアイテムを返します。
	 *
	 * @return アイテムの一覧
	 */
	public NewsItem[] getItems() {
		return items.toArray(new NewsItem[0]);
	}

	/**
	 * 指定したカテゴリのアイテムを返します。
	 *
	 * @param category アイテムのカテゴリ
	 *
	 * @return 該当するアイテムの一覧
	 */
	public NewsItem[] getItems(String category) {
		return categories.get(category).toArray(new NewsItem[0]);
	}

	/**
	 * フィードの表示言語を返します。
	 *
	 * @return フィードの言語
	 */
	public Locale getLanguage() {
		return language;
	}

	/**
	 * フィードに表示言語を設定します。
	 *
	 * @param language フィードの言語
	 */
	public void setLanguage(Locale language) {
		this.language = language;
	}

	/**
	 * フィードの参照先リンクを返します。
	 *
	 * @return リンク
	 */
	public URL getLink() {
		return link;
	}

	/**
	 * フィードに参照先リンクを設定します。
	 *
	 * @param link リンク
	 */
	public void setLink(URL link) {
		this.link = link;
	}

	/**
	 * フィードのタイトルを返します。
	 *
	 * @return フィードのタイトル
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * フィードにタイトルを設定します。
	 *
	 * @param title フィードのタイトル
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * フィードの文字列化表現を返します。
	 *
	 * @return 文字列化表現
	 */
	public String toString() {
		var sb = new StringBuilder(title);
		for (var category : categories.keySet()) {
			sb.append(" / ").append(category);
		}
		return sb.toString();
	}

}
