/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正規表現にマッチする記述をファイルから抽出して一覧を出力します。
 *
 * @author 無線部開発班
 * @since 2012年3月19日
 */
public class Grep extends Task<File> {
	private final PrintWriter out;
	private final Pattern LINE_SEPARATOR;

	/**
	 * Grepコマンドを生成します。
	 *
	 * @param writer 出力先(手動で閉じる必要がある)
	 */
	public Grep(Writer writer) {
		LINE_SEPARATOR = Pattern.compile(".*$", Pattern.MULTILINE);
		if (writer instanceof PrintWriter) {
			out = (PrintWriter) writer;
		} else {
			out = new PrintWriter(writer);
		}
	}

	/**
	 * 条件を指定してGREP検索します。
	 *
	 * @param dir     検索のルート
	 * @param filter  フィルタ
	 * @param chset   文字セット
	 * @param pattern 正規表現パターン
	 */
	public void grep(File dir, FileFilter filter, Charset chset, Pattern pattern) {
		var files = Find.listFiles(dir, filter);
		int step = files.length, index = 0;
		for (var file : files) {
			if (isCancelled()) return;
			progress(file, index++, step);
			try {
				grep(file, chset, pattern);
			} catch (IOException ex) {
				out.print(file.getAbsolutePath());
				out.print(" :");
				out.println(ex);
				out.flush();
			}
		}
	}

	/**
	 * 指定したファイル内をGREP検索します。
	 *
	 * @param file    検索するファイル
	 * @param chset   文字セット
	 * @param pattern 正規表現パターン
	 *
	 * @return 抽出結果
	 *
	 * @throws IOException 入出力に異常があった場合
	 */
	private void grep(File file, Charset chset, Pattern pattern) throws IOException {
		try (var stream = new FileInputStream(file); var channel = stream.getChannel()) {
			var buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
			var decoder = chset.newDecoder();
			grep(file, decoder.decode(buffer), pattern);
		}
	}

	/**
	 * 文字バッファから正規表現にマッチする部分を抽出します。
	 *
	 * @param file    現在のファイル
	 * @param buffer  文字バッファ
	 * @param pattern 正規表現
	 */
	private void grep(File file, CharBuffer buffer, Pattern pattern) {
		var lm = LINE_SEPARATOR.matcher(buffer);
		Matcher pm = null;
		var current = 0;
		while (lm.find()) {
			current++;
			var line = lm.group();
			if (pm == null) pm = pattern.matcher(line);
			else pm.reset(line);
			if (pm.find()) {
				out.print(file.getAbsolutePath());
				out.print("(");
				out.print(current);
				out.print(",");
				out.print(pm.start());
				out.print(") :");
				out.println(line);
				out.flush();
			}
			if (lm.end() == buffer.limit()) return;
		}
	}
}
