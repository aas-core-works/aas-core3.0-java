/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import aas_core.aas3_0.reporting.Reporting;
import aas_core.aas3_0.types.model.IClass;
import aas_core.aas3_0.xmlization.Xmlization;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

/** Provide methods for testing. */
public class Common {
  public static final boolean RECORD_MODE =
      System.getenv("AAS_CORE_AAS3_0_TESTS_RECORD_MODE") != null
          && System.getenv("AAS_CORE_AAS3_0_TESTS_RECORD_MODE").equalsIgnoreCase("true");

  public static String TEST_DATA_DIR = Paths.get("test_data").toAbsolutePath().toString();

  public static final List<String> CAUSES_FOR_VERIFICATION_FAILURE =
      Collections.unmodifiableList(
          Arrays.asList(
              "DateTimeStampUtcViolationOnFebruary29th",
              "MaxLengthViolation",
              "MinLengthViolation",
              "PatternViolation",
              "InvalidValueExample",
              "InvalidMinMaxExample",
              "SetViolation",
              "ConstraintViolation"));

  public static List<Path> findPaths(Path path, String fileExtension) throws IOException {
    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Path must be a directory!");
    }

    List<Path> result;
    try (Stream<Path> walk = Files.walk(path)) {
      result =
          walk.filter(p -> !Files.isDirectory(p))
              .filter(f -> f.toString().endsWith(fileExtension))
              .collect(Collectors.toList());
    }
    return result;
  }

  public static List<Path> findDirs(Path path) throws IOException {
    if (!Files.isDirectory(path)) {
      throw new IllegalArgumentException("Path must be a directory!");
    }

    List<Path> result;
    try (Stream<Path> walk = Files.walk(path)) {
      result = walk.filter(p -> Files.isDirectory(p)).collect(Collectors.toList());
    }
    return result;
  }

  @SuppressWarnings("unchecked")
  public static <T extends IClass> T mustFind(String type, IClass container) {
    if (type.equals(container.getClass().getSimpleName())) {
      return (T) container;
    }
    IClass instance = null;
    for (IClass current : container.descend()) {
      if (type.equals(current.getClass().getSimpleName())) {
        instance = current;
        break;
      }
    }
    if (instance == null) {
      throw new IllegalStateException("No instance of " + type + " could be found");
    }
    return (T) instance;
  }

  public static void assertNoVerificationErrors(List<Reporting.Error> errors, Path path) {

    if (!errors.isEmpty()) {
      StringBuilder stringBuilder = new StringBuilder();
      stringBuilder
          .append("Expected no errors when verifying the instance de-serialized from ")
          .append(path.toString())
          .append(", ")
          .append("but got ")
          .append(errors.size())
          .append(" error(s):")
          .append(System.lineSeparator());
      for (Reporting.Error error : errors) {
        stringBuilder
            .append(Reporting.generateJsonPath(error.getPathSegments()))
            .append(": ")
            .append(error.getCause());
      }
      fail(stringBuilder.toString());
    }
  }

  public static <T> List<T> asList(Iterable<T> iterable) {
    return StreamSupport.stream(iterable.spliterator(), false).collect(Collectors.toList());
  }

  public static void assertEqualsExpectedOrRerecordVerificationErrors(
      List<Reporting.Error> errors, Path path) throws IOException {
    if (errors.isEmpty()) {
      fail("Expected at least one verification error when verifying " + path + ", but got none");
    }
    final String got =
        errors.stream()
            .map(
                error ->
                    Reporting.generateJsonPath(error.getPathSegments()) + ": " + error.getCause())
            .collect(Collectors.joining(";\n"));
    final Path errorsPath = Paths.get(path + ".errors");
    if (RECORD_MODE) {
      Files.write(errorsPath, got.getBytes(StandardCharsets.UTF_8));
    } else {
      if (!Files.exists(errorsPath)) {
        throw new FileNotFoundException(
            "The file with the recorded errors does not exist: "
                + errorsPath
                + "; maybe you want to set the environment variable "
                + "AAS_CORE_AAS3_0_TESTS_RECORD_MODE");
      }
      final String expected = String.join("\n", Files.readAllLines(errorsPath));
      assertEquals(
          expected,
          got,
          "The expected verification errors do not match the actual ones for the file " + path);
    }
  }

  public static void assertEqualsExpectedOrRerecordDeserializationException(
      Xmlization.DeserializeException exception, Path path) throws IOException {
    if (exception == null) {
      fail("Expected a Xmlization exception when de-serializing " + path + ", but got none.");
    } else {
      final Path exceptionPath = Paths.get(path + ".exception");
      final String got = exception.getMessage();
      if (RECORD_MODE) {
        Files.write(exceptionPath, got.getBytes(StandardCharsets.UTF_8));
      } else {
        if (!Files.exists(exceptionPath)) {
          throw new FileNotFoundException(
              "The file with the recorded exception does not exist: "
                  + exceptionPath
                  + "; maybe you want to set the environment variable "
                  + "AAS_CORE_AAS3_0_TESTS_RECORD_MODE");
        }
        final String expected = String.join("\n", Files.readAllLines(exceptionPath));
        assertEquals(
            expected,
            got,
            "The expected exception does not match the actual one for the file " + path);
      }
    }
  }
} // class Common

/*
 * This code has been automatically generated by testgen.
 * Do NOT edit or append.
 */
