#!/bin/bash

# Install SDKMAN
curl -s "https://get.sdkman.io" | bash
source "$HOME/.sdkman/bin/sdkman-init.sh"

# Install Maven and Java 17 using SDKMAN
sdk install maven
sdk install java 17.0.1-open

# Set Java 17 as default
sdk default java 17.0.1-open

# Display installed versions
echo "SDKMAN, Maven, and Java 17 have been installed:"
echo "SDKMAN version: $(sdk version)"
echo "Maven version: $(mvn -v)"
echo "Java version: $(java -version)"

# Cleanup
unset SDKMAN_CANDIDATES_API
unset SDKMAN_PLATFORM
unset SDKMAN_CANDIDATE
unset SDKMAN_CANDIDATES_CSV
unset SDKMAN_ENV
unset SDKMAN_LEGACY_API
unset SDKMAN_VERSION

# Generate Target
mvn clean install