package ca.mcgill.ecse223.kingdomino.view;

import java.awt.Dimension;
import java.util.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import ca.mcgill.ecse223.kingdomino.model.Domino;

public class BrowsePage extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JButton browseDomino;
	private JButton observeDomino;
	private JButton filterByTerrainType;
	private JButton backButton;
	private JTextField entry;
	private JLabel result;
	private JLabel lblKingdomino;

	public BrowsePage() {
		this.setSize(1000, 700);
		initComponent();
	}

	public void initComponent() {
		lblKingdomino = new JLabel("");
		result = new JLabel("");
		result.setSize(new Dimension(900, 50));
		result.setLocation(100, 100);
		entry = new JTextField();
		entry.setSize(new Dimension(200, 50));
		entry.setLocation(350, 200);
		browseDomino = new JButton("Browse domino");
		browseDomino.setSize(new Dimension(200, 50));
		browseDomino.setLocation(350, 300);
		observeDomino = new JButton("Observe domino");
		observeDomino.setSize(new Dimension(200, 50));
		observeDomino.setLocation(350, 400);
		filterByTerrainType = new JButton("Filter by terrain type");
		filterByTerrainType.setSize(new Dimension(200, 50));
		filterByTerrainType.setLocation(350, 500);
		backButton = new JButton("Back");
		backButton.setSize(new Dimension(100, 50));
		backButton.setLocation(20, 20);
		lblKingdomino.setSize(new Dimension(300, 300));
		lblKingdomino.setLocation(0, 0);
		getContentPane().add(result);
		getContentPane().add(entry);
		getContentPane().add(browseDomino);
		getContentPane().add(observeDomino);
		getContentPane().add(filterByTerrainType);
		getContentPane().add(backButton);
		getContentPane().add(lblKingdomino);
		browseDomino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<Domino> list = KingdominoController.browseAllDomino();
					result.setText("All dominos: ");
					for (Domino domino : list) {
						result.setText(result.getText() + domino.getId() + ",");
					}
				} catch (Exception f) {
					result.setText("Invalid input");
				}
			}
		});
		observeDomino.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Domino domino = KingdominoController.observeDomino(Integer.decode(entry.getText()));
					result.setText("Domino: " + domino.getId() + ", Left terrain type: " + domino.getLeftTile()
							+ ", Right terrain type: " + domino.getRightTile() + ", Left number of crowns: "
							+ domino.getLeftCrown() + ", Right number of crowns: " + domino.getRightCrown()
							+ ", Status: " + domino.getStatus());
				} catch (Exception f) {
					result.setText("Invalid input");
				}
			}
		});
		filterByTerrainType.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					List<Domino> list = KingdominoController.filterByTerrainType(entry.getText());
					result.setText(entry.getText() + ": ");
					for (Domino domino : list) {
						result.setText(result.getText() + domino.getId() + ",");
					}
				} catch (Exception f) {
					result.setText("Invalid input");
				}
			}
		});
		backButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				KingdominoApplication.closeBrowsePage();
			}
		});
	}
}
