package markup;

public class Text implements Elementable {
    private String text;

    public Text(String text) {
        this.text = text;
    }

    public void toMarkdown(StringBuilder result) {
        result.append(text);
    }

    public void toHtml(StringBuilder result) {
        result.append(text);
    }
}