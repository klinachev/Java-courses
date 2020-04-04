package markup;

import java.util.List;

public class OrderedList extends AbstractList {
    public OrderedList(List<Itemable> elements) {
        super(elements);
    }

    @Override
    public void toHtml(StringBuilder result) {
        toHtml(result, "<ol>", "</ol>");
    }
}
