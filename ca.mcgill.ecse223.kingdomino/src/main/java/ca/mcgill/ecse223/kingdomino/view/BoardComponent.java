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

import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.KingdomTerritory;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

public class BoardComponent extends JPanel {

	private static final long serialVersionUID = 1L;
	public BufferedImage board, castle;
	private Player player;
	private double size;
	private double offset = 0;

	public BoardComponent(PlayerColor color, int size) {
		super();
		this.size = size;
		if (size % 90 != 0)
			offset = size % 90 % 5;
		try {
			board = ImageIO.read(getFileFromResources("board.png"));
			if (color == PlayerColor.Blue)
				castle = ImageIO.read(getFileFromResources("bluecastle.png"));
			else if (color == PlayerColor.Green)
				castle = ImageIO.read(getFileFromResources("greencastle.png"));
			else if (color == PlayerColor.Yellow)
				castle = ImageIO.read(getFileFromResources("yellowcastle.png"));
			else
				castle = ImageIO.read(getFileFromResources("redcastle.png"));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		board = resize(board, size, size);
		castle = resize(castle, size / 9, size / 9);
		player = KingdominoController.getPlayerByPlayerColor(color);

	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		doDrawing(g);
	}

	private void doDrawing(Graphics g) {
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.drawImage(board, 0, 0, this);
		g2d.drawImage(castle, (int) (size / 9 * 4 + offset), (int) (size / 9 * 4 + offset), this);
		for (KingdomTerritory territory : player.getKingdom().getTerritories()) {
			if (territory instanceof DominoInKingdom) {
				DominoInKingdom dom = (DominoInKingdom) territory;
				BufferedImage img = getBufferedImageByID(dom.getDomino().getId());
				img = resize(img, (int) (size / 45 * 10), (int) (size / 9));
				int x = dom.getX();
				int y = dom.getY();
				DirectionKind direction = dom.getDirection();
				if (direction == DirectionKind.Up)
					g2d.drawImage(img, (int) (x * size / 9 + size / 9 * 4 + offset),
							(int) (y * -size / 9 + size / 9 * 3 + offset), this);
				else if (direction == DirectionKind.Down) {
					img = rotate(img, 180);
					g2d.drawImage(img, (int) (x * size / 9 + size / 9 * 4 + offset),
							(int) (y * -size / 9 + size / 9 * 4 + offset), this);
				} else if (direction == DirectionKind.Left) {
					img = rotate(img, 270);
					g2d.drawImage(img, (int) (x * size / 9 + size / 9 * 3 + offset),
							(int) (y * -size / 9 + size / 9 * 4 + offset), this);
				} else {
					img = rotate(img, 90);
					g2d.drawImage(img, (int) (x * size / 9 + size / 9 * 4 + offset),
							(int) (y * -size / 9 + size / 9 * 4 + offset), this);
				}
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