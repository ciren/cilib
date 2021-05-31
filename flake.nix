{
  description = "cilib";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/master";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};
        cilib = import ./default.nix { pkgs = pkgs; };
      in
      {
        devShell = pkgs.mkShell { buildInputs = cilib.buildInputs; };
      }
    );
}
