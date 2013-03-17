/*****************************************************************************
 * Java Class Library 'LeafAPI' since 2010 June 8th
 * Language: Java Standard Edition 7
 *****************************************************************************
 * License : GNU General Public License v3 (see LICENSE.txt)
 * Author: University of Tokyo Amateur Radio Club (JA1ZLO)
*****************************************************************************/
package leaf.util.unix;

import java.io.*;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正規表現にマッチする記述をファイルから抽出して一覧を出力します。
 * 
 * @author 東大アマチュア無線クラブ
 * @since  Leaf 1.4 作成：2012年3月19日
 */
public class LeafGrep extends TaskWorker<File>{
	private final PrintWriter out;
	private final Pattern LINE_SEPARATOR;
	
	/**
	 * Grepコマンドを生成します。
	 * 
	 * @param writer 出力先(手動で閉じる必要がある)
	 */
	public LeafGrep(Writer writer){
		LINE_SEPARATOR = Pattern.compile(".*$", Pattern.MULTILINE);
		if(writer instanceof PrintWriter){
			out = (PrintWriter)writer;
		}else{
			out = new PrintWriter(writer);
		}
	}
	/**
	 * 条件を指定してGREP検索します。
	 * 
	 * @param dir 検索のルート
	 * @param filter フィルタ
	 * @param chset 文字セット
	 * @param pattern 正規表現パターン
	 */
	public void grep(File dir, FileFilter filter, Charset chset, Pattern pattern){
		File[] files = LeafFind.listFiles(dir, filter);
		if(files != null){
			int step = files.length, index = 0;
			for(File file : files){
				if(isCancelled()) return;
				progress(file, index++, step);
				try{
					grep(file, chset, pattern);
				}catch(IOException ex){
					out.print(file.getAbsolutePath());
					out.print(" :");
					out.println(ex);
					out.flush();
				}
			}
		}
	}
	/**
	 * 指定したファイル内をGREP検索します。
	 * 
	 * @param file 検索するファイル
	 * @param chset 文字セット
	 * @param pattern 正規表現パターン
	 * @return 抽出結果
	 * @throws IOException 入出力に異常があった場合
	 */
	private void grep(File file, Charset chset, Pattern pattern) throws IOException{
		FileInputStream stream = null;
		FileChannel channel = null;
		try{
			stream = new FileInputStream(file);
			channel = stream.getChannel();
			MappedByteBuffer buffer = channel.map(
				FileChannel.MapMode.READ_ONLY,
				0, channel.size());
			CharsetDecoder decoder = chset.newDecoder();
			grep(file, decoder.decode(buffer), pattern);
		}finally{
			if(channel != null) channel.close();
			if(stream != null) stream.close();
		}
	}
	/**
	 * 文字バッファから正規表現にマッチする部分を抽出します。
	 * 
	 * @param file 現在のファイル
	 * @param buffer 文字バッファ
	 * @param pattern 正規表現
	 */
	private void grep(File file, CharBuffer buffer, Pattern pattern){
		Matcher lm = LINE_SEPARATOR.matcher(buffer);
		Matcher pm = null;
		int current = 0;
		while(lm.find()){
			current++;
			String line = lm.group();
			if(pm == null) pm = pattern.matcher(line);
			else pm.reset(line);
			if(pm.find()){
				out.print(file.getAbsolutePath());
				out.print("(");
				out.print(current);
				out.print(",");
				out.print(pm.start());
				out.print(") :");
				out.println(line);
				out.flush();
			}
			if(lm.end() == buffer.limit()) return;
		}
	}
}