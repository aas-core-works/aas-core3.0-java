# Semantic Patching

## Building

Package everything with maven:

```
mvn package
```

The scripts will be built in `target/` directory.

## Patch

Run the script:

```
mvn \
    exec:java \
    -Dexec.mainClass="aas_core_works.PatchVerification" \
    -Dexec.args="{path to the source file} {path to the target file}"
```

If you just want to test it out, let it patch `test-data/Verification.java`:

```
mvn \
    exec:java \
    -Dexec.mainClass="aas_core_works.PatchVerification" \
    -Dexec.args="test-data/Verification.java VerificationPatched.java"
```
