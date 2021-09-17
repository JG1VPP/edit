/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.plugin;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * URLで指定された場所に存在するモジュールを検索します。
 *
 * @author 無線部開発班
 * @since 2012/06/16
 */
final class ModuleFinder extends ModuleLoader {

	/**
	 * 検索するJARファイルへのURLを指定します。
	 *
	 * @param url JARファイルの位置
	 *
	 * @throws IOException 入出力エラーが発生した場合
	 */
	public ModuleFinder(URL url) throws IOException {
		super(url);
	}

	/**
	 * コンストラクタで指定された位置からモジュールを検索します。
	 *
	 * @return 検出されたモジュールの管理情報のリスト
	 */
	public List<ModuleInfo> search() {
		List<ModuleInfo> list = new ArrayList<>();
		var e = super.jarfile.entries();
		while (e.hasMoreElements()) {
			var entry = e.nextElement();
			if (!entry.isDirectory()) {
				var info = loadModule(entry.getName());
				if (info != null) list.add(info);
			}
		}
		return list;
	}

}
