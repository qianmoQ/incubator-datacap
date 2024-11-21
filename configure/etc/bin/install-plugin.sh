#!/bin/sh

HOME=$(pwd)
VERSION=2024.4.0-SNAPSHOT
CDN_CENTER="https://cdn.north.devlive.org/applications/datacap/plugins/${VERSION}"

install_package() {
    DOWNLOAD_URL=$1
    TARGET_DIR=$2
    PACKAGE_NAME=$(basename $DOWNLOAD_URL)
    PKG_BASE_NAME=$(basename $PACKAGE_NAME -bin.tar.gz)

    if wget "$DOWNLOAD_URL" -P "/tmp"; then
        echo "Downloaded $PACKAGE_NAME successfully"
        if tar -xzf "/tmp/$PACKAGE_NAME" -C "$TARGET_DIR"; then
            echo "Extracted $PACKAGE_NAME to $TARGET_DIR successfully"
            rm "/tmp/$PACKAGE_NAME"
            TIMESTAMP=$(date +%Y%m%d%H%M%S)
            touch "${TARGET_DIR}/${PKG_BASE_NAME}/.${TIMESTAMP}.installed"
            return 0
        else
            echo "Failed to extract $PACKAGE_NAME"
            rm "/tmp/$PACKAGE_NAME"
            return 1
        fi
    else
        echo "Failed to download $PACKAGE_NAME"
        return 1
    fi
}

process_section() {
    SECTION=$1
    TYPE=$2

    if [ ! -d "${HOME}/plugins" ]; then
        mkdir "${HOME}/plugins"
        echo "Create plugins directory"
    fi

    echo "Installing $TYPE components, version: ${VERSION}"
    IN_SECTION=0

    while IFS= read -r line; do
        if [ "$line" = "-- ${SECTION} list --" ]; then
            IN_SECTION=1
            continue
        elif [[ "$line" = --* ]] && [ $IN_SECTION -eq 1 ]; then
            break
        fi

        if [ $IN_SECTION -eq 1 ] && [ ! -z "$line" ] && [[ ! "$line" =~ ^--.*$ ]]; then
            DOWNLOAD_URL="${CDN_CENTER}/${TYPE}/${line}-bin.tar.gz"
            echo "Installing ${TYPE} from: $DOWNLOAD_URL"
            install_package "$DOWNLOAD_URL" "${HOME}/plugins"
        fi
    done < "${HOME}/configure/plugin.conf"
}

echo "========== Starting installation =========="
echo "Version: ${VERSION}"
echo "CDN Center: ${CDN_CENTER}"

process_section "Plugin" "plugin"
process_section "Scheduler" "scheduler"
process_section "Parser" "parser"
process_section "Notify" "notify"
process_section "Fs" "fs"
process_section "Convert" "convert"
process_section "Executor" "executor"

echo "========== Installation complete =========="