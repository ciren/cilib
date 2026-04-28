{
  description = "cilib";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/release-25.11";
    flake-parts.url = "github:hercules-ci/flake-parts";
  };

  outputs = inputs@{ flake-parts, ...  }:
    flake-parts.lib.mkFlake { inherit inputs; } (_: {
      systems = [ "x86_64-linux" "x86_64-darwin" ];

      perSystem =
        { pkgs, ... }:
        {
          config.devShells.default =
            pkgs.mkShell {
              buildInputs = [
                pkgs.sbt
                pkgs.nodejs
                pkgs.gnupg
              ];
            };
        };
    });
}
