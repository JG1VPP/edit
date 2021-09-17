/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * 画像データのキャッシュです。
 *
 * @author 無線部開発班
 * @since 2012年9月30日
 */
final class ImageCache {
	private static Map<String, Image> cache;

	private ImageCache() {
	}

	/**
	 * 指定した名前の画像データを返します。
	 *
	 * @param name 画像ファイルの名前
	 *
	 * @return 画像
	 */
	public static Image getImage(String name) {
		if (cache == null) cache = new WeakHashMap<>();
		var image = cache.get(name);
		if (image != null) return image;
		return ImageLoader.load(name);
	}
}
