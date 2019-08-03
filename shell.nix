with import <nixpkgs> {};

mkShell {
  buildInputs = [
    jdk8
    sbt
  ];
}
