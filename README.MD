# Simple Plugin Loader
This project is an example to explain how external loaders work.

## Compatability
This project requires at least spigot (or any related fork) 1.8+ and Java version 8+

## Usage
### Loader server
- Build the loader backend server using shadowJar. `gradlew :loader-backend:shadowJar`
- Start the server with the required arguments. `Usage: java -jar loader-backend.jar --port=<port> --file=<jar file> --main-class=<main class>`
- - **port**: The port of the backend server to bind in. `Example: --port=5003`
- - **file**: The path to the jar to inject in target servers.`Example: --file=injected-plugin.jar`
- - **main-class**: The path to the main class of the injected jar. `Example: --main-class=me.akraml.loader.plugin.ExampleInjectedPlugin`
### Loader plugin
- Build the plugin using shadowJar. `gradlew :loader-plugi:shadowJar`
- Drop it in your server plugins folder.
- Configure it to match the loader backend server.
- Start the server.

## Disclaimer
This project **IS NOT MEANT** to be used for commercial use, it is just an explanation to the external loader theory.
If you want to use it in your project, you need to implement your own security system.