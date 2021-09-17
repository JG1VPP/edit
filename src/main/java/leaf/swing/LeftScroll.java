/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * 電光掲示板のセルオートマータによる実装です。
 *
 * @author 無線部開発班
 * @since 2012年3月18日
 */
public final class LeftScroll extends Automata {

	private final List<Color> colorList;

	/**
	 * 指定した縦横セル数でセルオートマータを構築します。
	 *
	 * @param w 横セル数
	 * @param h 縦セル数
	 */
	public LeftScroll(int w, int h) {
		super(w, h);
		colorList = new ArrayList<>();
		colorList.add(Color.BLACK);
	}

	/**
	 * 電光掲示板に色を追加します。
	 *
	 * @param color 追加する色
	 */
	public void addColor(Color color) {
		colorList.add(color);
	}

	/**
	 * ユーザーからの入力により、指定されたセルの状態を切り替えます。
	 *
	 * @param x 横方向の座標
	 * @param y 縦方向の座標
	 */
	@Override
	public void cellPressed(int x, int y) {
		final var state = getState(x, y) + 1;
		final var size = colorList.size();
		setState(x, y, (state < size) ? state : 0);
	}

	/**
	 * 指定された位置のセルの表示色を返します。
	 *
	 * @param x 横方向の座標
	 * @param y 縦方向の座標
	 *
	 * @return セルの表示色
	 */
	@Override
	public Color getCellColor(int x, int y) {
		final var state = getState(x, y);
		final var size = colorList.size();
		return colorList.get(state % size);
	}

	/**
	 * 指定された位置のセルの次世代での状態を計算します。
	 *
	 * @param x 横方向の座標
	 * @param y 縦方向の座標
	 *
	 * @return 次世代での状態
	 */
	@Override
	protected int getNextState(int x, int y) {
		return getState(x + 1, y);
	}
}
