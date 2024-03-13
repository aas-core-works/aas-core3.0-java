package aas_core_works;

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.NodeList;
import com.github.javaparser.ast.body.*;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class PatchVerification {
  public static void main(String[] args) {
    if (args == null || args.length != 2) {
      System.err.println("Expected exactly two arguments, the source and the target");
      System.exit(1);
    }
    final String sourcePath = args[0];
    final String targetPath = args[1];

    String text;
    try {
      text = Files.readString(Path.of(sourcePath));
    } catch (IOException e) {
      System.err.println("Failed to read from " + sourcePath + ": " + e.getMessage());
      System.exit(1);
      return;
    }

    CompilationUnit unit = StaticJavaParser.parse(text);

    unit.addImport("dk.brics.automaton.Automaton");
    unit.addImport("dk.brics.automaton.RegExp");

    Optional<ClassOrInterfaceDeclaration> maybeVerificationCls = unit.getClassByName("Verification");
    if (maybeVerificationCls.isEmpty()) {
      System.err.println("The class 'Verification' could not be found in " + sourcePath);
      System.exit(1);
      return;
    }

    ClassOrInterfaceDeclaration verificationCls = maybeVerificationCls.get();

    MethodDeclaration stripMethod = new MethodDeclaration();
    stripMethod.addModifier(Modifier.Keyword.PRIVATE);
    stripMethod.addModifier(Modifier.Keyword.STATIC);
    stripMethod.setType("String");
    Parameter parameter = new Parameter()
            .setType("String")
            .setName("pattern");
    stripMethod.setParameters(new NodeList<>(parameter));
    stripMethod.setName("stripCaretPrefixAndDollarSuffixForDkBricsAutomaton");
    stripMethod.setBody(
        StaticJavaParser.parseBlock("""
            {
              if (pattern.startsWith("^")) {
                pattern = pattern.replaceFirst("\\\\^", "");
              }
              if (pattern.endsWith("$")) {
                pattern = pattern.substring(0, pattern.length() - 1);
              }
              return pattern;
            }""")
    );

    verificationCls.addMember(stripMethod);

    MethodDeclaration constructMethod = null;
    for (MethodDeclaration method : verificationCls.getMethods()) {
      if (method.getName().toString().equals("constructMatchesRfc8089Path")) {
        constructMethod = method;
        break;
      }
    }

    if (constructMethod == null) {
      System.err.println(
          "Could not find the method constructMatchesRfc8089Path in " + sourcePath
      );
      System.exit(1);
      return;
    }

    constructMethod.setType("Automaton");

    constructMethod.setBody(
        StaticJavaParser.parseBlock("""
            {
            String h16 = "[0-9A-Fa-f]{1,4}";
            String decOctet = "([0-9]|[1-9][0-9]|1[0-9]{2}|2[0-4][0-9]|25[0-5])";
            String ipv4address = decOctet + "\\\\." + decOctet + "\\\\." + decOctet + "\\\\." + decOctet;
            String ls32 = "(" + h16 + ":" + h16 + "|" + ipv4address + ")";
            String ipv6address = "((" + h16 + ":){6}" + ls32 + "|::(" + h16 + ":){5}" + ls32 + "|(" + h16 + ")?::(" + h16 + ":){4}" + ls32 + "|((" + h16 + ":)?" + h16 + ")?::(" + h16 + ":){3}" + ls32 + "|((" + h16 + ":){2}" + h16 + ")?::(" + h16 + ":){2}" + ls32 + "|((" + h16 + ":){3}" + h16 + ")?::" + h16 + ":" + ls32 + "|((" + h16 + ":){4}" + h16 + ")?::" + ls32 + "|((" + h16 + ":){5}" + h16 + ")?::" + h16 + "|((" + h16 + ":){6}" + h16 + ")?::)";
            String unreserved = "[a-zA-Z0-9\\\\-._~]";
            String subDelims = "[!$&\\'()*+,;=]";
            String ipvfuture = "[vV][0-9A-Fa-f]+\\\\.(" + unreserved + "|" + subDelims + "|:)+";
            String ipLiteral = "\\\\[(" + ipv6address + "|" + ipvfuture + ")\\\\]";
            String pctEncoded = "%[0-9A-Fa-f][0-9A-Fa-f]";
            String regName = "(" + unreserved + "|" + pctEncoded + "|" + subDelims + ")*";
            String host = "(" + ipLiteral + "|" + ipv4address + "|" + regName + ")";
            String fileAuth = "(localhost|" + host + ")";
            String pchar = "(" + unreserved + "|" + pctEncoded + "|" + subDelims + "|[:@])";
            String segmentNz = "(" + pchar + ")+";
            String segment = "(" + pchar + ")*";
            String pathAbsolute = "/(" + segmentNz + "(/" + segment + ")*)?";
            String authPath = "(" + fileAuth + ")?" + pathAbsolute;
            String localPath = pathAbsolute;
            String fileHierPart = "(//" + authPath + "|" + localPath + ")";
            String fileScheme = "file";
            String fileUri = fileScheme + ":" + fileHierPart;
            String pattern = "^" + fileUri + "$";

            return new RegExp(stripCaretPrefixAndDollarSuffixForDkBricsAutomaton(pattern)).toAutomaton();
            }""")
    );

    VariableDeclarator regexVariable = null;
    for (FieldDeclaration field : verificationCls.getFields()) {
      for (VariableDeclarator declarator : field.getVariables()) {
        if (declarator.getNameAsString().equals("regexMatchesRfc8089Path")) {
          regexVariable = declarator;
          break;
        }
      }
      if (regexVariable != null) {
        break;
      }
    }

    if (regexVariable == null) {
      System.err.println(
          "Could not find regexMatchesRfc8089Path field in " + sourcePath
      );
      System.exit(1);
      return;
    }
    regexVariable.setType("Automaton");

    MethodDeclaration matchesMethod = null;
    for (MethodDeclaration method : verificationCls.getMethods()) {
      if (method.getName().toString().equals("matchesRfc8089Path")) {
        matchesMethod = method;
        break;
      }
    }

    if (matchesMethod == null) {
      System.err.println(
          "Could not find the method matchesRfc8089Path in " + sourcePath
      );
      System.exit(1);
      return;
    }

    matchesMethod.setBody(
        StaticJavaParser.parseBlock("""
            {
            return regexMatchesRfc8089Path.run(text);
            }""")
    );

    String code = unit.toString();

    try {
      BufferedWriter writer = new BufferedWriter(new FileWriter(targetPath));
      writer.write(code);
      writer.flush();
    } catch (IOException e) {
      System.err.println("Failed to write to " + targetPath + ": " + e.getMessage());
      System.exit(1);
    }
  }
}
