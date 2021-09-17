/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.net.MalformedURLException;
import java.net.URL;
import javax.swing.*;

import leaf.util.Properties;
import leaf.util.GoogleUtils;

/**
 * メインウィンドウ最下部に表示されるステータスバーです。
 *
 * @author 無線部開発班
 */
public final class MainStatusBar extends StatusBar {
	public static final int STATUS_LEFT = 0;
	public static final int STATUS_RIGHT = 1;
	private static final String NewsBarURL = "newsBarURL";
	private static final String defaultURL = GoogleUtils.getNewsURL();
	private final Properties properties;
	private final JLabel[] labels;
	private final LeafNewsBar bar;

	/**
	 * ステータスバーを構築します。
	 */
	public MainStatusBar() {
		properties = Properties.getInstance(getClass());
		setGlue(bar = new LeafNewsBar());
		bar.setAutoUpdateEnabled(true);
		bar.setURL(loadNewsBarURL());
		labels = new JLabel[2];
		addComp(labels[0] = new JLabel("", JLabel.CENTER));
		addComp(labels[1] = new JLabel("", JLabel.CENTER));
		var dim0 = new Dimension(120, 20);
		var dim1 = new Dimension(80, 20);
		labels[0].setMaximumSize(dim0);
		labels[1].setMaximumSize(dim1);
		labels[0].setPreferredSize(dim0);
		labels[1].setPreferredSize(dim1);
	}

	/**
	 * ニュースバーの接続先URLを返します。
	 *
	 * @return 接続先URL
	 */
	public URL getNewsBarURL() {
		return bar.getURL();
	}

	/**
	 * ニュースバーの接続先URLを設定します。
	 *
	 * @param url 接続先URL
	 */
	public void setNewsBarURL(URL url) {
		bar.setURL(url);
		properties.put(NewsBarURL, url);
	}

	/**
	 * ステータスバーに表示される文字列を返します。
	 *
	 * @param index ラベルの指定
	 *
	 * @return ラベルのテキスト
	 */
	public String getText(int index) {
		return labels[index].getText();
	}

	/**
	 * ステータスバーの言語表示を初期化します。
	 */
	public void initialize() {
		if (bar != null) bar.init(bar.getURL());
	}

	/**
	 * ニュースバーの接続先URLを取得します。
	 *
	 * @return 接続先URL
	 */
	private URL loadNewsBarURL() {
		try {
			return new URL(properties.get(NewsBarURL, defaultURL));
		} catch (MalformedURLException ex) {
			return null;
		}
	}

	/**
	 * ニュースバーの接続先URLを保存します。
	 */
	public void saveNewsBarURL() {
		properties.put(NewsBarURL, String.valueOf(getNewsBarURL()));
	}

	/**
	 * ステータスバーに表示される文字列を変更します。
	 *
	 * @param text  表示文字列
	 * @param index ラベルの指定
	 */
	public void setText(String text, int index) {
		labels[index].setText(text);
	}

}
