# Coherence Eclipse Plugin

# Contents

* [Overview](#overview)
* [Build the Plugin](#build-the-plugin)
* [Install the Plugin](#install-the-plugin)
* [Using the Plugin](#using-the-plugin)
* [Example Projects](examples/pof-plugin-example-project)
* [Development](#development)

# Overview

The Coherence Eclipse Plugin provides various features to help developer productivity
when working with Coherence.

Currently there this plugin supports the following features:
* `Coherence POF Plugin` - Provides a development time instrumentation of classes with the PortableType annotation to generate
  consistent (and correct) implementations of Evolvable POF serialization methods.
  This development time plugin can be used in conjunction with the `POF Maven Plugin` which instruments classes at project build time.

# Build the Plugin

You must have:
* JDK1.8
* Maven 3.6.3+
* Minimum Eclipse version 2020-03

1. Clone the repository

   ```bash
   git clone https://gitlab-odx.oracledx.com/coherence/coherence-eclipse-plugin.git
   ```

1. Build the plugin

   ```bash
   cd coherence-eclipse-plugin
   
   mvn clean install
   ```

This will generate a zip file called `./coherence.eclipse.plugin.repository/target/coherence-eclipse-plugin-repository-1.0.0-SNAPSHOT.zip`

# Install the Plugin

1. Help -> Install New Software

1. Click on `Add` button on the right

1. Choose a name such as `Coherence Local`

1. Click on `Archive` and choose the zip file mentioned above and click `Add`

1. Select the `Coherence Eclipse Plugin` displayed in the list and click `Next`, then `Next`

1. Accept the license terms and click `Finish`

1. You may receive a warning that the plugin is unsigned. Choose `Install Anyway`

1. Click `Restart Now` to restart Eclipse  

# Using the plugin

> Note: You can use the sample project [here](examples/pof-plugin-example-project) to test the plugin.

## Enable the Plugin

1. Right-click on your open project

2. Select `Configure` then `Enable Coherence POF Plugin`

![Messages](assets/enable.png)

## Disable the Plugin

1. Right-click on your open project

2. Select `Configure` then `Disable Coherence POF Plugin`

## Verifying

When you build your project, for any Java classes that use the `@PortableType` annotation
you will see an Info message in the `Problems` tab saying the class was instrumented.

A message indicating the class was not instrumented can indicate that it has already been instrumented
or is ignored because is does not implement the `PortableType` annotation.

![Messages](assets/messages.png)
   
# Uninstall the Plugin

1. Help -> Install New Software

1. Click on `What is already installed` link

1. Search for `Coherence` and select `Coherence Eclipse Plugin`

1. Click `Un-install` then `Finish`

1. Click `Restart Now` to restart Eclipse  

# Example projects

Please see [here](examples/pof-plugin-example-project) for and example project and instructions how to import.

# Development

To import the plugin project into your Eclipse IDE, carry out the following:

1. Clone the repository using:

   ```bash
   git clone https://gitlab-odx.oracledx.com/coherence/coherence-eclipse-plugin.git
   ```

1. Open Eclipse

1. Choose `File->Open Projects from Filesystem`

1. Choose `Directory` and select the `coherence-eclipse-plugin` project you cloned, then click `Done`

1. Ensure you only select the `coherence-ecplise-plugin/coherence.eclipse.plugin` as shown below:

   ![Import](assets/import.png)

> Note: Ignore any errors from the `pom.xml` file as this is only used for a Maven build.
