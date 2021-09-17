/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * エディタをユーザーの好みで装飾するためのカーソルファクトリです。
 *
 * @since 2011年12月28日
 */
public final class CursorFactory {
	/**
	 * カーソル設定を指定してカーソルを生成します。
	 *
	 * @param cs カーソル設定
	 *
	 * @return 生成されたカーソル
	 */
	public static Cursor createCursor(CursorSettings cs) {
		var cursor = Cursor.getPredefinedCursor(cs.getType());
		var file = cs.getPhotoFile();
		if (file != null) try {
			var image = ImageIO.read(file);
			var trim = cs.getPhotoTrimRect();
			if (trim != null) image = image.getSubimage(trim.x, trim.y, trim.width, trim.height);
			var spot = new Point(0, 0);
			var name = cursor.getName();
			var kit = Toolkit.getDefaultToolkit();
			cursor = kit.createCustomCursor(image, spot, name);
		} catch (IOException ex) {
		}
		return cursor;
	}
}
