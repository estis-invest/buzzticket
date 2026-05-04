#!/usr/bin/env bash
set -euo pipefail

# -----------------------------------------
# JWT RSA Key Generator
# -----------------------------------------
# Usage:
#   ./generate-jwt-keys.sh [output-directory]
#
# Example:
#   ./generate-jwt-keys.sh ./keys
# -----------------------------------------

OUTPUT_DIR="${1:-./keys}"
KEY_SIZE=2048
PRIVATE_KEY="$OUTPUT_DIR/jwt-private.pem"
PUBLIC_KEY="$OUTPUT_DIR/jwt-public.pem"

echo "🔐 Generating JWT RSA key pair"
echo "📁 Output directory: $OUTPUT_DIR"

mkdir -p "$OUTPUT_DIR"

if [[ -f "$PRIVATE_KEY" || -f "$PUBLIC_KEY" ]]; then
  echo "❌ Keys already exist in $OUTPUT_DIR"
  echo "   Aborting to avoid accidental overwrite."
  exit 1
fi

echo "→ Generating RSA private key (${KEY_SIZE} bits)..."
openssl genpkey \
  -algorithm RSA \
  -pkeyopt rsa_keygen_bits:${KEY_SIZE} \
  -out "$PRIVATE_KEY"

chmod 600 "$PRIVATE_KEY"

echo "→ Extracting public key..."
openssl rsa \
  -in "$PRIVATE_KEY" \
  -pubout \
  -out "$PUBLIC_KEY"

chmod 644 "$PUBLIC_KEY"

echo "✅ JWT RSA key pair generated successfully:"
echo "   🔒 Private key: $PRIVATE_KEY"
echo "   🔓 Public key : $PUBLIC_KEY"
echo ""
echo "⚠️  Keep the private key SECRET."
