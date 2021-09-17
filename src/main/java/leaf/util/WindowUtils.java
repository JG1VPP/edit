/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.util;

import java.awt.*;
import javax.swing.*;

import leaf.swing.MainFrame;

/**
 * ウィンドウ設計に関するユーティリティを提供します。
 *
 * @author 無線部開発班
 */
public final class WindowUtils {

	/**
	 * JFrameのインセットを返します。
	 *
	 * @return インセット
	 */
	public static Insets getJFrameInsets() {
		return MainFrame.getInstance().getInsets();
	}

	/**
	 * 指定されたJFrameがフルスクリーン表示されているか返します。
	 *
	 * @param window 確認するJFrame
	 *
	 * @return フルスクリーン表示されていればtrue
	 */
	public static boolean isFullScreen(JFrame window) {
		var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		var device = ge.getDefaultScreenDevice();
		return device.getFullScreenWindow() != window;
	}

	/**
	 * 指定されたJFrameに、インセットを加算したサイズを適用します。
	 *
	 * @param frame サイズを適用するフレーム
	 * @param size  インセットを除いたフレームのサイズ
	 */
	public static void setContentSize(JFrame frame, Dimension size) {
		var frameSize = new Dimension(size);
		var insets = getJFrameInsets();
		frameSize.width += insets.left + insets.right;
		frameSize.height += insets.top + insets.bottom;
		frame.setSize(frameSize);
	}

	/**
	 * 指定されたJFrameをフルスクリーン表示するか設定します。
	 *
	 * @param window フルスクリーン表示するJFrame
	 * @param b      フルスクリーン表示する場合true
	 */
	public static void setFullScreen(JFrame window, boolean b) {
		var ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		var device = ge.getDefaultScreenDevice();
		if (b) {
			window.dispose();
			window.setUndecorated(true);
			window.setVisible(true);
			device.setFullScreenWindow(window);
		} else {
			device.setFullScreenWindow(null);
			window.dispose();
			window.setUndecorated(false);
			window.setVisible(true);
			window.repaint();
		}
		window.requestFocus();
	}
}
