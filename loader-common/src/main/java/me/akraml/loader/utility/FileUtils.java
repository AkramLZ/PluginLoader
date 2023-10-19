package me.akraml.loader.utility;

import lombok.NonNull;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is a utility class which handles all file-related functions.
 */
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class FileUtils {

    /**
     * Converts the provided file into a bytes array.
     *
     * @param file File to convert.
     * @return Converted bytes array.
     * @throws IOException If an I/O issue occurs, like failing to read the file due to insufficient permissions.
     */
    public static byte[] toByteArray(@NonNull final File file) throws IOException {
        // If the file does not exist, then prevent conversion.
        if (!file.exists()) throw new IllegalStateException("File cannot be null");

        // Therefore, start converting process.
        final byte[] array = new byte[(int)file.length()];
        try (final FileInputStream fileInputStream = new FileInputStream(file)) {
            fileInputStream.read(array);
        }
        return array;
    }

    /**
     * Writes the provided bytes array into a file.
     *
     * @param toWriteIn File to write bytes in.
     * @param byteArray The bytes array to write.
     * @throws IOException If any I/O issue occurs, such as failing to create the file or not having enough permissions
     *                     to read/write.
     */
    public static void writeBytesToFile(final File toWriteIn,
                                        final byte[] byteArray) throws IOException {
        // If the file doesn't exist, then create it.
        if (!toWriteIn.exists()) toWriteIn.createNewFile();

        // Write bytes into the file.
        try (final FileOutputStream fileOutputStream = new FileOutputStream(toWriteIn)) {
            fileOutputStream.write(byteArray);
        }
    }

    /**
     * Converts the provided DataInputStream into a file.
     *
     * @see #writeBytesToFile(File, byte[])
     */
    public static void writeDataInputStreamToFile(final File toWriteIn,
                                                  final DataInputStream stream) throws IOException {
        final List<Byte> byteList = new ArrayList<>();
        for (int i = stream.read(); i != -1; i = stream.read()) {
            byteList.add((byte) i);
        }
        // Convert byte list into a byte array
        final byte[] byteArray = new byte[byteList.size()];
        for (int i = 0; i < byteList.size(); i++) {
            byteArray[i] = byteList.get(i);
        }
        // Apply everything to writeBytesToFile
        writeBytesToFile(toWriteIn, byteArray);
    }

}
