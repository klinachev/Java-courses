package mnk;

public interface Board {
    ProxyPosition getPosition();
    Cell getCell();
    Result makeMove(Move move);
}
