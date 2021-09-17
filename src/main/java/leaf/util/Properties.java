/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

/**
 * アプリケーションの永続オブジェクトをユーザー毎に一括管理します。
 *
 * @since 2011年12月17日
 */
public final class Properties {
	private static final Hibernate hibernate;
	private final static Map<Class<?>, Properties> instances;
	private static File dir;

	static {
		instances = new HashMap<>();
		dir = new File(dir, "users");
		var user = System.getProperty("user.name");
		hibernate = Hibernate.getInstance(dir, user);
	}

	private final String prefix;

	private Properties(Class<?> type) {
		prefix = type.getName().concat(".");
	}

	/**
	 * 指定した名前と型に対応するオブジェクトを返します。
	 *
	 * @param <T>  オブジェクトの型
	 * @param name オブジェクトの名前
	 * @param type オブジェクトの型
	 * @param defo デフォルトの代用値
	 *
	 * @return マッピングされたオブジェクト
	 *
	 * @throws ClassCastException 型が不適合の場合
	 */
	public <T> T get(String name, Class<T> type, T defo) {
		name = prefix.concat(name);
		if (!hibernate.contains(name)) {
			hibernate.put(name, defo);
			return defo;
		} else return hibernate.get(name, type);
	}

	/**
	 * 指定した名前と初期値に対応するオブジェクトを返します。
	 *
	 * @param name オブジェクトの名前
	 * @param defo デフォルトの代用値
	 *
	 * @return マッピングされたオブジェクト
	 *
	 * @throws ClassCastException 型が不適合の場合
	 */
	@SuppressWarnings("unchecked")
	public <T> T get(String name, T defo) {
		name = prefix.concat(name);
		if (!hibernate.contains(name)) {
			hibernate.put(name, defo);
			return defo;
		} else {
			return (T) hibernate.get(name);
		}
	}

	/**
	 * 指定された名前に対しオブジェクトをマッピングします。
	 *
	 * @param name オブジェクトの名前
	 * @param obj  マッピングするオブジェクト
	 */
	public void put(String name, Object obj) {
		hibernate.put(prefix.concat(name), obj);
	}

	/**
	 * 指定した名前に対応するオブジェクトを削除します。
	 *
	 * @param name オブジェクトの名前
	 *
	 * @return 削除されたオブジェクト
	 */
	public Object remove(String name) {
		name = prefix.concat(name);
		return hibernate.remove(name);
	}

	/**
	 * 対応するクラスを指定してプロパティオブジェクトを取得します。
	 *
	 * @param type プロパティを委託するクラス
	 *
	 * @return 対応するオブジェクト
	 */
	public static Properties getInstance(Class<?> type) {
		var instance = instances.get(type);
		if (instance != null) return instance;
		instances.put(type, instance = new Properties(type));
		return instance;
	}

	/**
	 * ハイバーネーションファイルにデータを保存します。
	 */
	public static void save() {
		try {
			if (!dir.isDirectory()) dir.mkdirs();
			hibernate.save(dir);
		} catch (FileNotFoundException | SecurityException ex) {
			ex.printStackTrace();
		}
	}

}
