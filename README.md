SolidBlue2
================================

[![Build Status](https://drone.io/bitbucket.org/tidalwave/solidblue2-src/status.png)](https://drone.io/bitbucket.org/tidalwave/solidblue2-src/latest)

This project is a tiny utility to compute fingerprints of files. It is mainly used as the reference code for
a series of posts about Java 8 lambda programming, starting at:

http://tidalwave.it/fabrizio/blog/java-8-parallelism-exercise-part-i/

But it can also be used as a working tool.


Bootstrapping
-------------

In order to build the project, run from the command line:

```mvn -DskipTests```

The project can be opened and built by a recent version of the NetBeans, Eclipse or Idea IDEs.


Documentation
-------------

More information can be found on the [homepage](http://tidalwave.kenai.com/solidblue2) of the project.


Where can I get the latest release?
-----------------------------------
You can download source and binaries from the [download page](https://bitbucket.org/tidalwave/solidblue2-src/src).

Alternatively you can pull it from the central Maven repositories:

```xml
<dependency>
    <groupId>it.tidalwave.solidblue2<groupId>
    <artifactId>solidblue2</artifactId>
    <version>-- version --</version>
</dependency>
```


Contributing
------------

We accept pull requests via BitBucket or GitHub.

There are some guidelines which will make applying pull requests easier for us:

* No tabs! Please use spaces for indentation.
* Respect the code style.
* Create minimal diffs - disable on save actions like reformat source code or organize imports. If you feel the source
  ode should be reformatted create a separate PR for this change.
* Provide TestNG tests for your changes and make sure your changes don't break any existing tests by running
```mvn clean test```.

If you plan to contribute on a regular basis, please consider filing a contributor license agreement. Contact us for
 more information


License
-------
Code is released under the [Apache Licence v2](https://www.apache.org/licenses/LICENSE-2.0.txt).


Additional Resources
--------------------

* [Tidalwave Homepage](http://tidalwave.it)
* [Project Issue Tracker (Jira)](http://services.tidalwave.it/jira/browse/SLBII)
* [Project Continuous Integration (hudson)](http://ci.tidalwave.it/ci/view/SolidBlue2)
