/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.util.List;
import javax.swing.*;

/**
 * 実行環境のメモリ使用量を監視するダイアログです。
 *
 * @author 無線部開発班
 * @since 2010年5月22日
 */
public final class LeafResourceMonitor extends LeafDialog {
	private final List<MemoryPoolMXBean> mpools;
	private ResourceMonitor monitor;
	private JComboBox<String> combo_pools;
	private JLabel label_usage;

	/**
	 * 親フレームを指定してダイアログを構築します。
	 *
	 * @param owner ダイアログの親
	 */
	public LeafResourceMonitor(Frame owner) {
		super(owner, false);
		setContentSize(new Dimension(400, 240));
		setResizable(false);
		setLayout(new BorderLayout());
		mpools = ManagementFactory.getMemoryPoolMXBeans();
		initialize();
	}

	/**
	 * 親ダイアログを指定してダイアログを構築します。
	 *
	 * @param owner ダイアログの親
	 */
	public LeafResourceMonitor(Dialog owner) {
		super(owner, false);
		setContentSize(new Dimension(400, 240));
		setResizable(false);
		setLayout(new BorderLayout());
		mpools = ManagementFactory.getMemoryPoolMXBeans();
		initialize();
	}

	@Override
	public void initialize() {
		setTitle(translate("title"));
		getContentPane().removeAll();
		var model_pools = new DefaultComboBoxModel<String>();
		combo_pools = new JComboBox<>(model_pools);
		for (var bean : mpools) {
			model_pools.addElement(bean.getName());
		}
		add(combo_pools, BorderLayout.NORTH);
		combo_pools.addItemListener(e -> {
			var index = combo_pools.getSelectedIndex();
			monitor.setMXBean(mpools.get(index));
		});
		monitor = new ResourceMonitor();
		add(monitor, BorderLayout.CENTER);
		monitor.setAutoSamplingEnabled(true);
		label_usage = new JLabel("", JLabel.RIGHT);
		label_usage.setPreferredSize(new Dimension(200, 30));
		add(label_usage, BorderLayout.SOUTH);
		if (!mpools.isEmpty()) monitor.setMXBean(mpools.get(0));
	}

	private class ResourceMonitor extends LeafMonitor {
		private MemoryPoolMXBean bean;

		public ResourceMonitor() {
			super(500);
		}

		@Override
		public int sample() {
			if (bean == null) return 0;
			float used = bean.getUsage().getUsed();
			float total = bean.getUsage().getCommitted();
			var pc = (int) (100f * used / total);
			var k = (long) total / 1024;
			label_usage.setText(String.format("%d%%  %6d kB  ", pc, k));
			return (int) (500 * used / total);
		}

		public void setMXBean(MemoryPoolMXBean bean) {
			this.bean = bean;
			super.clear();
		}
	}

}
