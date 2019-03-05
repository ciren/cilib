let
  pinnedVersion = "18.09";
  pkgs =
   import (builtins.fetchTarball {
     # Descriptive name to make the store path easier to identify
     name = "nixos-tag-${pinnedVersion}";
     # URL for nixpks releases on GitHub
     url = "https://github.com/NixOS/nixpkgs/archive/${pinnedVersion}.tar.gz";
     # Hash obtained using `nix-prefetch-url --unpack <url>`
     sha256 = "1ib96has10v5nr6bzf7v8kw7yzww8zanxgw2qi1ll1sbv6kj6zpd";
   }) {};
in
pkgs.mkShell {
  src = if pkgs.lib.inNixShell then null else pkgs.nix;
  buildInputs = with pkgs; [
    jdk8
    sbt
  ];
}
