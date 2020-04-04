package markup;

import java.util.List;

public class Paragraph extends AbstractMarkup implements Listable {
    public Paragraph(List<Elementable> elements) {
        super(elements);
    }

    public void toMarkdown(StringBuilder result) {
        toMarkdown(result, "");
    }

    public void toHtml(StringBuilder result) {
        toHtml(result, "", "");
    }
}
