package info.kgeorgiy.ja.klinachev.student;

import info.kgeorgiy.java.advanced.student.AdvancedQuery;
import info.kgeorgiy.java.advanced.student.Group;
import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StudentDB implements AdvancedQuery {
    private static final Comparator<Student> COMPARATOR_BY_ID = Comparator.comparingInt(Student::getId);

    private static final Comparator<Student> COMPARATOR_BY_NAME = Comparator.comparing(Student::getLastName)
            .thenComparing(Student::getFirstName).reversed()
            .thenComparingInt(Student::getId);

    private static <T, R> Stream<R> getFieldsStream(
            final Collection<T> students,
            final Function<? super T, ? extends R> f
    ) {
        return students.stream().map(f);
    }

    private static <S, R, T extends Collection<R>> T getFields(
            final List<S> students,
            final Function<? super S, ? extends R> f,
            final Collector<R, ?, T> collector
    ) {
        return getFieldsStream(students, f).collect(collector);
    }

    private static <T, R> List<R> getFieldsList(
            final List<T> students,
            final Function<? super T, ? extends R> f
    ) {
        return getFields(students, f, Collectors.toList());
    }

    private static <T> List<T> sortStreamToList(
            final Stream<T> students,
            final Comparator<? super T> comparator
    ) {
        return students.sorted(comparator).collect(Collectors.toList());
    }

    private static <S> List<S> sortStudent(
            final Collection<S> students,
            final Comparator<? super S> comparator
    ) {
        return sortStreamToList(students.stream(), comparator);
    }

    private static <S> Stream<S> filter(
            final Collection<S> students,
            final Predicate<? super S> predicate
    ) {
        return students.stream().filter(predicate);
    }

    private static <S> List<S> filterAndSort(
            final Collection<S> students,
            final Predicate<? super S> predicate,
            final Comparator<? super S> comparator
    ) {
        return sortStreamToList(filter(students, predicate), comparator);
    }

    private static List<Student> filterSortById(
            final Collection<Student> students,
            final Predicate<? super Student> predicate
    ) {
        return filterAndSort(students, predicate, Comparator.comparing(Student::getId));
    }

    private static <T, R> Predicate<R> filterByField(final T first, final Function<R, T> function) {
        return second -> first.equals(function.apply(second));
    }

    private static Predicate<Student> filterByGroupName(final GroupName groupName) {
        return filterByField(groupName, Student::getGroup);
    }

    private static List<Student> findByGroupSortById(
            final Collection<Student> students,
            final GroupName groupName
    ) {
        return filterSortById(students, filterByGroupName(groupName));
    }

    private static List<Student> filterSortByName(
            final Collection<Student> students,
            final Predicate<Student> predicate
    ) {
        return filterAndSort(students, predicate, COMPARATOR_BY_NAME);
    }

    @Override
    public List<String> getFirstNames(final List<Student> students) {
        return getFieldsList(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(final List<Student> students) {
        return getFieldsList(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(final List<Student> students) {
        return getFieldsList(students, Student::getGroup);
    }

    private static String getFullName(final Student student) {
        return student.getFirstName() + " " + student.getLastName();
    }

    @Override
    public List<String> getFullNames(final List<Student> students) {
        return getFieldsList(students, StudentDB::getFullName);
    }

    @Override
    public Set<String> getDistinctFirstNames(final List<Student> students) {
        return getFields(students, Student::getFirstName, Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(final List<Student> students) {
        return students.stream().max(COMPARATOR_BY_ID).map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(final Collection<Student> students) {
        return sortStudent(students, COMPARATOR_BY_ID);
    }

    @Override
    public List<Student> sortStudentsByName(final Collection<Student> students) {
        return sortStudent(students, COMPARATOR_BY_NAME);
    }

    private static <T> List<Student> findStudentsBySmth(
            final Collection<Student> students,
            final T name,
            final Function<Student, T> function
    ) {
        return filterSortByName(students, filterByField(name, function));
    }


    // :NOTE: Упростить
    @Override
    public List<Student> findStudentsByFirstName(final Collection<Student> students, final String name) {
        return findStudentsBySmth(students, name, Student::getFirstName);
    }

    @Override
    public List<Student> findStudentsByLastName(final Collection<Student> students, final String name) {
        return findStudentsBySmth(students, name, Student::getLastName);
    }

    @Override
    public List<Student> findStudentsByGroup(final Collection<Student> students, final GroupName group) {
        return findStudentsBySmth(students, group, Student::getGroup);
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(
            final Collection<Student> students,
            final GroupName group
    ) {
        return filter(students, filterByGroupName(group)).collect(
                Collectors.toMap(
                        Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(Comparator.naturalOrder())));
    }

    private static Stream<GroupName> getGroupNamesStream() {
        return Arrays.stream(GroupName.values()).sorted();
    }

    private static Stream<Group> getGroupsStream(final Function<GroupName, List<Student>> function) {
        return getGroupNamesStream()
                .map(groupName -> new Group(groupName, function.apply(groupName)))
                .filter(group -> !group.getStudents().isEmpty());
    }

    private static <A, B, R> Function<B, R> bind(final BiFunction<A, B, R> function, final A arg1) {
        return arg2 -> function.apply(arg1, arg2);
    }

    private static Stream<Group> getGroupsStream(
            final Collection<Student> students,
            final BiFunction<Collection<Student>, GroupName, List<Student>> function
    ) {
        return getGroupsStream(bind(function, students));
    }

    private static List<Group> getGroups(
            final Collection<Student> students,
            final BiFunction<Collection<Student>, GroupName, List<Student>> function
    ) {
        return getGroupsStream(students, function).collect(Collectors.toList());
    }


    @Override
    public List<Group> getGroupsByName(final Collection<Student> students) {
        return getGroups(students, this::findStudentsByGroup);
    }

    @Override
    public List<Group> getGroupsById(final Collection<Student> students) {
        return getGroups(students, StudentDB::findByGroupSortById);
    }

    public static <S, K, V> K getLargest(
            final Collection<S> students,
            final Function<S, K> function,
            final Collector<S, ?, V> collector,
            final Comparator<Map.Entry<K, V>> comparator,
            final K orElse
    ) {
        return students.stream()
                .collect(Collectors.groupingBy(function, collector))
                .entrySet()
                .stream()
                .max(comparator)
                .map(Map.Entry::getKey)
                .orElse(orElse);
    }

    public static GroupName getLargestGroup(
            final Collection<Student> students,
            final Collector<Student, ?, Long> collector,
            final Comparator<Map.Entry<GroupName, Long>> comparator
    ) {
        return getLargest(students, Student::getGroup, collector, comparator, null);
    }

    private static <T> Comparator<Map.Entry<T, Long>> comparingEntryByValue() {
        return Comparator.comparingLong(Map.Entry::getValue);
    }

    private static <T extends Comparable<? super T>> Comparator<Map.Entry<T, Long>> comparingEntry() {
        return StudentDB.<T>comparingEntryByValue().thenComparing(Map.Entry.comparingByKey());
    }

    @Override
    public GroupName getLargestGroup(final Collection<Student> students) {
        return getLargestGroup(
                students,
                Collectors.counting(),
                comparingEntry()
        );
    }

    private static <T> Collector<T, ?, Long> distinctCountCollector(final Function<? super T, ?> function) {
        return Collectors.mapping(
                function,
                Collectors.collectingAndThen(Collectors.toSet(), set -> (long) set.size())
        );
    }

    @Override
    public GroupName getLargestGroupFirstName(final Collection<Student> students) {
        return getLargestGroup(students,
                distinctCountCollector(Student::getFirstName),
                StudentDB.<GroupName>comparingEntryByValue()
                        .thenComparing(Map.Entry.<GroupName, Long>comparingByKey().reversed())
        );
    }

    @Override
    public String getMostPopularName(final Collection<Student> students) {
        return getLargest(
                students,
                Student::getFirstName,
                distinctCountCollector(Student::getGroup),
                comparingEntry(),
                ""
        );
    }

    private static <T> List<T> getFields(
            final Collection<Student> students,
            final int[] indices,
            final Function<Student, T> function
    ) {
        return Arrays.stream(indices)
                .mapToObj(List.copyOf(students)::get)
                .map(function)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getFirstNames(final Collection<Student> students, final int[] indices) {
        return getFields(students, indices, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(final Collection<Student> students, final int[] indices) {
        return getFields(students, indices, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(final Collection<Student> students, final int[] indices) {
        return getFields(students, indices, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(final Collection<Student> students, final int[] indices) {
        return getFields(students, indices, StudentDB::getFullName);
    }
}
