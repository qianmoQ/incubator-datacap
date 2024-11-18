#!/bin/sh
HOME=$(pwd)

VERSION=$(./mvnw -Dexec.executable='echo' -Dexec.args='${project.version}' --non-recursive exec:exec -Dorg.slf4j.simpleLogger.defaultLogLevel=WARN -Dorg.slf4j.simpleLogger.log.org.apache.maven.plugins.help=INFO | tail -1)

echo "Publish artifact, usage version is ${VERSION}"

CONF_FILE="${HOME}/configure/cloud.conf"
if [ -f "$CONF_FILE" ]; then
  . "$CONF_FILE"
else
  echo "The configuration file $CONF_FILE does not exist, please check the path."
  exit 1
fi

PLUGIN_DIR="${HOME}/dist/plugins"
if [ ! -d "$PLUGIN_DIR" ]; then
  echo "The directory $PLUGIN_DIR does not exist, please check the path."
  exit 1
fi

echo "Bucket Name: $BUCKET_NAME"
echo "Work Home: $WORK_HOME"

printf "============================================\n"
printf "Publish artifact ...\n"

for file in "$PLUGIN_DIR"/*.tar.gz; do
  if [ ! -e "$file" ]; then
    echo "No tar.gz files found in directory $PLUGIN_DIR."
    exit 0
  fi

  filename=$(basename "$file")
  echo "Document found: $filename"

  case "$filename" in
    datacap-executor-*)
      target_dir="$WORK_HOME/$VERSION/executor"
      ;;
    datacap-convert-*)
      target_dir="$WORK_HOME/$VERSION/convert"
      ;;
    datacap-fs-*)
      target_dir="$WORK_HOME/$VERSION/fs"
      ;;
    datacap-parser-*)
      target_dir="$WORK_HOME/$VERSION/parser"
      ;;
    datacap-scheduler-*)
      target_dir="$WORK_HOME/$VERSION/scheduler"
      ;;
    *)
      target_dir="$WORK_HOME/$VERSION/plugin"
      ;;
  esac

  echo "Uploading $filename to $target_dir"
  qshell rput "$BUCKET_NAME" "$target_dir/$filename" "${HOME}/dist/plugins/$filename"
done

printf "============================================\n"
