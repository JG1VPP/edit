/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import leaf.util.AudioPlayer;

/**
 * タイピング音を再生するプレーヤです。
 *
 * @author 無線部開発班
 */
public class TypingSoundPlayer extends KeyAdapter {
	private final AudioPlayer player;
	private boolean isEnabled = false;

	/**
	 * プレーヤを構築します。
	 */
	public TypingSoundPlayer() {
		player = new AudioPlayer();
	}

	@Override
	public void keyTyped(KeyEvent e) {
		if (isEnabled && !player.isPlaying()) player.start();
	}

	/**
	 * 音声データをファイルから読み込みます。
	 *
	 * @param file 音声ファイル
	 *
	 * @throws IOException 読み込みに失敗した場合
	 */
	public void load(File file) throws IOException {
		if (isEnabled = (file != null)) {
			player.load(file);
			var thread = new Thread(new ControleThread());
			thread.setDaemon(true);
			thread.start();
		}
	}

	private class ControleThread implements Runnable {
		final long length = player.getFrameLength();

		@Override
		public void run() {
			while (isEnabled) {
				if (player.getFramePosition() >= length) player.stop();
				try {
					Thread.sleep(100);
				} catch (InterruptedException ex) {
				}
			}
		}
	}
}
