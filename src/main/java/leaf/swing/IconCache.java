/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.util.Map;
import java.util.WeakHashMap;
import javax.swing.*;

/**
 * アイコンのキャッシュです。
 *
 * @author 無線部開発班
 * @since 2012年9月30日
 */
final class IconCache {
	private static Map<String, ImageIcon> cache;

	private IconCache() {
	}

	/**
	 * 指定した名前のアイコンデータを返します。
	 *
	 * @param name アイコンファイルの名前
	 *
	 * @return アイコン
	 */
	public static ImageIcon getIcon(String name) {
		if (cache == null) cache = new WeakHashMap<>();
		var icon = cache.get(name);
		if (icon != null) return icon;
		var image = ImageCache.getImage(name);
		if (image == null) return null;
		icon = new ImageIcon(image);
		cache.put(name, icon);
		return icon;
	}
}
