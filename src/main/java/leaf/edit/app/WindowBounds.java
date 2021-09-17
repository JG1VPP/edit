/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.edit.app;

import java.awt.*;

import leaf.swing.MainFrame;

/**
 * ウィンドウの位置と大きさを記憶します。
 *
 * @author 無線部開発班
 * @since 2012/10/21
 */
public class WindowBounds extends leaf.util.WindowBounds {
	public static final String MAIN_FRAME = MainFrame.class.getName();

	public WindowBounds(Window window, String name) {
		super(window, name);
	}

}
