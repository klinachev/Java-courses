package markup;

import java.util.List;

public class Strikeout extends AbstractMarkup implements Elementable {
    public Strikeout(List<? extends Elementable> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder result) {
        toMarkdown(result, "~");
    }

    @Override
    public void toHtml(StringBuilder result) {
        toHtml(result, "<s>", "</s>");
    }

}
