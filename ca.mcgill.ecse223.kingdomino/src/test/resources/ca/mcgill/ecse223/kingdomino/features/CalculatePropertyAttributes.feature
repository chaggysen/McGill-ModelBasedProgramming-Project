Feature: Calculate property attributes
  I want the Kingdomino app to automatically calculate 
  the size of a property and 
  the total number of crowns in that property. (F20)

  Background: 
    Given the game is initialized for calculate property attributes
    Given the player's kingdom has the following dominoes:
      | id | dominodir | posx | posy |
      | 21 | left      |    2 |    0 |
      | 16 | down      |    0 |   -1 |
      | 39 | left      |    2 |   -2 |

  Scenario: Calculate initial property parameters
    When calculate property attributes is initiated
    Then the player shall have a total of 5 properties
    Then the player shall have properties with the following attributes:
      | type  | size | crowns |
      | W |    1 |      1 |
      | G |    1 |      0 |
      | W |    1 |      0 |
      | S |    2 |      1 |
      | G |    1 |      0 |

  Scenario Outline: Calculate updated property score with no new property and no new crowns
    Given the player's kingdom also includes the domino <id> at position <posx>:<posy> with the direction "<dominodir>"
    When calculate property attributes is initiated
    Then the player shall have a total of 5 properties
    Then the player shall have properties with the following attributes:
      | type  | size | crowns |
      | W |    3 |      1 |
      | G |    1 |      0 |
      | W |    1 |      0 |
      | S |    2 |      1 |
      | G |    1 |      0 |

    Examples: 
      | id | dominodir | posx | posy |
      |  1 | up        |    1 |    1 |

  Scenario Outline: Calculate updated property score with new property with new crowns
    Given the player's kingdom also includes the domino <id> at position <posx>:<posy> with the direction "<dominodir>"
    When calculate property attributes is initiated
    Then the player shall have a total of 6 properties
    Then the player shall have properties with the following attributes:
      | type     | size | crowns |
      | W |    1 |      1 |
      | G |    1 |      0 |
      | W |    1 |      0 |
      | M |    1 |      2 |
      | S |    3 |      1 |
      | G |    1 |      0 |

    Examples: 
      | id | dominodir | posx | posy |
      | 46 | right     |    1 |   -1 |

  Scenario Outline: Calculate updated property score with merged properties
    Given the player's kingdom also includes the domino <id> at position <posx>:<posy> with the direction "<dominodir>"
    When calculate property attributes is initiated
    Then the player shall have a total of 3 properties
    Then the player shall have properties with the following attributes:
      | type  | size | crowns |
      | G |    3 |      2 |
      | W |    3 |      1 |
      | S |    2 |      1 |

    Examples: 
      | id | dominodir | posx | posy |
      | 41 | right     |    1 |   -1 |
