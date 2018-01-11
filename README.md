# spacman
<img align=right src="https://raw.githubusercontent.com/timmolderez/spacman/2d082e9e18531adf68ac424ae7d7e596e186cd14/src/main/resources/sprite/pacman_in_space.jpg"  ></img>
Spacman is a Scala port of the [jpacman](https://github.com/SERG-Delft/jpacman-framework) project. 
Like jpacman, spacman is an implementation of the Pacman game, with a test suite included, intended for use in Software Engineering courses.

It is currently used at the Vrije Universiteit Brussel to give students an introduction to unit testing, functional testing, mocking and design by contract. The test suite of spacman is written using the [ScalaTest](http://www.scalatest.org/) and [ScalaMock](http://scalamock.org) frameworks.

## Importing spacman in IntelliJ IDEA:

- Make sure you have [IntelliJ IDEA](https://www.jetbrains.com/idea/) installed, with the Scala plugin.
- Clone the project to your computer: ```git clone https://github.com/timmolderez/spacman.git```
- Start IntelliJ IDEA and click "Import Project"
- Select the folder where spacman was cloned.
- Select "Import project from external model" and choose "Gradle".
- Click "Next", then "Finish", and wait for the import process to finish. (During the import process, you may get a warning dialog "The modules below are not imported from Gradle anymore." You can just click "OK" to continue.)
- It is possible that the Project SDK is not set automatically. To do this, go to File > Project Structure... , and change the "Project SDK:" field to e.g. Java version 1.8.
- Once the import process is finished, you can try out spacman: in the project explorer, go to spacman/src/main/scala/be/vub/spacman/Launcher. Right-click this file and choose "Run 'Launcher.main()'".
