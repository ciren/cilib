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
              rev = version;
              sha256 = "1hqps7l5qrjh9f914r5i6kmcz6f1yb951nv4lby0cjnp5l253kps";
            };

            cargoSha256 = "x6ncQ+H6xHmSOmM0BiGauEEkau/Y2cvFUG32X3HC7J4=";

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
            openjdk11
            sbt
            yarn
            nodejs
            nushell
            nu_plugin_from_parquet
            #node2nix
            #yarn2nix
          ];
        };
      }
    );
}
