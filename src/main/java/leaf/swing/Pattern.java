/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * このクラスを利用すればセルオートマータの状態をストリームに保存できます。
 * <p>
 * サポートする状態の種類は1バイトまでであり、
 * これを超えるセルオートマトンについては適切な他の実装が必要です。
 *
 * @author 無線部開発班
 * @since 2012年3月17日
 */
public final class Pattern {
	private static final String MAGIC = "cellular automata leafapi";
	private final int[][] table;

	/**
	 * テーブルデータを指定してパターンを構築します。
	 *
	 * @param table テーブルデータ
	 */
	private Pattern(int[][] table) {
		this.table = table;
	}

	/**
	 * このパターンをセルオートマータに適用します。
	 *
	 * @param automata セルオートマータ
	 *
	 * @return セルオートマータ
	 *
	 * @throws IllegalArgumentException パターンの大きさとオートマータの大きさが異なる場合
	 */
	public Automata setPattern(Automata automata) throws IllegalArgumentException {
		final var aw = automata.getWidth();
		final var ah = automata.getHeight();
		final var pw = table.length;
		final var ph = table[0].length;
		if (aw == pw && ah == ph) {
			for (var y = 0; y < ah; y++) {
				for (var x = 0; x < aw; x++) {
					automata.setState(x, y, table[x][y]);
				}
			}
			return automata;
		}
		throw new IllegalArgumentException();
	}

	/**
	 * パターンをストリームに書き込みます。
	 *
	 * @param output ストリーム
	 *
	 * @throws IOException 書き込みに失敗した場合
	 */
	public void write(OutputStream output) throws IOException {
		try (var stream = new DataOutputStream(output)) {
			for (var i = 0; i < MAGIC.length(); i++) {
				stream.write(MAGIC.charAt(i));
			}
			stream.writeInt(table.length);
			stream.writeInt(table[0].length);
			final var w = table.length;
			final var h = table[0].length;
			for (var y = 0; y < h; y++) {
				for (var ints : table) {
					stream.write(ints[y]);
				}
			}
		}
	}

	/**
	 * セルオートマータの現在のパターンを取得します。
	 *
	 * @param automata セルオートマータ
	 *
	 * @return セルオートマータのパターン
	 */
	public static Pattern getPattern(Automata automata) {
		final var w = automata.getWidth();
		final var h = automata.getHeight();
		var table = new int[w][h];
		for (var y = 0; y < h; y++) {
			for (var x = 0; x < w; x++) {
				table[x][y] = automata.getState(x, y);
			}
		}
		return new Pattern(table);
	}

	/**
	 * ストリームを読み込んでパターンを構築します。
	 *
	 * @param input ストリーム
	 *
	 * @throws IOException 読み込みに失敗した場合
	 */
	public static Pattern read(InputStream input) throws IOException {
		try (var stream = new DataInputStream(input)) {
			for (var i = 0; i < MAGIC.length(); i++) {
				var ch = stream.read();
				if (MAGIC.charAt(i) == ch) continue;
				throw new IOException("illegal format");
			}
			final var w = stream.readInt();
			final var h = stream.readInt();
			if (w >= 0 && h >= 0) {
				var table = new int[w][h];
				for (var y = 0; y < h; y++) {
					for (var x = 0; x < w; x++) {
						var dat = stream.read();
						if (dat >= 0) table[x][y] = dat;
						else throw new IOException("end of stream");
					}
				}
				return new Pattern(table);
			}
		}
		throw new IOException("illegal format");
	}
}
