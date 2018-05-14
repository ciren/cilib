with import <nixpkgs> {};

stdenv.mkDerivation {
  name = "java8-env";
  buildInputs = [
    jdk8
    sbt
  ];
}
