/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;

/**
 * LeafAPIで用意されているアイコンリソースを提供します。
 *
 * @author 無線部開発班
 * @since 2010年6月12日
 */
public final class LeafIcons {
	private static final String ICON_DIR_PATH = "images/16x16/";
	private static final String IMAGE_DIR_PATH = "images/style/";

	private LeafIcons() {}

	/**
	 * 指定した名前でサイズが16x16のアイコンを取り出します。
	 *
	 * @param name アイコンの名前
	 *
	 * @return 存在しない場合nullを返す
	 */
	public static Icon getIcon(String name) {
		return IconCache.getIcon(ICON_DIR_PATH + name + ".png");
	}

	/**
	 * 指定した名前の画像を取り出します。
	 *
	 * @param name 画像の名前
	 *
	 * @return 画像が存在しない場合nullを返す
	 */
	public static Image getImage(String name) {
		return ImageCache.getImage(IMAGE_DIR_PATH + name + ".png");
	}
}
