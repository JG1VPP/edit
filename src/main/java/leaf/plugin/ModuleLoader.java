/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarFile;

/**
 * URLで指定された場所に存在するモジュールを取得します。
 *
 * @author 無線部開発班
 * @since 2012/06/16
 */
class ModuleLoader {
	protected final JarFile jarfile;
	private final String entry;
	private final URL url, jpath;
	private final URLClassLoader loader;

	/**
	 * 検索するJARファイルへのURLを指定します。
	 *
	 * @param url JARファイルの位置またはそのエントリ
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public ModuleLoader(URL url) throws IOException {
		var conn = (this.url = url).openConnection();
		if (conn instanceof JarURLConnection) {
			var jarconn = (JarURLConnection) conn;
			jpath = jarconn.getJarFileURL();
			entry = jarconn.getEntryName();
			jarfile = jarconn.getJarFile();
			var cl = Module.class.getClassLoader();
			loader = new URLClassLoader(new URL[]{jpath}, cl);
		} else throw new IOException("not jar:" + url);
	}

	private String getClassName(String entry) {
		if (entry == null) return null;
		entry = entry.replace('/', '.');
		var dot = entry.lastIndexOf('.');
		if (dot < 0) return null;
		var suffix = entry.substring(dot + 1);
		if (suffix.equals("class")) {
			return entry.substring(0, dot);
		}
		return null;
	}

	private String getURL(String entry) {
		return "jar:" + jpath + "!/" + entry;
	}

	private boolean isModule(Class<?> type) {
		var mod = type.getModifiers();
		if (Modifier.isAbstract(mod)) return false;
		return Module.class.isAssignableFrom(type);
	}

	/**
	 * コンストラクタで指定したモジュールをロードします。
	 *
	 * @return モジュールのインスタンスを含む管理情報
	 *
	 * @throws IOException モジュールが見つからなかった場合
	 */
	public ModuleInfo load() throws IOException {
		var info = loadModule(entry);
		if (info != null) return info;
		throw new IOException("not found:" + url);
	}

	/**
	 * 指定した名前のエントリからモジュールを取得します。
	 *
	 * @param entry エントリの名前
	 *
	 * @return エントリの指すモジュールを含む管理情報
	 */
	protected ModuleInfo loadModule(String entry) {
		var cname = getClassName(entry);
		if (cname == null) return null;
		ModuleInfo info = null;
		try {
			var type = loader.loadClass(cname);
			if (!isModule(type)) return null;
			var method = type.getMethod("newInstance");
			var module = (Module) method.invoke(null);
			var dname = Module.getName(module);
			info = new ModuleInfo(getURL(entry), dname);
			info.setModule(module);
			module.start();
		} catch (IllegalAccessException | InvocationTargetException | IllegalArgumentException | SecurityException | ClassCastException | NoSuchMethodException | ClassNotFoundException ex) {
		}
		return info;
	}

}
