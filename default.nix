let
  pinnedVersion = "19.03";

  pkgs =
   import (builtins.fetchTarball {
     # Descriptive name to make the store path easier to identify
     name = "nixos-tag-${pinnedVersion}";
     # URL for nixpks releases on GitHub
     url = "https://github.com/NixOS/nixpkgs/archive/${pinnedVersion}.tar.gz";
     # Hash obtained using `nix-prefetch-url --unpack <url>`
     sha256 = "0q2m2qhyga9yq29yz90ywgjbn9hdahs7i8wwlq7b55rdbyiwa5dy";
   }) {};

  buildInputs = with pkgs; [
    openjdk11
    sbt
    nodejs-11_x  # Needed for the website and documentation
  ];

in
{
  shell =
    pkgs.mkShell {
      src = if pkgs.lib.inNixShell then null else pkgs.nix;
      inherit buildInputs;
    };
}
