package me.akraml.loader.server;

import me.akraml.loader.LoaderBackend;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;

/**
 * This server handles
 */
public final class LoaderServer {

    private final ServerSocket serverSocket;

    public LoaderServer(final int bindingPort) throws IOException {
        serverSocket = new ServerSocket(bindingPort);
    }

    private void startHandler() {
        CompletableFuture.runAsync(() -> {
            while (!serverSocket.isClosed()) {
                try (final Socket socket = serverSocket.accept()) {
                    // Log the received connection.
                    final String hostname = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
                    LoaderBackend.getLogger().info("Received connection from /"
                            + hostname);

                    // Declare input & output stream variables.
                    final DataInputStream inputStream = new DataInputStream(socket.getInputStream());
                    final DataOutputStream outputStream = new DataOutputStream(socket.getOutputStream());
                } catch (final Exception exception) {
                    exception.printStackTrace(System.err);
                }
            }
        }).exceptionally(throwable -> {
            LoaderBackend.getLogger().severe("An error occurred: " + throwable.getMessage());
            throwable.printStackTrace(System.err);
            return null;
        });
    }

}
