package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;

public class WelcomePage extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton btnStartANew;
	private JButton btnLoad;
	private JButton btnSettings;
	private JButton btnExit;
	private JLabel lblKingdomino;
	private JLabel error;

	public WelcomePage() {
		initPage();
	}

	public void initPage() {
		this.setSize(1250, 650);
		this.setTitle("Welcome to Kingdomino!");
		try {
			lblKingdomino = new JLabel(
					new ImageIcon(resize(ImageIO.read(getFileFromResources("GUI_Mockup_Main_Page.png")), 650, 1250)));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		error = new JLabel("");
		error.setLocation(350, 300);
		error.setSize(new Dimension(200, 50));
		btnStartANew = new JButton("PLAY");
		btnLoad = new JButton("LOAD GAME");
		btnSettings = new JButton("SETTINGS");
		btnExit = new JButton("EXIT");
		btnStartANew.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoApplication.setOptionsPage();
			}
		});
		btnLoad.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				loadGame();
			}
		});
		btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});
		lblKingdomino.setSize(new Dimension(300, 300));
		lblKingdomino.setLocation(0, 0);
		btnStartANew.setSize(new Dimension(200, 50));
		btnStartANew.setLocation(450, 250);
		btnLoad.setLocation(450, 310);
		btnLoad.setSize(new Dimension(200, 50));
		btnSettings.setSize(new Dimension(200, 50));
		btnSettings.setLocation(450, 380);
		btnExit.setSize(new Dimension(200, 50));
		btnExit.setLocation(450, 470);
		getContentPane().add(error);
		getContentPane().add(btnStartANew);
		getContentPane().add(btnLoad);
		getContentPane().add(btnSettings);
		getContentPane().add(btnExit);
		getContentPane().add(lblKingdomino);
	}

	private void loadGame() {
		String name = inputDialog("Enter file name (don't forget the extension):", "Save Game As", null);
		boolean found = false;
		if (name != null) {
			if (!name.equals("")) {
				try {
					name = "src/test/resources/" + name;
					KingdominoController.loadGame(name);
					error.setText("Loaded");
					found = true;
				} catch (Exception e) {
					error.setText("Invalid input");
				}
			} else {
				error.setText("Invalid input");
			}
		} else {
			error.setText("Invalid input");
		}
		if (found)
			KingdominoApplication.setLoadPage();

	}

	private String inputDialog(String message, String title, String initValue) {
		return (String) JOptionPane.showInputDialog((Component) this, (Object) message, title,
				JOptionPane.INFORMATION_MESSAGE, (Icon) null, (Object[]) null, (Object) initValue);
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
}