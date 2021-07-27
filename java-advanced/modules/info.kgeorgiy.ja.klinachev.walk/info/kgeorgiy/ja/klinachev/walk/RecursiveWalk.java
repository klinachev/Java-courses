package info.kgeorgiy.ja.klinachev.walk;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RecursiveWalk extends Walk {

    public RecursiveWalk(String input, String output) throws WalkException {
        super(input, output);
    }

    @Override
    protected void visitFile(Path file, PJWHashFileVisitor walker) throws IOException {
        Files.walkFileTree(file, walker);
    }

    public static void main(String[] args) {
        try {
            checkArgs(args);
            Walk walk = new RecursiveWalk(args[0], args[1]);
            walk.run();
        } catch (WalkException e) {
//            System.out.println(e.getMessage());
        }
    }
}
