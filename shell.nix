let
  flake-compat = import (builtins.fetchGit "https://github.com/edolstra/flake-compat");
in
(flake-compat { src = ./.; }).shellNix


# { pkgs ? null }:
# let
#   pinnedVersion = "20.09";

#   localPkgs =
#     if pkgs == null then
#       (import
#         (builtins.fetchTarball {
#           # Descriptive name to make the store path easier to identify
#           name = "nixos-tag-${pinnedVersion}";
#           # URL for nixpks releases on GitHub
#           url = "https://github.com/NixOS/nixpkgs/archive/${pinnedVersion}.tar.gz";
#           # Hash obtained using `nix-prefetch-url --unpack <url>`
#           sha256 = "1wg61h4gndm3vcprdcg7rc4s1v3jkm5xd7lw8r2f67w502y94gcy";
#         })
#         { }) else pkgs;
# in
# rec {
#   buildInputs = with localPkgs; [
#     openjdk11
#     sbt
#     yarn
#     nodejs
#     #node2nix
#     #yarn2nix
#   ];

#   shell =
#     localPkgs.mkShell {
#       src = if localPkgs.lib.inNixShell then null else localPkgs.nix;
#       inherit buildInputs;
#     };
# }
