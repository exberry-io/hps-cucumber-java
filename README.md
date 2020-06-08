# hps-cucumber-java

[![Build Status](https://travis-ci.org/hiptest/hps-cucumber-java.svg?branch=master)](https://travis-ci.org/hiptest/hps-cucumber-java)

Hiptest publisher samples with Cucumber/Java

In this repository you'll find tests generated in Cucumber/Java format from [Hiptest](https://hiptest.com), using [Hiptest publisher](https://github.com/hiptest/hiptest-publisher).

The goals are:

 * to show how tests are exported in Cucumber/Java.
 * to check exports work out of the box (well, with implemented actionwords)

System under test
------------------

The SUT is exchange multi tenant cluster

Update tests
-------------


To update the tests:

    "hiptest-publisher -c cucumber-java.conf --only=features,step_definitions"

The tests are generated in [``src/main/test/java/io/exberry/e2e``](https://github.com/exberry-io/hps-tests/tree/master/src/test/java/io/exberry/e2e)

Run tests
---------


To build the project and run the tests, use the following command:

    mvn test
    
    
The test report is generated in ```target/cuke-results.json```
