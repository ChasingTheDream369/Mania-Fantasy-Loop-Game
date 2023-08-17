# Loop-Mania

An interactive and enjoyable game in which the character must achieve various objectives throughout a succession of runs in order to complete and win the game. The game has a variety of game modes as well as random game goals and enemy movements to provide the player with a unique situation upon every run. The game makes use of Object Oriented design patterns such as **Observer, State, Template, Abstract Factory, Strategy, Singleton and Decorator** are used in development to ensure that code is open to extension but closed to modification. Java serves as the backend and JavaFx as fronted.

<img width="577" alt="simple_example_annotated" src="https://user-images.githubusercontent.com/50875291/179395925-fea89ff6-5d19-44e3-8415-fd24f318edbe.png">

The game world contains paths made up of tiles that form loops. The character starts at the hero's castle and automatically moves clockwise from position to position along this path. The game world includes buildings, enemies, gold, healing potions, and characters. For more information on Gold Potions and Health Potions, see the item list below. Enemies  move along the path, and the method  depends on the type of enemy. It is important to note that in this document, human players and characters are different. A character is an in-game hero who wants to help a human player win the game and is represented by an image of the person. The character completes many interactions such as Automatically move and fight without input from a human player. A human player is a user who is playing a gaming application. Human players want to help their characters achieve all their goals and win the game. You can help your character win the game by building buildings, equipping items, buying and selling items, consuming health potions, and pausing the game.

## Design

![download (5)](https://user-images.githubusercontent.com/50875291/179396275-9d3bd8b3-0fc7-4f7f-9865-6cb15bd5a082.png)
