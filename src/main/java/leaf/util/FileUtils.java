/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.File;

/**
 * ファイル操作のユーティリティです。
 *
 * @author 無線部開発班
 * @since 2012/12/28
 */
public class FileUtils {
	/**
	 * 指定されたルート下の階層を順に並べてファイルを構築します。
	 *
	 * @param root 起点となるディレクトリ
	 * @param dir  ディレクトリ階層の配列
	 *
	 * @return ファイル
	 */
	public static File newFile(File root, String... dir) {
		var file = root;
		for (var f : dir) {
			file = new File(file, f);
		}
		return file;
	}

	/**
	 * 実行ディレクトリ下の階層を順に並べてファイルを構築します。
	 *
	 * @param dir ディレクトリ名の配列
	 *
	 * @return ファイル
	 */
	public static File newFile(String... dir) {
		return newFile(new File("."), dir);
	}

}
