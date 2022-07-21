{
  description = "cilib";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/22.05";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      with import nixpkgs { system = "${system}"; };
      let
        buildInputs =  [
            sbt
            yarn
            nodejs
            #nushell
            #pqrs
            #node2nix
            #yarn2nix
          ];
      in
      {
        packages.pkg = callPackage ./default.nix { inherit buildInputs; };

        defaultPackage = self.packages.${system}.pkg;

        devShell = mkShell { inherit buildInputs; };
      }
    );
}
