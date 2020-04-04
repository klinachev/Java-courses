package markup;

import java.util.List;

public class Emphasis extends AbstractMarkup implements Elementable {
    public Emphasis(List<? extends Elementable> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder result) {
        toMarkdown(result, "*");
    }

    @Override
    public void toHtml(StringBuilder result) {
        toHtml(result, "<em>", "</em>");
    }

}
