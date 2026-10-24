#!/usr/bin/env bash

set -euo pipefail

validator="$(dirname "$0")/validate_plugin_portal_release.sh"

"$validator" v2.1.1 2.1.1 >/dev/null
"$validator" v.2.1.1 2.1.1 >/dev/null

if "$validator" v2.1.1 2.1.1-SNAPSHOT >/dev/null 2>&1; then
    echo "snapshot versions must be rejected" >&2
    exit 1
fi

if "$validator" v2.1.2 2.1.1 >/dev/null 2>&1; then
    echo "tag/version mismatches must be rejected" >&2
    exit 1
fi

if "$validator" v2.1.1 2.1 >/dev/null 2>&1; then
    echo "non-release versions must be rejected" >&2
    exit 1
fi

echo "Plugin Portal release validation tests passed"
