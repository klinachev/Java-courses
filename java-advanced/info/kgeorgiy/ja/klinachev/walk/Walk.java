package info.kgeorgiy.ja.klinachev.walk;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Walk {
    private final Path input;
    private final Path output;

    public Walk(String input, String output) throws WalkException {
        this.input = tryPathsGet(input);
        this.output = tryPathsGet(output);
    }

    public static Path tryPathsGet(String path) throws WalkException {
        try {
            return Paths.get(path);
        } catch (InvalidPathException e) {
            throw new WalkException("String: \"" + path + "\" cannot be converted to a java.nio.file.Path");
        }
    }

    protected void visitFile(Path file, PJWHashFileVisitor walker) throws IOException {
        walker.visitOnlyFile(file);
    }

    private void createOutputFileDirectory() throws WalkException {
        Path parent;
        if ((parent = output.getParent()) != null) {
            try {
                Files.createDirectories(parent);
            } catch (IOException e) {
                throw new WalkException("Can't create output file: ", e);
            }
        }
    }

    public void run() throws WalkException {
        createOutputFileDirectory();
        try (BufferedReader reader = Files.newBufferedReader(input)) {
            try (BufferedWriter writer = Files.newBufferedWriter(output)) {
                PJWHashFileVisitor walker = new PJWHashFileVisitor(writer);
                try {
                    String filePath;
                    while ((filePath = reader.readLine()) != null) {
                        Path file;
                        try {
                            file = tryPathsGet(filePath);
                            if (!Files.exists(file)) {
                                walker.write(0, filePath);
                                continue;
                            }
                        } catch (WalkException e) { // NOTE: bad way to handle inalid paths
                            walker.write(0, filePath);
                            continue;
                        }
                        try {
                            visitFile(file, walker);
                        } catch (IOException e) {
                            throw new WalkException("Can't write to the output file: ", e);
                        }
                    }
                } catch (IOException e) {
                    throw new WalkException("Can't read from input file: ", e);
                }
            } catch (IOException e) {
                throw new WalkException("Can't open output file: ", e);
            }
        } catch (IOException e) {
            throw new WalkException("Can't open input file: ", e);
        }
    }

    public static void checkArgs(String[] args) throws WalkException {
        if (args == null || args.length != 2 || args[0] == null || args[1] == null) {
            throw new WalkException("2 arguments expected");
        }
    }


    public static void main(String[] args) {
        try {
            checkArgs(args);
            Walk walk = new Walk(args[0], args[1]);
            walk.run();
        } catch (WalkException e) {
            System.out.println(e.getMessage());
        }
    }
}
