#!/bin/bash

# shellcheck disable=SC2120
update_admob_key() {
  CI_ADMOB_APP_ID="$1"

  MANIFEST_FILE="$GITHUB_WORKSPACE/app/src/main/AndroidManifest.xml"

  if [ -n "$CI_ADMOB_APP_ID" ]; then
      echo "Updating AdmobAppId: $CI_ADMOB_APP_ID"
      if [[ "$OSTYPE" == "darwin"* ]]; then
        sed -i '' "s#android:value=\"ca-app-pub-[^\"]*\"#android:value=\"$CI_ADMOB_APP_ID\"#" "$MANIFEST_FILE"
      else
        sed -i "s#android:value=\"ca-app-pub-[^\"]*\"#android:value=\"$CI_ADMOB_APP_ID\"#" "$MANIFEST_FILE"
      fi
  else
      echo "Failed to update admob key: key is undefined."
  fi
}

update_admob_key "$1"
