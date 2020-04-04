package markup;

public interface Markable {
    void toHtml(StringBuilder result);
    void toMarkdown(StringBuilder result);
}
