/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 条件を指定することによってファイルやディレクトリを検索します。
 *
 * @author 無線部開発班
 * @since 2012年3月21日
 */
public class Find {

	private Find() {
	}

	/**
	 * ファイルの拡張子を返します。
	 *
	 * @param file ファイル
	 *
	 * @return 拡張子
	 */
	public static String getSuffix(File file) {
		if (file == null) return null;
		var name = file.getName();
		var index = name.lastIndexOf(".");
		if (index < 0) return null;
		return name.substring(index + 1).toLowerCase();
	}

	/**
	 * ファイルが拡張子を持つか確認します。
	 *
	 * @param file ファイル
	 *
	 * @return 拡張子を持つ場合true
	 */
	public static boolean hasSuffix(File file) {
		return getSuffix(file) != null;
	}

	/**
	 * 指定したディレクトリ内のファイルを深さ優先検索します。
	 *
	 * @param root   起点ディレクトリ
	 * @param filter フィルタ
	 *
	 * @return ファイルの一覧
	 */
	public static File[] listFiles(File root, FileFilter filter) {
		List<File> list = new ArrayList<>();
		listFiles(list, root, filter);
		return list.toArray(new File[0]);
	}

	/**
	 * 指定したディレクトリ内の全てのファイルを再帰的に検索します。
	 *
	 * @param list   見つかったファイルを格納するリスト
	 * @param dir    現在のディレクトリ
	 * @param filter フィルタ
	 */
	private static void listFiles(List<File> list, File dir, FileFilter filter) {
		var children = dir.listFiles(filter);
		if (children == null) return;
		for (var child : children) {
			if (child.isDirectory()) listFiles(list, child, filter);
			else list.add(child);
		}
	}

	/**
	 * 指定したディレクトリ内のファイルを深さ優先検索します。
	 *
	 * @param root   起点ディレクトリ
	 * @param filter フィルタ
	 *
	 * @return ファイルの一覧
	 */
	public static File[] listFiles(File root, FilenameFilter filter) {
		List<File> list = new ArrayList<>();
		listFiles(list, root, filter);
		return list.toArray(new File[0]);
	}

	/**
	 * 指定したディレクトリ内の全てのファイルを再帰的に検索します。
	 *
	 * @param list   見つかったファイルを格納するリスト
	 * @param dir    現在のディレクトリ
	 * @param filter フィルタ
	 */
	private static void listFiles(List<File> list, File dir, FilenameFilter filter) {
		var children = dir.listFiles(filter);
		if (children == null) return;
		for (var child : children) {
			if (child.isDirectory()) listFiles(list, child, filter);
			else list.add(child);
		}
	}
}
