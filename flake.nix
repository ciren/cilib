{
  description = "cilib";

  inputs.nixpkgs.url = "github:NixOS/nixpkgs/master";
  inputs.flake-utils.url = "github:numtide/flake-utils";

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = nixpkgs.legacyPackages.${system};

        nu_plugin_from_parquet =
          pkgs.rustPlatform.buildRustPackage rec {
            pname = "nu_plugin_from_parquet";
            version = "0.0.0";

            src = pkgs.fetchFromGitHub {
              owner = "jakeswenson";
              repo = pname;
              rev = "004993608f34df7cfbb0f7ef6c877a6d276d1715";
              sha256 = "F4xOkBd9lphEAIFc5S1ai06rvWC/ceSAP13X8TxcWmg=";
            };

            cargoSha256 = "cc1KtAUN7EDYEFIGZ1QlJlG6ByHC7s4b+njVMUX0OXE=";

            meta = with pkgs.lib; {
              description = "";
              homepage = "https://github.com/jakeswenson/nu_plugin_from_parquet";
              license = licenses.mit;
              maintainers = [];
            };
          };
      in
      {
        devShell = pkgs.mkShell {
          buildInputs = with pkgs; [
            openjdk16
            sbt
            yarn
            nodejs

            nushell
            nu_plugin_from_parquet
            pqrs

            #node2nix
            #yarn2nix
          ];
        };
      }
    );
}
