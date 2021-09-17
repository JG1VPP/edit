/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Objects;
import java.util.jar.JarFile;

/**
 * JARファイル内に格納されているリソースを読み込むユーティリティです。
 *
 * @author 無線部開発班
 * @since 2013/01/14
 */
public final class Resources {
	private final URL url;
	private final JarFile jar;
	private final URLClassLoader loader;

	/**
	 * クラスを格納するJARファイルのResourcesオブジェクトを構築します。
	 *
	 * @param c JARファイルに格納されているクラス
	 *
	 * @throws IOException クラスがJARファイル内にない場合
	 */
	public Resources(Class<?> c) throws IOException {
		var curl = c.getResource(c.getSimpleName() + ".class");
		var uc = Objects.requireNonNull(curl).openConnection();
		jar = ((JarURLConnection) uc).getJarFile();
		url = ((JarURLConnection) uc).getJarFileURL();
		var parent = getClass().getClassLoader();
		loader = new URLClassLoader(new URL[]{url}, parent);
	}

	/**
	 * JARファイル内を探索する{@link ClassLoader}を返します。
	 *
	 * @return クラスローダー
	 */
	public ClassLoader getClassLoader() {
		return loader;
	}

	/**
	 * 指定したパスのエントリを読み込む{@link InputStream}を返します。
	 *
	 * @param path JARファイルを起点とするJARファイル内のパス
	 *
	 * @return InputStream
	 *
	 * @throws IOException パスに対応するエントリが存在しない場合
	 */
	public InputStream getInputStream(String path) throws IOException {
		return jar.getInputStream(jar.getEntry(path));
	}

	/**
	 * {@link JarFile}オブジェクトを返します。
	 *
	 * @return JarFile
	 */
	public JarFile getJarFile() {
		return jar;
	}

	/**
	 * JARファイルへのパスを表すURLを返します。
	 *
	 * @return JARファイルのURL
	 */
	public URL getJarURL() {
		return url;
	}

	/**
	 * 指定したパスのエントリを表すURLを返します。
	 *
	 * @param path JARファイルを起点とするJARファイル内のパス
	 *
	 * @return pathに対応するURL
	 */
	public URL getURL(String path) {
		return loader.getResource(path);
	}

	/**
	 * 指定されたクラスのJARファイルのあるディレクトリを返します。
	 *
	 * @param c クラス
	 *
	 * @return JARファイルが配置されている場所 JARでなければnull
	 */
	public static File getJarFileDir(Class<?> c) {
		var jarfile = getJarFileOf(c);
		return jarfile != null ? jarfile.getParentFile() : null;
	}

	/**
	 * 指定されたクラスが格納されているJARファイルを返します。
	 *
	 * @param c クラス
	 *
	 * @return JARファイルへのパス JARでなければnull
	 */
	public static File getJarFileOf(Class<?> c) {
		try {
			return new File(new Resources(c).getJarFile().getName());
		} catch (IOException ex) {
			return null;
		}
	}

}
