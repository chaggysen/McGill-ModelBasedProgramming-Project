package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom;
import ca.mcgill.ecse223.kingdomino.model.Game;
import ca.mcgill.ecse223.kingdomino.model.Gameplay;
import ca.mcgill.ecse223.kingdomino.model.Gameplay.Gamestatus;
import ca.mcgill.ecse223.kingdomino.model.Gameplay.GamestatusInGame;
import ca.mcgill.ecse223.kingdomino.model.Gameplay.GamestatusInitializing;
import ca.mcgill.ecse223.kingdomino.model.Player;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;

public class GamePage extends JFrame {
	private static final long serialVersionUID = 1L;
	private JButton btnUp;
	private JButton btnDown;
	private JButton btnRight;
	private JButton btnLeft;
	private JButton btnPlace;
	private JButton btnDiscard;
	private JButton saveGame;
	private JButton newGame;
	private JButton rotateClockwise;
	private JButton rotateCounterclockwise;
	private JButton browse;
	private static BoardComponent blueComponent;
	private static BoardComponent yellowComponent;
	private static BoardComponent greenComponent;
	private static BoardComponent pinkComponent;
	private static DraftComponent currentDraft;
	private static DraftComponent nextDraft;
	private JButton choose1;
	private JButton choose2;
	private JButton choose3;
	private JButton choose4;
	private JLabel txtCurDraft;
	private JLabel txtNextDraft;
	private JLabel message;
	private JLabel error;
	private JLabel blueCrown;
	private JLabel greenCrown;
	private JLabel yellowCrown;
	private JLabel pinkCrown;
	private JLabel blueScore;
	private JLabel yellowScore;
	private JLabel greenScore;
	private JLabel pinkScore;
	private JLabel draftNumber;
	private JLabel ranking1;
	private JLabel ranking2;
	private JLabel ranking3;
	private JLabel ranking4;
	private JLabel txtRanking;
	// Refresh Page to get Final Ranking
	private Gameplay gameplay;
	private int reset = 1;

	private static final int componentSize = 320;

	public GamePage() {
		initComponent();
	}

	private void initComponent() {

		initFrame();
		gameplay = KingdominoApplication.getGameplay();

		try {
			blueCrown = new JLabel(new ImageIcon(
					resize(ImageIO.read(getFileFromResources("bluecrown.png")), componentSize / 9, componentSize / 9)));
			yellowCrown = new JLabel(new ImageIcon(resize(ImageIO.read(getFileFromResources("yellowcrown.png")),
					componentSize / 9, componentSize / 9)));
			greenCrown = new JLabel(new ImageIcon(resize(ImageIO.read(getFileFromResources("greencrown.png")),
					componentSize / 9, componentSize / 9)));
			pinkCrown = new JLabel(new ImageIcon(
					resize(ImageIO.read(getFileFromResources("redcrown.png")), componentSize / 9, componentSize / 9)));
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		blueCrown.setSize(new Dimension(componentSize / 9, componentSize / 9));
		yellowCrown.setSize(new Dimension(componentSize / 9, componentSize / 9));
		greenCrown.setSize(new Dimension(componentSize / 9, componentSize / 9));
		pinkCrown.setSize(new Dimension(componentSize / 9, componentSize / 9));
		getContentPane().add(blueCrown);
		getContentPane().add(yellowCrown);
		getContentPane().add(greenCrown);
		getContentPane().add(pinkCrown);

		blueScore = new JLabel(
				"Blue score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Blue).getTotalScore());
		blueScore.setSize(new Dimension(150, 50));
		blueScore.setLocation(componentSize * 2 + 200, 300);
		yellowScore = new JLabel(
				"Yellow score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Yellow).getTotalScore());
		yellowScore.setSize(new Dimension(150, 50));
		yellowScore.setLocation(componentSize * 2 + 200, 350);
		greenScore = new JLabel(
				"Green score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Green).getTotalScore());
		greenScore.setSize(new Dimension(150, 50));
		greenScore.setLocation(componentSize * 2 + 200, 400);
		pinkScore = new JLabel(
				"Pink score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Pink).getTotalScore());
		pinkScore.setSize(new Dimension(150, 50));
		pinkScore.setLocation(componentSize * 2 + 200, 450);
		getContentPane().add(blueScore);
		getContentPane().add(yellowScore);
		getContentPane().add(greenScore);
		getContentPane().add(pinkScore);

		draftNumber = new JLabel(
				"Draft number " + (KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size()));
		draftNumber.setSize(new Dimension(200, 100));
		draftNumber.setLocation(componentSize + 20, 70);
		getContentPane().add(draftNumber);

		message = new JLabel("");
		message.setSize(new Dimension(150, 50));
		message.setLocation(componentSize + 20, 300);
		message.setFont(new Font("Arial", Font.ITALIC, 12));
		error = new JLabel("No errors");
		error.setSize(new Dimension(200, 50));
		error.setLocation(componentSize + 250, 300);
		error.setFont(new Font("Arial", Font.ITALIC, 12));
		getContentPane().add(message);
		getContentPane().add(error);

		blueComponent = new BoardComponent(PlayerColor.Blue, componentSize);
		blueComponent.setLocation(0, 0);
		blueComponent.setSize(new Dimension(componentSize, componentSize));
		yellowComponent = new BoardComponent(PlayerColor.Yellow, componentSize);
		yellowComponent.setLocation(componentSize * 3, 0);
		yellowComponent.setSize(new Dimension(componentSize, componentSize));
		greenComponent = new BoardComponent(PlayerColor.Green, componentSize);
		greenComponent.setLocation(0, componentSize);
		greenComponent.setSize(new Dimension(componentSize, componentSize));
		pinkComponent = new BoardComponent(PlayerColor.Pink, componentSize);
		pinkComponent.setLocation(componentSize * 3, componentSize);
		pinkComponent.setSize(new Dimension(componentSize, componentSize));
		getContentPane().setLayout(null);
		getContentPane().add(blueComponent);
		getContentPane().add(yellowComponent);
		getContentPane().add(greenComponent);
		getContentPane().add(pinkComponent);

		nextDraft = new DraftComponent(componentSize + 40, "Next Draft");
		nextDraft.setSize(new Dimension(componentSize, 80));
		nextDraft.setLocation(componentSize + 200, 20);
		currentDraft = new DraftComponent(componentSize + 40, "Current Draft");
		currentDraft.setSize(new Dimension(componentSize, 80));
		currentDraft.setLocation(componentSize + 200, 200);
		getContentPane().add(nextDraft);
		getContentPane().add(currentDraft);

		choose1 = new JButton("Choose");
		choose1.setSize(new Dimension(80, 50));
		choose1.setLocation(componentSize + 180, 100);
		choose2 = new JButton("Choose");
		choose2.setSize(new Dimension(80, 50));
		choose2.setLocation(componentSize + 260, 100);
		choose3 = new JButton("Choose");
		choose3.setSize(new Dimension(80, 50));
		choose3.setLocation(componentSize + 340, 100);
		choose4 = new JButton("Choose");
		choose4.setSize(new Dimension(80, 50));
		choose4.setLocation(componentSize + 420, 100);
		txtNextDraft = new JLabel("Next Draft:");
		txtNextDraft.setLocation(componentSize + 100, 20);
		txtNextDraft.setSize(new Dimension(100, 50));
		txtCurDraft = new JLabel("Current Draft:");
		txtCurDraft.setLocation(componentSize + 100, 200);
		txtCurDraft.setSize(new Dimension(100, 50));
		getContentPane().add(choose1);
		getContentPane().add(choose2);
		getContentPane().add(choose3);
		getContentPane().add(choose4);
		getContentPane().add(txtNextDraft);
		getContentPane().add(txtCurDraft);

		rotateClockwise = new JButton("Rotate Clockwise");
		rotateClockwise.setLocation(componentSize + 150, 350);
		rotateClockwise.setSize(new Dimension(150, 50));
		rotateCounterclockwise = new JButton("Rotate Counterclockwise");
		rotateCounterclockwise.setLocation(componentSize + 300, 350);
		rotateCounterclockwise.setSize(new Dimension(200, 50));
		getContentPane().add(rotateClockwise);
		getContentPane().add(rotateCounterclockwise);
		btnUp = new JButton("UP");
		btnDown = new JButton("DOWN");
		btnLeft = new JButton("LEFT");
		btnRight = new JButton("RIGHT");
		btnUp.setLocation(componentSize + 200 + 50, 450);
		btnDown.setLocation(componentSize + 200 + 50, 550);
		btnLeft.setLocation(componentSize + 200, 500);
		btnRight.setLocation(componentSize + 200 + 100, 500);
		btnUp.setSize(new Dimension(100, 50));
		btnDown.setSize(new Dimension(100, 50));
		btnLeft.setSize(new Dimension(100, 50));
		btnRight.setSize(new Dimension(100, 50));
		btnPlace = new JButton("Place Domino");
		btnPlace.setLocation(componentSize + 200 + 100 + 150, 500);
		btnPlace.setSize(new Dimension(150, 50));
		btnDiscard = new JButton("Discard Domino");
		btnDiscard.setLocation(componentSize + 30, 500);
		btnDiscard.setSize(new Dimension(150, 50));
		browse = new JButton("Browse");
		browse.setLocation(componentSize + 520, 150);
		browse.setSize(new Dimension(100, 50));
		getContentPane().add(btnUp);
		getContentPane().add(btnDown);
		getContentPane().add(btnLeft);
		getContentPane().add(btnRight);
		getContentPane().add(btnPlace);
		getContentPane().add(btnDiscard);
		getContentPane().add(browse);

		saveGame = new JButton("Save Game");
		saveGame.setLocation(componentSize + 50, 600);
		saveGame.setSize(new Dimension(100, 50));
		newGame = new JButton("New Game");
		newGame.setLocation(componentSize + 450, 600);
		newGame.setSize(new Dimension(100, 50));
		getContentPane().add(saveGame);
		getContentPane().add(newGame);

		txtRanking = new JLabel("Final Ranking");
		txtRanking.setLocation(componentSize + 20, 350);
		txtRanking.setSize(new Dimension(100, 20));
		ranking1 = new JLabel("");
		ranking1.setLocation(componentSize + 20, 370);
		ranking1.setSize(100, 20);
		ranking2 = new JLabel("");
		ranking2.setLocation(componentSize + 20, 390);
		ranking2.setSize(100, 20);
		ranking3 = new JLabel("");
		ranking3.setLocation(componentSize + 20, 410);
		ranking3.setSize(100, 20);
		ranking4 = new JLabel("");
		ranking4.setLocation(componentSize + 20, 430);
		ranking4.setSize(100, 20);

		rotateClockwise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					if (!gameplay.rotateDom(player, domino, "clockwise"))
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		rotateCounterclockwise.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					if (!gameplay.rotateDom(player, domino, "counterclockwise"))
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		btnUp.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(domino, player.getKingdom());
					if (!gameplay.moveDom(dom, "up", player))
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		btnDown.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(domino, player.getKingdom());
					if (!gameplay.moveDom(dom, "down", player))
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		btnLeft.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(domino, player.getKingdom());
					if (!gameplay.moveDom(dom, "left", player))
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		btnRight.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(domino, player.getKingdom());
					if (!gameplay.moveDom(dom, "right", player))
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		btnPlace.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					DominoInKingdom dom = KingdominoController.getDominoInKingdomByDomino(domino, player.getKingdom());
					if (dom == null) {
						KingdominoController.initPlacement(player, domino);
						dom = KingdominoController.getDominoInKingdomByDomino(domino, player.getKingdom());
					}
					if (!gameplay.placeDom(player, domino, dom.getX(), dom.getY(), dom.getDirection(), true))
						error.setText("Invalid action");
					if (!gameplay.placeReady())
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		btnDiscard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					Domino domino = player.getDominoSelection().getDomino();
					if (!gameplay.discardDom(player.getKingdom(), domino))
						error.setText("Invalid action");
					if (!gameplay.placeReady())
						error.setText("Invalid action");
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		saveGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				saveGame();
			}
		});
		newGame.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoApplication.setOptionsPage();
			}
		});
		browse.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoApplication.openBrowsePage();
			}
		});
		choose1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInitializing() == GamestatusInitializing.SelectingFirstDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.SelectingDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					if (!gameplay.selectDom(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()
							.getIdSortedDomino(0).getId()))
						error.setText("Invalid action");
					boolean move = gameplay.isCurrentPlayerTheLastInTurn();
					if (!gameplay.selectReady())
						error.setText("Invalid action");
					else {
						getCrownByPlayer(player).setLocation(componentSize + 200, 20);
						if ((gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
								|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) && move) {
							getCrownByPlayer(game.getPlayer(0)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(1)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(2)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(3)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getY() + 180);
						}
					}
					refreshData();
				} else
					error.setText("Invalid action");

			}
		});
		choose2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInitializing() == GamestatusInitializing.SelectingFirstDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.SelectingDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					if (!gameplay.selectDom(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()
							.getIdSortedDomino(1).getId()))
						error.setText("Invalid action");
					boolean move = gameplay.isCurrentPlayerTheLastInTurn();
					if (!gameplay.selectReady())
						error.setText("Invalid action");
					else {
						getCrownByPlayer(player).setLocation(componentSize + 280, 20);
						if ((gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
								|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) && move) {
							getCrownByPlayer(game.getPlayer(0)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(1)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(2)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(3)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getY() + 180);
						}
					}
				} else
					error.setText("Invalid action");

			}
		});
		choose3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInitializing() == GamestatusInitializing.SelectingFirstDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.SelectingDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					if (!gameplay.selectDom(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()
							.getIdSortedDomino(2).getId()))
						error.setText("Invalid action");
					boolean move = gameplay.isCurrentPlayerTheLastInTurn();
					if (!gameplay.selectReady())
						error.setText("Invalid action");
					else {
						getCrownByPlayer(player).setLocation(componentSize + 360, 20);
						if ((gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
								|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) && move) {
							getCrownByPlayer(game.getPlayer(0)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(1)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(2)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(3)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getY() + 180);
						}
					}
				} else
					error.setText("Invalid action");

			}
		});
		choose4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				if (gameplay.getGamestatusInitializing() == GamestatusInitializing.SelectingFirstDomino
						|| gameplay.getGamestatusInGame() == GamestatusInGame.SelectingDomino) {
					Game game = KingdominoApplication.getKingdomino().getCurrentGame();
					Player player = game.getNextPlayer();
					if (!gameplay.selectDom(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft()
							.getIdSortedDomino(3).getId()))
						error.setText("Invalid action");
					boolean move = gameplay.isCurrentPlayerTheLastInTurn();
					if (!gameplay.selectReady())
						error.setText("Invalid action");
					else {
						getCrownByPlayer(player).setLocation(componentSize + 440, 20);
						if ((gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
								|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) && move) {
							getCrownByPlayer(game.getPlayer(0)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(1)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(2)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getY() + 180);
							getCrownByPlayer(game.getPlayer(3)).setLocation(
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getX(),
									(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getY() + 180);
						}
					}
				} else
					error.setText("Invalid action");

			}
		});
		refreshData();
	}

	private void saveGame() {
		String name = inputDialog("Enter file name (don't forget the extension):", "Save Game As", null);
		if (name != null) {
			if (!name.equals("")) {
				try {
					KingdominoController.saveGame(name);
					error.setText("Saved");
				} catch (Exception e) {
					error.setText("Invalid input");
				}
			} else {
				error.setText("Invalid input");
			}
		} else {
			error.setText("Invalid input");
		}

	}

	private String inputDialog(String message, String title, String initValue) {
		return (String) JOptionPane.showInputDialog((Component) this, (Object) message, title,
				JOptionPane.INFORMATION_MESSAGE, (Icon) null, (Object[]) null, (Object) initValue);
	}

	private void initFrame() {
		this.setSize(1440, 720);
		setTitle("Kingdomino Game");
	}

	private void refreshData() {
		reset = -reset;
		blueComponent.repaint();
		yellowComponent.repaint();
		greenComponent.repaint();
		pinkComponent.repaint();

		if (KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft() != null)
			nextDraft.repaint();
		if (gameplay.getGamestatus() == Gamestatus.Initializing) {
			int x = currentDraft.getX();
			int y = currentDraft.getY();
			Dimension dim = currentDraft.getSize();
			getContentPane().remove(currentDraft);
			currentDraft = new DraftComponent(componentSize + 40, "No current draft");
			currentDraft.setLocation(x, y);
			currentDraft.setSize(dim);
			getContentPane().add(currentDraft);
		} else {
			int x = currentDraft.getX();
			int y = currentDraft.getY();
			Dimension dim = currentDraft.getSize();
			getContentPane().remove(currentDraft);
			currentDraft = new DraftComponent(componentSize + 40, "Current Draft");
			currentDraft.setLocation(x, y);
			currentDraft.setSize(dim);
			getContentPane().add(currentDraft);
		}
		if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino) {
			int x = nextDraft.getX();
			int y = nextDraft.getY();
			Dimension dim = nextDraft.getSize();
			getContentPane().remove(nextDraft);
			nextDraft = new DraftComponent(componentSize + 40, "No more drafts");
			nextDraft.setLocation(x, y);
			nextDraft.setSize(dim);
			nextDraft.removeAll();
			getContentPane().add(nextDraft);
			nextDraft.repaint();
		}

		blueScore.setText(
				"Blue score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Blue).getTotalScore());
		yellowScore.setText(
				"Yellow score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Yellow).getTotalScore());
		greenScore.setText(
				"Green score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Green).getTotalScore());
		pinkScore.setText(
				"Pink score: " + KingdominoController.getPlayerByPlayerColor(PlayerColor.Pink).getTotalScore());

		Color color = KingdominoController.getColorByPlayerColor(
				KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getColor());
		message.setText("It is " + KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer().getColor()
				+ "'s turn");
		message.setBackground(color);

		if (KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size() != 1)
			error.setText("Domino is " + KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer()
					.getDominoSelection().getDomino().getStatus());

		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		if (gameplay.getGamestatusInGame() == GamestatusInGame.PlacingDomino
				|| gameplay.getGamestatusInGame() == GamestatusInGame.PlacingLastDomino
				|| gameplay.getGamestatusInGame() == GamestatusInGame.SelectingDomino) {
			getCrownByPlayer(game.getPlayer(0)).setLocation(
					(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getX(),
					(int) getCrownByPlayer(game.getPlayer(0)).getLocation().getY() + reset);
			getCrownByPlayer(game.getPlayer(1)).setLocation(
					(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getX(),
					(int) getCrownByPlayer(game.getPlayer(1)).getLocation().getY() + reset);
			getCrownByPlayer(game.getPlayer(2)).setLocation(
					(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getX(),
					(int) getCrownByPlayer(game.getPlayer(2)).getLocation().getY() + reset);
			getCrownByPlayer(game.getPlayer(3)).setLocation(
					(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getX(),
					(int) getCrownByPlayer(game.getPlayer(3)).getLocation().getY() + reset);
		}
		draftNumber.setText(
				"Draft number " + (KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size()));
		if (gameplay.getGamestatus() == Gamestatus.Finalizing) {
			HashMap<Player, Integer> map = KingdominoController.calculateRanking();
			HashMap<Integer, Player> newMap = new HashMap<Integer, Player>();
			for (int i = 0; i < 4; i++) {
				newMap.put(map.get(game.getPlayer(i)), game.getPlayer(i));
			}
			ranking1.setText("#1 : " + newMap.get(1).getColor());
			ranking2.setText("#2 : " + newMap.get(2).getColor());
			ranking3.setText("#3 : " + newMap.get(3).getColor());
			ranking4.setText("#4 : " + newMap.get(4).getColor());
			getContentPane().add(txtRanking);
			getContentPane().add(ranking1);
			getContentPane().add(ranking2);
			getContentPane().add(ranking3);
			getContentPane().add(ranking4);
			newMap.get(1).getUser().setWonGames(newMap.get(1).getUser().getWonGames() + 1);
			newMap.get(1).getUser().setPlayedGames(newMap.get(1).getUser().getPlayedGames() + 1);
			newMap.get(2).getUser().setPlayedGames(newMap.get(2).getUser().getPlayedGames() + 1);
			newMap.get(3).getUser().setPlayedGames(newMap.get(3).getUser().getPlayedGames() + 1);
			newMap.get(4).getUser().setPlayedGames(newMap.get(4).getUser().getPlayedGames() + 1);
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

	private JLabel getCrownByPlayer(Player player) {
		if (player.getColor() == PlayerColor.Blue)
			return blueCrown;
		else if (player.getColor() == PlayerColor.Yellow)
			return yellowCrown;
		else if (player.getColor() == PlayerColor.Green)
			return greenCrown;
		else
			return pinkCrown;
	}
}