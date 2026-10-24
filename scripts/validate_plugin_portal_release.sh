#!/usr/bin/env bash

set -euo pipefail

release_tag=${1:?release tag is required}
project_version=${2:?project version is required}

case "$release_tag" in
    v.*) expected_version=${release_tag#v.} ;;
    v*) expected_version=${release_tag#v} ;;
    *) expected_version=$release_tag ;;
esac

if [[ -z "$project_version" || "$project_version" == *SNAPSHOT* ]]; then
    echo "Plugin Portal releases require a final, non-SNAPSHOT project version" >&2
    exit 1
fi

if [[ ! "$project_version" =~ ^[0-9]+(\.[0-9]+){2}([.-][0-9A-Za-z.-]+)?$ ]]; then
    echo "Project version '$project_version' is not a valid final release version" >&2
    exit 1
fi

if [[ "$expected_version" != "$project_version" ]]; then
    echo "Release tag '$release_tag' does not match project version '$project_version'" >&2
    exit 1
fi

echo "Validated Plugin Portal release $project_version from tag $release_tag"
