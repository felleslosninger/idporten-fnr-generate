# idporten-generate-fnr

![Maven build status](https://github.com/felleslosninger/idporten-generate-fnr/actions/workflows/call-maventests.yml/badge.svg)
[![Latest Stable Version](https://img.shields.io/github/v/release/felleslosninger/idporten-generate-fnr?display_name=tag)](https://github.com/felleslosninger/idporten-generate-fnr/releases)

Generate random syntetic personidentfiers (fodselsnummer) for Norway.
https://www.skatteetaten.no/person/folkeregister/fodsel-og-navnevalg/barn-fodt-i-norge/fodselsnummer/

## Requirements
To build and run the library you need:

* Java 11
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
            <version>0.1.5</version>
            <scope>test</scope>
        </dependency>
```
Simple usage in UnitTest:
```
public void test(){
        GenerateSynteticFodselsnummer synpidGenerator = new GenerateSynteticFodselsnummer();
        String personIdentifier = synpidGenerator.generateSynteticFodselsnummers();
        // Use it and do your testing
}
```
