package no.idporten.test.generate.fnr;

import no.idporten.validators.identifier.PersonIdentifierValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GenerateSynteticFodselsnummerTest {

    public void setup(){
        PersonIdentifierValidator.setSyntheticPersonIdentifiersAllowed(true);
        PersonIdentifierValidator.setRealPersonIdentifiersAllowed(false);
    }

    @Test
    public void when_generate_one_fnr_then_one_valid_fnr_is_returned_for_year(){
        GenerateSynteticFodselsnummer generator = new GenerateSynteticFodselsnummer();
        int birthYear = 2001;
        String fnr = generator.generateSynteticFodselsnummerFromYear(birthYear);
        System.out.println(fnr);
        assertNotNull(fnr);
        assertEquals(11, fnr.length());
        assertEquals(4, fnr.indexOf("01")); //ddMMyy.....
        PersonIdentifierValidator.isValid(fnr);
    }

    @Test
    public void when_generate_one_fnr_then_one_valid_fnr_is_returned(){
        GenerateSynteticFodselsnummer generator = new GenerateSynteticFodselsnummer();
        String fnr = generator.generateSynteticFodselsnummers();
        System.out.println(fnr);
        assertNotNull(fnr);
        PersonIdentifierValidator.isValid(fnr);
    }

    @Test
    public void when_generate_10_fnr_then_10_valid_fnr_is_returned(){
        GenerateSynteticFodselsnummer generator = new GenerateSynteticFodselsnummer();
        List<String> fnrs = generator.generateListOfSynteticFodselsnummers(10);
        assertEquals(10, fnrs.size());
        for(String fnr: fnrs){
            System.out.println(fnr);
            assertNotNull(fnr);
            PersonIdentifierValidator.isValid(fnr);
        }

    }
}