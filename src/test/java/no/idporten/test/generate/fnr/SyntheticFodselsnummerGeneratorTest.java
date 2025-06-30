package no.idporten.test.generate.fnr;

import no.idporten.validators.identifier.PersonIdentifierValidator;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SyntheticFodselsnummerGeneratorTest {

    public void setup(){
        PersonIdentifierValidator.setSyntheticPersonIdentifiersAllowed(true);
        PersonIdentifierValidator.setRealPersonIdentifiersAllowed(false);
        PersonIdentifierValidator.setExpandedPersonIdentifiersAllowed(false);
    }

    @Test
    public void when_generate_one_fnr_then_one_valid_fnr_is_returned_for_year(){
        SyntheticFodselsnummerGenerator generator = new SyntheticFodselsnummerGenerator();
        int birthYear = 2001;
        String fnr = generator.fodselsnummerFromYear(birthYear);
        System.out.println(fnr);
        assertNotNull(fnr);
        assertEquals(11, fnr.length());
        assertEquals(4, fnr.indexOf("01")); //ddMMyy.....
        PersonIdentifierValidator.isValid(fnr);
    }

    @Test
    public void when_generate_one_fnr_then_one_valid_fnr_is_returned(){
        SyntheticFodselsnummerGenerator generator = new SyntheticFodselsnummerGenerator();
        String fnr = generator.fodselsnummer();
        System.out.println(fnr);
        assertNotNull(fnr);
        PersonIdentifierValidator.isValid(fnr);
    }

    @Test
    public void when_generate_one_dnr_then_one_valid_dnr_is_returned(){
        SyntheticFodselsnummerGenerator generator = new SyntheticFodselsnummerGenerator();
        String dnr = generator.dnummer();
        System.out.println(dnr);
        assertNotNull(dnr);
        PersonIdentifierValidator.isValid(dnr);
        assertTrue(dnr.startsWith("4") || dnr.startsWith("5") || dnr.startsWith("6") || dnr.startsWith("7"));
    }

    @Test
    public void when_generate_10_fnr_then_10_valid_fnr_is_returned(){
        SyntheticFodselsnummerGenerator generator = new SyntheticFodselsnummerGenerator();
        List<String> fnrs = generator.listOfFodselsnummers(10);
        assertEquals(10, fnrs.size());
        for(String fnr: fnrs){
            System.out.println(fnr);
            assertNotNull(fnr);
            PersonIdentifierValidator.isValid(fnr);
        }

    }

    @Test
    public void when_generate_one_expanded_fnr_then_one_valid_expanded_fnr_is_returned(){
        SyntheticFodselsnummerGenerator generator = new SyntheticFodselsnummerGenerator();
        String fnr = generator.generateExpandedFodselsnummer();
        System.out.println(fnr);
        assertNotNull(fnr);
        PersonIdentifierValidator.setExpandedPersonIdentifiersAllowed(true);
        PersonIdentifierValidator.isValid(fnr);
        PersonIdentifierValidator.setRealPersonIdentifiersAllowed(false);
        assertFalse(PersonIdentifierValidator.isValid(fnr));
    }

    @Test
    public void when_generate_one_expanded_dnr_then_one_valid_expanded_dnr_is_returned(){
        SyntheticFodselsnummerGenerator generator = new SyntheticFodselsnummerGenerator();
        String dnr = generator.generateExpandedDnummer();
        System.out.println(dnr);
        assertNotNull(dnr);
        PersonIdentifierValidator.setExpandedPersonIdentifiersAllowed(true);
        PersonIdentifierValidator.isValid(dnr);
        PersonIdentifierValidator.setRealPersonIdentifiersAllowed(false);
        assertFalse(PersonIdentifierValidator.isValid(dnr));
        assertTrue(dnr.startsWith("4") || dnr.startsWith("5") || dnr.startsWith("6") || dnr.startsWith("7"));
    }


}