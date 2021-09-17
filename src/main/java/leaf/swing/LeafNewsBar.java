/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import javax.swing.*;

import leaf.util.NewsReader;

/**
 * ニュースバーの実装です。
 *
 * @author 無線部開発班
 * @since 2011年5月4日
 */
@SuppressWarnings("serial")
public class LeafNewsBar extends JPanel {
	private LeafBusyLabel indicator;
	private boolean isLoading = false;
	private ConfigBar cfgbar;
	private NotBorderedButton bsets, bnews;
	private LeafScrollLabel label;
	private java.util.Timer timer;
	private NewsReader reader;
	private FeedWorker worker;
	private JToolBar box;

	/**
	 * ニュースバーを構築します。
	 */
	public LeafNewsBar() {
		this(null);
	}

	/**
	 * URLを指定してニュースバーを構築します。
	 *
	 * @param url 接続先URL
	 */
	public LeafNewsBar(URL url) {
		super();
		setLayout(new BorderLayout());
		init(url);
	}

	private void alternate() {
		if (isLoading ^= true) startScroll();
		else stopScroll();
	}

	/**
	 * 接続先のURLを返します。
	 *
	 * @return 接続先URL
	 */
	public URL getURL() {
		if (reader != null) return reader.getURL();
		else return null;
	}

	/**
	 * 接続先のURLを設定します。
	 *
	 * @param url 接続先URL
	 */
	public void setURL(URL url) {
		if (url == null) reader = null;
		else reader = new NewsReader(url);
		bnews.setEnabled(url != null);
		bsets.setEnabled(true);
		cfgbar.setURL(url);
	}

	/**
	 * 接続先のURLを設定します。
	 *
	 * @param url 接続先URLの文字列
	 *
	 * @throws MalformedURLException URLが無効な場合
	 */
	public void setURL(String url) throws MalformedURLException {
		try {
			bsets.setEnabled(true);
			setURL(url != null ? new URL(url) : null);
		} catch (MalformedURLException ex) {
			label.setText(ex.getMessage());
			label.start();
			throw ex;
		}
	}

	/**
	 * ニュースバーを初期化します。
	 *
	 * @param url 接続先URL
	 */
	public void init(URL url) {
		removeAll();
		box = new JToolBar();
		box.setFloatable(false);
		box.setLayout(new ToolBarLayout());
		add(box, BorderLayout.CENTER);
		bsets = new NotBorderedButton("button_settings");
		bsets.addActionListener(e -> {
			bsets.setEnabled(false);
			cfgbar.expand(true);
		});
		box.add(bsets);
		bnews = new NotBorderedButton("button_news");
		bnews.addActionListener(e -> alternate());
		box.add(bnews);
		box.add(label = new LeafScrollLabel());
		label.setScrollCount(1);
		box.add(indicator = new LeafBusyLabel());
		cfgbar = new ConfigBar(this);
		add(cfgbar, BorderLayout.NORTH);
		setURL(url);
	}

	/**
	 * 自動更新が有効であるか返します。
	 *
	 * @return 自動更新する場合true
	 */
	public boolean isAutoUpdateEnabled() {
		return timer != null;
	}

	/**
	 * 自動更新を有効化/無効化するための簡易的なメソッドです。
	 * 時刻を詳細に指定する場合は、別途{@link #update()}メソッドと
	 * {@link Timer java.util.Timer}を利用して外部から制御します。
	 *
	 * @param able 自動更新する場合true デフォルトはfalse
	 */
	public void setAutoUpdateEnabled(boolean able) {
		setAutoUpdate(10);
	}

	private void setAutoUpdate(int min) {
		if (min > 0) {
			var cal = Calendar.getInstance();
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			var task = new TimerTask() {
				public void run() {
					if (isShowing()) update();
				}
			};
			timer = new java.util.Timer();
			timer.scheduleAtFixedRate(task, cal.getTime(), 60000L * min);
		} else {
			if (timer != null) timer.cancel();
			timer = null;
		}
	}

	private void startScroll() {
		if (reader != null) {
			bnews.setText("button_stop");
			bsets.setEnabled(false);
			indicator.start();
			indicator.setVisible(true);
			worker = new FeedWorker(reader);
			worker.execute();
		}
	}

	private void stopScroll() {
		if (worker != null) {
			worker.cancel(true);
			worker = null;
		}
		label.setText("");
		label.stop();
		label.repaint();
	}

	/**
	 * 接続を開始し、最新のフィードを取得します。
	 */
	public void update() {
		stopScroll();
		startScroll();
		isLoading = true;
	}

	class FeedWorker extends SwingWorker<String, String> {
		private final NewsReader reader;

		public FeedWorker(NewsReader reader) {
			this.reader = reader;
		}

		@Override
		protected String doInBackground() throws IOException {
			var feed = reader.read();
			var items = feed.getItems();
			var sb = new StringBuilder();
			sb.append(feed.getTitle());
			for (var item : items) {
				sb.append("  /  ");
				sb.append(item.getTitle());
			}
			return sb.toString();
		}

		@Override
		public void done() {
			indicator.setVisible(false);
			bnews.setText("button_news");
			bsets.setEnabled(true);
			isLoading = false;
			indicator.stop();
			try {
				label.setText(get());
			} catch (InterruptedException ex) {
			} catch (ExecutionException ex) {
				label.setText(ex.getMessage());
			}
			label.start();
			worker = null;
		}
	}

	private class ToolBarLayout extends BoxLayout {
		public ToolBarLayout() {
			super(box, BoxLayout.X_AXIS);
		}

		@Override
		public Dimension maximumLayoutSize(Container target) {
			return target.getMaximumSize();
		}
	}

}
