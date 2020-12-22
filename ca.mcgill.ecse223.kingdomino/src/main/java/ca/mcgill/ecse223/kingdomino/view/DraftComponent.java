package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.Draft;

public class DraftComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	private double size;
	private double offset = 0;
	private Draft draft;
	private String type;

	public DraftComponent(int size, String type) {
		super();
		this.size = size;
		this.type = type;
		if (type.equals("Next Draft"))
			draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		else
			draft = KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft();
		if (size % 90 != 0)
			offset = size % 90 % 5;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		if (type.equals("Next Draft"))
			draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();
		else if (type.equals("Current Draft"))
			draft = KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft();
		else if (type.equals("No more drafts")) {
			Graphics2D g2d = (Graphics2D) g.create();
			BufferedImage img = null;
			try {
				img = ImageIO.read(getFileFromResources("nomoredrafts.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g2d.drawImage(img, 0, 0, this);
		} else {
			Graphics2D g2d = (Graphics2D) g.create();
			BufferedImage img = null;
			try {
				img = ImageIO.read(getFileFromResources("nocurrentdraft.png"));
			} catch (IOException e) {
				e.printStackTrace();
			}
			g2d.drawImage(img, 0, 0, this);
		}
		if (type.equals("Current Draft") || type.equals("Next Draft")) {
			Graphics2D g2d = (Graphics2D) g.create();
			int i = 0;
			for (Domino domino : draft.getIdSortedDominos()) {
				BufferedImage img = getBufferedImageByID(domino.getId());
				img = resize(img, (int) (size / 45 * 10), (int) (size / 9));
				g2d.drawImage(img, i, 0, this);
				i += 80;
			}
		}
	}

	private File getFileFromResources(String fileName) {
		ClassLoader classLoader = getClass().getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null)
			throw new IllegalArgumentException("file is not found!");
		else {
			return new File(resource.getFile());
		}
	}

	private static BufferedImage resize(BufferedImage img, int height, int width) {
		Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
		BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = resized.createGraphics();
		g2d.drawImage(tmp, 0, 0, null);
		g2d.dispose();
		return resized;
	}

	public BufferedImage rotate(BufferedImage img, double angle) {
		double rads = Math.toRadians(angle);
		double sin = Math.abs(Math.sin(rads)), cos = Math.abs(Math.cos(rads));
		int w = img.getWidth();
		int h = img.getHeight();
		int newWidth = (int) Math.floor(w * cos + h * sin);
		int newHeight = (int) Math.floor(h * cos + w * sin);

		BufferedImage rotated = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = rotated.createGraphics();
		AffineTransform at = new AffineTransform();
		at.translate((newWidth - w) / 2, (newHeight - h) / 2);

		int x = w / 2;
		int y = h / 2;

		at.rotate(rads, x, y);
		g2d.setTransform(at);
		g2d.drawImage(img, 0, 0, this);
		g2d.dispose();

		return rotated;
	}

	public BufferedImage getBufferedImageByID(int id) {
		try {
			return ImageIO.read(getFileFromResources("dom" + id + ".png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}