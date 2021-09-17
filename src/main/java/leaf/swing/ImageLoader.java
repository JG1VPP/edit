/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * 画像データをロードします。
 *
 * @author 無線部開発班
 * @since 2012年9月30日
 */
final class ImageLoader {
	private static URLClassLoader loader = null;

	private ImageLoader() {
	}

	/**
	 * 指定したパスで画像を取り出します。
	 *
	 * @param path 画像までのパス
	 *
	 * @return 画像が存在しない場合nullを返す
	 */
	public static Image load(String path) {
		InputStream stream = null;
		try {
			if (loader == null) loader = loader();
			stream = loader.getResourceAsStream(path);
			return ImageIO.read(Objects.requireNonNull(stream));
		} catch (IllegalArgumentException | IOException ex) {
			return null;
		} finally {
			try {
				if (stream != null) stream.close();
			} catch (IOException ex) {
			}
		}
	}

	/**
	 * 画像のロードに利用するクラスローダーを準備します。
	 *
	 * @return クラスローダー
	 *
	 * @throws IOException 準備に失敗した場合
	 */
	private static URLClassLoader loader() throws IOException {
		var clazz = LeafIcons.class;
		var file = clazz.getSimpleName() + ".class";
		var curl = clazz.getResource(file);
		var conn = Objects.requireNonNull(curl).openConnection();
		var root = ((JarURLConnection) conn).getJarFileURL();
		return URLClassLoader.newInstance(new URL[]{root});
	}
}
