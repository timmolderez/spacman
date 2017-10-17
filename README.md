# spacman
<img align=right src="https://raw.githubusercontent.com/timmolderez/spacman/2d082e9e18531adf68ac424ae7d7e596e186cd14/src/main/resources/sprite/pacman_in_space.jpg"  ></img>
Spacman is a Scala port of the [jpacman](https://github.com/SERG-Delft/jpacman-framework) project. 
Like jpacman, spacman is an implementation of the Pacman game, with a test suite included, intended to be used in Software Engineering courses.

## Importing spacman in IntelliJ IDEA:

- Make sure you have [IntelliJ IDEA](https://www.jetbrains.com/idea/) installed, with the Scala plugin.
- Clone the project to your computer: ```git clone https://github.com/timmolderez/spacman.git```
- Start IntelliJ IDEA and click "Import Project"
- Select the folder where spacman was cloned.
- Select "Import project from external model" and choose "Gradle".
- Click "Next", then "Finish", and wait for the import process to finish.
- It is possible that the Project SDK is not set automatically. To do this, go to File > Project Structure... , and change the "Project SDK:" field to e.g. Java version 1.8.
- Once the import process is finished, you can try out spacman: in the project explorer, go to spacman/src/main/scala/be/vub/spacman/Launcher. Right-click this file and choose "Run 'Launcher.main()'".
