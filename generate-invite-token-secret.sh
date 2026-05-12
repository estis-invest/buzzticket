#!/usr/bin/env bash
set -euo pipefail

ENV_FILE=".env"
VAR_NAME="STAFF_INVITE_HMAC_SECRET"

# Generate a 256-bit (32-byte) random secret, Base64 encoded
SECRET=$(openssl rand -base64 32)

# Ensure .env exists
touch "$ENV_FILE"

# Remove any existing definition
sed -i.bak "/^${VAR_NAME}=/d" "$ENV_FILE"

# Append new secret
echo "${VAR_NAME}=${SECRET}" >> "$ENV_FILE"

echo "✅ ${VAR_NAME} generated and saved to ${ENV_FILE}"