/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;

/**
 * セルの状態を管理し、次状態を導出するセルオートマータです。
 *
 * @author 無線部開発班
 * @since 2012年3月16日
 */
public abstract class Automata {
	private final int width;
	private final int height;
	private boolean isUpdating = false;
	private int[][] states;

	/**
	 * 指定した縦横セル数でセルオートマータを構築します。
	 *
	 * @param w 横セル数
	 * @param h 縦セル数
	 */
	public Automata(int w, int h) {
		states = new int[width = w][height = h];
	}

	/**
	 * ユーザーからの入力により、指定されたセルの状態を切り替えます。
	 *
	 * @param x 横方向の座標
	 * @param y 縦方向の座標
	 */
	public abstract void cellPressed(int x, int y);

	/**
	 * 指定された位置のセルの表示色を返します。
	 *
	 * @param x 横方向の座標
	 * @param y 縦方向の座標
	 *
	 * @return セルの表示色
	 */
	public abstract Color getCellColor(int x, int y);

	/**
	 * セルオートマータの縦方向のセル数を返します。
	 *
	 * @return 縦方向のセル数
	 */
	public final int getHeight() {
		return height;
	}

	/**
	 * 指定された位置のセルの次世代での状態を計算します。
	 *
	 * @param x 横方向の座標
	 * @param y 縦方向の座標
	 *
	 * @return 次世代での状態
	 */
	protected abstract int getNextState(int x, int y);

	/**
	 * 指定された位置のセルの現世代での状態を返します。
	 *
	 * @param x 横方向の座標
	 * @param y 縦方向の座標
	 *
	 * @return 現世代での状態
	 */
	public final int getState(int x, int y) {
		var xmod = x % width;
		var ymod = y % height;
		if (xmod < 0) xmod += width;
		if (ymod < 0) ymod += height;
		return states[xmod][ymod];
	}

	/**
	 * セルオートマータの横方向のセル数を返します。
	 *
	 * @return 横方向のセル数
	 */
	public final int getWidth() {
		return width;
	}

	/**
	 * セルオートマータの状態を初期化します。
	 */
	public final void init() {
		states = new int[width][height];
	}

	/**
	 * セルオートマータが次世代の計算中であるか確認します。
	 *
	 * @return 次世代の計算途中である場合true
	 */
	public boolean isUpdating() {
		return isUpdating;
	}

	/**
	 * セルオートマータが次世代の計算中であるか設定します。
	 *
	 * @param 次世代の計算途中でない場合false
	 */
	final void setUpdating(boolean isUpdating) {
		this.isUpdating = isUpdating;
	}

	/**
	 * 指定された位置のセルの現世代での状態を設定します。
	 *
	 * @param x     横方向の座標
	 * @param y     縦方向の座標
	 * @param state 現世代での状態
	 *
	 * @throws IllegalStateException {@link #updateNext()}の内部で呼び出された場合
	 */
	public final void setState(int x, int y, int state) throws IllegalStateException {
		var xmod = x % width;
		var ymod = y % height;
		if (xmod < 0) xmod += width;
		if (ymod < 0) ymod += height;
		if (!isUpdating) states[xmod][ymod] = state;
		else throw new IllegalStateException();
	}

	/**
	 * セルオートマータ全体の次世代での状態を計算します。
	 */
	public void updateNext() {
		var next = new int[width][height];
		isUpdating = true;
		for (var x = 0; x < width; x++) {
			for (var y = 0; y < height; y++) {
				next[x][y] = getNextState(x, y);
			}
		}
		for (var x = 0; x < width; x++) {
			System.arraycopy(next[x], 0, states[x], 0, height);
		}
		isUpdating = false;
	}

}
