/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import javax.swing.*;

import leaf.main.Application;

/**
 * LeafVeinsアプリケーションのメインウィンドウです。
 *
 * @author 無線部開発班
 * @since 2012/03/27
 */
public final class MainFrame extends JFrame {
	private static MainFrame instance;

	private final JPanel centerpane;
	private final MainStatusBar statusbar;
	private JToolBar toolbar;

	protected MainFrame() {
		setIconImage(LeafIcons.getImage("LUNA"));
		setMinimumSize(new Dimension(500, 250));
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		centerpane = new JPanel(new BorderLayout());
		statusbar = new MainStatusBar();
		add(centerpane, BorderLayout.CENTER);
		add(statusbar, BorderLayout.SOUTH);
	}

	/**
	 * ウィンドウ中央のパネルを返します。
	 *
	 * @return パネル
	 */
	public JPanel getCenterPane() {
		return centerpane;
	}

	/**
	 * ウィンドウ上部のツールバーを返します。
	 *
	 * @return ツールバー
	 */
	public JToolBar getJToolBar() {
		return toolbar;
	}

	/**
	 * ウィンドウ上部のツールバーを設定します。
	 *
	 * @param toolbar ツールバー
	 */
	public void setJToolBar(JToolBar toolbar) {
		final var old = this.toolbar;
		if (this.toolbar != null) remove(this.toolbar);
		add(toolbar, BorderLayout.NORTH);
		this.toolbar = toolbar;
		firePropertyChange("jToolBar", old, toolbar);
	}

	/**
	 * ウィンドウ下部のステータスバーを返します。
	 *
	 * @return ステータスバー
	 */
	public MainStatusBar getStatusBar() {
		return statusbar;
	}

	/**
	 * ウィンドウの表示を初期化します。
	 */
	public void initialize() {
		statusbar.initialize();
	}

	/**
	 * メインウィンドウのインスタンスを返します。
	 * <p>
	 * {@link Application}からの利用は推奨されません。
	 *
	 * @return メインウィンドウ
	 */
	public static MainFrame getInstance() {
		if (instance == null) instance = new MainFrame();
		return instance;
	}

}
