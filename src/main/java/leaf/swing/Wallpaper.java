/*******************************************************************************
 * Java Swing Library 'Leaf' and 'Tsukishiro Editor' since 2009 February 24th
 * License: GNU General Public License v3+ (see LICENSE)
 * Author: Journal of Hamradio Informatics (http://pafelog.net)
*******************************************************************************/
package leaf.swing;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;
import javax.swing.border.Border;

/**
 * 壁紙を表示する{@link Border}の実装です。
 *
 * @author 無線部開発班
 * @since 2011年12月20日
 */
public final class Wallpaper implements Border {
	private final WallpaperSettings settings;
	private BufferedImage image;
	private Rectangle size, trim;

	/**
	 * 壁紙設定を指定して壁紙を生成します。
	 *
	 * @param ws 壁紙の設定
	 *
	 * @throws IOException 壁紙の読み込みに失敗した場合
	 */
	public Wallpaper(WallpaperSettings ws) throws IOException {
		this.settings = ws;
		if (ws.getPhotoFile() != null) loadImage();
	}

	private void loadImage() throws IOException {
		image = ImageIO.read(Objects.requireNonNull(settings.getPhotoFile()));
		var iw = image.getWidth();
		var ih = image.getHeight();
		size = new Rectangle(0, 0, iw, ih);
		trim = settings.getPhotoTrimRect();
		if (trim == null) trim = size;
		var tx = trim.x;
		var ty = trim.y;
		var tw = trim.width;
		var th = trim.height;
		image = image.getSubimage(tx, ty, tw, th);
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		var g2 = (Graphics2D) g;
		g2.setPaint(settings.getBackgroundColor());
		g2.fillRect(x, y, width, height);
		if (image != null) {
			x += (width - size.width) / 2 + trim.x;
			y += (height - size.height) / 2 + trim.y;
			var alpha = settings.getPhotoAlpha();
			g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
			g2.drawRenderedImage(image, AffineTransform.getTranslateInstance(x, y));
		}
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return new Insets(0, 0, 0, 0);
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}

}
