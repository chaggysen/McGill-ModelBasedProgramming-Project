package ca.mcgill.ecse223.kingdomino.view;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JButton;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.*;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Game;

import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JSlider;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseEvent;
import javax.swing.GroupLayout.Alignment;

public class KingdominoPlayerOptionsPage extends JFrame {

	private JFrame playerPage;
	private JLabel errorMessage;
	private JLabel errorMessage2;
	private javax.swing.JLabel label;
	private javax.swing.JLabel label1;
	private javax.swing.JLabel label2;
	private javax.swing.JLabel label3;
	private javax.swing.JLabel label4;
	private JRadioButton players2;
	private JRadioButton players3;
	private JRadioButton players4;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JButton bonusButton;
	private JScrollPane sp;
	private JFrame bonusPage;
	private JRadioButton middleKingdom;
	private JRadioButton harmony;
	private JRadioButton both;
	private JRadioButton none;
	private JButton startButton;
	private JButton backButton;
	private String error = null;
	private boolean isUsingMiddleKingdom;
	private boolean isUsingHarmony;

	public void setAllVisible(boolean b) {
		playerPage.setVisible(b);
		bonusPage.setVisible(b);
		this.setVisible(b);
	}

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					KingdominoPlayerOptionsPage window = new KingdominoPlayerOptionsPage();
					window.playerPage.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public KingdominoPlayerOptionsPage() {
		initialize();

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		playerPage = new JFrame();
		playerPage.getContentPane().setBackground(new Color(0, 191, 255));
		playerPage.setBounds(0, 0, 450, 700);
		playerPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		playerPage.getContentPane().setLayout(null);
		playerPage.setVisible(true);

		errorMessage = new JLabel();
		errorMessage.setText("");
		errorMessage.setSize(428, 36);
		errorMessage.setLocation(16, 600);
		errorMessage.setForeground(Color.RED);
		playerPage.getContentPane().add(errorMessage);

		JLabel lblSelectTheNumber = new JLabel("Select the Number of Players", SwingConstants.CENTER);
		lblSelectTheNumber.setBounds(126, 91, 192, 16);
		playerPage.getContentPane().add(lblSelectTheNumber);

		players2 = new JRadioButton("2 Players");
		players2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (players2.isSelected()) {

					players3.setSelected(false);
					players4.setSelected(false);

				}
			}
		});
		players2.setBounds(164, 140, 141, 23);
		playerPage.getContentPane().add(players2);

		players3 = new JRadioButton("3 Players");
		players3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (players3.isSelected()) {
					players2.setSelected(false);
					players4.setSelected(false);

				}
			}
		});
		players3.setBounds(164, 182, 141, 23);
		playerPage.getContentPane().add(players3);

		players4 = new JRadioButton("4 Players");
		players4.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (players4.isSelected()) {
					players2.setSelected(false);
					players3.setSelected(false);
				}
			}
		});
		players4.setBounds(164, 227, 141, 23);
		playerPage.getContentPane().add(players4);

		JLabel label = new JLabel("", JLabel.CENTER);
		label.setBounds(0, 0, 450, 90);
		playerPage.getContentPane().add(label);

		label.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("Title.PNG")).getImage()
				.getScaledInstance(450, 90, Image.SCALE_SMOOTH)));

		textField = new JTextField();
		textField.setBounds(155, 330, 130, 26);
		playerPage.getContentPane().add(textField);
		textField.setColumns(10);

		textField_1 = new JTextField();
		textField_1.setBounds(155, 385, 130, 26);
		playerPage.getContentPane().add(textField_1);
		textField_1.setColumns(10);

		textField_2 = new JTextField();
		textField_2.setBounds(155, 440, 130, 26);
		playerPage.getContentPane().add(textField_2);
		textField_2.setColumns(10);

		textField_3 = new JTextField();
		textField_3.setBounds(155, 495, 130, 26);
		playerPage.getContentPane().add(textField_3);
		textField_3.setColumns(10);

		JLabel label1 = new JLabel("label1");
		label1.setBounds(39, 326, 61, 36);
		playerPage.getContentPane().add(label1);

		JLabel label2 = new JLabel("label2");
		label2.setBounds(39, 386, 61, 36);
		playerPage.getContentPane().add(label2);

		JLabel label3 = new JLabel("label3");
		label3.setBounds(39, 441, 61, 36);
		playerPage.getContentPane().add(label3);

		JLabel label4 = new JLabel("label4");
		label4.setBounds(39, 496, 61, 36);
		playerPage.getContentPane().add(label4);

		label1.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("bluecrown.png")).getImage()
				.getScaledInstance(61, 36, Image.SCALE_SMOOTH)));
		label2.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("yellowcrown.png")).getImage()
				.getScaledInstance(61, 36, Image.SCALE_SMOOTH)));
		label3.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("greencrown.png")).getImage()
				.getScaledInstance(61, 36, Image.SCALE_SMOOTH)));
		label4.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("redcrown.png")).getImage()
				.getScaledInstance(61, 36, Image.SCALE_SMOOTH)));

		bonusButton = new JButton("Bonus Option Page");
		bonusButton.setBounds(161, 568, 144, 29);
		playerPage.getContentPane().add(bonusButton);

		bonusButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				bonusButtonActionPerformed(evt);
			}
		});

		bonusPage = new JFrame();
		bonusPage.getContentPane().setBackground(new Color(0, 191, 255));
		bonusPage.getContentPane().setLayout(null);
		bonusPage.setBounds(0, 0, 450, 700);
		bonusPage.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		bonusPage.setVisible(false);

		errorMessage2 = new JLabel();
		errorMessage2.setText("");
		errorMessage2.setSize(428, 36);
		errorMessage2.setLocation(16, 600);
		errorMessage2.setForeground(Color.RED);
		bonusPage.getContentPane().add(errorMessage2);

		JLabel labelbonus = new JLabel("label");
		labelbonus.setBounds(0, 0, 450, 90);
		bonusPage.getContentPane().add(labelbonus);

		labelbonus.setIcon(new ImageIcon(new javax.swing.ImageIcon(getClass().getResource("Title.PNG")).getImage()
				.getScaledInstance(450, 90, Image.SCALE_SMOOTH)));

		JLabel lblSelectTheNumberbonus = new JLabel("Select the Bonus Option", SwingConstants.CENTER);
		lblSelectTheNumberbonus.setFont(new Font("Lucida Grande", Font.PLAIN, 16));
		lblSelectTheNumberbonus.setBounds(126, 91, 192, 16);
		bonusPage.getContentPane().add(lblSelectTheNumberbonus);

		middleKingdom = new JRadioButton("The Middle Kingdom");
		middleKingdom.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (middleKingdom.isSelected()) {
					harmony.setSelected(false);
					both.setSelected(false);
					none.setSelected(false);

				}

			}
		});
		middleKingdom.setBounds(38, 200, 192, 23);
		bonusPage.getContentPane().add(middleKingdom);

		JTextArea txtrScoreAdditional = new JTextArea();
		txtrScoreAdditional.setBackground(new Color(0, 191, 255));
		txtrScoreAdditional.setText(" score 10 additional points\nif your castle is in the center of your kingdom.");
		txtrScoreAdditional.setBounds(48, 235, 324, 42);
		bonusPage.getContentPane().add(txtrScoreAdditional);

		harmony = new JRadioButton("Harmony");
		harmony.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				if (harmony.isSelected()) {
					middleKingdom.setSelected(false);
					both.setSelected(false);
					none.setSelected(false);
				}
			}
		});
		harmony.setBounds(38, 300, 141, 23);
		bonusPage.getContentPane().add(harmony);

		JTextArea txtrScoreAdditional_1 = new JTextArea();
		txtrScoreAdditional_1.setToolTipText("");
		txtrScoreAdditional_1
				.setText(" score 5 additional points \nif your territory is complete (no discarded dominoes).");
		txtrScoreAdditional_1.setBackground(new Color(0, 191, 255));
		txtrScoreAdditional_1.setBounds(48, 335, 336, 42);
		bonusPage.getContentPane().add(txtrScoreAdditional_1);

		both = new JRadioButton("Both Middle Kingdom and Harmony");
		both.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if (both.isSelected()) {
					middleKingdom.setSelected(false);
					harmony.setSelected(false);
					none.setSelected(false);
				}
			}
		});
		both.setBounds(38, 402, 258, 23);
		bonusPage.getContentPane().add(both);

		none = new JRadioButton("None");
		none.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {

				if (none.isSelected()) {
					middleKingdom.setSelected(false);
					harmony.setSelected(false);
					both.setSelected(false);
				}
			}
		});
		none.setBounds(38, 470, 141, 23);
		bonusPage.getContentPane().add(none);

		startButton = new JButton("Start game");
		startButton.setBounds(168, 558, 117, 29);
		backButton = new JButton("Back");
		backButton.setBounds(20, 100, 117, 29);
		playerPage.getContentPane().add(backButton);
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoApplication.setWelcomePage();
			}
		});

		bonusPage.getContentPane().add(startButton);

		startButton.addActionListener(new java.awt.event.ActionListener() {
			public void actionPerformed(java.awt.event.ActionEvent evt) {
				startButtonActionPerformed(evt);
			}
		});

	}

	private void bonusButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		if (!(players2.isSelected()) && !(players3.isSelected()) && !(players4.isSelected())) {
			error = "Number of players need to go to the bonus options page";

		} else if (players2.isSelected()) {
			KingdominoController.initializeGame();
			KingdominoController.setPlayerNumber(2);
			if (!KingdominoController.addUsersAndPlayers(KingdominoApplication.getKingdomino().getCurrentGame(),
					textField.getText(), textField_1.getText(), textField_2.getText(), textField_3.getText()))
				error = "Wrong inputs";
		} else if (players3.isSelected()) {
			KingdominoController.initializeGame();
			KingdominoController.setPlayerNumber(3);
			if (!KingdominoController.addUsersAndPlayers(KingdominoApplication.getKingdomino().getCurrentGame(),
					textField.getText(), textField_1.getText(), textField_2.getText(), textField_3.getText()))
				error = "Wrong inputs";
		} else {
			KingdominoController.initializeGame();
			KingdominoController.setPlayerNumber(4);
			if (!KingdominoController.addUsersAndPlayers(KingdominoApplication.getKingdomino().getCurrentGame(),
					textField.getText(), textField_1.getText(), textField_2.getText(), textField_3.getText()))
				error = "Wrong inputs";
		}
		if (error.length() == 0) {

			playerPage.setVisible(false);
			bonusPage.setVisible(true);

		}
		errorMessage.setText(error);
	}

	private void startButtonActionPerformed(java.awt.event.ActionEvent evt) {
		error = "";
		isUsingMiddleKingdom = false;
		isUsingHarmony = false;
		if (!(middleKingdom.isSelected()) && !(harmony.isSelected()) && !(both.isSelected()) && !(none.isSelected())) {
			error = "Bonus option need to be selected to start the game";

		}
		if (error.length() == 0) {

			if (middleKingdom.isSelected()) {
				isUsingMiddleKingdom = true;
				KingdominoController.initializeGame();
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			} else if (harmony.isSelected()) {
				isUsingHarmony = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);

			} else if (both.isSelected()) {
				isUsingMiddleKingdom = true;
				isUsingHarmony = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);

			} else if (none.isSelected()) {
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);

			}

			if (middleKingdom.isSelected()) {
				isUsingMiddleKingdom = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			} else if (harmony.isSelected()) {
				isUsingHarmony = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			} else if (both.isSelected()) {
				isUsingMiddleKingdom = true;
				isUsingHarmony = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			} else if (none.isSelected()) {
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);

			}

			if (middleKingdom.isSelected()) {
				isUsingMiddleKingdom = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			} else if (harmony.isSelected()) {
				isUsingHarmony = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			} else if (both.isSelected()) {
				isUsingMiddleKingdom = true;
				isUsingHarmony = true;
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			} else if (none.isSelected()) {
				KingdominoController.setBonusOptions(isUsingHarmony, isUsingMiddleKingdom);
			}

		}
		errorMessage2.setText(error);
		if (error.length() == 0)
			KingdominoApplication.setMainPage();
	}

}