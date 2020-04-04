package markup;

import java.util.List;

public class UnorderedList extends AbstractList {
    public UnorderedList(List<Itemable> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder result) {
        toHtml(result, "<ul>", "</ul>");
    }
}
