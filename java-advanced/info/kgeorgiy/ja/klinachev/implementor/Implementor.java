package info.kgeorgiy.ja.klinachev.implementor;

import info.kgeorgiy.java.advanced.implementor.ImplerException;
import info.kgeorgiy.java.advanced.implementor.JarImpler;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.jar.JarOutputStream;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;

/**
 * Implementation of the {@code JarImpler}.
 * Class for creating implementation for interfaces and abstract classes.
 * Abstract {@code Methods} in created class return the default value.
 * {@code Constructors} only calls parents {@code Constructors}.
 */
public class Implementor implements JarImpler {

    /**
     * Constructs Implementor instance.
     */
    public Implementor() {
    }


    /**
     * Convert {@code String} to ASCII Unicode escaped String.
     * Replace every non-ASCII character with {@code String},
     * that represent this symbol in unicode format.
     *
     * @param string {@code String} to convert
     * @return {@code String} in ASCII Unicode format.
     */
    private static String toASCII(final String string) {
        final StringBuilder out = new StringBuilder();
        for (int i = 0; i < string.length(); i++) {
            final char ch = string.charAt(i);
            if (ch <= 127) {
                out.append(ch);
            } else {
                out.append(String.format("\\u%04x", (int) ch));
            }
        }
        return out.toString();
    }

    /**
     * Returns the name of implementing class.
     * Class name is the token class name plus "Impl".
     *
     * @param token The class for implementing
     * @return the name of implementing class
     */
    private static String getImplName(final Class<?> token) {
        return token.getPackageName() + '.' + token.getSimpleName() + "Impl";
    }

    /**
     * Replace "." with {@link java.io.File#separator}, then add extension to result.
     *
     * @param string    {@link String} in witch "." replacing with {@link java.io.File#separator}
     * @param extension {@link String} that added to Result
     * @return The resulting string
     */
    private static String classNameToPath(final String string, final String extension) {
        return string.replace(".", File.separator) + extension;
    }

    /**
     * Return the {@link Path}, file, with ".java" extension,
     * for implementation provided class regarding the root.
     * If file doesn't exist creates a directory and file.
     *
     * @param token class for witch implementation creates
     * @param root  the path relative to witch creates resulting file
     * @return a {@code Path} to file for implementation
     * @throws ImplerException if resulting file doesn't exist and creating failed
     */
    private static Path getPlace(final Class<?> token, final Path root) throws ImplerException {
        final Path place = root
                .resolve(classNameToPath(
                        getImplName(token),
                        ".java"))
                .toAbsolutePath();
        if (Files.exists(place)) {
            return place;
        }
        try {
            Files.createDirectories(place.getParent());
            Files.createFile(place);
        } catch (final IOException e) {
            throw new ImplerException("Can't create file: " + place.toAbsolutePath(), e);
        }
        return place;
    }

    /**
     * Validate, that provided class can be implemented.
     * Check that {@code Class} is not private or final or primitive or array or {@code Enum}
     * and if {@code Class} is not an interface, it have a non-private constructor.
     *
     * @param token {@code Class} to validate
     * @throws ImplerException if validate failed
     */
    private static void validate(final Class<?> token) throws ImplerException {
        if (token.isPrimitive()
                || token.isEnum()
                || Modifier.isPrivate(token.getModifiers())
                || token.isArray()
                || token == Enum.class
                || Modifier.isFinal(token.getModifiers())
                || !token.isInterface() && Arrays
                .stream(token.getDeclaredConstructors())
                .allMatch(constructor -> Modifier.isPrivate(constructor.getModifiers()))) {
            throw new ImplerException("Can't extend or implement class: " + token);
        }
    }

    /**
     * Return the returning {@code String}, with default result.
     * Return string depends on returning type:
     * <ul>
     *     <li>void - ""</li>
     *     <li>boolean - "return false;"</li>
     *     <li>other primitive - "return 0;"</li>
     *     <li>otherwise - "return null;"</li>
     * </ul>
     *
     * @param clazz return value type
     * @return {@code String} returning default result
     */
    private static String returnString(final Class<?> clazz) {
        final StringBuilder stringBuilder = new StringBuilder();
        if (clazz != void.class) {
            stringBuilder.append("return ");
            if (clazz.isPrimitive()) {
                if (clazz == boolean.class) {
                    stringBuilder.append("false;");
                } else {
                    stringBuilder.append("0;");
                }
            } else {
                stringBuilder.append("null;");
            }
        }
        return stringBuilder.toString();
    }


    /**
     * Return enumerations {@code executable} of parameters
     * formatting by {@code function} {@code String}.
     * Parameters names generated by {@link Executable#getParameters()}
     * Parameters separated by commas.
     * If parameters contains non-ASCII characters,
     * it replaced with unicode format string.
     *
     * @param executable {@link Executable} to get parameters
     * @param function   {@link Function} to specify parameters
     * @return resulting string
     */
    private static String parametersString(
            final Executable executable,
            final Function<Parameter, String> function
    ) {
        return Arrays.stream(executable.getParameters())
                .map(function)
                .map(Implementor::toASCII)
                .collect(Collectors.joining(","));
    }

    /**
     * Return {@code String} calling the ancestor constructor.
     * String format: "super('constructor parameters')".
     * Constructor parameters names
     * generated by {@link Executable#getParameters()}
     *
     * @param constructor ancestor constructor
     * @return {@code String} calling the ancestor constructor
     */
    private static String superString(final Constructor<?> constructor) {
        return String.format("super(%s);",
                parametersString(constructor, Parameter::getName));
    }

    /**
     * Return {@code String} of {@link Executable} parameters.
     *
     * @param executable {@link Executable}, witch parameters
     * @return {@code String} of {@link Executable} parameters
     * @see Implementor#parametersString(Executable, Function)
     */
    private static String executableArgs(final Executable executable) {
        return parametersString(
                executable,
                parameter -> parameter.getType().getCanonicalName() + ' ' + parameter.getName());
    }

    /**
     * Return {@code String} of {@link Method} modifiers without {@code abstract} modifier.
     *
     * @param method {@link Method}, witch parameters are returned
     * @return {@code String} of modifiers without {@code abstract}
     * @see Modifier#methodModifiers()
     */
    private static String methodModifiers(final Method method) {
        return Modifier.toString(method.getModifiers() & Modifier.methodModifiers() & ~Modifier.ABSTRACT);
    }

    /**
     * Return {@code String} of {@link Constructor} modifiers.
     *
     * @param constructor {@link Constructor}, witch parameters are returned
     * @return {@code String} of modifiers
     * @see Modifier#constructorModifiers()
     */
    private static String constructorModifiers(final Constructor<?> constructor) {
        return Modifier.toString(constructor.getModifiers() & Modifier.constructorModifiers());
    }

    /**
     * Return {@code String} of {@code exceptions}, that {@code executable} may throw.
     * If {@code executable} doesn't throw {@link Exception} return empty {@code String}
     * "throw" plus enumeration of all exceptions, that this {@code executable} may throw, otherwise.
     *
     * @param executable {@link Executable}, witch exceptions
     * @return empty {@code String} if {@code executable} throw nothing
     * {@code String} of all exceptions enumeration, otherwise
     */
    private static String throwString(final Executable executable) {
        final Class<?>[] exceptionTypes = executable.getExceptionTypes();
        return exceptionTypes.length == 0 ? "" : "throws " + Arrays.stream(exceptionTypes)
                .map(Class::getCanonicalName)
                .map(Implementor::toASCII)
                .collect(Collectors.joining(","));
    }

    /**
     * Return implementation of this {@link Method}.
     * Format: "'modifiers' 'return type' 'method name'('parameters')
     * {'{@link Implementor#returnString(Class)}'}".
     * if method has abstract modifier, implementation will not have it.
     * Implementation return the default value of returning type.
     *
     * @param method {@link Method}, that implemented
     * @return {@code String} of default implementation of method
     * @see #returnString(Class)
     */
    private static String implementMethod(final Method method) {
        final Class<?> returnType = method.getReturnType();
        return String.format("%s %s %s(%s){%s}",
                methodModifiers(method),
                toASCII(returnType.getCanonicalName()),
                toASCII(method.getName()),
                executableArgs(method),
                returnString(returnType));
    }

    /**
     * Return implementation of {@link Constructor},
     * that only calls the ancestor constructor.
     * String format: "'modifiers' 'constructor's class name'Impl ('constructor parameters')
     * '{@link Implementor#throwString(Executable)}' {super(constructor parameter names)}",
     * plus "throws 'exception names'", if constructor throws checked exceptions.
     * {@link Constructor} calls with the same arguments.
     *
     * @param constructor {@link Constructor}, that implemented
     * @return {@code String} of default implementation of method
     * @see #superString(Constructor)
     */
    private static String implementConstructor(final Constructor<?> constructor) {
        return String.format("%s %sImpl(%s) %s {%s}",
                constructorModifiers(constructor),
                toASCII(constructor.getDeclaringClass().getSimpleName()),
                executableArgs(constructor),
                throwString(constructor),
                superString(constructor));
    }


    /**
     * Return package {@code String} of {@code token} class.
     * If the class is in an unnamed package return empty {@code String}
     * Otherwise this string have format: "package 'package name';".
     * If package name contains non-ASCII characters,
     * it replaced with unicode format string.
     *
     * @param token {@link Class}, witch is implemented
     * @return package {@code String} of implementor class
     */
    private static String packageString(final Class<?> token) {
        return token.getPackageName().length() == 0 ? "" : "package " + toASCII(token.getPackageName()) + ";";
    }

    /**
     * Return declaration {@code String} of {@link Class} that implement {@code token} class.
     * Format: "public class 'token name'Impl extends 'token name'" or
     * "public class 'token name'Impl implement 'token name'".
     * Public is the only modifier this title have.
     * if {@code token} is {@code interface} new class will implement it.
     * And extends otherwise.
     *
     * @param token {@link Class}, witch is implemented
     * @return declaration {@code String} of implementor class
     * @see Modifier#isInterface(int)
     */
    private static String declareString(final Class<?> token) {
        return String.format(
                "public class %sImpl %s %s",
                toASCII(token.getSimpleName()),
                Modifier.isInterface(token.getModifiers()) ? "implements" : "extends",
                toASCII(token.getCanonicalName()));
    }

    /**
     * {@link Method} class wrapper.
     * Only different with {@link Method} is that {@link MyMethod#equals(Object)}
     * doesn't compare the declaring {@link Class}.
     */
    private static class MyMethod {
        /**
         * {@link Method}, that this instance wraps.
         */
        final private Method method;

        /**
         * Constructs a {@code MyMethod} of another {@code MyMethod}.
         *
         * @param method {@link Method} to wrap
         */
        public MyMethod(final Method method) {
            this.method = method;
        }

        /**
         * Return wrapped {@link Method} value
         *
         * @return wrapped {@link Method}
         */
        public Method getMethod() {
            return method;
        }

        /**
         * Check if params1 and params2 are equal.
         * Check that params size are equal, then compare each elements in arrays.
         *
         * @param params1 first array to check
         * @param params2 second array to check
         * @return true if params1 equal params2
         * else otherwise
         */
        private static boolean equalParamTypes(final Class<?>[] params1, final Class<?>[] params2) {
            if (params1.length == params2.length) {
                for (int i = 0; i < params1.length; i++) {
                    if (params1[i] != params2[i])
                        return false;
                }
                return true;
            }
            return false;
        }

        /**
         * Compares this {@code MyMethod} against the specified object.  Returns
         * true if the field {@code method} are the same not counting the Class name.
         * Two {@code MyMethods} are the same if they have the same name
         * and formal parameter types and return type.
         *
         * @see Method#equals(Object)
         */
        @Override
        public boolean equals(final Object obj) {
            if (obj instanceof MyMethod) {
                final MyMethod other = (MyMethod) obj;
                if (method.getName().equals(other.method.getName())) {
                    if (!method.getReturnType().equals(other.method.getReturnType()))
                        return false;
                    return equalParamTypes(method.getParameterTypes(), other.method.getParameterTypes());
                }
            }
            return false;
        }

        /**
         * Returns a hashcode for this {@code Method}.  The hashcode is computed
         * as the exclusive-or of the hashcodes for the underlying
         * method's the method's name, parameters count.
         *
         * @see Method#hashCode()
         */
        @Override
        public int hashCode() {
            return method.getParameterCount() ^ method.getName().hashCode();
        }
    }

    /**
     * Write in {@link BufferedWriter} implementations of all abstract {@link Method Methods}
     * of {@code token}, that only returns default value of returning type.
     *
     * @param bufferedWriter {@link BufferedWriter}, that implemented
     * @param token          {@link Class}, {@link Constructor}'s of the witch are implemented
     * @throws IOException that {@link BufferedWriter#write(String)} throws
     * @see #returnString(Class)
     */
    private static void writeMethods(
            final BufferedWriter bufferedWriter,
            final Class<?> token
    ) throws IOException {
        final Set<MyMethod> abstractMethods = new HashSet<>();
        final Set<MyMethod> nonAbstractMethods = new HashSet<>();
        for (final Method method : token.getMethods()) {
            if (Modifier.isAbstract(method.getModifiers())) {
                abstractMethods.add(new MyMethod(method));
            }
        }
        for (Class<?> clazz = token;
             clazz != null && Modifier.isAbstract(clazz.getModifiers());
             clazz = clazz.getSuperclass()
        ) {
            for (final Method method : clazz.getDeclaredMethods()) {
                final MyMethod myMethod = new MyMethod(method);
                if (Modifier.isAbstract(method.getModifiers()) && !nonAbstractMethods.contains(myMethod)) {
                    abstractMethods.add(myMethod);
                } else {
                    nonAbstractMethods.add(myMethod);
                }
            }
        }
        for (final MyMethod method : abstractMethods) {
            bufferedWriter.write(implementMethod(method.getMethod()));
        }
    }

    /**
     * Write in {@link BufferedWriter} implementation of all not private {@link Constructor Constructors}
     * of {@code token} {@link Constructor Constructors}, that only calls the ancestor constructor.
     * Parent {@link Constructor} calls with the same arguments.
     *
     * @param bufferedWriter {@link BufferedWriter}, that implemented
     * @param token          {@link Class}, {@link Constructor}'s of the witch are implemented
     * @throws IOException that {@link BufferedWriter#write(String)} throws
     * @see #superString(Constructor)
     */
    private static void writeConstructors(
            final BufferedWriter bufferedWriter,
            final Class<?> token
    ) throws IOException {
        for (final Constructor<?> constructor : token.getDeclaredConstructors()) {
            bufferedWriter.write(implementConstructor(constructor));
        }
    }

    /**
     * Delete Directory and all contents of the directory.
     * If {@link IOException} happened, print stack trace to {@link System#err}
     *
     * @param path {@link Path} to Directory
     * @see File#delete()
     */
    private static void deleteDir(final Path path) {
        try (final Stream<Path> walk = Files.walk(path)) {
            walk.sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEachOrdered(File::delete);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Compile {@code source} file with provided classpath.
     * Compiled files are located in {@code dir} directory.
     * Compilation use encoding utf-8.
     *
     * @param source    file to compile
     * @param dir       directory for compiled files
     * @param classPath directory for classpath
     * @throws ImplerException if compilation failed
     */
    private static void compile(final Path source, final Path dir, final Path classPath) throws ImplerException {
        final JavaCompiler javaCompiler = ToolProvider.getSystemJavaCompiler();
        if (javaCompiler == null) {
            throw new ImplerException("Can't get system java compiler");
        }
        final int res;
        res = javaCompiler.run(null, null, null,
                "-d",
                dir.toAbsolutePath().toString(),
                "-encoding",
                "utf-8",
                "-cp",
                classPath.toString(),
                source.toString());
        if (res != 0) {
            throw new ImplerException("Can't compile Impl class");
        }
    }

    /**
     * Produces <var>.jar</var> file implementing class or interface specified by provided <var>token</var>.
     * <p>
     * Generated class classes name same as classes name of the type token with <var>Impl</var> suffix
     * added.
     * <p>
     * This method firstly calls {@link Implementor#implement(Class, Path)} to create implementation,
     * then compile it and creates {@code jarFile} with compiled file.
     * All files except {@code jarFile} are created in temporary directory,
     * that deleted in the end, if deleting fails it printed in {@link System#err}.
     *
     * @param token   type token to create implementation for.
     * @param jarFile target <var>.jar</var> file.
     * @throws ImplerException if token fails {@link Implementor#validate(Class)}
     *                         or IOException happened.
     */
    @Override
    public void implementJar(final Class<?> token, final Path jarFile) throws ImplerException {
        final Path tmp = Path.of("implementorTmp");
        try {
            Files.createDirectories(tmp);
        } catch (final IOException e) {
            throw new ImplerException(e);
        }
        implement(token, tmp);
        final Path impl = getPlace(token, tmp);
        try {
            if (token.getProtectionDomain().getCodeSource() == null) {
                deleteDir(tmp);
                throw new ImplerException("Can't compile Impl class");
            }
            final Path cp = Path.of(token.getProtectionDomain().getCodeSource().getLocation().toURI());
            compile(impl, tmp, cp);
        } catch (final URISyntaxException | ImplerException e) {
            deleteDir(tmp);
            throw new ImplerException(e);
        }
        final String path = classNameToPath(
                token.getPackageName() + File.separator + token.getSimpleName(),
                "Impl.class");
        try (final JarOutputStream jarOutputStream = new JarOutputStream(Files.newOutputStream(jarFile))
        ) {
            final String zipName = path.replace(File.separator, "/");
            final ZipEntry zipEntry = new ZipEntry(zipName);
            jarOutputStream.putNextEntry(zipEntry);
            Files.copy(tmp.resolve(path), jarOutputStream);
            jarOutputStream.closeEntry();
        } catch (final IOException e) {
            deleteDir(tmp);
            throw new ImplerException(e);
        }
        deleteDir(tmp);
    }

    /**
     * Produces code implementing class or interface specified by provided {@code token}.
     * <p>
     * Generated class classes name same as classes name of the type token with {@code Impl} suffix
     * added. Generated source code placed in the correct subdirectory of the specified
     * {@code root} directory and have correct file name. For example, the implementation of the
     * interface {@link java.util.List} go to {@code $root/java/util/ListImpl.java}
     *
     * @param token type token to create implementation for.
     * @param root  root directory.
     * @throws ImplerException if token fails {@link Implementor#validate(Class)}
     *                         or IOException happened.
     */
    @Override
    public void implement(final Class<?> token, final Path root) throws ImplerException {
        validate(token);
        final Path place = getPlace(token, root);
        try (final BufferedWriter bufferedWriter = Files.newBufferedWriter(place)) {
            bufferedWriter.write(packageString(token));
            bufferedWriter.write(declareString(token));
            bufferedWriter.write(" {");
            writeConstructors(bufferedWriter, token);
            writeMethods(bufferedWriter, token);
            bufferedWriter.write("}");
        } catch (final IOException e) {
            throw new ImplerException(e);
        }
    }

    /**
     * Implement provided {@link Class}, if "-jar"
     * option provided create *.jar file with compiled implementation.
     * if exception occurred print stack trace to {@link System#err}
     *
     * @param args name of {@link Class} to implement
     *             or "-jar", name of {@link Class} to implement, path to *.jar file.
     */
    public static void main(final String[] args) {
        if (args.length != 1 && !(args.length == 3 && args[0].trim().equals("-jar"))) {
            System.err.println("Format: <class name> or -jar <class name> <source path>");
            return;
        }
        final Implementor implementor = new Implementor();
        try {
            if (args.length == 1) {
                implementor.implement(Class.forName(args[0]), Path.of(""));
            } else {
                implementor.implementJar(Class.forName(args[1]), Path.of(args[2]));
            }
        } catch (final ImplerException e) {
            System.err.println("Implementation failed");
        } catch (final ClassNotFoundException e) {
            System.err.println("Class not found: " + e.getMessage());
        }
    }

}
