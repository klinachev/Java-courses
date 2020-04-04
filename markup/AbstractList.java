package markup;

import java.util.List;

public abstract class AbstractList extends AbstractMarkup implements Listable {

    protected AbstractList(List<Itemable> elements) {
        super(elements);
    }

    @Override
    public void toMarkdown(StringBuilder result) {
        throw new NoSuchMethodError("Don't use this method");
    }
}
