#!/usr/bin/env bash
#
# Manual release build for Virtual Power Button (no CI required).
#
# Produces a SIGNED release APK (and optionally an AAB). Signing inputs are
# read from environment variables or, if missing, prompted for interactively.
# No secret is ever written to a committed file.
#
# Usage:
#   ./release.sh                 build a signed APK
#   ./release.sh --bundle        also build a signed AAB (for Google Play)
#   ./release.sh --no-clean      skip the clean step (faster, incremental)
#   ./release.sh --help          show this help
#
# Signing inputs (env vars; prompted if unset):
#   RELEASE_STORE_FILE       path to the keystore (.jks)
#   RELEASE_STORE_PASSWORD   keystore password
#   RELEASE_KEY_ALIAS        key alias
#   RELEASE_KEY_PASSWORD     key password
#
# Create a keystore once with:
#   keytool -genkeypair -v -keystore release.jks -alias myalias \
#     -keyalg RSA -keysize 2048 -validity 10000

set -euo pipefail

cd "$(dirname "$0")"

bundle=false
clean=true
for arg in "$@"; do
  case "$arg" in
    --bundle) bundle=true ;;
    --no-clean) clean=false ;;
    -h|--help) awk 'NR==1{next} /^#/{sub(/^# ?/,""); print; next} {exit}' "$0"; exit 0 ;;
    *) echo "Unknown option: $arg (use --help)" >&2; exit 2 ;;
  esac
done

# --- Resolve signing inputs -------------------------------------------------
prompt_if_empty() {
  local var="$1" message="$2" secret="${3:-false}" value
  if [ -z "${!var:-}" ]; then
    if [ "$secret" = true ]; then
      read -r -s -p "$message: " value; echo
    else
      read -r -p "$message: " value
    fi
    export "$var=$value"
  fi
}

prompt_if_empty RELEASE_STORE_FILE "Keystore path (.jks)"
if [ ! -f "$RELEASE_STORE_FILE" ]; then
  echo "Keystore not found: $RELEASE_STORE_FILE" >&2
  echo "Create one with:" >&2
  echo "  keytool -genkeypair -v -keystore release.jks -alias myalias -keyalg RSA -keysize 2048 -validity 10000" >&2
  exit 1
fi
prompt_if_empty RELEASE_KEY_ALIAS "Key alias"
prompt_if_empty RELEASE_STORE_PASSWORD "Keystore password" true
prompt_if_empty RELEASE_KEY_PASSWORD "Key password" true

# --- Build ------------------------------------------------------------------
tasks=()
$clean && tasks+=("clean")
tasks+=(":app:assembleRelease")
$bundle && tasks+=(":app:bundleRelease")

echo "Building: ${tasks[*]}"
./gradlew "${tasks[@]}"

# --- Report -----------------------------------------------------------------
apk="app/build/outputs/apk/release/app-release.apk"
aab="app/build/outputs/bundle/release/app-release.aab"

echo
echo "Build finished."
if [ -f "$apk" ]; then
  echo "APK: $apk"
  if command -v apksigner >/dev/null 2>&1; then
    apksigner verify --print-certs "$apk" >/dev/null 2>&1 \
      && echo "APK signature: OK" \
      || echo "APK signature: could not verify (check apksigner output manually)"
  fi
else
  echo "Expected signed APK not found at $apk" >&2
  echo "(If it built as app-release-unsigned.apk, the signing inputs were not picked up.)" >&2
  exit 1
fi
if $bundle && [ -f "$aab" ]; then
  echo "AAB: $aab"
fi
