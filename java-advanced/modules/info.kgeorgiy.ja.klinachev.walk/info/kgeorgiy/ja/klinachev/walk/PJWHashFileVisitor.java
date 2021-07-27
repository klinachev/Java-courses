package info.kgeorgiy.ja.klinachev.walk;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class PJWHashFileVisitor extends SimpleFileVisitor<Path> {
    private final static long HASH_IF_EXCEPTION = 0;
    private final BufferedWriter output;

    PJWHashFileVisitor(BufferedWriter output) {
        this.output = output;
    }

    public void write(long value, String file) throws IOException {
        output.write(String.format("%016x %s%n", value, file));
    }

    public void write(long value, Path file) throws IOException {
        output.write(String.format("%016x %s%n", value, file.toString()));
    }

    private void evaluateAndWriteHash(Path file) throws IOException {
        long hashValue = 0;
        try (InputStream inputStream = Files.newInputStream(file)) {
            byte[] buff = new byte[1024];
            final int bits = 64;
            int size;
            while ((size = inputStream.read(buff)) >= 0) {
                for (int i = 0; i < size; ++i) {
                    hashValue = (hashValue << (bits / 8)) + (buff[i] & 0xff);
                    long high = (0xffL << (bits * 7 / 8)) & hashValue;
                    if (high != 0) {
                        hashValue ^= high >> (bits * 3 / 4);
                        hashValue &= ~high;
                    }
                }
            }
        } catch (IOException e) {
            hashValue = HASH_IF_EXCEPTION;
        }
        write(hashValue, file);
    }

    public void visitOnlyFile(Path file) throws IOException {
        if (Files.isDirectory(file)) {
            write(HASH_IF_EXCEPTION, file);
            return;
        }
        evaluateAndWriteHash(file);
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attrs)
            throws IOException {
        evaluateAndWriteHash(file);
        return FileVisitResult.CONTINUE;
    }

//    @Override
//    public FileVisitResult visitFileFailed(Path file, IOException exc)
//            throws IOException {
//        write(HASH_IF_EXCEPTION, file);
//        return FileVisitResult.CONTINUE;
//    }
}
