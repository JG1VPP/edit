/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.awt.*;

/**
 * ウィンドウの位置と大きさを永続化します。
 *
 * @author 無線部開発班
 * @since 2012年10月21日
 */
public class WindowBounds {
	private final Window window;
	private final String name;
	private final Properties prop;

	/**
	 * 指定されたウィンドウに関連付けられたオブジェクトを生成します。
	 *
	 * @param window 関連付けるウィンドウ
	 * @param name   ウィンドウの名前
	 */
	public WindowBounds(Window window, String name) {
		prop = Properties.getInstance(getClass());
		this.window = window;
		this.name = name;
	}

	/**
	 * ウィンドウに位置と大きさの情報を適用します。
	 */
	public void applyBounds() {
		var size = new Dimension(720, 480);
		window.setSize(prop.get(key("size"), Dimension.class, size));
		var location = prop.get(key("location"), Point.class, null);
		if (location != null) window.setLocation(location);
		else window.setLocationRelativeTo(null);
		if (window instanceof Frame) applyExtendedState((Frame) window);
	}

	private void applyExtendedState(Frame frame) {
		var extended = prop.get(key("extendedState"), Integer.class, null);
		frame.setExtendedState(extended != null ? extended : Frame.NORMAL);
	}

	private String key(String key) {
		return window.getClass().getName() + "." + name + "." + key;
	}

	/**
	 * ウィンドウの位置と大きさの情報を保存します。
	 */
	public void saveBounds() {
		if (window instanceof Frame) {
			var state = ((Frame) window).getExtendedState();
			prop.put(key("extendedState"), state);
			if (state == Frame.MAXIMIZED_BOTH) return;
		}
		prop.put(key("size"), window.getSize());
		prop.put(key("location"), window.getLocation());
	}
}
