/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import leaf.util.Properties;

/**
 * モジュールの管理を行うマネージャです。
 *
 * @author 無線部開発班
 * @since 2012/07/13
 */
public final class ModuleManager {
	private static final ModuleManager instance = new ModuleManager();
	private final ModuleMap map;

	private ModuleManager() {
		var prop = Properties.getInstance(getClass());
		map = prop.get("map", ModuleMap.class, new ModuleMap());
	}

	/**
	 * モジュールの管理情報をマップに追加します。
	 *
	 * @param info 追加する管理情報
	 */
	void addModuleInfo(ModuleInfo info) {
		map.put(info.getName(), info);
	}

	/**
	 * URLで指定されたJARファイル内のモジュールを検索・ロードします。
	 *
	 * @param url JARファイルのURL
	 *
	 * @throws IOException モジュールのロードに失敗した場合
	 */
	void findAndLoadModules(URL url) throws IOException {
		map.findAndLoadModule(url);
	}

	/**
	 * マップに登録されている全てのモジュールの管理情報を返します。
	 *
	 * @return 全ての管理情報
	 */
	ModuleInfo[] getAllModuleInfo() {
		var list = new ArrayList<ModuleInfo>();
		for (var name : map.keySet()) {
			list.add(map.get(name));
		}
		return list.toArray(new ModuleInfo[0]);
	}

	/**
	 * 指定された名前のモジュールのインスタンスを返します。
	 *
	 * @param name モジュールの名前
	 *
	 * @return モジュールの本体
	 *
	 * @throws ModuleException モジュールが登録されていない場合
	 */
	public Module getModuleByName(String name) throws ModuleException {
		return map.getModule(name);
	}

	/**
	 * 登録されている全てのモジュールを起動します。
	 */
	public void loadAllModules() {
		for (var name : map.keySet()) {
			try {
				map.loadModule(name);
			} catch (IOException | ModuleException ex) {
			}
		}
	}

	/**
	 * モジュールの管理情報をマップから削除します。
	 *
	 * @param info 削除する管理情報
	 */
	void removeModuleInfo(ModuleInfo info) {
		map.remove(info.getName());
	}

	/**
	 * 全てのモジュールを終了します。
	 *
	 * @return 全て終了した場合true
	 */
	public boolean shutdownAllModules() {
		for (var name : map.keySet()) {
			var info = map.get(name);
			if (!info.getModule().shutdown()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * モジュールマネージャのインスタンスを返します。
	 *
	 * @return マネージャのインスタンス
	 */
	public static ModuleManager getInstance() {
		return instance;
	}

}
