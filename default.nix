{ stdenv, pkgs, src, buildInputs }:

stdenv.mkDerivation {
  inherit src buildInputs;
  name = "cilib";
  buildPhase = "touch $out";
  installPhase = ":";
}
