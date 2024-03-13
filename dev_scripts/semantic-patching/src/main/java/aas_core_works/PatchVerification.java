

import com.github.javaparser.StaticJavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.Modifier;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.FieldDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.VariableDeclarator;
import com.github.javaparser.ast.expr.AssignExpr;
import com.github.javaparser.ast.expr.Expression;
import com.github.javaparser.ast.expr.VariableDeclarationExpr;
import com.github.javaparser.ast.stmt.ExpressionStmt;
import com.github.javaparser.ast.stmt.ReturnStmt;
import com.github.javaparser.ast.stmt.Statement;

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

    if (!constructMethod.getBody().isPresent()) {
      System.err.println(
          "The constructMatchesRfc8089Path lacks the body in " + sourcePath
      );
      System.exit(1);
      return;
    }



    VariableDeclarator expectedPatternDecl = null;
    ReturnStmt expectedReturnStmt = null;

    for (Statement statement : constructMethod.getBody().get().getStatements()) {
      if (
          statement.isExpressionStmt()
              && statement instanceof ExpressionStmt
      ) {
        ExpressionStmt expressionStmt = (ExpressionStmt) statement;

        Expression expression = expressionStmt.getExpression();

        if (expression instanceof VariableDeclarationExpr) {
          VariableDeclarationExpr varDeclExpr = (VariableDeclarationExpr) expression;

          if (expression.toString().equals("String pattern = \"^\" + fileUri + \"$\"")) {
            expectedPatternDecl = varDeclExpr.getVariable(0);
          }
        }
      } else if (statement.isReturnStmt()) {
        ReturnStmt returnStmt = (ReturnStmt) statement;

        if (returnStmt.toString().equals("return Pattern.compile(pattern);")) {
          expectedReturnStmt = returnStmt;
        }
      }
    }

    if (expectedPatternDecl == null) {
      System.err.println(
          "The constructMatchesRfc8089Path lacks the 'String pattern = \"^\" + fileUri + \"$\"' statement in "
              + sourcePath
      );
      System.exit(1);
      return;
    }

    if (expectedReturnStmt == null) {
      System.err.println(
          "The constructMatchesRfc8089Path lacks the expected return statement " +
              "'return Pattern.compile(pattern);' in " + sourcePath
      );
      System.exit(1);
      return;
    }

    expectedPatternDecl.setInitializer("fileUri");

    expectedReturnStmt.setExpression(
        StaticJavaParser.parseExpression("new RegExp(pattern).toAutomaton()")
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
