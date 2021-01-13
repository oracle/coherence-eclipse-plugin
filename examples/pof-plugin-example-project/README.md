# POF Plugin Example Project

This document explains how to import and use the `pof-plugin-example-project` which contains
example Coherence code which utilizes the Coherence POF Plugin.

# Download coherence

Use the following to download the Coherence jar:

```bash
mvn -DgroupId=com.oracle.coherence.ce -DartifactId=coherence -Dversion=20.12 dependency:get
```

# Import the project

> Note: Ensure you have installed the `Coherence POF Plugin` as described [here](../../README.md)
> into your Eclipse IDE.

1. Choose `File->Open Projects from Filesystem`

1. Choose `Directory` and select the `pof-plugin-example-project`, then click `Done`

# Add a coherence user library

1. Right-click on the `pof-plugin-example-project` project and choose `Build Path -> Configure Build Path`

1. Click `Add Library` and Choose `User Library`

1. Click on `User Library` then `New`

1. Name the library `coherence` and and click on `Add External Jars`

1. Select `coherence-20.12.jar` from `~/.m2/repository/com/oracle/coherence/ce/coherence/20.12/`

1. Click on `Apply and Close`, `Finish` then `Apply and close` again

# Build the Project

1. Right-Click on the project to select configure and enable the POF plugin

   ![Enable](../../assets/enable.png)

1. Select `Project->Build All`

1. You should see messages in `Problems` tab showing the classes are instrumented.

   ![Messages](../../assets/messages.png)

# Run the Example

1. Run the class RunExample.java

The output should be similar to the following, indicating that  the classes were successful annotated

```bash
Oracle Coherence Version 20.12 Build 83712
 Community Edition: Development mode
Copyright (c) 2000, 2020, Oracle and/or its affiliates. All rights reserved.

Person cache size is 1
Person [id=1, name=John, address=Address [addressLine1=Address 1, addressLine2=Address 2, city=Perth, state=WA, postCode=3674, country=AU]]
Person classes are equal? true
Person is PortableObject? true
Address is PortableObject? true
Name is Tim Jones
Employee is Employee [empId=123456, employer=Oracle, id=2, name=Tim Jones, address=Address [addressLine1=Address 1, addressLine2=Address 2, city=Perth, state=WA, postCode=2964, country=AU]]
```
