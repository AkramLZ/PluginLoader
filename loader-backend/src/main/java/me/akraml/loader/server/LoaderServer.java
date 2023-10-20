package me.akraml.loader.server;

import me.akraml.loader.LoaderBackend;
import me.akraml.loader.utility.FileUtils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * This server handles incoming connections and requests to load the requested plugin.
 */
public final class LoaderServer {

    private final ServerSocket serverSocket;
    private final Thread serverThread;
    private final String fileName, mainClassName;

    /**
     * Constructs a new instance of Loader server class and start the handler.
     *
     * @param bindingPort  Port to bind the loader server in.
     * @param fileName     Name of the file to be sent to the client.
     * @param mainClass    The main class of the injected plugin.
     * @throws IOException If an I/O issue occurs.
     */
    public LoaderServer(final int bindingPort,
                        final String fileName,
                        final String mainClass) throws IOException {
        // Initialize local variables
        this.serverSocket  = new ServerSocket(bindingPort);
        this.fileName      = fileName;
        this.mainClassName = mainClass;

        // Start the server
        this.serverThread = new Thread("LoaderServer-Thread") {
            @Override
            public void run() {
                startHandler();
            }
        };
    }

    private void startHandler() {
        while (!serverSocket.isClosed()) {
            try (final Socket socket = serverSocket.accept()) {
                // Log the received connection.
                final String hostname = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                LoaderBackend.getLogger().info("Received connection from /"
                        + hostname);

                // Declare output stream variables.
                final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());

                // Send main class name to the client.
                outputStream.writeUTF(mainClassName);

                // Declare file to be sent, convert it into bytes then send it.
                final File file = new File(fileName);
                final byte[] bytes = FileUtils.toByteArray(file);
                outputStream.writeInt(bytes.length);
                outputStream.write(bytes);
            } catch (final Exception exception) {
                if (exception instanceof SocketException) return;
                exception.printStackTrace(System.err);
            }
        }
    }

    public void shutdownServer() {
        try {
            serverSocket.close();
            serverThread.interrupt();
        } catch (final Exception exception) {
            LoaderBackend.getLogger().severe("An error occurred when trying to shut down loader server");
            exception.printStackTrace(System.err);
        }
    }

}
