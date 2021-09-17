/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.*;

/**
 * 二次元セルオートマータを視覚化するGUIコンポーネントです。
 *
 * @author 無線部開発班
 * @since 2012年3月16日
 */
public class LeafCellTable extends JComponent {
	private static final long serialVersionUID = 1L;
	private Automata automata;
	private Dimension cellsize;
	private Image screen;
	private Graphics graph;

	private Timer timer;
	private int interval = 100;

	/**
	 * コンポーネントを構築します。
	 */
	public LeafCellTable() {
		setBackground(Color.BLACK);
		cellsize = new Dimension(10, 10);
		addMouseListener(new ClickListener());
	}

	/**
	 * セルテーブルの世代更新周期の設定値をミリ秒単位で返します。
	 *
	 * @return 世代更新周期
	 */
	public int getAutoUpdateInterval() {
		return interval;
	}

	/**
	 * セルテーブルの世代更新周期をミリ秒単位で設定します。
	 *
	 * @param ms 世代更新周期
	 *
	 * @throws IllegalArgumentException 正数でない周期を指定した場合
	 */
	public void setAutoUpdateInterval(int ms) throws IllegalArgumentException {
		final var old = this.interval;
		if (ms > 0) this.interval = ms;
		else throw new IllegalArgumentException("not positive : " + ms);
		firePropertyChange("autoUpdateInterval", old, ms);
	}

	/**
	 * このテーブルコンポーネントのセルオートマータを返します。
	 *
	 * @return セルオートマータ
	 */
	public Automata getCellAutomata() {
		return automata;
	}

	/**
	 * このテーブルコンポーネントにセルオートマータを設定します。
	 *
	 * @param automata セルオートマータ
	 */
	public void setCellAutomata(Automata automata) {
		this.automata = automata;
		updatePreferredSize(cellsize);
		repaint();
	}

	/**
	 * セルオートマータのテーブルを初期化します。
	 */
	public final void init() {
		if (automata != null) automata.init();
		repaint();
	}

	/**
	 * セルテーブルの自動的な世代更新動作が稼働中であるか返します。
	 *
	 * @return 稼働中である場合はtrue 停止中である場合はfalse
	 */
	public boolean isAutoUpdateEnabled() {
		return timer != null;
	}

	/**
	 * セルテーブルの自動的な世代更新を開始または停止します。
	 *
	 * @param b 開始する場合はtrue 停止する場合はfalse
	 */
	public synchronized void setAutoUpdateEnabled(boolean b) {
		if (b != (timer == null)) return;
		if (b) {
			timer = new Timer(true);
			timer.schedule(new AutoUpdateTask(), 0, interval);
		} else {
			timer.cancel();
			timer = null;
			automata.setUpdating(false);
		}
		firePropertyChange("autoUpdateEnabled", !b, b);
	}

	@Override
	protected void paintComponent(Graphics g) {
		final var size = getPreferredSize();
		if (screen == null) {
			screen = createImage(size.width, size.height);
			graph = screen.getGraphics();
		}
		graph.setColor(getBackground());
		graph.fillRect(0, 0, size.width, size.height);
		if (automata == null) return;
		final var cw = cellsize.width;
		final var ch = cellsize.height;
		final var iw = (getWidth() - size.width) / 2;
		final var ih = (getHeight() - size.height) / 2;
		for (var x = 0; x < automata.getWidth(); x++) {
			for (var y = 0; y < automata.getHeight(); y++) {
				graph.setColor(automata.getCellColor(x, y));
				graph.fillRect(x * cw, y * ch, cw - 1, ch - 1);
			}
		}
		g.clearRect(iw, ih, size.width, size.height);
		g.drawImage(screen, iw, ih, this);
	}

	/**
	 * このテーブルコンポーネントの1セルの大きさを設定します。
	 *
	 * @param cellsize 1セルの大きさ
	 */
	public void setCellSize(Dimension cellsize) {
		final var old = this.cellsize;
		firePropertyChange("cellSize", old, cellsize);
		updatePreferredSize(cellsize);
	}

	/**
	 * セルオートマータを次の世代に移行します。
	 */
	public void updateNext() {
		automata.updateNext();
		repaint();
	}

	private void updatePreferredSize(Dimension cellsize) {
		if (automata != null && cellsize != null) {
			final var aw = automata.getWidth();
			final var ah = automata.getHeight();
			final var tw = aw * cellsize.width;
			final var th = ah * cellsize.height;
			setPreferredSize(new Dimension(tw, th));
			this.cellsize = cellsize;
		}
	}

	private final class ClickListener extends MouseAdapter {
		@Override
		public void mousePressed(MouseEvent e) {
			if (automata != null && !automata.isUpdating()) {
				final var size = getPreferredSize();
				final var cw = cellsize.width;
				final var ch = cellsize.height;
				final var tw = size.width;
				final var th = size.height;
				var ex = e.getX() - (getWidth() - tw) / 2;
				var ey = e.getY() - (getHeight() - th) / 2;
				if (ex >= 0 && ex < tw && ey >= 0 && ey < th) {
					automata.cellPressed(ex / cw, ey / ch);
					repaint();
				}
			}
		}
	}

	private class AutoUpdateTask extends TimerTask {
		@Override
		public void run() {
			try {
				updateNext();
			} catch (IllegalArgumentException ex) {
			}
		}
	}

}
