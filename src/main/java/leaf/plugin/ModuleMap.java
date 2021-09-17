/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.io.IOException;
import java.net.URL;
import java.util.TreeMap;

/**
 * モジュールの管理情報を保管し、ローカルに保存するためのマップです。
 *
 * @author 無線部開発班
 * @since 2012/06/16
 */
public final class ModuleMap extends TreeMap<String, ModuleInfo> {
	private static final long serialVersionUID = 3797078950621805327L;

	/**
	 * URLで指定されたJARファイル内にあるモジュールを検索してロードします。
	 *
	 * @param url JARファイルのURL
	 *
	 * @throws IOException モジュールのロードに失敗した場合
	 */
	public void findAndLoadModule(URL url) throws IOException {
		url = new URL("jar:" + url + "!/");
		for (var info : new ModuleFinder(url).search()) {
			put(info.getName(), info);
		}
	}

	/**
	 * 指定された名前のロード済みモジュールを返します。
	 *
	 * @param name モジュールの名前
	 *
	 * @return モジュールのインスタンス ロード済みでない場合null
	 *
	 * @throws ModuleException モジュールが登録されていない場合
	 */
	public Module getModule(String name) throws ModuleException {
		if (containsKey(name)) return get(name).getModule();
		throw new ModuleException(String.format("Module '%s' not found", name));
	}

	/**
	 * 指定された名前のモジュールをロードします。
	 *
	 * @param name モジュールの名前
	 *
	 * @return モジュールのインスタンス
	 *
	 * @throws IOException     モジュールのロードに失敗した場合
	 * @throws ModuleException モジュールが登録されていない場合
	 */
	public Module loadModule(String name) throws IOException, ModuleException {
		if (containsKey(name)) {
			var info = new ModuleLoader(get(name).getLocationURL()).load();
			put(name, info);
			return info.getModule();
		}
		throw new ModuleException(String.format("Module '%s' not found", name));
	}

	/**
	 * この{@link ModuleMap}が設定ファイルに出力しないプロパティを設定します。
	 *
	 * @param name プロパティ名
	 *
	 * @throws IntrospectionException
	 */
	static void setTransient(String name) {
		try {
			var beaninfo = Introspector.getBeanInfo(ModuleInfo.class);
			for (var pd : beaninfo.getPropertyDescriptors())
				if (pd.getName().equals(name)) pd.setValue("transient", true);
		} catch (IntrospectionException ex) {
			ex.printStackTrace();
		}
	}

}
