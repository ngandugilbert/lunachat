## Getting Started

Welcome to the VS Code Java world. Here is a guideline to help you get started to write Java code in Visual Studio Code.

## Folder Structure

The workspace contains two folders by default, where:

- `src`: the folder to maintain sources
- `lib`: the folder to maintain dependencies

Meanwhile, the compiled output files will be generated in the `bin` folder by default.

> If you want to customize the folder structure, open `.vscode/settings.json` and update the related settings there.

## Dependency Management

The `JAVA PROJECTS` view allows you to manage your dependencies. More details can be found [here](https://github.com/microsoft/vscode-java-dependency#manage-dependencies).

## How to setup the JavaFX modules
- Extract the javafx SDK to prefered location
- Create a configuration in the project, for this project, I used vscode.
- Setup the vmArgs as shown below.👇
```
"vmArgs": "--module-path <Path to javafx sdk> --add-modules javafx.controls,javafx.fxml",
```
### How to setup the environment using IntelliJ
- Steps are the same for getting the sdk and the rest
- Just check this out to see how to set the VM arguments in IntelliJ
- [Setup Intellij for javaFx](https://openjfx.io/openjfx-docs/)


