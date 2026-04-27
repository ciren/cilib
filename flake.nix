{
  description = "cilib";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/release-25.11";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      with import nixpkgs { system = "${system}"; };
      let
        buildInputs =  [
            sbt
            nodejs
            #nushell
            #pqrs
          ];
      in
      {
        packages.pkg = callPackage ./default.nix { inherit buildInputs; };

        defaultPackage = self.packages.${system}.pkg;

        devShell = mkShell { inherit buildInputs; };
      }
    );
}
