let
  pinnedVersion = "20.03";

  pkgs =
   import (builtins.fetchTarball {
     # Descriptive name to make the store path easier to identify
     name = "nixos-tag-${pinnedVersion}";
     # URL for nixpks releases on GitHub
     url = "https://github.com/NixOS/nixpkgs/archive/${pinnedVersion}.tar.gz";
     # Hash obtained using `nix-prefetch-url --unpack <url>`
     sha256 = "0182ys095dfx02vl2a20j1hz92dx3mfgz2a6fhn31bqlp1wa8hlq";
   }) {};

  buildInputs = with pkgs; [
    openjdk11
    sbt
    yarn
    nodejs
    #node2nix
    #yarn2nix
  ];

in
{
  shell =
    pkgs.mkShell {
      src = if pkgs.lib.inNixShell then null else pkgs.nix;
      inherit buildInputs;
    };
}
