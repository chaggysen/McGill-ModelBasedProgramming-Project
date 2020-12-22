package ca.mcgill.ecse223.kingdomino.controller;

import ca.mcgill.ecse223.kingdomino.model.*;
import ca.mcgill.ecse223.kingdomino.model.Domino.DominoStatus;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.model.Draft.DraftStatus;
import ca.mcgill.ecse223.kingdomino.model.Player.PlayerColor;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.*;
import java.awt.*;
import java.util.List;
import java.io.*;
import java.util.*;

public class KingdominoController {

	// Helper methods
	private static void createAllDominoes(Game game) {
		try {
			BufferedReader br = new BufferedReader(new FileReader("src/main/resources/alldominoes.dat"));
			String line = "";
			String delimiters = "[:\\+()]";
			while ((line = br.readLine()) != null) {
				String[] dominoString = line.split(delimiters); // {id, leftTerrain, rightTerrain, crowns}
				int dominoId = Integer.decode(dominoString[0]);
				TerrainType leftTerrain = getTerrainType(dominoString[1]);
				TerrainType rightTerrain = getTerrainType(dominoString[2]);
				int numCrown = 0;
				if (dominoString.length > 3) {
					numCrown = Integer.decode(dominoString[3]);
				}
				new Domino(dominoId, leftTerrain, rightTerrain, numCrown, game);
			}
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
			throw new java.lang.IllegalArgumentException(
					"Error occured while trying to read alldominoes.dat: " + e.getMessage());
		}
	}

	private static TerrainType getTerrainType(String terrain) {

		switch (terrain) {
		case "W":
			return TerrainType.WheatField;
		case "F":
			return TerrainType.Forest;
		case "M":
			return TerrainType.Mountain;
		case "G":
			return TerrainType.Grass;
		case "S":
			return TerrainType.Swamp;
		case "L":
			return TerrainType.Lake;
		case "wheat":
			return TerrainType.WheatField;
		case "forest":
			return TerrainType.Forest;
		case "mountain":
			return TerrainType.Mountain;
		case "grass":
			return TerrainType.Grass;
		case "swamp":
			return TerrainType.Swamp;
		case "lake":
			return TerrainType.Lake;
		default:
			throw new java.lang.IllegalArgumentException("Invalid terrain type: " + terrain);
		}
	}

	private static void addDefaultUsersAndPlayers(Game game) {
		ArrayList<String> userNames = new ArrayList<String>();
		if (game.getNumberOfPlayers() == 2) {
			userNames.add("User 1");
			userNames.add("User 2");
			game.getKingdomino().addUser(userNames.get(0));
			game.getKingdomino().addUser(userNames.get(1));
		} else if (game.getNumberOfPlayers() == 3) {
			userNames.add("User 1");
			userNames.add("User 2");
			userNames.add("User 3");
			game.getKingdomino().addUser(userNames.get(0));
			game.getKingdomino().addUser(userNames.get(1));
			game.getKingdomino().addUser(userNames.get(2));
		} else {
			userNames.add("User 1");
			userNames.add("User 2");
			userNames.add("User 3");
			userNames.add("User 4");
			game.getKingdomino().addUser(userNames.get(0));
			game.getKingdomino().addUser(userNames.get(1));
			game.getKingdomino().addUser(userNames.get(2));
			game.getKingdomino().addUser(userNames.get(3));
		}

		for (int i = 0; i < userNames.size(); i++) {
			User user = getUserByName(userNames.get(i));
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
		}
	}

	public static boolean addUsersAndPlayers(Game game, String User1, String User2, String User3, String User4) {
		ArrayList<String> userNames = new ArrayList<String>();
		userNames.add(User1);
		userNames.add(User2);
		userNames.add(User3);
		userNames.add(User4);
		try {
			KingdominoController.addNewUser(User1);
			KingdominoController.addNewUser(User2);
			KingdominoController.addNewUser(User3);
			KingdominoController.addNewUser(User4);
		} catch (InvalidInputException e) {
			return false;
		}

		for (int i = 0; i < userNames.size(); i++) {
			User user = getUserByName(userNames.get(i));
			Player player = new Player(game);
			player.setUser(user);
			player.setColor(PlayerColor.values()[i]);
			Kingdom kingdom = new Kingdom(player);
			new Castle(0, 0, kingdom, player);
		}
		return true;
	}

	private static User getUserByName(String name) {
		for (User user : KingdominoApplication.getKingdomino().getUsers()) {
			if (user.getName().equals(name))
				return user;
		}
		return null;
	}

	private static DirectionKind getDirection(String dir) {
		switch (dir) {
		case "up":
			return DirectionKind.Up;
		case "down":
			return DirectionKind.Down;
		case "left":
			return DirectionKind.Left;
		case "right":
			return DirectionKind.Right;
		default:
			throw new java.lang.IllegalArgumentException("Invalid direction: " + dir);
		}
	}

	private static DominoStatus getDominoStatus(String status) {
		switch (status) {
		case "inPile":
			return DominoStatus.InPile;
		case "excluded":
			return DominoStatus.Excluded;
		case "inCurrentDraft":
			return DominoStatus.InCurrentDraft;
		case "inNextDraft":
			return DominoStatus.InNextDraft;
		case "erroneouslyPreplaced":
			return DominoStatus.ErroneouslyPreplaced;
		case "correctlyPreplaced":
			return DominoStatus.CorrectlyPreplaced;
		case "placedInKingdom":
			return DominoStatus.PlacedInKingdom;
		case "discarded":
			return DominoStatus.Discarded;
		default:
			throw new java.lang.IllegalArgumentException("Invalid domino status: " + status);
		}
	}

	public static Domino getdominoByID(int id) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Domino domino : game.getAllDominos()) {
			if (domino.getId() == id) {
				return domino;
			}
		}
		throw new java.lang.IllegalArgumentException("Domino with ID " + id + " not found.");
	}

	public static Player getPlayerByPlayerColor(PlayerColor color) {
		Game game = KingdominoApplication.getKingdomino().getCurrentGame();
		for (Player player : game.getPlayers()) {
			if (player.getColor() == color)
				return player;
		}
		return null;
	}

	public static Color getColorByPlayerColor(PlayerColor color) {
		if (color == PlayerColor.Blue)
			return Color.BLUE;
		else if (color == PlayerColor.Yellow)
			return Color.YELLOW;
		else if (color == PlayerColor.Green)
			return Color.YELLOW;
		else
			return Color.PINK;
	}

	/**
	 * !!!!!!!! F#1: Set game options: As a player, I want to configure the
	 * designated options of the Kingdomino game including the number of players (2,
	 * 3 or 4) and the bonus scoring options.
	 * 
	 * @author DANMO YANG
	 */
	public static void setGameOptions() {
		System.out.println("Setting game options");
	}

	// Set game options
	public static void setPlayerNumber(int numplayer) {
		try {
			Kingdomino kingDomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingDomino.getCurrentGame();
		} catch (Exception e) {

		}
		Kingdomino kd = KingdominoApplication.getKingdomino();
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		if (numplayer >= 2 && numplayer <= Game.maximumNumberOfPlayers()) {
			currentGame.setNumberOfPlayers(numplayer);
		}
	}

	// Set game options
	public static void setBonusOptions(boolean isUsingHarmony, boolean isUsingMiddleKingdom) {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		Game game = kd.getCurrentGame();
		BonusOption bonusHarmony = getBonusOptionByName("Harmony");
		BonusOption bonusMK = getBonusOptionByName("Middle Kingdom");
		if (isUsingHarmony) {
			game.addSelectedBonusOption(bonusHarmony);
		} else if (!isUsingHarmony)
			game.removeSelectedBonusOption(bonusHarmony);
		if (isUsingMiddleKingdom)
			game.addSelectedBonusOption(bonusMK);
		else if (!isUsingMiddleKingdom)
			game.removeSelectedBonusOption(bonusMK);
	}

	// Helper
	public static BonusOption getBonusOptionByName(String name) {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		for (int i = 0; i < kd.getBonusOptions().size(); i++) {
			if (kd.getBonusOption(i).getOptionName().equals(name))
				return kd.getBonusOption(i);
		}
		return null;
	}

	/**
	 * F#2: Provide user profile: As a player, I wish to use my unique user name in
	 * when a game starts. I also want the Kingdomino app to maintain my game
	 * statistics (e.g. number of games played, won, etc.).
	 *
	 * 
	 * 
	 * This method creates a new user in the Kingdomino
	 * 
	 * 
	 * 
	 * @author Haipeng Yue
	 * 
	 * @param name The unique name of the game
	 * 
	 * @throws InvalidInputException If the user is not created
	 * @return a boolean if a user is successfully added to the kingdomino
	 */
	public static boolean addNewUser(String username) throws InvalidInputException {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		boolean check = false;
		if (!username.equals(null) && !username.contains(".") && !username.contains("()")) {
			try {
				User auser = kd.addUser(username);
				if (auser != null) {
					check = true;
					return check;
				}

			} catch (RuntimeException e) {

				throw new InvalidInputException("Createtheuser failed");

			}
		}
		return check;
	}

	/**
	 * 
	 * This method get a user by its name
	 * 
	 * 
	 * @author Haipeng Yue
	 * @param String string
	 * @return a User have the user name as the parameter
	 */
	public static User getUsers(String string) {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		List<User> users = kd.getUsers();
		User reuser = null;
		if (users.isEmpty() != true) {
			users.get(0);
			reuser = User.getWithName(string);
		}

		return reuser;

	}

	/**
	 * 
	 * This method get the game played by a user by the User
	 * 
	 * 
	 * 
	 * @author Haipeng Yue
	 * @param User user
	 * @return an integer indicates the number of game play by the user
	 */

	public static Integer getPlayed(User user) {
		return user.getPlayedGames();
	}

	/**
	 * 
	 * This method get the game won by a user by the User
	 * 
	 * 
	 * 
	 * @author Haipeng Yue
	 * @param User user
	 * @return an integer indicates the number of game won by the user
	 */

	public static Integer getWon(User user) {
		return user.getWonGames();
	}

	/**
	 * This method Sort a list of users by their names
	 * 
	 * 
	 * @author Haipeng Yue
	 * @param List<User> users
	 * @return an sorted ArrayList of String of user names
	 */
	public static ArrayList<String> listAllUsers(List<User> users) {
		ArrayList<String> unsorted = new ArrayList<String>();
		if (users.isEmpty() != true) {
			for (int i = 0; i < users.size(); i++) {
				unsorted.add(users.get(i).getName());
			}
			Collections.sort(unsorted);

		}
		return unsorted;

	}

	/**
	 * F#3: Start a new game: As a Kingdomino player, I want to start a new game of
	 * Kingdomino against some opponents with my castle placed on my territory with
	 * the current settings of the game. The initial order of player should be
	 * randomly determined.
	 * 
	 * @author Noah Chamberland
	 */
	public static void startNewGame() {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		Game game = kd.getCurrentGame();
		// Populate game
		if (game.getKingdomino().getUsers().size() == 0)
			addDefaultUsersAndPlayers(game);
		createAllDominoes(game);
		KingdominoApplication.setKingdomino(kd);
		KingdominoController.shuffleDomino();
		KingdominoController.orderAndRevealNextDraft();
		Random rand = new Random();
		game.setNextPlayer(game.getPlayer(rand.nextInt(game.getNumberOfPlayers() - 1)));
	}

	public static void initializeGame() {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		Game game = new Game(48, kd);
		kd.setCurrentGame(game);
		new BonusOption("Harmony", kd);
		new BonusOption("Middle Kingdom", kd);
	}

	/**
	 * !!!!!!!!!! F#4: Browse domino pile: As a player, I wish to browse the set of
	 * all dominos in increasing order of numbers prior to playing the game so that
	 * I can adjust my strategy.
	 * 
	 * @author DANMO YANG
	 */
	public static List<Domino> browseAllDomino() {

		Kingdomino kD = KingdominoApplication.getKingdomino();
		Game currentGame = kD.getCurrentGame();

		return currentGame.getAllDominos();
	}

	/**
	 * Observe domino
	 * 
	 * @param index
	 */
	public static Domino observeDomino(int index) {

		return getdominoByID(index);
	}

	/**
	 * Filter by terrain type
	 * 
	 * @param terrain
	 * @return List of integers
	 */
	public static List<Domino> filterByTerrainType(String terrain) {

		List<Domino> allDomino = browseAllDomino();
		ArrayList<Domino> dominoOfTerrain = new ArrayList<Domino>();

		for (Domino domino : allDomino) {
			if (domino.getLeftTile() == getTerrainType(terrain) || domino.getRightTile() == getTerrainType(terrain)) {
				dominoOfTerrain.add(domino);
			}
		}

		return dominoOfTerrain;

	}

	/**
	 * F#5: Shuffle domino pile: As a player, I want to play have a randomly
	 * shuffled pile of domino so that every game becomes unique
	 * 
	 * @author Wang Sen
	 * @return A boolean. True if the pile has been shuffled, False if it isn't
	 *         shuffled
	 * @throws Exception if the current game isn't initialized
	 **/
	public static boolean shuffleDomino() {

		// Exception Catching: Check if there is a current Game
		try {
			Kingdomino kingDomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingDomino.getCurrentGame();
		} catch (Exception e) {
			throw e;
		}

		// Create new firstDraft and set it as current draft
		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		// Shuffle dominos and update the linked list
		ArrayList<Domino> shuffledDominos = new ArrayList<Domino>();

		if (shuffledDominos.addAll(currentGame.getAllDominos())) {
			Collections.shuffle(shuffledDominos);
		}

		Domino currentDomino = null;
		currentGame.setTopDominoInPile(shuffledDominos.get(0));
		currentDomino = currentGame.getTopDominoInPile().getNextDomino();
		for (int i = 1; i < shuffledDominos.size() - 1; i++) {
			currentDomino = shuffledDominos.get(i);
			currentDomino.setNextDomino(shuffledDominos.get(i + 1));
			currentDomino.setPrevDomino(shuffledDominos.get(i - 1));
		}

		// Create shallow copy of the dominos Lists

		// shallow copy for 4 players

		ArrayList<Domino> shuffledDominos4players = new ArrayList<Domino>(shuffledDominos);

		// shallow copy for 2 players
		ArrayList<Domino> shuffledDominos2players = new ArrayList<Domino>();
		for (int i = 0; i < 24; i++) {
			shuffledDominos2players.add(currentGame.getAllDominos().get(i));
		}
		// shallow copy for 3 players
		ArrayList<Domino> shuffledDominos3players = new ArrayList<Domino>();
		for (int i = 0; i < 36; i++) {
			shuffledDominos3players.add(currentGame.getAllDominos().get(i));
		}

		// change all the dominos status to InPile
		for (int i = 0; i < shuffledDominos4players.size(); i++) {
			shuffledDominos4players.get(i).setStatus(DominoStatus.InPile);
		}

		KingdominoController.createNextDraftOfDominoes(1);

		return true;
	}

	/**
	 * F#6: Load game: As a player, I want to load a previously played game so that
	 * I can continue it from the last position.
	 * 
	 * @author Noah Chamberland
	 * @param name
	 * @throws IOException
	 */
	public static void loadGame(String name) throws IOException {
		File loadFile = new File(name);

		if (loadFile.isFile() && loadFile.canRead()) {
			Kingdomino kd = KingdominoApplication.getKingdomino();
			ArrayList<String> dataLines = new ArrayList<String>();
			try {
				BufferedReader rd = new BufferedReader(new FileReader(name));
				String dataString = rd.readLine();
				while (dataString != null) {
					dataLines.add(dataString);
					dataString = rd.readLine();
				}
				rd.close();
			} catch (Exception e) {
				throw new IOException("Invalid File");
			}

			Game currentGame = null;
			if (dataLines.size() == 4) { // 2 players
				currentGame = new Game(24, kd);
				currentGame.setNumberOfPlayers(2);
			} else if (dataLines.size() == 5) { // 3 players
				currentGame = new Game(36, kd);
				currentGame.setNumberOfPlayers(3);
			} else { // 4 players
				currentGame = new Game(48, kd);
				currentGame.setNumberOfPlayers(4);
			}

			kd.setCurrentGame(currentGame);
			addDefaultUsersAndPlayers(currentGame);
			createAllDominoes(currentGame);
			shuffleDomino();
			Draft draft = new Draft(DraftStatus.FaceUp, currentGame);
			Draft nextDraft = new Draft(DraftStatus.FaceUp, currentGame);
			currentGame.setCurrentDraft(draft);
			currentGame.setNextDraft(nextDraft);
			ArrayList<Integer> list = new ArrayList<Integer>();
			ArrayList<Domino> dominosLeft = new ArrayList<Domino>(currentGame.getAllDominos());
			int numUnclaimed = 0;
			int numInBoard = 0;

			for (String line : dataLines) {
				if (line.charAt(0) == 'C') {
					// Claimed
					String concat = line.substring(2);
					String[] arr = concat.split(",");
					for (int i = 0; i < arr.length; i++) {
						arr[i] = arr[i].trim();
					}

					for (int i = 0; i < arr.length; i++) {
						// Give Domino to corresponding Player
						int value = Integer.decode(arr[i]);
						Domino currentDomino = getdominoByID(value);
						Player currentPlayer = currentGame.getPlayer(i);
						currentDomino.setStatus(DominoStatus.InCurrentDraft);
						new DominoSelection(currentPlayer, currentDomino, draft);
						draft.addIdSortedDomino(currentDomino);
						dominosLeft.remove(currentDomino);
					}
				}

				if (line.charAt(0) == 'U') {
					// Unclaimed
					String concat = line.substring(2);
					String[] arr = concat.split(",");
					for (int i = 0; i < arr.length; i++) {
						arr[i] = arr[i].trim();
					}
					for (int i = 0; i < arr.length; i++) {
						// Unclaim the domino
						if (arr[i].equals(""))
							break;
						int value = Integer.decode(arr[i]);
						Domino current = getdominoByID(value);
						current.setStatus(DominoStatus.InNextDraft);
						numUnclaimed++;
						draft.addIdSortedDomino(current);
					}
				}

				if (line.charAt(0) == 'P') {
					// Player P has dominos on the grid
					int numPlayer = line.charAt(1) - 48;
					Player player = currentGame.getPlayer(numPlayer - 1);
					String concat = line.substring(3);
					String arr[] = concat.split("\\)");

					// Fix inconsistencies
					for (int i = 0; i < arr.length; i++) {
						if (!arr[i].equals(",")) {
							if (i == 0) {
								arr[i] = arr[i].substring(1);
							} else {
								arr[i] = arr[i].substring(2);
							}
						}
					}

					// Separate to get the appropriate values
					for (int i = 0; i < arr.length; i++) {
						if (!arr[i].equals(",")) {
							int index = 0;
							int value = 0;
							while (arr[i].charAt(index) != '@') {
								index++;
							}

							if (index == 1) { // Single digit
								value = arr[i].charAt(0) - 48;
								arr[i] = arr[i].substring(3);
							}

							else { // Double digit
								value = Integer.decode(arr[i].substring(0, 2));
								arr[i] = arr[i].substring(4);
							}

							Domino curDomino = getdominoByID(value);
							dominosLeft.remove(curDomino);
							String[] pos = arr[i].split(",");
							int posx = Integer.decode(pos[0]);
							int posy = Integer.decode(pos[1]);
							String direction = pos[2].trim();
							DominoInKingdom.DirectionKind kind = null;

							switch (direction) {
							case "U":
								kind = DominoInKingdom.DirectionKind.Up;
								break;
							case "D":
								kind = DominoInKingdom.DirectionKind.Down;
								break;
							case "L":
								kind = DominoInKingdom.DirectionKind.Left;
								break;
							case "R":
								kind = DominoInKingdom.DirectionKind.Right;
								break;
							default:
								throw new IllegalArgumentException("Invalid File");
							}
							try {
								if (!placeDomino(player, curDomino, posx, posy, kind, true))
									throw new IllegalArgumentException("Invalid File");
								numInBoard++;
							} catch (Exception e) {
								throw new IllegalArgumentException("Invalid File");
							}
						}
					}
				}
			}
			Domino cur = currentGame.getTopDominoInPile();
			while (list.size() != 4) {
				if (dominosLeft.contains(cur))
					list.add(cur.getId());
				cur = cur.getNextDomino();
			}

			currentGame.setTopDominoInPile(cur);
			Collections.sort(list);
			for (int i = 0; i < 4; i++) {
				nextDraft.addIdSortedDomino(getdominoByID(list.get(i)));
				getdominoByID(list.get(i)).setStatus(DominoStatus.InNextDraft);
			}

			for (int i = 0; i < numInBoard / currentGame.getNumberOfPlayers() - 1; i++) {
				new Draft(DraftStatus.FaceUp, currentGame);
			}

			if (numInBoard % currentGame.getNumberOfPlayers() == 0 && numUnclaimed != currentGame.getNumberOfPlayers()
					&& numUnclaimed != 0)
				throw new IllegalArgumentException("Invalid File");
			int num3, num2, num1, num0;

			if (numUnclaimed != currentGame.getNumberOfPlayers()) {
				num3 = currentGame.getPlayer(3).getKingdom().getTerritories().size();
				num2 = currentGame.getPlayer(2).getKingdom().getTerritories().size();
				num1 = currentGame.getPlayer(1).getKingdom().getTerritories().size();
				num0 = currentGame.getPlayer(0).getKingdom().getTerritories().size();
			} else {
				num3 = currentGame.getPlayer(3).getDominoSelection().getDomino().getId();
				num2 = currentGame.getPlayer(2).getDominoSelection().getDomino().getId();
				num1 = currentGame.getPlayer(1).getDominoSelection().getDomino().getId();
				num0 = currentGame.getPlayer(0).getDominoSelection().getDomino().getId();
			}

			if (num3 < num2 && num3 < num1 && num3 < num0) {
				currentGame.setNextPlayer(currentGame.getPlayer(3));
			} else if (num2 < num3 && num2 < num1 && num2 < num0) {
				currentGame.setNextPlayer(currentGame.getPlayer(2));
			} else if (num1 < num3 && num1 < num2 && num1 < num0) {
				currentGame.setNextPlayer(currentGame.getPlayer(1));
			} else {
				currentGame.setNextPlayer(currentGame.getPlayer(0));
			}
		} else
			throw new IOException("Invalid File");
	}

	/**
	 * F#7: Save game: As a player, I want to save the current game if the game has
	 * not yet been finished so that I can continue it later
	 * 
	 * @author Wang Sen
	 * @return A boolean. True if the game has been saved. False if it hasn't been
	 *         saved
	 * @throws NullPointerException if the current player doesn't have any
	 *                              dominoSelection yet
	 * @throws NullPointerException if the current player doesn't have any
	 *                              territories in his or her kingdom yet
	 **/
	public static boolean saveGame(String name) {

		// Get currentGame and create file path
		String fileName = name;
		String path = "./src/test/resources/" + fileName;
		Kingdomino kingDomino = KingdominoApplication.getKingdomino();
		Game currentGame = kingDomino.getCurrentGame();

		// Delete the old file and create a new one
		File f_old = new File("./src/test/resources/" + fileName);
		f_old.delete();
		File f_new = new File("./src/test/resources/" + fileName);

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(f_new, false));

			bw.write("C:");

			// claimed domino
			try {
				for (int i = 0; i < currentGame.getPlayers().size(); i++) {
					bw.write(" " + currentGame.getPlayers().get(i).getDominoSelection().getDomino().getId() + ",");
				}
			} catch (NullPointerException e) {
				// The player does not have claimed domino
			}

			bw.newLine();

			bw.write("U:");

			// Unclaimed domino

			try {
				for (int i = 0; i < currentGame.getCurrentDraft().getSelections().size(); i++) {
					if (currentGame.getCurrentDraft().getSelections().get(i).getDomino()
							.hasDominoSelection() == false) {
						bw.write(" " + currentGame.getCurrentDraft().getSelections().get(i).getDomino().getId());
					}
				}
			} catch (NullPointerException e) {
				// The current draft does not have unclaimed domino
			}

			// Players domino
			bw.newLine();
			try {
				for (int i = 0; i < currentGame.getNumberOfPlayers(); i++) {
					bw.write("P" + (i + 1) + ":");
					for (int j = 1; j < currentGame.getPlayers().get(i).getKingdom().getTerritories().size(); j++) {

						DominoInKingdom dominoInKingdom = (DominoInKingdom) currentGame.getPlayers().get(i).getKingdom()
								.getTerritories().get(j);

						String direction = null;

						switch (dominoInKingdom.getDirection()) {

						case Up:
							direction = "U";
							break;
						case Down:
							direction = "D";
							break;
						case Right:
							direction = "R";
							break;
						case Left:
							direction = "L";
							break;
						}
						bw.write(" " + dominoInKingdom.getDomino().getId() + "@(" + dominoInKingdom.getX() + ","
								+ dominoInKingdom.getY() + "," + direction + "),");
					}
					bw.newLine();
				}

			} catch (NullPointerException e) {
				// The player does not have domino
			}
			bw.close();
		} catch (Exception e) {
			// Counldn't save the game
			return false;
		}
		return true;
	}

	/**
	 * F#8: Create Next Draft of Dominoes. As a player, I want the Kingdomino app to
	 * automatically provide the next four dominos once the previous round is
	 * finished
	 * 
	 * @author Noah Chamberland
	 * @param draftNum
	 * @return true if draft is created, false if draft is not created
	 */
	public static boolean createNextDraftOfDominoes(int draftNum) {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		Game game = kd.getCurrentGame();

		if (draftNum != 1) {
			game.setCurrentDraft(game.getNextDraft());
		}

		if (draftNum > 12) {

			game.setNextDraft(null);
			return false;
		}

		ArrayList<Domino> dominos = new ArrayList<Domino>();
		Domino current = kd.getCurrentGame().getTopDominoInPile();

		for (int i = 0; i < kd.getCurrentGame().getNumberOfPlayers(); i++) {
			dominos.add(current);
			dominos.get(i).setStatus(DominoStatus.InNextDraft);
			current = current.getNextDomino();
		}

		game.setTopDominoInPile(current);
		Draft draft = new Draft(DraftStatus.FaceDown, kd.getCurrentGame());

		game.setNextDraft(draft);

		for (int i = 0; i < dominos.size(); i++) {
			draft.addIdSortedDomino(dominos.get(i));
		}

		return true;
	}

	/**
	 * F#9: Order and Reveal Next Draft of Dominoes: As a player, I want the
	 * Kingdomino app to automatically order and reveal the next draft of dominos in
	 * increasing order with respect to their numbers so that I know which are the
	 * more valuable dominos.
	 * 
	 * @author John Park
	 * @return True if game is initialized, false if not
	 */
	public static boolean orderAndRevealNextDraft() {
		try {
			Kingdomino kingDomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingDomino.getCurrentGame();
		} catch (Exception e) {
			return false;
		}

		Game currentGame = KingdominoApplication.getKingdomino().getCurrentGame();

		if (currentGame.getNextDraft() == null) {
			System.out.println("No more drafts");
			return false;
		}

		if (currentGame.getNextDraft().getDraftStatus().equals(DraftStatus.Sorted)) {
			currentGame.getNextDraft().setDraftStatus(DraftStatus.FaceUp);
		} else {
			Draft nextDraft = currentGame.getNextDraft();
			ArrayList<Integer> arrayNumbers = new ArrayList<Integer>();
			for (int i = 0; i < 4; i++) {
				arrayNumbers.add(nextDraft.getIdSortedDominos().get(0).getId());
				nextDraft.removeIdSortedDomino(nextDraft.getIdSortedDominos().get(0));
			}

			Collections.sort(arrayNumbers);

			// nextDraft.setDraftStatus(DraftStatus.FaceUp);
			for (int i = 0; i < arrayNumbers.size(); i++) {
				nextDraft.addIdSortedDomino(getdominoByID(arrayNumbers.get(i)));
			}
			nextDraft.setDraftStatus(DraftStatus.Sorted);
			if (currentGame.getAllDrafts().size() == 1)
				currentGame.setCurrentDraft(nextDraft);
			return true;
		}
		return false;
	}

	public static boolean chooseNextDomino(int value) {
		Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
		DominoSelection selection = null;
		Draft draft = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft();

		if (player.getDominoSelection() == null) {
			if (draft.getIdSortedDominos().contains(getdominoByID(value))
					&& getdominoByID(value).getDominoSelection() == null) {
				selection = new DominoSelection(player, getdominoByID(value),
						KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft());
			} else {
				return false;
			}
		} else {
			if (draft.getIdSortedDominos().contains(getdominoByID(value))
					&& getdominoByID(value).getDominoSelection() == null) {
				selection = player.getDominoSelection();
				selection.setDomino(getdominoByID(value));
				selection.setDraft(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft());
			} else {
				return false;
			}
		}
		if (KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size() == 1) {
			Player nextPlayer = findNextPlayerForFirstDraft(player, draft);
			if (nextPlayer != null) {
				KingdominoApplication.getKingdomino().getCurrentGame().setNextPlayer(nextPlayer);
			} else {
				Domino current = draft.getIdSortedDomino(0);
				nextPlayer = current.getDominoSelection().getPlayer();
				KingdominoApplication.getKingdomino().getCurrentGame().setNextPlayer(nextPlayer);
			}
		} else {
			if (allDominosInDraftAreSelected(draft)) {
				Domino current = draft.getIdSortedDomino(0);
				Player newPlayer = current.getDominoSelection().getPlayer();
				KingdominoApplication.getKingdomino().getCurrentGame().setNextPlayer(newPlayer);
			} else {
				Domino current = getNextDominoInDraft(
						KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft());
				Player nextPlayer = current.getDominoSelection().getPlayer();
				KingdominoApplication.getKingdomino().getCurrentGame().setNextPlayer(nextPlayer);
			}
		}
		return true;
	}

	// Helper for choose next domino
	public static boolean allDominosInDraftAreSelected(Draft draft) {
		boolean result = true;
		for (int i = 0; i < draft.getIdSortedDominos().size(); i++) {
			if (!draft.getIdSortedDomino(i).hasDominoSelection())
				result = false;
		}
		return result;
	}

	// Helper for choose next domino
	public static Domino getNextDominoInDraft(Draft draft) {
		for (int i = 0; i < draft.getIdSortedDominos().size(); i++) {
			if (draft.getIdSortedDomino(i).getStatus() != DominoStatus.PlacedInKingdom
					&& draft.getIdSortedDomino(i).getStatus() != DominoStatus.CorrectlyPreplaced
					&& draft.getIdSortedDomino(i).getStatus() != DominoStatus.Discarded)
				return draft.getIdSortedDomino(i);

		}
		return null;
	}

	// Helper for choose next domino
	public static Player findNextPlayerForFirstDraft(Player curPlayer, Draft draft) {
		ArrayList<Player> remainingPlayers = new ArrayList<Player>();
		Game game = curPlayer.getGame();
		for (int i = 0; i < game.getNumberOfPlayers(); i++) {
			if (game.getPlayer(i).getDominoSelection() == null)
				remainingPlayers.add(game.getPlayer(i));
		}
		if (remainingPlayers.isEmpty())
			return null;
		if (remainingPlayers.size() == 1)
			return remainingPlayers.get(0);
		Random rand = new Random();
		int number = rand.nextInt(remainingPlayers.size() - 1);
		return remainingPlayers.get(number);
	}

	/**
	 * F#11: Move current domino: As a player, I wish to evaluate a provisional
	 * placement of my current domino by moving the domino around into my kingdom
	 * (up, down, left, right).
	 * 
	 * @param aDomino
	 * @param movement
	 * @param player
	 * 
	 * @author Noah Chamberland
	 */
	public static void moveCurrentDomino(DominoInKingdom aDomino, String movement, Player player) {
		switch (movement) {
		case "left":
			aDomino.setX(aDomino.getX() - 1);
			if (!(verifyBoardSize(aDomino)))
				aDomino.setX(aDomino.getX() + 1);
			break;
		case "right":
			aDomino.setX(aDomino.getX() + 1);
			if (!(verifyBoardSize(aDomino)))
				aDomino.setX(aDomino.getX() - 1);
			break;
		case "up":
			aDomino.setY(aDomino.getY() + 1);
			if (!(verifyBoardSize(aDomino)))
				aDomino.setY(aDomino.getY() - 1);
			break;
		case "down":
			aDomino.setY(aDomino.getY() - 1);
			if (!(verifyBoardSize(aDomino)))
				aDomino.setY(aDomino.getY() + 1);
			break;
		default:
			throw new IllegalArgumentException("Invalid movement");
		}
		placeDomino(player, aDomino.getDomino(), aDomino.getX(), aDomino.getY(), aDomino.getDirection(), false);
	}

	// Helper method for move current domino
	private static boolean verifyBoardSize(DominoInKingdom aDomino) {
		if (aDomino.getX() == 5 || aDomino.getY() == 5)
			return false;
		else if (aDomino.getDirection() == DirectionKind.Up) {
			if (aDomino.getY() == 4)
				return false;
		} else if (aDomino.getDirection() == DirectionKind.Down) {
			if (aDomino.getY() == -4)
				return false;
		} else if (aDomino.getDirection() == DirectionKind.Left) {
			if (aDomino.getX() == -4)
				return false;
		} else if (aDomino.getDirection() == DirectionKind.Right) {
			if (aDomino.getX() == 4)
				return false;
		}
		return true;
	}

	// Helper method for move current domino
	public static void initPlacement(Player aPlayer, Domino domino) {
		placeDomino(aPlayer, domino, 0, 0, getDirection("up"), false);
	}

	// Helper method for move current domino
	public static DominoInKingdom getDominoInKingdomByDomino(Domino aDomino, Kingdom kingdom) {
		for (KingdomTerritory territory : kingdom.getTerritories()) {
			if (territory instanceof DominoInKingdom) {
				DominoInKingdom cur = (DominoInKingdom) territory;
				if (cur.getDomino().getId() == aDomino.getId())
					return cur;
			}
		}
		return null;
	}

	/**
	 * F#12: Rotate current domino: As a player, I wish to evaluate a provisional
	 * placement of my current domino in my kingdom by rotating it (clockwise or
	 * counter-clockwise).
	 * 
	 * @author ege karadibak
	 * @author Noah Chamberland
	 * @param p
	 * @param domino
	 * @param rotation
	 */
	public static void rotateCurrentDomino(Player p, Domino domino, String rotation) {

		Kingdom kingdom = p.getKingdom();
		DominoInKingdom dom = null;

		for (int i = 0; i < kingdom.getTerritories().size(); i++) {
			if (kingdom.getTerritory(i) instanceof DominoInKingdom) {
				DominoInKingdom d = (DominoInKingdom) kingdom.getTerritory(i);
				if (d.getDomino().equals(domino))
					dom = d;
			}
		}
		DirectionKind dir = dom.getDirection();

		switch (rotation) {
		case "clockwise":
			if (dir == DominoInKingdom.DirectionKind.Down) {
				dom.setDirection(DominoInKingdom.DirectionKind.Left);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Down);
				}

			} else if (dir == DominoInKingdom.DirectionKind.Up) {
				dom.setDirection(DominoInKingdom.DirectionKind.Right);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Up);
				}
			} else if (dir == DominoInKingdom.DirectionKind.Right) {
				dom.setDirection(DominoInKingdom.DirectionKind.Down);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Right);
				}
			} else if (dir == DominoInKingdom.DirectionKind.Left) {
				dom.setDirection(DominoInKingdom.DirectionKind.Up);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Left);
				}
			}
			break;
		case "counterclockwise":
			if (dir == DominoInKingdom.DirectionKind.Down) {
				dom.setDirection(DominoInKingdom.DirectionKind.Right);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Down);
				}

			} else if (dir == DominoInKingdom.DirectionKind.Up) {
				dom.setDirection(DominoInKingdom.DirectionKind.Left);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Up);
				}
			} else if (dir == DominoInKingdom.DirectionKind.Right) {
				dom.setDirection(DominoInKingdom.DirectionKind.Up);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Right);
				}
			} else if (dir == DominoInKingdom.DirectionKind.Left) {
				dom.setDirection(DominoInKingdom.DirectionKind.Down);
				if (!verifyBoardSize(dom)) {
					dom.setDirection(DirectionKind.Left);
				}
			}
			break;
		}
		KingdominoController.placeDomino(p, dom.getDomino(), dom.getX(), dom.getY(), dom.getDirection(), false);
	}

	/**
	 * F#13: Place domino: As a player, I wish to place my selected domino to my
	 * kingdom. If I am satisfied with its placement, and its current position
	 * respects the adjacency rules, I wish to finalize the placement. (Actual
	 * checks of adjacency conditions are implemented as separate features)
	 * 
	 * @author Noah Chamberland
	 * @param aPlayer
	 * @param aDomino
	 * @param x
	 * @param y
	 * @param direction
	 * @param satisfied
	 * @return true if can place domino, false if cannot place domino
	 */
	public static boolean placeDomino(Player aPlayer, Domino aDomino, int x, int y,
			DominoInKingdom.DirectionKind direction, boolean satisfied) {
		Kingdom kingdom = aPlayer.getKingdom();
		DominoInKingdom dominoToPlace = null;
		for (int i = 0; i < kingdom.getTerritories().size(); i++) {
			if (kingdom.getTerritory(i) instanceof DominoInKingdom) {
				DominoInKingdom cur = (DominoInKingdom) kingdom.getTerritory(i);
				if (cur.getDomino() != null && cur.getDomino().equals(aDomino))
					dominoToPlace = cur;
			}
		}
		if (dominoToPlace == null) {
			try {
				dominoToPlace = new DominoInKingdom(x, y, kingdom, aDomino);
			} catch (Exception e) {
				return false;
			}
		}
		dominoToPlace.setX(x);
		dominoToPlace.setY(y);
		dominoToPlace.setDirection(direction);
		if (verifyNoOverlapping(dominoToPlace, kingdom)
				&& (verifyCastleAdjacency(dominoToPlace) || verifyAdjacencyComplete(dominoToPlace, kingdom))
				&& verifyKingdomGridSize(kingdom)) {
			// dominoInKingdom can be placed in kingdom
			aDomino.setStatus(DominoStatus.CorrectlyPreplaced);
			if (satisfied)
				aDomino.setStatus(DominoStatus.PlacedInKingdom);
			return true;
		} else {
			aDomino.setStatus(DominoStatus.ErroneouslyPreplaced);
			return false;
		}
	}

	/**
	 * F#14: Verify castle adjacency: As a player, I want the Kingdomino app to
	 * automatically check if my current domino is placed next to my castle
	 * 
	 * This method check if a domino in the kingdom is placed near the castle or not
	 * 
	 * 
	 * 
	 * @author Haipeng Yue
	 * @param DominoInKingdom dom
	 * @return a boolean true if the domino is place neal the castle else false
	 */
	public static boolean verifyCastleAdjacency(DominoInKingdom dom) {
		Kingdomino kd = KingdominoApplication.getKingdomino();
		Game game = kd.getCurrentGame();
		boolean check = false;
		int x = dom.getX();
		int y = dom.getY();
		DirectionKind dir = dom.getDirection();
		String re = null;
		if (dir == DirectionKind.Up) {
			if ((x == -1 && y == 0) || (x == 1 && y == -1) || (x == -1 && y == -1) || (x == 1 && y == 0)
					|| (x == 0 && y == 1) || (x == 0 && y == -2)) {
				check = true;
			}
		} else if (dir == DirectionKind.Right) {
			if ((x == 1 && y == 0) || (x == -1 && y == -1) || (x == -2 && y == 0) || (x == 0 && y == 1)
					|| (x == 0 && y == -1) || (x == -1 && y == 1)) {
				check = true;
			}

		} else if (dir == DirectionKind.Left) {
			if ((x == -1 && y == 0) || (x == 1 && y == -1) || (x == 2 && y == 0) || (x == 0 && y == -1)
					|| (x == 0 && y == 1) || (x == 1 && y == 1)) {
				check = true;
			}
		} else if (dir == DirectionKind.Down) {
			if ((x == -1 && y == 0) || (x == -1 && y == 1) || (x == 1 && y == 1) || (x == 1 && y == 0)
					|| (x == 0 && y == -1) || (x == 0 && y == 2)) {
				check = true;
			}
		} else {
			check = false;

		}
		return check;

	}

	/**
	 * F#15: Verify neighbor adjacency: As a player, I want the Kingdomino app to
	 * automatically check if my current domino is placed to an adjacent territory
	 * 
	 * @author Wang Sen
	 * @param dominoToBePlaced:   The domino that is about to be placed
	 * @param dominoToBeCompared: The domino that is compared to the
	 *                            dominoToBePlaced
	 * @return a boolean variable: True if neighbor adjacency is valid, False if
	 *         neighbor adjacency isn't valid
	 * @throws Exception if the current game isn't initialized
	 **/

	public static boolean neighborAdjacency(DominoInKingdom dominoToBePlaced, DominoInKingdom dominoToBeCompared) {
		try {
			Kingdomino kingdomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingdomino.getCurrentGame();
		} catch (Exception e) {
			throw new IllegalArgumentException("Game is not initialized");
		}
		// DominoToBePlaced is Up and DominoToBeCompared is Up
		if (dominoToBePlaced.getDirection() == DirectionKind.Up
				&& dominoToBeCompared.getDirection() == DirectionKind.Up) {
			if (dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0
					&& Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()
						|| dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino()
								.getRightTile()) {
					return true;
				}

			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}

			} else if (dominoToBePlaced.getX() == dominoToBeCompared.getX()
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() == dominoToBeCompared.getX()
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			}
			// DominoToBePlaced is down and DominoToBeCompared is Up
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Down
				&& dominoToBeCompared.getDirection() == DirectionKind.Up) {

			if (dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1
					&& Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()
						|| dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino()
								.getLeftTile()) {
					return true;
				}
			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}

			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() == dominoToBeCompared.getX()
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() == dominoToBeCompared.getX()
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 3) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			}
			// DominoToBePlaced is right and DominoToBeCompared is Up
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Right
				&& dominoToBeCompared.getDirection() == DirectionKind.Up) {

			if (dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2
					&& dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}

			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			}
			// DominoToBePlaced is Left and DominoToBeCompared is Up
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Left
				&& dominoToBeCompared.getDirection() == DirectionKind.Up) {
			if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1
					&& dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			}
			// DominoToBePlaced is Up and dominoToBeCompared is Down
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Up
				&& dominoToBeCompared.getDirection() == DirectionKind.Down) {
			if (dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1
					&& Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()
						|| dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino()
								.getLeftTile()) {
					return true;
				}
			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getY() - dominoToBePlaced.getY() == 3
					&& dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1
					&& dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			}
			// DominoToBePlaced is down and DominoToBeCompared is down
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Down
				&& dominoToBeCompared.getDirection() == DirectionKind.Down) {
			if (dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0
					&& Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()
						|| dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino()
								.getRightTile()) {
					return true;
				}

			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (Math.abs(dominoToBePlaced.getX() - dominoToBeCompared.getX()) == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}

			} else if (dominoToBePlaced.getX() == dominoToBeCompared.getX()
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() == dominoToBeCompared.getX()
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			}
			// DominoToBePlaced is right and dominoToBeCompared us down
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Right
				&& dominoToBeCompared.getDirection() == DirectionKind.Down) {
			if (dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1
					&& dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}

			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			}
			// DominoToBePlaced is left and dominoToBeCompared is down
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Left
				&& dominoToBeCompared.getDirection() == DirectionKind.Down) {
			if (dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1
					&& dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}

			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			}
			// DominoToBePlaced is Up and dominoToBeCompared is right
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Up
				&& dominoToBeCompared.getDirection() == DirectionKind.Right) {
			if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			}
			// dominoToBePlaced is down and dominoToBeCompared is right
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Down
				&& dominoToBeCompared.getDirection() == DirectionKind.Right) {
			if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			}
			// dominoToBePlaced is right and dominoToBeCompared is right
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Right
				&& dominoToBeCompared.getDirection() == DirectionKind.Right) {
			if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()
						|| dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			}
			// dominoToBePlaced is left and dominoToBeCompared is right
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Left
				&& dominoToBeCompared.getDirection() == DirectionKind.Right) {
			if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()
						|| dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino()
								.getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 3
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			}
			// dominoToBePlaced is up and dominoToBeCompared is left
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Up
				&& dominoToBeCompared.getDirection() == DirectionKind.Left) {
			if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			}
			// dominoToBePlaced is down and dominoToBeCompared is left
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Down
				&& dominoToBeCompared.getDirection() == DirectionKind.Left) {
			if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 2) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBePlaced.getY() - dominoToBeCompared.getY() == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			}
			// dominoToBePlaced is right and dominoToBeCompared is left
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Right
				&& dominoToBeCompared.getDirection() == DirectionKind.Left) {
			if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getRightTile()
						|| dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino()
								.getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 0
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 3
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBePlaced.getDomino().getLeftTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			}
			// dominoToBePlaced is left and dominoToBeCompared is left
		} else if (dominoToBePlaced.getDirection() == DirectionKind.Left
				&& dominoToBeCompared.getDirection() == DirectionKind.Left) {
			if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 0
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getLeftTile() == dominoToBePlaced.getDomino().getLeftTile()
						|| dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino()
								.getRightTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 1
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 1
					&& Math.abs(dominoToBePlaced.getY() - dominoToBeCompared.getY()) == 1) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBeCompared.getX() - dominoToBePlaced.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBeCompared.getDomino().getRightTile() == dominoToBePlaced.getDomino().getLeftTile()) {
					return true;
				}
			} else if (dominoToBePlaced.getX() - dominoToBeCompared.getX() == 2
					&& dominoToBeCompared.getY() - dominoToBePlaced.getY() == 0) {
				if (dominoToBePlaced.getDomino().getRightTile() == dominoToBeCompared.getDomino().getLeftTile()) {
					return true;
				}
			}
		}
		// returns false if neighbor adjacency isn't valid
		return false;
	}

	/**
	 * Verify adjacency of a DominoInKingdom over a whole kingdom. (helper method)
	 * 
	 * @param aDomino
	 * @param aKingdom
	 * @return true if has adjacency, false if has no adjacency
	 */
	public static boolean verifyAdjacencyComplete(DominoInKingdom aDomino, Kingdom aKingdom) {
		boolean adjacency = false;
		for (int i = 0; i < aKingdom.getTerritories().size(); i++) {
			if (aKingdom.getTerritory(i) instanceof DominoInKingdom) {
				DominoInKingdom current = (DominoInKingdom) aKingdom.getTerritory(i);
				if (neighborAdjacency(aDomino, current))
					adjacency = true;
			}
		}
		return adjacency;
	}

	/**
	 * F#16: Verify no overlapping: As a player, I want the Kingdomino app to
	 * automatically check that my current domino is not overlapping with existing
	 * dominos.
	 * 
	 * @author Noah Chamberland
	 * @param aDomino
	 * @param aKingdom
	 * @return true if not overlapping, false if overlapping
	 */
	public static boolean verifyNoOverlapping(DominoInKingdom aDomino, Kingdom aKingdom) {
		boolean accept = true;
		int x = aDomino.getX();
		int y = aDomino.getY();
		DominoInKingdom.DirectionKind direction = aDomino.getDirection();
		for (int i = 0; i < aKingdom.getTerritories().size(); i++) {
			if (aKingdom.getTerritory(i).getX() == x && aKingdom.getTerritory(i).getY() == y
					&& !aKingdom.getTerritory(i).equals(aDomino))
				accept = false;
			else if (aKingdom.getTerritory(i) instanceof Castle) {
				Castle current = (Castle) aKingdom.getTerritory(i);
				if (direction == DominoInKingdom.DirectionKind.Down && x == current.getX() && y - 1 == current.getY())
					accept = false;
				else if (direction == DominoInKingdom.DirectionKind.Up && x == current.getX()
						&& y + 1 == current.getY())
					accept = false;
				else if (direction == DominoInKingdom.DirectionKind.Left && x - 1 == current.getX()
						&& y == current.getY())
					accept = false;
				else if (direction == DominoInKingdom.DirectionKind.Right && x + 1 == current.getX()
						&& y == current.getY())
					accept = false;
			} else {
				DominoInKingdom current = (DominoInKingdom) aKingdom.getTerritory(i);
				if (current.getDirection() == DominoInKingdom.DirectionKind.Down) {
					if (direction == DominoInKingdom.DirectionKind.Down && x == current.getX()
							&& (y - 1 == current.getY() || y + 1 == current.getY()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Up && x == current.getX()
							&& (y + 2 == current.getY() || y + 1 == current.getY()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Left
							&& ((x - 1 == current.getX() && y == current.getY())
									|| (x - 1 == current.getX() && y + 1 == current.getY())
									|| (x == current.getX() && y + 1 == current.getY())))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Right
							&& ((x + 1 == current.getX() && y == current.getY())
									|| (x + 1 == current.getX() && y + 1 == current.getY())
									|| (x == current.getX() && y + 1 == current.getY())))
						accept = false;
				} else if (current.getDirection() == DominoInKingdom.DirectionKind.Up) {
					if (direction == DominoInKingdom.DirectionKind.Down && x == current.getX()
							&& (y - 1 == current.getY() || y - 2 == current.getY()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Up && x == current.getX()
							&& (y - 1 == current.getY() || y + 1 == current.getY()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Left
							&& ((x - 1 == current.getX() && y == current.getY())
									|| (x - 1 == current.getX() && y - 1 == current.getY())
									|| (x == current.getX() && y - 1 == current.getY())))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Right
							&& ((x + 1 == current.getX() && y == current.getY())
									|| (x + 1 == current.getX() && y - 1 == current.getY())
									|| (x == current.getX() && y - 1 == current.getY())))
						accept = false;
				} else if (current.getDirection() == DominoInKingdom.DirectionKind.Left) {
					if (direction == DominoInKingdom.DirectionKind.Left && y == current.getY()
							&& (x + 1 == current.getX() || x - 1 == current.getX()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Right && y == current.getY()
							&& (x + 1 == current.getX() || x + 2 == current.getX()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Up
							&& ((x == current.getX() && y + 1 == current.getY())
									|| (x + 1 == current.getX() && y + 1 == current.getY())
									|| (x + 1 == current.getX() && y == current.getY())))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Down
							&& ((x == current.getX() && y - 1 == current.getY())
									|| (x + 1 == current.getX() && y - 1 == current.getY())
									|| (x + 1 == current.getX() && y == current.getY())))
						accept = false;
				} else if (current.getDirection() == DominoInKingdom.DirectionKind.Right) {
					if (direction == DominoInKingdom.DirectionKind.Left && y == current.getY()
							&& (x - 1 == current.getX() || x - 2 == current.getX()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Right && y == current.getY()
							&& (x + 1 == current.getX() || x - 1 == current.getX()))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Up
							&& ((x == current.getX() && y + 1 == current.getY())
									|| (x - 1 == current.getX() && y + 1 == current.getY())
									|| (x - 1 == current.getX() && y == current.getY())))
						accept = false;
					else if (direction == DominoInKingdom.DirectionKind.Down
							&& ((x == current.getX() && y - 1 == current.getY())
									|| (x - 1 == current.getX() && y - 1 == current.getY())
									|| (x - 1 == current.getX() && y == current.getY())))
						accept = false;
				}
			}
		}
		return accept;
	}

	/**
	 * F#17: Verify kingdom grid size: As a player, I want the Kingdomino app to
	 * automatically check if the grid of my kingdom has not yet exceeded a square
	 * of 5x5 tiles (including my castle).
	 *
	 * This method check if kingdom have more than exceeded a square of 5*5 tiles
	 * including my castle
	 * 
	 * @author Haipeng Yue
	 * @para Kingdom kingdom
	 * @return a boolean true if the kingdom does exceeded a square of 5*5 tiles
	 *         including my castle else false
	 */
	public static boolean verifyKingdomGridSize(Kingdom kingdom) {
		boolean check = false;
		Kingdomino kd = KingdominoApplication.getKingdomino();
		Game game = kd.getCurrentGame();
		int totalx = 1, totaly = 1;
		List<KingdomTerritory> list = kingdom.getTerritories();
		ArrayList<Integer> xall = new ArrayList();

		ArrayList<Integer> yall = new ArrayList();
		for (int i = 1; i < list.size(); i++) {
			DominoInKingdom dom = (DominoInKingdom) list.get(i);
			if (dom.getDomino() == null)
				continue;
			int x = dom.getX();
			xall.add(x);
			int y = dom.getY();
			yall.add(y);
			int ox = 0;
			int oy = 0;
			DirectionKind dir = dom.getDirection();

			if (dir == DirectionKind.Down) {
				ox = x;
				oy = y - 1;

			} else if (dir == DirectionKind.Up) {
				ox = x;
				oy = y + 1;
			} else if (dir == DirectionKind.Left) {
				ox = x - 1;
				oy = y;
			} else if (dir == DirectionKind.Right) {
				ox = x + 1;
				oy = y;
			}

			xall.add(ox);
			yall.add(oy);
		}
		Collections.sort(xall);
		Collections.sort(yall);

		if ((xall.get(xall.size() - 1) - xall.get(0) <= 4) && (yall.get(xall.size() - 1) - yall.get(0) <= 4)) {
			check = true;
		}
		return check;
	}

	/**
	 * F#18: Discard domino: As a player, I wish to discard a domino if it cannot be
	 * placed to my kingdom in a valid way.
	 * 
	 * @author Noah Chamberland
	 * @param k
	 * @param d
	 */
	public static boolean discardDomino(Kingdom k, Domino d) {
		boolean availablePlace = false;
		for (int x = -4; x < 5; x++) {
			for (int y = -4; y < 5; y++) {
				DirectionKind direction = DirectionKind.Up;
				if (placeDomino(k.getPlayer(), d, x, y, direction, false)) {
					availablePlace = true;
					break;
				}
				if (availablePlace)
					break;
				direction = DirectionKind.Down;
				if (placeDomino(k.getPlayer(), d, x, y, direction, false)) {
					availablePlace = true;
					break;
				}
				if (availablePlace)
					break;
				direction = DirectionKind.Left;
				if (placeDomino(k.getPlayer(), d, x, y, direction, false)) {
					availablePlace = true;
					break;
				}
				if (availablePlace)
					break;
				direction = DirectionKind.Right;
				if (placeDomino(k.getPlayer(), d, x, y, direction, false)) {
					availablePlace = true;
					break;
				}
				if (availablePlace)
					break;
			}
			if (availablePlace)
				break;
		}
		if (availablePlace) {
			d.setStatus(DominoStatus.ErroneouslyPreplaced);
			return false;
		} else {
			getDominoInKingdomByDomino(d, k).delete();
			d.setStatus(DominoStatus.Discarded);
			return true;
		}
	}

	/**
	 * F#19: Identify kingdom properties: As a player, I want the Kingdomino app to
	 * automatically determine each properties of my kingdom so that my score can be
	 * calculated.
	 * 
	 * @param kingdom
	 * @author Noah Chamberland
	 */
	public static void identifyKingdomProperties(Kingdom kingdom) {
		for (int y = 4; y > -5; y--) {
			for (int x = -4; x < 5; x++) {
				DominoInKingdom current = getDominoAt(x, y, kingdom);
				if (current != null) {
					int curx = current.getX();
					int cury = current.getY();
					Domino dom = current.getDomino();
					Property property = getPropertyAt(current, dom, x, y, kingdom);
					if (property == null) {
						property = new Property(kingdom);
						property.addIncludedDomino(dom);
						if (curx == x && cury == y)
							property.setPropertyType(dom.getLeftTile());
						else
							property.setPropertyType(dom.getRightTile());
					}
					checkAndAddDominoToPropertyAt(x - 1, y, kingdom, property);
					checkAndAddDominoToPropertyAt(x + 1, y, kingdom, property);
					checkAndAddDominoToPropertyAt(x, y - 1, kingdom, property);
					checkAndAddDominoToPropertyAt(x, y + 1, kingdom, property);
				}
			}
		}
	}

	// Helper for identify properties
	private static void checkAndAddDominoToPropertyAt(int x, int y, Kingdom kingdom, Property property) {
		DominoInKingdom check = getDominoAt(x, y, kingdom);
		if (check != null) {
			Domino checkdom = check.getDomino();
			Property checkproperty = getPropertyAt(check, checkdom, x, y, kingdom);
			if (checkproperty == null) {
				TerrainType terrain = null;
				if (check.getX() == x && check.getY() == y)
					terrain = checkdom.getLeftTile();
				else
					terrain = checkdom.getRightTile();
				if (terrain == property.getPropertyType())
					property.addIncludedDomino(checkdom);
			} else {
				if (checkproperty != null) {
					if (property.getPropertyType() == checkproperty.getPropertyType() && property != checkproperty) {
						mergeProperties(property, checkproperty, kingdom);
					}
				}
			}
		}

	}

	// Helper for identify properties
	private static void mergeProperties(Property property, Property checkproperty, Kingdom kingdom) {
		TerrainType terrain = property.getPropertyType();
		List<Domino> dominos = new ArrayList<Domino>(property.getIncludedDominos());
		for (Domino domino : checkproperty.getIncludedDominos()) {
			dominos.add(domino);
		}
		property.delete();
		checkproperty.delete();
		Property newProperty = new Property(kingdom);
		newProperty.setPropertyType(terrain);
		for (Domino domino : dominos) {
			newProperty.addIncludedDomino(domino);
		}
	}

	// Helper for identify properties
	public static DominoInKingdom getDominoAt(int x, int y, Kingdom kingdom) {
		DominoInKingdom current = null;
		for (int i = 0; i < kingdom.getTerritories().size(); i++) {
			if (kingdom.getTerritory(i) instanceof DominoInKingdom) {
				current = (DominoInKingdom) kingdom.getTerritory(i);
			}
			if (current != null) {
				int curx = current.getX();
				int cury = current.getY();
				if (curx == x && cury == y)
					return current;
				else if (current.getDirection() == DirectionKind.Up && curx == x && cury + 1 == y)
					return current;
				else if (current.getDirection() == DirectionKind.Down && curx == x && cury - 1 == y)
					return current;
				else if (current.getDirection() == DirectionKind.Left && curx - 1 == x && cury == y)
					return current;
				else if (current.getDirection() == DirectionKind.Right && curx + 1 == x && cury == y)
					return current;
			}
		}
		return null;
	}

	// Helper for identify properties
	public static Property getPropertyAt(DominoInKingdom current, Domino dom, int x, int y, Kingdom kingdom) {
		int curx = current.getX();
		int cury = current.getY();
		for (int i = 0; i < kingdom.getProperties().size(); i++) {
			Property property = kingdom.getProperty(i);
			if (property.getIncludedDominos().contains(dom)) {
				if (x == curx && y == cury) {
					if (property.getPropertyType() == dom.getLeftTile())
						return property;
				} else {
					if (property.getPropertyType() == dom.getRightTile())
						return property;
				}
			}
		}
		return null;
	}

	/**
	 * F#20: Calculate property attributes I want the Kingdomino app to
	 * automatically calculate the size of a property and the total number of crowns
	 * in that property.
	 *
	 * 
	 * This method calculate the number of the properties of a kingdom
	 * 
	 * @author Sen Wang
	 * @param kingdom
	 */
	public static void calculatePropertyAttribute(Kingdom kingdom) {
		// KingdominoController.identifyKingdomProperties(kingdom);
		try {
			Kingdomino kingdomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingdomino.getCurrentGame();
		} catch (Exception e) {
			throw new IllegalArgumentException("Game is not initialized");
		}
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game currentGame = kingdomino.getCurrentGame();
		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			KingdominoController.identifyKingdomProperties(currentGame.getPlayers().get(i).getKingdom());
		}
		for (int i = 0; i < kingdom.getProperties().size(); i++) {
			int propertySize = 0;
			for (int j = 0; j < kingdom.getProperties().get(i).getIncludedDominos().size(); j++) {
				if (kingdom.getProperties().get(i).getIncludedDominos().get(j).getLeftTile() == kingdom.getProperties()
						.get(i).getIncludedDominos().get(j).getRightTile()) {
					propertySize += 2;
				} else {
					propertySize += 1;
				}
			}
			kingdom.getProperties().get(i).setSize(propertySize);
			int numberOfCrown = 0;
			for (int j = 0; j < kingdom.getProperties().get(i).getIncludedDominos().size(); j++) {
				if (kingdom.getProperties().get(i).getIncludedDominos().get(j).getLeftTile()
						.equals(kingdom.getProperties().get(i).getPropertyType())) {
					numberOfCrown += kingdom.getProperties().get(i).getIncludedDominos().get(j).getLeftCrown();
				} else if (kingdom.getProperties().get(i).getIncludedDominos().get(j).getRightTile()
						.equals(kingdom.getProperties().get(i).getPropertyType())) {
					numberOfCrown += kingdom.getProperties().get(i).getIncludedDominos().get(j).getRightCrown();
					;
				}
			}
			kingdom.getProperties().get(i).setCrowns(numberOfCrown);
		}
	}

	/**
	 * F#21: Calculate bonus scores: As a player, I want the Kingdomino app to
	 * automatically calculate the bonus scores (for Harmony and middle Kingdom) if
	 * those bonus scores were selected as a game option.
	 * 
	 * @author Sen Wang
	 */
	public static void calculateBonusScores() {

		try {
			Kingdomino kingdomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingdomino.getCurrentGame();
		} catch (Exception e) {
			throw new IllegalArgumentException("Game is not initialized");
		}
		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game currentGame = kingdomino.getCurrentGame();

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			KingdominoController.identifyKingdomProperties(currentGame.getPlayers().get(i).getKingdom());
			KingdominoController.calculatePropertyAttribute(currentGame.getPlayers().get(i).getKingdom());
		}
		for (int i = 0; i < currentGame.getSelectedBonusOptions().size(); i++) {
			if (currentGame.getSelectedBonusOptions().get(i).getOptionName().equals("Harmony")) {
				for (int j = 0; j < currentGame.getPlayers().size(); j++) {
					int oldBonusScore = currentGame.getPlayers().get(j).getBonusScore();
					int totalPropertySize = 0;
					for (int k = 0; k < currentGame.getPlayers().get(j).getKingdom().getProperties().size(); k++) {
						totalPropertySize += currentGame.getPlayers().get(j).getKingdom().getProperties().get(k)
								.getSize();
					}
					if (totalPropertySize == 24) {
						currentGame.getPlayers().get(j).setBonusScore(5);
					}
				}
			}
			if (currentGame.getSelectedBonusOptions().get(i).getOptionName().equals("Middle Kingdom")) {
				for (int j = 0; j < currentGame.getPlayers().size(); j++) {
					int oldBonusScore = currentGame.getPlayers().get(j).getBonusScore();
					boolean hasUp = false;
					boolean hasDown = false;
					boolean hasLeft = false;
					boolean hasRight = false;
					for (int k = 0; k < currentGame.getPlayers().get(j).getKingdom().getTerritories().size(); k++) {
						if (currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k).getX() <= 2
								&& currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k).getX() >= -2
								&& currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k).getY() <= 2
								&& currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k).getY() >= -2) {

							for (int x = -2; x <= 2; x++) {
								if (currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k)
										.equals(getDominoAt(x, -2, currentGame.getPlayers().get(j).getKingdom()))) {
									hasDown = true;
								}
								if (currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k)
										.equals(getDominoAt(x, 2, currentGame.getPlayers().get(j).getKingdom()))) {
									hasUp = true;
								}
							}
							for (int y = -2; y <= 2; y++) {
								if (currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k)
										.equals(getDominoAt(-2, y, currentGame.getPlayers().get(j).getKingdom()))) {
									hasLeft = true;
								}
								if (currentGame.getPlayers().get(j).getKingdom().getTerritories().get(k)
										.equals(getDominoAt(2, y, currentGame.getPlayers().get(j).getKingdom()))) {
									hasRight = true;
								}
							}
						}
					}
					if (hasUp && hasDown && hasLeft && hasRight) {
						if (oldBonusScore == 5)
							currentGame.getPlayers().get(j).setBonusScore(15);
						else {
							currentGame.getPlayers().get(j).setBonusScore(10);
						}
					}
				}
			}
		}
	}

	/**
	 * 
	 * F#22: Calculate player score: As a player, I want the Kingdomino app to
	 * automatically calculate the score for each player by summing up their
	 * property scores and their bonus scores.
	 * 
	 * @author Sen Wang
	 */

	public static void calculatePlayerScore() {

		try {
			Kingdomino kingdomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingdomino.getCurrentGame();
		} catch (Exception e) {
			throw new IllegalArgumentException("Game is not initialized");
		}

		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game currentGame = kingdomino.getCurrentGame();

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			KingdominoController.identifyKingdomProperties(currentGame.getPlayers().get(i).getKingdom());
			KingdominoController.calculatePropertyAttribute(currentGame.getPlayers().get(i).getKingdom());
		}

		// Players property score

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			int playerPropertyScore = 0;
			for (int j = 0; j < currentGame.getPlayers().get(i).getKingdom().getProperties().size(); j++) {
				playerPropertyScore += (currentGame.getPlayers().get(i).getKingdom().getProperties().get(j).getCrowns()
						* currentGame.getPlayers().get(i).getKingdom().getProperties().get(j).getSize());

			}
			currentGame.getPlayers().get(i).setPropertyScore(playerPropertyScore);

		}

		// Players Bonus score
		KingdominoController.calculateBonusScores();

	}

	/**
	 * F#23: Calculate ranking: As a player, I want the Kingdomino App to
	 * automatically calculate the ranking order to know the winner of a finished
	 * game
	 * 
	 * @author Wang Sen
	 * @return An ArrayList of ranked Players
	 * @throws Exception if the current game isn't initialized
	 * 
	 */

	public static HashMap<Player, Integer> calculateRanking() {

		try {
			Kingdomino kingdomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingdomino.getCurrentGame();
		} catch (Exception e) {
			throw new IllegalArgumentException("Game is not initialized");
		}

		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game currentGame = kingdomino.getCurrentGame();

		// Create a new HashMap that maps players to their scores

		HashMap<Player, Integer> scoreMap = new HashMap<Player, Integer>();
		ArrayList<Player> rankedPlayers = new ArrayList();

		KingdominoController.calculatePlayerScore();

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			scoreMap.put(currentGame.getPlayers().get(i), currentGame.getPlayers().get(i).getTotalScore());
		}

		HashMap<Player, Integer> sortedMap = new HashMap<Player, Integer>();
		sortedMap = KingdominoController.sortByValue(scoreMap);

		Iterator<Player> it = sortedMap.keySet().iterator();
		while (it.hasNext()) {
			rankedPlayers.add(it.next());
		}

		Collections.reverse(rankedPlayers);

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			// currentGame.getPlayers().get(i).setCurrentRanking(rankedPlayers.indexOf(currentGame.getPlayers().get(i))
			// + 1);
			for (int j = 0; j < rankedPlayers.size(); j++) {
				if (currentGame.getPlayers().get(i).equals(rankedPlayers.get(j))) {
					currentGame.getPlayers().get(i)
							.setCurrentRanking(rankedPlayers.indexOf(currentGame.getPlayers().get(i)) + 1);
				}
			}
		}
		KingdominoController.resolveTieBreak();

		HashMap<Player, Integer> rankedPlayerMap = new HashMap<Player, Integer>();

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			rankedPlayerMap.put(currentGame.getPlayers().get(i), currentGame.getPlayers().get(i).getCurrentRanking());
		}

		/*
		 * for (int i = 0; i < currentGame.getPlayers().size(); i++) {
		 * rankedPlayers.set(currentGame.getPlayers().get(i).getCurrentRanking() - 1,
		 * currentGame.getPlayers().get(i)); }
		 */
		return rankedPlayerMap;

	}

	// function to sort hashmap by values
	private static HashMap<Player, Integer> sortByValue(HashMap<Player, Integer> hashMap) {
		// Create a list from elements of HashMap
		List<Map.Entry<Player, Integer>> list = new LinkedList<Map.Entry<Player, Integer>>(hashMap.entrySet());

		// Sort the list
		Collections.sort(list, new Comparator<Map.Entry<Player, Integer>>() {
			public int compare(Map.Entry<Player, Integer> o1, Map.Entry<Player, Integer> o2) {
				return (o1.getValue()).compareTo(o2.getValue());
			}
		});

		// put data from sorted list to hashmap
		HashMap<Player, Integer> temp = new LinkedHashMap<Player, Integer>();
		for (Map.Entry<Player, Integer> aa : list) {
			temp.put(aa.getKey(), aa.getValue());
		}
		return temp;
	}

	/**
	 * 
	 * F#24: Resolve tiebreak: As a player, I want the Kingdomino app to
	 * automatically resolve a potential tiebreak (i.e. equal score between players)
	 * by evaluating the most extended (largest) property and then the total number
	 * of crowns.
	 * 
	 * @author Sen Wang
	 */

	public static void resolveTieBreak() {
		try {
			Kingdomino kingdomino = KingdominoApplication.getKingdomino();
			Game currentGame = kingdomino.getCurrentGame();
		} catch (Exception e) {
			throw new IllegalArgumentException("Game is not initialized");
		}

		Kingdomino kingdomino = KingdominoApplication.getKingdomino();
		Game currentGame = kingdomino.getCurrentGame();

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			KingdominoController.identifyKingdomProperties(currentGame.getPlayers().get(i).getKingdom());
			KingdominoController.calculatePlayerScore();
		}

		for (int i = 0; i < currentGame.getPlayers().size(); i++) {
			for (int j = i + 1; j < currentGame.getPlayers().size(); j++) {
				if (currentGame.getPlayers().get(i).getTotalScore() == currentGame.getPlayers().get(j)
						.getTotalScore()) {
					Player firstPlayer = currentGame.getPlayers().get(i);
					Player secondPlayer = currentGame.getPlayers().get(j);
					int firstPlayerBiggestPropertySize = 0;
					int secondPlayerBiggestPropertySize = 0;

					for (int k = 0; k < firstPlayer.getKingdom().getProperties().size(); k++) {
						if (firstPlayer.getKingdom().getProperties().get(k)
								.getSize() > firstPlayerBiggestPropertySize) {
							firstPlayerBiggestPropertySize = firstPlayer.getKingdom().getProperties().get(k).getSize();
						}
					}
					for (int k = 0; k < secondPlayer.getKingdom().getProperties().size(); k++) {
						if (secondPlayer.getKingdom().getProperties().get(k)
								.getSize() > secondPlayerBiggestPropertySize) {
							secondPlayerBiggestPropertySize = secondPlayer.getKingdom().getProperties().get(k)
									.getSize();
						}
					}
					if (firstPlayerBiggestPropertySize > secondPlayerBiggestPropertySize) {

						if (firstPlayer.getCurrentRanking() > secondPlayer.getCurrentRanking()) {
							int pastFirstPlayerRanking = firstPlayer.getCurrentRanking();
							int pastSecondPlayerRanking = secondPlayer.getCurrentRanking();
							secondPlayer.setCurrentRanking(pastFirstPlayerRanking);
							firstPlayer.setCurrentRanking(pastSecondPlayerRanking);
						}
					} else if (firstPlayerBiggestPropertySize < secondPlayerBiggestPropertySize) {

						if (secondPlayer.getCurrentRanking() > firstPlayer.getCurrentRanking()) {
							int pastFirstPlayerRanking = firstPlayer.getCurrentRanking();
							int pastSecondPlayerRanking = secondPlayer.getCurrentRanking();
							secondPlayer.setCurrentRanking(pastFirstPlayerRanking);
							firstPlayer.setCurrentRanking(pastSecondPlayerRanking);
						}
					} else if (firstPlayerBiggestPropertySize == secondPlayerBiggestPropertySize) {
						int firstPlayerNbOfCrowns = 0;
						int secondPlayerNbOfCrowns = 0;

						for (int k = 0; k < firstPlayer.getKingdom().getProperties().size(); k++) {
							firstPlayerNbOfCrowns += firstPlayer.getKingdom().getProperties().get(k).getCrowns();
						}

						for (int k = 0; k < secondPlayer.getKingdom().getProperties().size(); k++) {
							secondPlayerNbOfCrowns += secondPlayer.getKingdom().getProperties().get(k).getCrowns();
						}

						if (firstPlayerNbOfCrowns > secondPlayerNbOfCrowns) {
							if (firstPlayer.getCurrentRanking() > secondPlayer.getCurrentRanking()) {
								int pastFirstPlayerRanking = firstPlayer.getCurrentRanking();
								int pastSecondPlayerRanking = secondPlayer.getCurrentRanking();
								secondPlayer.setCurrentRanking(pastFirstPlayerRanking);
								firstPlayer.setCurrentRanking(pastSecondPlayerRanking);
							}
						} else if (firstPlayerNbOfCrowns < secondPlayerNbOfCrowns) {

							if (secondPlayer.getCurrentRanking() > firstPlayer.getCurrentRanking()) {
								int pastFirstPlayerRanking = firstPlayer.getCurrentRanking();
								int pastSecondPlayerRanking = secondPlayer.getCurrentRanking();
								secondPlayer.setCurrentRanking(pastFirstPlayerRanking);
								firstPlayer.setCurrentRanking(pastSecondPlayerRanking);
							}
						} else if (firstPlayerNbOfCrowns == secondPlayerNbOfCrowns) {
							if (firstPlayer.getCurrentRanking() > secondPlayer.getCurrentRanking()) {
								for (int k = 0; k < currentGame.getPlayers().size(); k++) {
									int oldRanking = currentGame.getPlayers().get(k).getCurrentRanking();
									if (!currentGame.getPlayers().get(k).equals(secondPlayer)) {
										currentGame.getPlayers().get(k).setCurrentRanking(oldRanking - 1);
									}
								}
							} else if (firstPlayer.getCurrentRanking() < secondPlayer.getCurrentRanking()) {
								for (int k = 0; k < currentGame.getPlayers().size(); k++) {
									int oldRanking = currentGame.getPlayers().get(k).getCurrentRanking();
									if (!currentGame.getPlayers().get(k).equals(firstPlayer)) {
										currentGame.getPlayers().get(k).setCurrentRanking(oldRanking - 1);
									}
								}
							}
						}
					}
				}
			}

		}

	}
}