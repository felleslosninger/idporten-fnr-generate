package no.idporten.test.generate.fnr;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SyntheticFodselsnummerGenerator {
    final int ILLEGAL_CHECKSUM_VALUE = 10;


    public String fodselsnummerFromYear(final int year) {
        return generateValidFodselsnummer(year);
    }

    public String fodselsnummer() {
        int randomYear = getRandomNumberInRange(1855, Year.now().getValue());
        return fodselsnummerFromYear(randomYear);
    }

    public List<String> listOfFodselsnummers(final int count) {

        List<String> fodselsnummers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int randomYear = getRandomNumberInRange(1855, Year.now().getValue());
            String fnr = generateValidFodselsnummer(randomYear);
            fodselsnummers.add(fnr);
        }
        return fodselsnummers;

    }

    private String generateValidFodselsnummer(final int year) {
        try {
            return generateFodselsnummer(year);
        } catch (RuntimeException e) {
            System.out.println("Try regenerate, but failed: " + e.getMessage());
            return generateValidFodselsnummer(year);
        }
    }


    private String generateFodselsnummer(int year) {
        int[] digits = getFodselsnummerWithEmptyChecksums(year);
        int checksum1 = FodselsnummerChecksum.getChecksum1(digits);

        if (checksum1 == ILLEGAL_CHECKSUM_VALUE) {
            throw new RuntimeException("Invalid checksum1 " + checksum1 + ", please try different individNumber.");
        }
        digits[9] = checksum1;
        int checksum2 = FodselsnummerChecksum.getChecksum2(digits, checksum1);
        if (checksum2 == ILLEGAL_CHECKSUM_VALUE) {
            throw new RuntimeException("Invalid checksum2: " + checksum2 + ", please try different individNumber.");
        }
        digits[10] = checksum2;

        String synteticPid = Arrays
                .stream(digits)
                .mapToObj(String::valueOf)
                .reduce((a, b) -> a.concat("").concat(b))
                .get();

        //if (!PersonIdentifierValidator.isValid(synteticPid)) {
        //  throw new RuntimeException("Sorry, I failed to generate valid, syntetic fodselsnummer: " + synteticPid);
        //}

        return synteticPid;
    }

    private int[] getFodselsnummerWithEmptyChecksums(int randomYear) {
        final String ssnDate = generateRandomSsnDate(randomYear);
        String individString = padding0sToStart(getIndividNumber(randomYear), 3);
        String ssn = ssnDate + individString + "00"; // add 0 for the two checksum digits.
        return ssn.chars().map(i -> Character.digit((char) i, 10)).toArray();
    }

    /**
     * Individnummer:
     * 1. 1854-1899, brukes serien 749-500
     * 2. 1900-1999, brukes serien 499-000
     * 3. 1940-1999, brukes også serien 999-900
     * 4. 2000-2039, brukes serien 999-500
     * <p>
     * https://www.skatteetaten.no/person/folkeregister/fodsel-og-navnevalg/barn-fodt-i-norge/fodselsnummer/
     * <p>
     * No all individNumbers are valid, some might give checksum equal to 10 and thus can not be used.
     *
     * @param birthYear 4 digits, between 1855 and 2040
     * @return 3 digit individNumber, -1 if failed to calculate for input year
     */
    private int getIndividNumber(final int birthYear) {
        if (birthYear < 1900 && birthYear > 1854) {
            return getRandomNumberInRange(500, 749);
        }
        if (birthYear < 2000 && birthYear >= 1900) {
            if (birthYear >= 1940 && Math.random() > 0.9) { // Who knows people in this range? Only randomly put 10% here.
                return getRandomNumberInRange(900, 999);
            }
            return getRandomNumberInRange(0, 499);
        }
        if (birthYear >= 2000 && birthYear < 2040) {
            return getRandomNumberInRange(500, 999);
        }

        return -1;
    }


    private String generateRandomSsnDate(int year) {
        int randomMonth = getRandomNumberInRange(1, 12);

        YearMonth yearMonthObject = YearMonth.of(year, randomMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        int randomDay = getRandomNumberInRange(1, daysInMonth);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");
        String randomDate = LocalDate.of(year, randomMonth, randomDay).format(dateTimeFormatter);

        // Add 80 to month to become syntetic fodselsnummer instead of reel fnr
        return padding0sToStart(Integer.parseInt(randomDate) + 8000, 6);
    }

    private String padding0sToStart(int number, int length) {
        StringBuilder s = new StringBuilder(String.valueOf(number));
        while (s.length() < length) {
            s.insert(0, "0");
        }
        return s.toString();
    }


    private Integer getRandomNumberInRange(int min, int max) {
        Random random = new Random();
        return random.ints(min, (max + 1)).findFirst().getAsInt();
    }

}
