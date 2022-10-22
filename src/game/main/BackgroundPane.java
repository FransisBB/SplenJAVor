package game.main;

import java.awt.Graphics;
import java.awt.Image;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class BackgroundPane extends JPanel{
	private static final long serialVersionUID = 4628982726033671510L;
	@Override
	public void paintComponent(Graphics g) {
		URL imageURL = getClass().getClassLoader().getResource("images/background.jpg");
		Image tileImage = new ImageIcon(imageURL).getImage();
		int width = getWidth();
		int height = getHeight();
		int imageW = tileImage.getWidth(this);
		int imageH = tileImage.getHeight(this);

		for (int x = 0; x < width; x += imageW) {
			for (int y = 0; y < height; y += imageH) {
				g.drawImage(tileImage, x, y, this);
			}
		}
	}
}
