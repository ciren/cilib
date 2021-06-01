let
  flake-compat = import (builtins.fetchGit "https://github.com/edolstra/flake-compat");
in
(flake-compat { src = ./.; }).shellNix
