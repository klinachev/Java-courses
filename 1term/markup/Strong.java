package markup;

import java.util.List;

public class Strong extends AbstractMarkup implements Elementable {
    public Strong(List<? extends Elementable> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder result) {
        toMarkdown(result, "__");
    }

    @Override
    public void toHtml(StringBuilder result) {
        toHtml(result, "<strong>", "</strong>");
    }

}
