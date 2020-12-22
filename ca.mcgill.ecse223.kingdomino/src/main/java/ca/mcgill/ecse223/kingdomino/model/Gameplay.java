/*PLEASE DO NOT EDIT THIS CODE*/
/*This code was generated using the UMPLE 1.29.0.4181.a593105a9 modeling language!*/

package ca.mcgill.ecse223.kingdomino.model;
import ca.mcgill.ecse223.kingdomino.model.DominoInKingdom.DirectionKind;
import ca.mcgill.ecse223.kingdomino.KingdominoApplication;
import ca.mcgill.ecse223.kingdomino.controller.KingdominoController;
import java.util.*;

// line 3 "../../../../../Gameplay.ump"
public class Gameplay
{

  //------------------------
  // MEMBER VARIABLES
  //------------------------

  //Gameplay Attributes
  private boolean selectValid;
  private boolean placeValid;
  private boolean discardValid;
  private boolean hasNext;

  //Gameplay State Machines
  public enum Gamestatus { Initializing, InGame, Finalizing }
  public enum GamestatusInitializing { Null, CreatingFirstDraft, SelectingFirstDomino }
  public enum GamestatusInGame { Null, CreatingDraft, SortingAndRevealingDraft, SelectingDomino, PlacingDomino, PlacingLastDomino, UpdateNextPlayer, CalculatingPlayerScore }
  public enum GamestatusFinalizing { Null, CalculatingPlayerScore, CalculatingRanking }
  private Gamestatus gamestatus;
  private GamestatusInitializing gamestatusInitializing;
  private GamestatusInGame gamestatusInGame;
  private GamestatusFinalizing gamestatusFinalizing;

  //------------------------
  // CONSTRUCTOR
  //------------------------

  public Gameplay()
  {
    selectValid = false;
    placeValid = false;
    discardValid = false;
    hasNext = false;
    setGamestatusInitializing(GamestatusInitializing.Null);
    setGamestatusInGame(GamestatusInGame.Null);
    setGamestatusFinalizing(GamestatusFinalizing.Null);
    setGamestatus(Gamestatus.Initializing);
  }

  //------------------------
  // INTERFACE
  //------------------------

  public boolean setSelectValid(boolean aSelectValid)
  {
    boolean wasSet = false;
    selectValid = aSelectValid;
    wasSet = true;
    return wasSet;
  }

  public boolean setPlaceValid(boolean aPlaceValid)
  {
    boolean wasSet = false;
    placeValid = aPlaceValid;
    wasSet = true;
    return wasSet;
  }

  public boolean setDiscardValid(boolean aDiscardValid)
  {
    boolean wasSet = false;
    discardValid = aDiscardValid;
    wasSet = true;
    return wasSet;
  }

  public boolean setHasNext(boolean aHasNext)
  {
    boolean wasSet = false;
    hasNext = aHasNext;
    wasSet = true;
    return wasSet;
  }

  public boolean getSelectValid()
  {
    return selectValid;
  }

  public boolean getPlaceValid()
  {
    return placeValid;
  }

  public boolean getDiscardValid()
  {
    return discardValid;
  }

  public boolean getHasNext()
  {
    return hasNext;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isSelectValid()
  {
    return selectValid;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isPlaceValid()
  {
    return placeValid;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isDiscardValid()
  {
    return discardValid;
  }
  /* Code from template attribute_IsBoolean */
  public boolean isHasNext()
  {
    return hasNext;
  }

  public String getGamestatusFullName()
  {
    String answer = gamestatus.toString();
    if (gamestatusInitializing != GamestatusInitializing.Null) { answer += "." + gamestatusInitializing.toString(); }
    if (gamestatusInGame != GamestatusInGame.Null) { answer += "." + gamestatusInGame.toString(); }
    if (gamestatusFinalizing != GamestatusFinalizing.Null) { answer += "." + gamestatusFinalizing.toString(); }
    return answer;
  }

  public Gamestatus getGamestatus()
  {
    return gamestatus;
  }

  public GamestatusInitializing getGamestatusInitializing()
  {
    return gamestatusInitializing;
  }

  public GamestatusInGame getGamestatusInGame()
  {
    return gamestatusInGame;
  }

  public GamestatusFinalizing getGamestatusFinalizing()
  {
    return gamestatusFinalizing;
  }

  public boolean initReady()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    switch (aGamestatusInitializing)
    {
      case CreatingFirstDraft:
        exitGamestatusInitializing();
        // line 20 "../../../../../Gameplay.ump"
        shuffleDominoPile(); orderNextDraft(); revealNextDraft(); generateInitialPlayerOrder();
        setGamestatusInitializing(GamestatusInitializing.SelectingFirstDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean initLoaded()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    switch (aGamestatusInitializing)
    {
      case CreatingFirstDraft:
        exitGamestatus();
        setGamestatusInGame(GamestatusInGame.PlacingDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean selectDom(int id)
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInitializing)
    {
      case SelectingFirstDomino:
        exitGamestatusInitializing();
        // line 24 "../../../../../Gameplay.ump"
        chooseNextDomino(id);
        setGamestatusInitializing(GamestatusInitializing.SelectingFirstDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    switch (aGamestatusInGame)
    {
      case SelectingDomino:
        exitGamestatusInGame();
        // line 37 "../../../../../Gameplay.ump"
        chooseNextDomino(id);
        setGamestatusInGame(GamestatusInGame.SelectingDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean selectReady()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInitializing aGamestatusInitializing = gamestatusInitializing;
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInitializing)
    {
      case SelectingFirstDomino:
        if (getSelectValid()&&!(isCurrentPlayerTheLastInTurn()))
        {
          exitGamestatusInitializing();
          setGamestatusInitializing(GamestatusInitializing.SelectingFirstDomino);
          wasEventProcessed = true;
          break;
        }
        if (getSelectValid()&&isCurrentPlayerTheLastInTurn())
        {
          exitGamestatus();
          setGamestatusInGame(GamestatusInGame.CreatingDraft);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    switch (aGamestatusInGame)
    {
      case SelectingDomino:
        if (!(isCurrentPlayerTheLastInTurn())&&!(isCurrentTurnTheLastInGame())&&getSelectValid())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.PlacingDomino);
          wasEventProcessed = true;
          break;
        }
        if (isCurrentPlayerTheLastInTurn()&&!(isCurrentTurnTheLastInGame())&&getSelectValid())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.CreatingDraft);
          wasEventProcessed = true;
          break;
        }
        if (isCurrentPlayerTheLastInTurn()&&isCurrentTurnTheLastInGame()&&getSelectValid())
        {
          exitGamestatusInGame();
        // line 40 "../../../../../Gameplay.ump"
          shiftNextDraft();
          setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
          wasEventProcessed = true;
          break;
        }
        if (!(isCurrentPlayerTheLastInTurn())&&isCurrentTurnTheLastInGame()&&getSelectValid())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.PlacingDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition115__()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case CreatingDraft:
        exitGamestatusInGame();
        setGamestatusInGame(GamestatusInGame.SortingAndRevealingDraft);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition116__()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case SortingAndRevealingDraft:
        exitGamestatusInGame();
        setGamestatusInGame(GamestatusInGame.PlacingDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean moveDom(DominoInKingdom dom,String movement,Player player)
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case PlacingDomino:
        if (isCurrentDominoInKingdom())
        {
          exitGamestatusInGame();
        // line 45 "../../../../../Gameplay.ump"
          moveCurrentDomino(dom, movement, player);
          setGamestatusInGame(GamestatusInGame.PlacingDomino);
          wasEventProcessed = true;
          break;
        }
        if (!(isCurrentDominoInKingdom()))
        {
          exitGamestatusInGame();
        // line 46 "../../../../../Gameplay.ump"
          initPlacement();
          setGamestatusInGame(GamestatusInGame.PlacingDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      case PlacingLastDomino:
        if (isCurrentDominoInKingdom())
        {
          exitGamestatusInGame();
        // line 55 "../../../../../Gameplay.ump"
          moveCurrentDomino(dom, movement, player);
          setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
          wasEventProcessed = true;
          break;
        }
        if (!(isCurrentDominoInKingdom()))
        {
          exitGamestatusInGame();
        // line 56 "../../../../../Gameplay.ump"
          initPlacement();
          setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean rotateDom(Player p,Domino domino,String rotation)
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case PlacingDomino:
        if (isCurrentDominoInKingdom())
        {
          exitGamestatusInGame();
        // line 47 "../../../../../Gameplay.ump"
          rotateCurrentDomino(p, domino, rotation);
          setGamestatusInGame(GamestatusInGame.PlacingDomino);
          wasEventProcessed = true;
          break;
        }
        if (!(isCurrentDominoInKingdom()))
        {
          exitGamestatusInGame();
        // line 48 "../../../../../Gameplay.ump"
          initPlacement();
          setGamestatusInGame(GamestatusInGame.PlacingDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      case PlacingLastDomino:
        if (isCurrentDominoInKingdom())
        {
          exitGamestatusInGame();
        // line 57 "../../../../../Gameplay.ump"
          rotateCurrentDomino(p, domino, rotation);
          setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
          wasEventProcessed = true;
          break;
        }
        if (!(isCurrentDominoInKingdom()))
        {
          exitGamestatusInGame();
        // line 58 "../../../../../Gameplay.ump"
          initPlacement();
          setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean discardDom(Kingdom k,Domino d)
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case PlacingDomino:
        exitGamestatusInGame();
        // line 49 "../../../../../Gameplay.ump"
        discardDomino(k,d);
        setGamestatusInGame(GamestatusInGame.PlacingDomino);
        wasEventProcessed = true;
        break;
      case PlacingLastDomino:
        exitGamestatusInGame();
        // line 59 "../../../../../Gameplay.ump"
        discardDomino(k,d);
        setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean placeDom(Player player,Domino domino,int x,int y,DirectionKind direction,boolean satisfied)
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case PlacingDomino:
        exitGamestatusInGame();
        // line 50 "../../../../../Gameplay.ump"
        placeDomino(player, domino, x, y, direction, satisfied);
        setGamestatusInGame(GamestatusInGame.PlacingDomino);
        wasEventProcessed = true;
        break;
      case PlacingLastDomino:
        exitGamestatusInGame();
        // line 60 "../../../../../Gameplay.ump"
        placeDomino(player, domino, x, y, direction, satisfied);
        setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  public boolean placeReady()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case PlacingDomino:
        if (getPlaceValid())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.CalculatingPlayerScore);
          wasEventProcessed = true;
          break;
        }
        if (getDiscardValid())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.CalculatingPlayerScore);
          wasEventProcessed = true;
          break;
        }
        break;
      case PlacingLastDomino:
        if (getPlaceValid())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.UpdateNextPlayer);
          wasEventProcessed = true;
          break;
        }
        if (getDiscardValid())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.UpdateNextPlayer);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition117__()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case UpdateNextPlayer:
        if (getHasNext())
        {
          exitGamestatusInGame();
          setGamestatusInGame(GamestatusInGame.PlacingLastDomino);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition118__()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case UpdateNextPlayer:
        if (!getHasNext())
        {
          exitGamestatus();
          setGamestatusFinalizing(GamestatusFinalizing.CalculatingPlayerScore);
          wasEventProcessed = true;
          break;
        }
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition119__()
  {
    boolean wasEventProcessed = false;
    
    GamestatusInGame aGamestatusInGame = gamestatusInGame;
    switch (aGamestatusInGame)
    {
      case CalculatingPlayerScore:
        exitGamestatusInGame();
        setGamestatusInGame(GamestatusInGame.SelectingDomino);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private boolean __autotransition120__()
  {
    boolean wasEventProcessed = false;
    
    GamestatusFinalizing aGamestatusFinalizing = gamestatusFinalizing;
    switch (aGamestatusFinalizing)
    {
      case CalculatingPlayerScore:
        exitGamestatusFinalizing();
        setGamestatusFinalizing(GamestatusFinalizing.CalculatingRanking);
        wasEventProcessed = true;
        break;
      default:
        // Other states do respond to this event
    }

    return wasEventProcessed;
  }

  private void exitGamestatus()
  {
    switch(gamestatus)
    {
      case Initializing:
        exitGamestatusInitializing();
        break;
      case InGame:
        exitGamestatusInGame();
        break;
      case Finalizing:
        exitGamestatusFinalizing();
        break;
    }
  }

  private void setGamestatus(Gamestatus aGamestatus)
  {
    gamestatus = aGamestatus;

    // entry actions and do activities
    switch(gamestatus)
    {
      case Initializing:
        if (gamestatusInitializing == GamestatusInitializing.Null) { setGamestatusInitializing(GamestatusInitializing.CreatingFirstDraft); }
        break;
      case InGame:
        if (gamestatusInGame == GamestatusInGame.Null) { setGamestatusInGame(GamestatusInGame.CreatingDraft); }
        break;
      case Finalizing:
        if (gamestatusFinalizing == GamestatusFinalizing.Null) { setGamestatusFinalizing(GamestatusFinalizing.CalculatingPlayerScore); }
        break;
    }
  }

  private void exitGamestatusInitializing()
  {
    switch(gamestatusInitializing)
    {
      case CreatingFirstDraft:
        setGamestatusInitializing(GamestatusInitializing.Null);
        break;
      case SelectingFirstDomino:
        setGamestatusInitializing(GamestatusInitializing.Null);
        break;
    }
  }

  private void setGamestatusInitializing(GamestatusInitializing aGamestatusInitializing)
  {
    gamestatusInitializing = aGamestatusInitializing;
    if (gamestatus != Gamestatus.Initializing && aGamestatusInitializing != GamestatusInitializing.Null) { setGamestatus(Gamestatus.Initializing); }
  }

  private void exitGamestatusInGame()
  {
    switch(gamestatusInGame)
    {
      case CreatingDraft:
        setGamestatusInGame(GamestatusInGame.Null);
        break;
      case SortingAndRevealingDraft:
        setGamestatusInGame(GamestatusInGame.Null);
        break;
      case SelectingDomino:
        setGamestatusInGame(GamestatusInGame.Null);
        break;
      case PlacingDomino:
        setGamestatusInGame(GamestatusInGame.Null);
        break;
      case PlacingLastDomino:
        setGamestatusInGame(GamestatusInGame.Null);
        break;
      case UpdateNextPlayer:
        setGamestatusInGame(GamestatusInGame.Null);
        break;
      case CalculatingPlayerScore:
        setGamestatusInGame(GamestatusInGame.Null);
        break;
    }
  }

  private void setGamestatusInGame(GamestatusInGame aGamestatusInGame)
  {
    gamestatusInGame = aGamestatusInGame;
    if (gamestatus != Gamestatus.InGame && aGamestatusInGame != GamestatusInGame.Null) { setGamestatus(Gamestatus.InGame); }

    // entry actions and do activities
    switch(gamestatusInGame)
    {
      case CreatingDraft:
        // line 31 "../../../../../Gameplay.ump"
        createNextDraft();
        __autotransition115__();
        break;
      case SortingAndRevealingDraft:
        // line 34 "../../../../../Gameplay.ump"
        orderNextDraft(); revealNextDraft();
        __autotransition116__();
        break;
      case UpdateNextPlayer:
        // line 67 "../../../../../Gameplay.ump"
        updateNextPlayer();
        __autotransition117__();
        __autotransition118__();
        break;
      case CalculatingPlayerScore:
        // line 73 "../../../../../Gameplay.ump"
        calculatePlayerScore();
        __autotransition119__();
        break;
    }
  }

  private void exitGamestatusFinalizing()
  {
    switch(gamestatusFinalizing)
    {
      case CalculatingPlayerScore:
        setGamestatusFinalizing(GamestatusFinalizing.Null);
        break;
      case CalculatingRanking:
        setGamestatusFinalizing(GamestatusFinalizing.Null);
        break;
    }
  }

  private void setGamestatusFinalizing(GamestatusFinalizing aGamestatusFinalizing)
  {
    gamestatusFinalizing = aGamestatusFinalizing;
    if (gamestatus != Gamestatus.Finalizing && aGamestatusFinalizing != GamestatusFinalizing.Null) { setGamestatus(Gamestatus.Finalizing); }

    // entry actions and do activities
    switch(gamestatusFinalizing)
    {
      case CalculatingPlayerScore:
        // line 78 "../../../../../Gameplay.ump"
        calculatePlayerScore();
        __autotransition120__();
        break;
      case CalculatingRanking:
        // line 82 "../../../../../Gameplay.ump"
        calculateRanking();
        break;
    }
  }

  public void delete()
  {}


  /**
   * Setter for test setup
   */
  // line 93 "../../../../../Gameplay.ump"
   public void setGamestatus(String status){
    switch (status) {
       	case "CreatingFirstDraft":
       	    gamestatus = Gamestatus.Initializing;
       	    gamestatusInitializing = GamestatusInitializing.CreatingFirstDraft;
       	    break;
       	case "InGame":
       		gamestatus = Gamestatus.InGame;
       		gamestatusInGame = GamestatusInGame.PlacingDomino;
       		break;
       	case "Finalizing":
       		gamestatus = Gamestatus.Finalizing;
       		gamestatusFinalizing = GamestatusFinalizing.CalculatingPlayerScore;
       		break;
       	default:
       	    throw new RuntimeException("Invalid gamestatus string was provided: " + status);
       	}
  }


  /**
   * Guards
   */
  // line 116 "../../../../../Gameplay.ump"
   public boolean isCurrentPlayerTheLastInTurn(){
    return KingdominoController.allDominosInDraftAreSelected(KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft());
  }

  // line 120 "../../../../../Gameplay.ump"
   public boolean isCurrentTurnTheLastInGame(){
    return KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size() >= 12;
  }

  // line 124 "../../../../../Gameplay.ump"
   public boolean isCurrentDominoInKingdom(){
    Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
    	return KingdominoController.getDominoInKingdomByDomino(player.getDominoSelection().getDomino(), player.getKingdom()) != null;
  }


  /**
   * You may need to add more guards here
   * Actions for creating first draft
   */
  // line 135 "../../../../../Gameplay.ump"
   public void shuffleDominoPile(){
    System.out.println("Shuffling Dominoes..");
        KingdominoController.startNewGame();
  }

  // line 140 "../../../../../Gameplay.ump"
   public void generateInitialPlayerOrder(){
    System.out.println("Generating initial player order..");
  }

  // line 144 "../../../../../Gameplay.ump"
   public void createNextDraft(){
    System.out.println("Creating next draft..");
        KingdominoController.createNextDraftOfDominoes(KingdominoApplication.getKingdomino().getCurrentGame().getAllDrafts().size()+1);
  }

  // line 149 "../../../../../Gameplay.ump"
   public void orderNextDraft(){
    System.out.println("Ordering next draft..");
        KingdominoController.orderAndRevealNextDraft();
  }

  // line 154 "../../../../../Gameplay.ump"
   public void revealNextDraft(){
    System.out.println("Revealing next draft: ");
       	List<Domino> dominos = KingdominoApplication.getKingdomino().getCurrentGame().getNextDraft().getIdSortedDominos();
       	List<Integer> ids = new ArrayList<Integer>();
		for(int i = 0; i < 4; i++) {
			ids.add(dominos.get(i).getId());			
		}
        System.out.println(Arrays.toString(ids.toArray()));
  }


  /**
   * Actions for selecting domino
   */
  // line 169 "../../../../../Gameplay.ump"
   public void chooseNextDomino(int id){
    selectValid = KingdominoController.chooseNextDomino(id);
  }


  /**
   * Action for placing domino
   */
  // line 178 "../../../../../Gameplay.ump"
   public void placeDomino(Player player, Domino domino, int x, int y, DirectionKind direction, boolean satisfied){
    placeValid = KingdominoController.placeDomino(player, domino, x, y, direction, satisfied);

     	discardValid = false;
  }

  // line 185 "../../../../../Gameplay.ump"
   public void moveCurrentDomino(DominoInKingdom dom, String movement, Player player){
    KingdominoController.moveCurrentDomino(dom, movement, player);
  }

  // line 190 "../../../../../Gameplay.ump"
   public void rotateCurrentDomino(Player p, Domino domino, String rotation){
    KingdominoController.rotateCurrentDomino(p, domino, rotation);
  }

  // line 194 "../../../../../Gameplay.ump"
   public void discardDomino(Kingdom k, Domino d){
    discardValid = KingdominoController.discardDomino(k,d);
     	placeValid = false;
  }

  // line 199 "../../../../../Gameplay.ump"
   public void initPlacement(){
    Player player = KingdominoApplication.getKingdomino().getCurrentGame().getNextPlayer();
     	KingdominoController.initPlacement(player, player.getDominoSelection().getDomino());
  }

  // line 204 "../../../../../Gameplay.ump"
   public void updateNextPlayer(){
    Domino current = KingdominoController.getNextDominoInDraft(KingdominoApplication.getKingdomino().getCurrentGame().getCurrentDraft());
		if(current != null){
			Player nextPlayer = current.getDominoSelection().getPlayer();
			KingdominoApplication.getKingdomino().getCurrentGame().setNextPlayer(nextPlayer);
			hasNext = true;
		}
		else{
			hasNext = false;
		}
  }

  // line 216 "../../../../../Gameplay.ump"
   public void shiftNextDraft(){
    Game game = KingdominoApplication.getKingdomino().getCurrentGame();
     	game.setCurrentDraft(game.getNextDraft());
  }


  /**
   * Actions for calculating player score
   */
  // line 225 "../../../../../Gameplay.ump"
   public void calculatePlayerScore(){
    System.out.println("Calculating player score...");
     	KingdominoController.calculatePlayerScore();
     	Game game = KingdominoApplication.getKingdomino().getCurrentGame();
     	for(Player player : game.getPlayers()){
     		System.out.println(player.getColor() + " : " + player.getTotalScore());
     	}
  }


  /**
   * Actions for calculating ranking
   */
  // line 237 "../../../../../Gameplay.ump"
   public void calculateRanking(){
    System.out.println("Calculating ranking..");
     	HashMap<Player, Integer> map = KingdominoController.calculateRanking();
     	Game game = KingdominoApplication.getKingdomino().getCurrentGame();
     	for(Player player: game.getPlayers()){
     		System.out.println(player.getColor() + " : " + player.getCurrentRanking());
     	}
  }


  public String toString()
  {
    return super.toString() + "["+
            "selectValid" + ":" + getSelectValid()+ "," +
            "placeValid" + ":" + getPlaceValid()+ "," +
            "discardValid" + ":" + getDiscardValid()+ "," +
            "hasNext" + ":" + getHasNext()+ "]";
  }
}