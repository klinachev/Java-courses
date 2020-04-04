package markup;

import java.util.List;

public class ListItem extends AbstractMarkup implements Itemable {
    public ListItem(List<Listable> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder result) {
        toHtml(result, "<li>", "</li>");
    }

    @Override
    public void toMarkdown(StringBuilder result) {

    }
}
