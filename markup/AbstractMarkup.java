package markup;

import java.util.List;

public abstract class AbstractMarkup implements Markable {
    private List<? extends Markable> elements;

    public AbstractMarkup(List<? extends Markable> elements) {
        this.elements = elements;
    }

    protected void toMarkdown(StringBuilder result, String border) {
        result.append(border);
        for (Markable cur : elements) {
            cur.toMarkdown(result);
        }
        result.append(border);
    }

    protected void toHtml(StringBuilder result, String leftBorder, String rightBorder) {
        result.append(leftBorder);
        for (Markable cur : elements) {
            cur.toHtml(result);
        }
        result.append(rightBorder);
    }
}
