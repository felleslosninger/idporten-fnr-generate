# idporten-fnr-generate

![Maven build status](https://github.com/felleslosninger/idporten-fnr-generate/actions/workflows/call-maventests.yml/badge.svg)
[![Latest Stable Version](https://img.shields.io/github/v/release/felleslosninger/idporten-fnr-generate?display_name=tag)](https://github.com/felleslosninger/idporten-fnr-generate/releases)

Generate random syntetic personidentfiers (fodselsnummer) for Norway.
https://www.skatteetaten.no/person/folkeregister/fodsel-og-navnevalg/barn-fodt-i-norge/fodselsnummer/

## Requirements
To build and run the library you need:

* Java 17
* Maven

## Running the library locally

From the command line: 
```
mvn install
```
See [GenerateSynteticFodselsnummerTest.java](/src/test/java/no/idporten/test/generate/fnr/GenerateSynteticFodselsnummerTest.java)

## To use it to generate test-user in another projects test

Include in pom.xml
```
        <dependency>
            <groupId>no.idporten.test.generate</groupId>
            <artifactId>idporten-fnr-generate</artifactId>
            <version>1.1.0</version>
            <scope>test</scope>
        </dependency>
```
Simple usage in UnitTest:
```
public void test(){
        SyntheticFodselsnummerGenerator synFnrGenerator = new SyntheticFodselsnummerGenerator();
        String personIdentifier = synFnrGenerator.fodselsnummer();
        // Use it and do your testing
}
```
