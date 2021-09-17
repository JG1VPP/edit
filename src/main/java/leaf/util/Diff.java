/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.LinkedList;

import static leaf.util.Diff.Edit.ADD;
import static leaf.util.Diff.Edit.COMMON;
import static leaf.util.Diff.Edit.DELETE;

/**
 * 新旧配列間の差分を抽出し、追加・維持・削除の編集操作列に変換します。
 *
 * @author 無線部開発班
 * @since 2010年9月20日
 */
public class Diff {

	/**
	 * 差分抽出オブジェクトを構築します。
	 */
	public Diff() {
	}

	/**
	 * 新旧の配列の差分を抽出して編集操作列を返します。
	 *
	 * @param oldarr 古い配列
	 * @param newarr 新しい配列
	 *
	 * @return 編集内容
	 */
	public EditList compare(Object[] oldarr, Object[] newarr) {
		var list = new LinkedList<Edit>();
		var table = new int[oldarr.length + 1][newarr.length + 1];
		for (var n1 = oldarr.length - 1; n1 >= 0; n1--) {
			for (var n2 = newarr.length - 1; n2 >= 0; n2--) {
				if (equals(oldarr[n1], newarr[n2])) {
					table[n1][n2] = table[n1 + 1][n2 + 1] + 1;
				} else {
					table[n1][n2] = Math.max(table[n1][n2 + 1], table[n1 + 1][n2]);
				}
			}
		}
		int n1 = 0, n2 = 0;
		while (n1 < oldarr.length && n2 < newarr.length) {
			if (equals(oldarr[n1], newarr[n2])) {
				list.add(new Edit(COMMON, oldarr[n1++]));
				n2++;
			} else if (table[n1 + 1][n2] >= table[n1][n2 + 1]) {
				list.add(new Edit(DELETE, oldarr[n1++]));
			} else {
				list.add(new Edit(ADD, newarr[n2++]));
			}
		}
		while (n1 < oldarr.length || n2 < newarr.length) {
			if (n1 == oldarr.length) {
				list.add(new Edit(ADD, newarr[n2++]));
			} else if (n2 == newarr.length) {
				list.add(new Edit(DELETE, oldarr[n1++]));
			}
		}
		return new EditList(list.toArray(new Edit[0]));
	}

	/**
	 * 新旧の文字列の差分を抽出して編集操作列を返します。
	 *
	 * @param oldtext 古い文字列
	 * @param newtext 新しい文字列
	 *
	 * @return 編集内容
	 */
	public EditList compare(String oldtext, String newtext) {
		var oldarr = oldtext.split("\r?\n");
		var newarr = newtext.split("\r?\n");
		return compare(oldarr, newarr);
	}

	/**
	 * 新旧のテキストファイルの差分を抽出して編集操作列を返します。
	 *
	 * @param of 古いファイル
	 * @param nf 新しいファイル
	 * @param os 古いファイルの文字セット
	 * @param ns 新しいファイルの文字セット
	 *
	 * @return 編集内容
	 */
	public EditList compare(File of, File nf, Charset os, Charset ns) throws IOException {
		return compare(read(of, os), read(nf, ns));
	}

	/**
	 * 比較元の文字列と比較先のテキストファイルの差分を
	 * 抽出して編集操作列を返します。
	 *
	 * @param ot 比較元の文字列
	 * @param nf 比較先のファイル
	 * @param ns 比較先の文字セット
	 */
	public EditList compare(String ot, File nf, Charset ns) throws IOException {
		return compare(ot.split("\r?\n"), read(nf, ns));
	}

	/**
	 * 比較元のテキストファイルと比較先の文字列の差分を
	 * 抽出して編集操作列を返します。
	 *
	 * @param of 比較元のファイル
	 * @param nt 比較先の文字列
	 * @param os 比較元の文字セット
	 */
	public EditList compare(File of, String nt, Charset os) throws IOException {
		return compare(read(of, os), nt.split("\r?\n"));
	}

	/**
	 * オブジェクトの比較時に呼び出される委譲メソッドです。
	 *
	 * @param oobj 古い配列側のオブジェクト
	 * @param nobj 新しい配列側のオブジェクト
	 *
	 * @return 等価である場合には真を返す
	 */
	protected boolean equals(Object oobj, Object nobj) {
		try {
			return oobj.equals(nobj);
		} catch (NullPointerException ex) {
			return oobj == null && nobj == null;
		}
	}

	private String[] read(File file, Charset chset) throws IOException {
		var stream = new FileInputStream(file);
		var channel = stream.getChannel();
		var buffer = channel.map(FileChannel.MapMode.READ_ONLY, 0, channel.size());
		var decoder = chset.newDecoder();
		var cb = decoder.decode(buffer);
		channel.close();
		stream.close();
		return cb.toString().split("\r?\n");
	}

	/**
	 * 新旧の配列間の編集内容を表すコンテナクラスです。
	 *
	 * @author 無線部開発班
	 * @since 2010年9月20日
	 */

	public static class Edit {
		/**
		 * 要素の追加挿入操作です。
		 */
		public static final byte ADD = 0;

		/**
		 * 要素の削除操作です。
		 */
		public static final byte DELETE = 1;

		/**
		 * 要素の維持操作です。
		 */
		public static final byte COMMON = 2;

		private byte type;
		private Object content;

		/**
		 * 空の編集を生成します。
		 */
		public Edit() {
			type = COMMON;
		}

		/**
		 * 編集の操作と対象の内容を指定して編集を生成します。
		 *
		 * @param type 編集の操作
		 * @param cont 対象の内容
		 */
		public Edit(byte type, Object cont) {
			if (type >= ADD && type <= COMMON) {
				this.type = type;
				this.content = cont;
			} else throw new IllegalArgumentException();
		}

		/**
		 * 操作対象の内容を返します。
		 *
		 * @return 対象の内容
		 */
		public Object getContent() {
			return content;
		}

		/**
		 * 操作対象の内容を指定します。
		 *
		 * @param cont 対象の内容
		 */
		public void setContent(Object cont) {
			this.content = cont;
		}

		/**
		 * 編集の操作を返します。
		 *
		 * @return 操作
		 */
		public byte getType() {
			return type;
		}

		/**
		 * 編集の操作を指定します。
		 *
		 * @param type 操作
		 */
		public void setType(byte type) {
			this.type = type;
		}

		/**
		 * 編集内容を表す文字列表現を返します。
		 *
		 * @return 文字列表現
		 */
		public String toString() {
			var value = String.valueOf(content);
			switch (type) {
				case ADD:
					return "> ".concat(value);
				case DELETE:
					return "< ".concat(value);
				case COMMON:
					return "  ".concat(value);
			}
			return "";
		}
	}

	/**
	 * 一連の編集操作を順序どおりに管理するリストです。
	 *
	 * @author 無線部開発班
	 * @since 2010年9月20日
	 */
	public static class EditList {
		private Edit[] edits;

		/**
		 * 空の編集内容を持つリストを生成します。
		 */
		public EditList() {
			edits = new Edit[0];
		}

		/**
		 * 編集内容を指定してリストを生成します。
		 *
		 * @param edits 編集内容
		 */
		public EditList(Edit[] edits) {
			this.edits = edits;
		}

		/**
		 * 編集内容を返します。
		 *
		 * @return 編集の手順
		 */
		public Edit[] getEdits() {
			return edits;
		}

		/**
		 * 編集内容を設定します。
		 *
		 * @param edits 編集の手順
		 */
		public void setEdits(Edit[] edits) {
			this.edits = edits;
		}

		/**
		 * 指定されたXMLファイルにリストを保存します。
		 *
		 * @param file XMLファイル
		 *
		 * @throws FileNotFoundException ファイルに書き込めない場合
		 */
		public void save(File file) throws FileNotFoundException {
			FileOutputStream fstream;
			BufferedOutputStream bstream;
			XMLEncoder encoder = null;
			try {
				fstream = new FileOutputStream(file);
				bstream = new BufferedOutputStream(fstream);
				encoder = new XMLEncoder(bstream);
				encoder.writeObject(this);

			} finally {
				if (encoder != null) encoder.close();
			}
		}

		/**
		 * 編集内容を表現する文字列を返します。
		 *
		 * @return 差分表示文字列
		 */
		public String toString() {
			var sb = new StringBuilder();
			final var max = edits.length;
			for (var edit : edits) {
				sb.append(edit).append('\n');
			}
			return sb.toString();
		}

		/**
		 * 指定されたXMLファイルからリストを読み込んで生成します。
		 *
		 * @param file XMLファイル
		 *
		 * @return リスト
		 *
		 * @throws FileNotFoundException          ファイルが見つからない場合
		 * @throws ArrayIndexOutOfBoundsException ストリームにオブジェクトがなかった場合
		 * @throws ClassCastException             読み込んだオブジェクトの型が一致しない場合
		 */
		public static EditList load(File file) throws FileNotFoundException, ArrayIndexOutOfBoundsException, ClassCastException {
			FileInputStream fstream;
			BufferedInputStream bstream;
			XMLDecoder decoder = null;
			try {
				fstream = new FileInputStream(file);
				bstream = new BufferedInputStream(fstream);
				decoder = new XMLDecoder(bstream);
				var obj = decoder.readObject();
				if (obj instanceof EditList) {
					return (EditList) obj;
				} else {
					throw new ClassCastException();
				}
			} finally {
				if (decoder != null) decoder.close();
			}
		}
	}
}
