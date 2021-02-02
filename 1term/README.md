
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html><head>
<wbr>
<table id="body"><tr><td id="main">
<h3 id="homework-1">
Домашнее задание 1. Запусти меня!</h3><p><a href="http://www.kgeorgiy.info/git/geo/prog-intro-2020/">Тесты к домашним заданиям</a></p><ol><li>
            Установите
            <a href="https://adoptopenjdk.net/?variant=openjdk14&amp;jvmVariant=hotspot">JDK 11+</a></li><li>
            Скопируйте один из вариантов <code>HelloWorld</code>,
            рассмотренных на лекции.
        </li><li>
            Откомпилируйте <code>HelloWorld.java</code> и получите
            <code>HelloWorld.class</code>.
        </li><li>
            Запустите <code>HelloWorld</code> и проверьте его работоспособность.
        </li><li>
            Создайте скрипт, компилирующий и запускающий
            <code>HelloWorld</code> из командной строки.
        </li></ol><h3 id="homework-2">Домашнее задание 2. Сумма чисел</h3><ol><li>
            Разработайте класс <code>Sum</code>, который при запуске из командной
            строки будет складывать переданные в качестве аргументов целые
            числа и выводить их сумму на консоль.
        </li><li>
            Примеры запуска программы:
            <dl><dt><code>java Sum 1 2 3</code></dt><dd>Результат: 6</dd><dt><code>java Sum 1 2 -3</code></dt><dd>Результат: 0</dd><dt><code>java Sum "1 2 3"</code></dt><dd>Результат: 6</dd><dt><code>java Sum "1 2" " 3"</code></dt><dd>Результат: 6</dd><dt><code>java Sum "   "</code></dt><dd>Результат: 0</dd></dl>
            Аргументы могут содержать:
            <ul><li>цифры;</li><li>знаки <code>+</code> и <code>-</code>;</li><li>произвольные <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Character.html#isWhitespace(char)">пробельные символы</a>.</li></ul></li><li>
            При выполнении задания можно считать, что для представления входных данных
            и промежуточных результатов достаточен тип <code>int</code>.
        </li><li>
            Перед выполнением задания ознакомьтесь с документацией к классам
            <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/String.html">String</a>
            и
            <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Integer.html">Integer</a>.
        </li><li>
            Для отладочного вывода используйте
            <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#err">System.err</a>,
            тогда он будет игнорироваться проверяющей программой.
        </li></ol><h3 id="homework-3">Домашнее задание 3. Реверс</h3><ol><li>
            Разработайте класс <code>Reverse</code>,
            читающий числа из
            <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#in">стандартного ввода</a>,
            и выводящий их на
            <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/System.html#out">стандартный вывод</a>
            в обратном порядке.
        </li><li>
            В каждой строке входа содержится некоторое количество целых чисел
            (может быть 0). Числа разделены пробелами. Каждое число
            помещается в тип <code>int</code>.
        </li><li>
            Порядок строк в выходе должен быть обратным по сравнению
            с порядком строк во входе.
            Порядок чисел в каждой строке так же должен быть обратным к порядку
            чисел во входе.
        </li><li>
            Вход содержит не более 10<sup>6</sup> чисел и строк.
        </li><li>
            Для чтения чисел используйте класс
            <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/Scanner.html">Scanner</a>.
        </li><li>
            Примеры работы программы:
            <table class="black"><tr><th>Ввод</th><th>Вывод</th></tr><tr><td><pre>
1 2
3
</pre></td><td><pre>
3
2 1
</pre></td></tr><tr><td><pre>
3
2 1
</pre></td><td><pre>
1 2
3
</pre></td></tr><tr><td><pre>
1

2 -3
</pre></td><td><pre>
-3 2

1
</pre></td></tr><tr><td><pre>
1     2
3     4
</pre></td><td><pre>
4 3
2 1
</pre></td></tr></table></li></ol><h3 id="homework-4">Домашнее задание 4. Статистика слов</h3><ol><li>
            Разработайте класс <code>WordStat</code>,
            который будет подсчитывать статистику встречаемости слов
            во входном файле.
        </li><li>
            Словом называется непрерывная последовательность букв,
            апострофов и тире (Unicode category
            <a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/Character.html#DASH_PUNCTUATION">Punctuation, Dash</a>).
            Для подсчета статистики, слова приводятся к нижнему регистру.
        </li><li>
            Выходной файл должен содержать все различные слова,
            встречающиеся во входном файле, в порядке их появления.
            Для каждого слова должна быть выведена одна строка,
            содержащая слово и число его вхождений во входной файл.
        </li><li>
            Имена входного и выходного файла задаются в качестве аргументов
            командной строки. Кодировка файлов: UTF-8.
        </li><li>
            Примеры работы программы:
            <table class="black"><tr><th>Входной файл</th><th>Выходной файл</th></tr><tr><td style="vertical-align: top"><pre>
To be, or not to be, that is the question:
</pre></td><td><pre>
to 2
be 2
or 1
not 1
that 1
is 1
the 1
question 1
</pre></td></tr><tr><td style="vertical-align: top"><pre>
Monday's child is fair of face.
Tuesday's child is full of grace.
</pre></td><td><pre>
monday's 1
child 2
is 2
fair 1
of 2
face 1
tuesday's 1
full 1
grace 1
</pre></td></tr><tr><td style="vertical-align: top"><pre>
Шалтай-Болтай
Сидел на стене.
Шалтай-Болтай
Свалился во сне.
</pre></td><td><pre>
шалтай-болтай 2
сидел 1
на 1
стене 1
свалился 1
во 1
сне 1
</pre></td></tr></table></li></ol><h3 id="homework-5">Домашнее задание 5. Свой сканер</h3><ol><li>
            Реализуйте свой аналог класса <code>Scanner</code> на
            основе <code>Reader</code>.
        </li><li>
            Примените разработанный <code>Scanner</code> для
            решения задания &laquo;Реверс&raquo;.
        </li><li>
            Примените разработанный <code>Scanner</code> для
            решения задания &laquo;Статистика слов&raquo;.
        </li><li>
            Код, управляющий чтением должен быть общим.
        </li><li><em>Сложный вариант</em>.
            Код, выделяющий числа и слова должен быть общим.
        </li><li>
            При реализации блочного чтения обратите внимание на
            слова/числа, пересекающие границы блоков,
            особенно &mdash; больше одного раза.
        </li></ol><h3 id="homework-6">Домашнее задание 6. Статистика слов++</h3><ol><li>
            Разработайте класс <code>WordStatIndex</code>,
            который будет подсчитывать статистику встречаемости слов
            во входном файле.
        </li><li>
            Словом называется непрерывная последовательность букв,
            апострофов и тире (Unicode category Punctuation, Dash).
            Для подсчета статистики, слова приводятся к нижнему регистру.
        </li><li>
            Выходной файл должен содержать все различные слова,
            встречающиеся во входном файле, в порядке их появления.
            Для каждого слова должна быть выведена одна строка,
            содержащая слово, число его вхождений во входной файл и
            номера вхождений этого слова среди всех слов во входном файле.
        </li><li>
            Имена входного и выходного файла задаются в качестве аргументов
            командной строки. Кодировка файлов: UTF-8.
        </li><li>
            Программа должна работать за линейное от размера входного файла
            время.
        </li><li>
            Для реализации программы используйте Collections Framework.
        </li><li><em>Сложный вариант.</em>
            Реализуйте и примените класс <code>IntList</code>,
            компактно хранящий список целых чисел.
        </li><li>
            Примеры работы программы:
            <table class="black"><tr><th>Входной файл</th><th>Выходной файл</th></tr><tr><td style="vertical-align: top"><pre>
    To be, or not to be, that is the question:
</pre></td><td><pre>
    to 2 1 5
    be 2 2 6
    or 1 3
    not 1 4
    that 1 7
    is 1 8
    the 1 9
    question 1 10
</pre></td></tr><tr><td style="vertical-align: top"><pre>
    Monday's child is fair of face.
    Tuesday's child is full of grace.
</pre></td><td><pre>
    monday's 1 1
    child 2 2 8
    is 2 3 9
    fair 1 4
    of 2 5 11
    face 1 6
    tuesday's 1 7
    full 1 10
    grace 1 12
</pre></td></tr><tr><td style="vertical-align: top"><pre>
    Шалтай-Болтай
    Сидел на стене.
    Шалтай-Болтай
    Свалился во сне.
</pre></td><td><pre>
    шалтай-болтай 2 1 5
    сидел 1 2
    на 1 3
    стене 1 4
    свалился 1 6
    во 1 7
    сне 1 8
</pre></td></tr></table></li></ol><h3 id="homework-7">Домашнее задание 7. Разметка</h3><ol><li>
            Разработайте набор классов для текстовой разметки.
        </li><li>
            Класс <tt>Paragraph</tt> может содержать произвольное
            число других элементов разметки и текстовых элементов.
        </li><li>
            Класс <tt>Text</tt> &ndash; текстовый элемент.
        </li><li>
            Классы разметки <tt>Emphasis</tt>, <tt>Strong</tt>, <tt>Strikeout</tt>
            &ndash; выделение, сильное выделение и зачеркивание.
            Элементы разметки могут содержать произвольное
            число других элементов разметки и текстовых элементов.
        </li><li>
            Все классы должны реализовывать метод <tt>toMarkdown(<a href="https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/lang/StringBuilder.html">StringBuilder</a>)</tt>,
            которой должен генерировать <a href="https://ru.wikipedia.org/wiki/Markdown">Markdown</a>-разметку
            по следующим правилам:
            <ol><li>
                    текстовые элементы выводятся как есть;
                </li><li>
                    выделенный текст окружается символами '<tt>*</tt>';
                </li><li>
                    сильно выделенный текст окружается символами '<tt>__</tt>';
                </li><li>
                    зачеркнутый текст окружается символами '<tt>~</tt>'.
                </li></ol></li><li>
            Следующий код должен успешно компилироваться:
<pre>
    Paragraph paragraph = new Paragraph(List.of(
        new Strong(List.of(
            new Text("1"),
            new Strikeout(List.of(
                new Text("2"),
                new Emphasis(List.of(
                    new Text("3"),
                    new Text("4")
                )),
                new Text("5")
            )),
            new Text("6")
        ))
    ));
</pre>
            Вызов <tt>paragraph.toMakdown(new StringBuilder())</tt>
            должен заполнять переданный <tt>StringBuilder</tt>
            следующим содержимым:
<pre>
    __1~2*34*5~6__
</pre></li><li>
            Разработанные классы должны находиться в пакете <tt>markup</tt>.
        </li></ol><h3 id="homework-8">Домашнее задание 8. Чемпионат</h3><ol><li>
            Решите как можно больше задач Чемпионата северо-запада России
            по программированию 2019.
        </li><li>
            Материалы соревнования:
            <ul><li><a href="https://pcms.itmo.ru/">PCMS</a>: Java. North-Western Russia Regional Contest - 2019</li><li><a href="https://nerc.itmo.ru/archive/2019/northern/nwrrc-2019-statements.pdf">Условия задач</a></li><li><a href="https://nerc.itmo.ru/archive/2019/northern/nwrrc-2019-tutorials.pdf">Разбор задач</a></li></ul></li><li>
            Задачи для решения
            <table><tr><th colspan="2">Задача</th><th>Тема</th><th>Сложность</th></tr><tr><td>A.</td><td>Accurate Movement</td><td>Формула</td><td>5</td></tr><tr><td>B.</td><td>Bad Treap</td><td>Циклы</td><td>10</td></tr><tr><td>C.</td><td>Cross-Stitch</td><td>Графы</td><td>40</td></tr><tr><td>D.</td><td>Double Palindrome</td><td>Массивы</td><td>40</td></tr><tr><td>E.</td><td>Equidistant</td><td>Деревья</td><td>30</td></tr><tr><td>H.</td><td>High Load Database</td><td>Массивы</td><td>20</td></tr><tr><td>I.</td><td>Ideal Pyramid</td><td>Циклы</td><td>15</td></tr><tr><td>J.</td><td>Just the Last Digit</td><td>Матрицы</td><td>20</td></tr><tr><td>K.</td><td>King&rsquo;s Children</td><td>Массивы</td><td>40</td></tr><tr><td>M.</td><td>Managing Difficulties</td><td>Коллекции</td><td>10</td></tr></table></li><li>Рекомендуемое время выполнения задания: 3 часа</li></ol><h3 id="homework-9">Домашнее задание 9. Игра m,n,k</h3><ol><li>
            Реализуйте <a href="https://en.wikipedia.org/wiki/M,n,k-game">игру m,n,k</a>.
        </li><li>
            Добавьте обработку ошибок ввода пользователя.
        </li><li><em>Простая версия</em>.
            Проверку выигрыша можно производить за <i>O(nmk)</i>.
        </li><li><em>Сложная версия</em>.
            <ul><li>
                    Проверку выигрыша нужно производить за <i>O(k)</i>.
                </li><li>
                   Предотвратите жульничество: у игрока не должно быть
                   возможности достать <code>Board</code>
                   из <code>Position</code>.
                </li></ul></li><li><em>Бонусная версия</em>.
            Реализуйте <code>Winner</code> &mdash; игрок, который
            выигрывает всегда, когда это возможно
            (против любого соперника).
        </li></ol><h3 id="homework-10">Домашнее задание 10. Выражения</h3><ol><li>
            Разработайте классы <code>Const</code>, <code>Variable</code>, <code>Add</code>, <code>Subtract</code>, <code>Multiply</code>,
            <code>Divide</code> для вычисления выражений с одной переменной
            в типе <code>int</code>.
        </li><li>
            Классы должны позволять составлять выражения вида
            <pre>
new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).evaluate(5)
            </pre>
            При вычислении такого выражения вместо каждой переменной подставляется значение,
            переданное в качестве параметра методу <code>evaluate</code> (на данном этапе
            имена переменных игнорируются). Таким образом, результатом вычисления
            приведенного примера должно стать число 7.
        </li><li>
            Метод <code>toString</code> должен выдавать запись выражения
            в полноскобочной форме. Например
            <pre>
new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).toString()
            </pre>
            должен выдавать <code>((2 * x) - 3)</code>.
        </li><li><em>Сложный вариант.</em>
            Метод <code>toMiniString</code>
            должен выдавать выражение с минимальным числом скобок.
            Например
            <pre>
new Subtract(
    new Multiply(
        new Const(2),
        new Variable("x")
    ),
    new Const(3)
).toMiniString()
            </pre>
            должен выдавать <code>2 * x - 3</code>.
        </li><li>
            Реализуйте метод <code>equals</code>, проверяющий,
            что два выражения совпадают. Например,
            <pre>
new Multiply(new Const(2), new Variable("x"))
    .equals(new Multiply(new Const(2), new Variable("x")))
            </pre>
            должно выдавать <code>true</code>, а
            <pre>
new Multiply(new Const(2), new Variable("x"))
    .equals(new Multiply(new Variable("x"), new Const(2)))
            </pre>
            должно выдавать <code>false</code>.
        </li><li>
            Для тестирования программы должен быть создан класс <code>Main</code>, который
            вычисляет значение выражения <code>x<sup>2</sup>&minus;2x+1</code>, для
            <code>x</code>, заданного в командной строке.
        </li><li>
            При выполнении задания следует обратить внимание на:
            <ul><li>
                    Выделение общего интерфейса создаваемых классов.
                </li><li>
                    Выделение абстрактного базового класса для бинарных операций.
                </li></ul></li></ol><h3 id="homework-11">Домашнее задание 11. Разбор выражений</h3><ol><li>
            Доработайте предыдущее домашнее задание, так что бы
            выражение строилось по записи вида
            <pre>x * (x - 2)*x + 1</pre></li><li>
            В записи выражения могут встречаться:
            умножение <code>*</code>, деление <code>/</code>,
            сложение <code>+</code>, вычитание <code>-</code>,
            унарный минус <code>-</code>,
            целочисленные константы (в десятичной системе счисления, которые помещаются в 32-битный знаковый целочисленный тип),
            круглые скобки, переменные (<code>x</code>) и произвольное число пробельных символов
            в любом месте (но не внутри констант).
        </li><li>
            Приоритет операторов, начиная с наивысшего
            <ol><li>унарный минус;</li><li>умножение и деление;</li><li>сложение и вычитание.</li></ol></li><li>
            Разбор выражений рекомендуется производить
            <a href="https://ru.wikibooks.org/wiki/%D0%A0%D0%B5%D0%B0%D0%BB%D0%B8%D0%B7%D0%B0%D1%86%D0%B8%D0%B8_%D0%B0%D0%BB%D0%B3%D0%BE%D1%80%D0%B8%D1%82%D0%BC%D0%BE%D0%B2/%D0%9C%D0%B5%D1%82%D0%BE%D0%B4_%D1%80%D0%B5%D0%BA%D1%83%D1%80%D1%81%D0%B8%D0%B2%D0%BD%D0%BE%D0%B3%D0%BE_%D1%81%D0%BF%D1%83%D1%81%D0%BA%D0%B0">методом рекурсивного спуска</a>.
            Алгоритм должен работать за линейное время.
        </li></ol><h3 id="homework-12">Домашнее задание 12. Обработка ошибок</h3><ol><li>
            Добавьте в программу вычисляющую выражения обработку ошибок, в том числе:
            <ul><li>ошибки разбора выражений;</li><li>ошибки вычисления выражений.</li></ul></li><li>
            Для выражения <code>1000000*x*x*x*x*x/(x-1)</code> вывод программы
            должен иметь следующий вид:
            <pre>
x       f
0       0
1       division by zero
2       32000000
3       121500000
4       341333333
5       overflow
6       overflow
7       overflow
8       overflow
9       overflow
10      overflow
            </pre>
            Результат <code>division by zero</code> (<code>overflow</code>) означает,
            что в процессе вычисления произошло деление на ноль (переполнение).
        </li><li>
            При выполнении задания следует обратить внимание на дизайн и обработку исключений.
        </li><li>
            Человеко-читаемые сообщения об ошибках должны выводится на консоль.
        </li><li>
            Программа не должна &laquo;вылетать&raquo; с исключениями (как стандартными, так и добавленными).
        </li></ol><h3 id="homework-13">Домашнее задание 13. Markdown to HTML</h3><ol><li>
            Разработайте конвертер из
            <a href="https://ru.wikipedia.org/wiki/Markdown">Markdown</a>-разметки
            в <a href="https://ru.wikipedia.org/wiki/HTML">HTML</a>.
        </li><li>
            Конвертер должен поддерживать следующие возможности:
            <ol><li>
                    Абзацы текста разделяются пустыми строками.
                </li><li>
                    Элементы строчной разметки:
                    выделение (<tt>*</tt> или <tt>_</tt>),
                    сильное выделение (<tt>**</tt> или <tt>__</tt>),
                    зачеркивание (<tt>--</tt>),
                    код (<tt>`</tt>)
                </li><li>
                    Заголовки (<tt>#</tt> * уровень заголовка)
                </li></ol></li><li>
            Конвертер должен называться <tt>Md2Html</tt> и
            принимать два аргумента: название входного файла
            с Markdown-разметкой и название выходного файла
            c HTML-разметкой. Оба файла должны иметь кодировку UTF-8.
        </li><li>
            Пример
          <ul><li>
              Входной файл
            <pre>
# Заголовок первого уровня

## Второго

### Третьего ## уровня

#### Четвертого
# Все еще четвертого

Этот абзац текста,
содержит две строки.

    # Может показаться, что это заголовок.
Но нет, это абзац начинающийся с `#`.

#И это не заголовок.

###### Заголовки могут быть многострочными
(и с пропуском заголовков предыдущих уровней)

Мы все любим *выделять* текст _разными_ способами.
**Сильное выделение**, используется гораздо реже,
но __почему бы и нет__?
Немного --зачеркивания-- еще ни кому не вредило.
Код представляется элементом `code`.

Обратите внимание, как экранируются специальные
HTML-символы, такие как `&lt;`, `&gt;` и `&amp;`.

Знаете ли вы, что в Markdown, одиночные * и _
не означают выделение?
Они так же могут быть заэкранированы
при помощи обратного слэша: \*.



Лишние пустые строки должны игнорироваться.

Любите ли вы *вложеные __выделения__* так,
как __--люблю--__ их я?
            </pre></li><li>
            Выходной файл
            <pre>
&lt;h1&gt;Заголовок первого уровня&lt;/h1&gt;
&lt;h2&gt;Второго&lt;/h2&gt;
&lt;h3&gt;Третьего ## уровня&lt;/h3&gt;
&lt;h4&gt;Четвертого
# Все еще четвертого&lt;/h4&gt;
&lt;p&gt;Этот абзац текста,
содержит две строки.&lt;/p&gt;
&lt;p&gt;    # Может показаться, что это заголовок.
Но нет, это абзац начинающийся с &lt;code&gt;#&lt;/code&gt;.&lt;/p&gt;
&lt;p&gt;#И это не заголовок.&lt;/p&gt;
&lt;h6&gt;Заголовки могут быть многострочными
(и с пропуском заголовков предыдущих уровней)&lt;/h6&gt;
&lt;p&gt;Мы все любим &lt;em&gt;выделять&lt;/em&gt; текст &lt;em&gt;разными&lt;/em&gt; способами.
&lt;strong&gt;Сильное выделение&lt;/strong&gt;, используется гораздо реже,
но &lt;strong&gt;почему бы и нет&lt;/strong&gt;?
Немного &lt;s&gt;зачеркивания&lt;/s&gt; еще ни кому не вредило.
Код представляется элементом &lt;code&gt;code&lt;/code&gt;.&lt;/p&gt;
&lt;p&gt;Обратите внимание, как экранируются специальные
HTML-символы, такие как &lt;code&gt;&amp;lt;&lt;/code&gt;, &lt;code&gt;&amp;gt;&lt;/code&gt; и &lt;code&gt;&amp;amp;&lt;/code&gt;.&lt;/p&gt;
&lt;p&gt;Знаете ли вы, что в Markdown, одиночные * и _
не означают выделение?
Они так же могут быть заэкранированы
при помощи обратного слэша: *.&lt;/p&gt;
&lt;p&gt;Лишние пустые строки должны игнорироваться.&lt;/p&gt;
&lt;p&gt;Любите ли вы &lt;em&gt;вложеные &lt;strong&gt;выделения&lt;/strong&gt;&lt;/em&gt; так,
как &lt;strong&gt;&lt;s&gt;люблю&lt;/s&gt;&lt;/strong&gt; их я?&lt;/p&gt;
            </pre></li><li>
            Реальная разметка
<h1 id="N66359">Заголовок первого уровня</h1><h2 id="N66361">Второго</h2><h3 id="N66363">Третьего ## уровня</h3><h4 id="N66365">Четвертого
# Все еще четвертого</h4><p>Этот абзац текста,
содержит две строки.</p><p>    # Может показаться, что это заголовок.
Но нет, это абзац начинающийся с <code>#</code>.</p><p>#И это не заголовок.</p><h6>Заголовки могут быть многострочными
(и с пропуском заголовков предыдущих уровней)</h6><p>Мы все любим <em>выделять</em> текст <em>разными</em> способами.
<strong>Сильное выделение</strong>, используется гораздо реже,
но <strong>почему бы и нет</strong>?
Немного <s>зачеркивания</s> еще ни кому не вредило.
Код представляется элементом <code>code</code>.</p><p>Обратите внимание, как экранируются специальные
HTML-символы, такие как <code>&lt;</code>, <code>&gt;</code> и <code>&amp;</code>.</p><p>Знаете ли вы, что в Markdown, одиночные * и _
не означают выделение?
Они так же могут быть заэкранированы
при помощи обратного слэша: *.</p><p>Лишние пустые строки должны игнорироваться.</p><p>Любите ли вы <em>вложеные <strong>выделения</strong></em> так,
как <strong><s>люблю</s></strong> их я?</p></li></ul></li></ol></td><td id="sidebar"><div id="sidebar-head"><form method="get" action="https://www.google.com/search"><p><input type="hidden" name="sitesearch" value="kgeorgiy.info"></p><table><tr><td style="width:100%"><input style="width:100%" type="text" name="q" maxlength="255"></td><td><button type="submit" value="Search"><img alt="Search" src="/design/find.png" width="16" height="16"></button></td></tr></table></form></div><div id="sidebar-body"><h3><a href="#homework-1">Домашнее задание 1. Запусти меня!</a></h3><h3><a href="#homework-2">Домашнее задание 2. Сумма чисел</a></h3><h3><a href="#homework-3">Домашнее задание 3. Реверс</a></h3><h3><a href="#homework-4">Домашнее задание 4. Статистика слов</a></h3><h3><a href="#homework-5">Домашнее задание 5. Свой сканер</a></h3><h3><a href="#homework-6">Домашнее задание 6. Статистика слов++</a></h3><h3><a href="#homework-7">Домашнее задание 7. Разметка</a></h3><h3><a href="#homework-8">Домашнее задание 8. Чемпионат</a></h3><h3><a href="#homework-9">Домашнее задание 9. Игра m,n,k</a></h3><h3><a href="#homework-10">Домашнее задание 10. Выражения</a></h3><h3><a href="#homework-11">Домашнее задание 11. Разбор выражений</a></h3><h3><a href="#homework-12">Домашнее задание 12. Обработка ошибок</a></h3><h3><a href="#homework-13">Домашнее задание 13. Markdown to HTML</a></h3><h1><a href="#N66359">Заголовок первого уровня</a></h1><h2><a href="#N66361">Второго</a></h2><h3><a href="#N66363">Третьего ## уровня</a></h3><h4><a href="#N66365">Четвертого
# Все еще четвертого</a></h4></div>
</wbr>
</body></html>
