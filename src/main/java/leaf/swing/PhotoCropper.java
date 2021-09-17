/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.swing.*;

import static java.awt.BasicStroke.CAP_SQUARE;
import static java.awt.BasicStroke.JOIN_ROUND;

/**
 * マウスのドラッグ操作によって画像を切り抜くコンポーネントです。
 *
 * @author 無線部開発班
 * @since 2012/04/30
 */
public class PhotoCropper extends JComponent {
	private final BasicStroke stroke;
	private final MediaTracker tracker;
	private BufferedImage fullsize, cropped;
	private Image view;
	private Rectangle drag, trim; //点線用/切り抜き用

	/**
	 * デフォルトの何も表示しないコンポーネントを生成します。
	 */
	public PhotoCropper() {
		this(null);
	}

	/**
	 * デフォルトで表示する画像を指定してコンポーネントを生成します。
	 *
	 * @param image 表示する画像
	 */
	public PhotoCropper(BufferedImage image) {
		tracker = new MediaTracker(this);
		setImage(image);
		addComponentListener(new ResizeListener());
		var listener = new DragListener();
		addMouseListener(listener);
		addMouseMotionListener(listener);
		var pattern = new float[]{3f, 3f, 3f, 3f};
		stroke = new BasicStroke(1f, CAP_SQUARE, JOIN_ROUND, 1f, pattern, 0f);
		trim = new Rectangle(0, 0, 0, 0);
	}

	/**
	 * ActionListenerを追加登録します。
	 *
	 * @param lis 登録するリスナー
	 */
	public void addActionListener(ActionListener lis) {
		listenerList.add(ActionListener.class, lis);
	}

	private void fireActionPerformed() {
		var e = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "trim");
		var listeners = listenerList.getListeners(ActionListener.class);
		for (var listener : listeners) {
			listener.actionPerformed(e);
		}
	}

	/**
	 * 表示画像を返します。
	 *
	 * @return 表示画像
	 */
	public BufferedImage getImage() {
		return fullsize;
	}

	/**
	 * 表示画像を設定します。このメソッドは画像を完全にロードするまで復帰しません。
	 *
	 * @param image 表示する画像
	 */
	public void setImage(BufferedImage image) {
		final Image old = fullsize;
		fullsize = cropped = image;
		if (image != null) try {
			tracker.addImage(image, 0);
			tracker.waitForID(0);
			trim.x = trim.y = 0;
			trim.width = image.getWidth();
			trim.height = image.getHeight();
			resizeViewImage();
		} catch (InterruptedException ex) {
			ex.printStackTrace();
		}
		firePropertyChange("image", old, image);
	}

	/**
	 * 画像を切り抜く座標を返します。
	 *
	 * @return 画像を切り抜く座標
	 */
	public Rectangle getTrimRect() {
		return trim;
	}

	/**
	 * 指定した座標で表示画像を切り抜きます。
	 *
	 * @param trim 画像を切り抜く座標
	 */
	public void setTrimRect(Rectangle trim) {
		final var old = this.trim;
		try {
			this.trim = trim = (Rectangle) trim.clone();
			cropped = fullsize.getSubimage(trim.x, trim.y, trim.width, trim.height);
			firePropertyChange("trimRect", old, trim);
			resizeViewImage();
		} catch (Exception ex) {
			setImage(fullsize);
		}
		repaint();
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (cropped != null) {
			g.drawImage(view, 0, 0, this);
			if (drag != null) {
				var g2 = (Graphics2D) g;
				g2.setColor(getBackground());
				g2.setXORMode(getForeground());
				g2.setStroke(stroke);
				g2.draw(drag);
			}
		}
	}

	/**
	 * ActionListenerを削除します。
	 *
	 * @param lis 削除するリスナー
	 */
	public void removeActionListener(ActionListener lis) {
		listenerList.remove(ActionListener.class, lis);
	}

	private void resizeViewImage() {
		if (cropped != null && isShowing()) {
			var bounds = getVisibleRect();
			view = cropped.getScaledInstance(bounds.width, bounds.height, Image.SCALE_SMOOTH);
		}
		repaint();
	}

	private class ResizeListener extends ComponentAdapter {
		@Override
		public void componentResized(ComponentEvent e) {
			resizeViewImage();
		}
	}

	private class DragListener extends MouseAdapter {
		private Point start = new Point();
		private double wratio = 1, hratio = 1;

		@Override
		public void mousePressed(MouseEvent e) {
			if (cropped != null) {
				start = e.getPoint();
				wratio = (double) cropped.getWidth() / getWidth();
				hratio = (double) cropped.getHeight() / getHeight();
			}
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			if (cropped != null) {
				drag = null;
				trim.x += (int) (start.x * wratio);
				trim.y += (int) (start.y * hratio);
				trim.width = (int) ((e.getX() - start.x) * wratio);
				trim.height = (int) ((e.getY() - start.y) * hratio);
				setTrimRect(trim);
				fireActionPerformed();
			}
		}

		@Override
		public void mouseDragged(MouseEvent e) {
			if (cropped != null) {
				drag = new Rectangle(start.x, start.y, e.getX() - start.x, e.getY() - start.y);
				repaint();
			}
		}
	}
}
