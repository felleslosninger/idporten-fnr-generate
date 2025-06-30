package no.idporten.test.generate.fnr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.time.Year;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SyntheticFodselsnummerGenerator {
    private static final Logger log = LoggerFactory.getLogger(SyntheticFodselsnummerGenerator.class);
    final int ILLEGAL_CHECKSUM_VALUE = 10;


    public String generateExpandedFodselsnummer(final boolean isDnummer) {
        int randomYear = getRandomNumberInRange(1855, Year.now().getValue());
        return generateExpandedFodselsnummerFromYear(randomYear, isDnummer);
    }

    public String generateExpandedFodselsnummerFromYear(final int year, final boolean isDnummer) {
        return generateValidFodselsnummer(year, isDnummer, true);
    }

    public String fodselsnummerFromYear(final int year) {
        return generateValidFodselsnummer(year, false, false);
    }

    public String dnummerFromYear(final int year) {
        return generateValidFodselsnummer(year, true, false);
    }

    public String dnummer() {
        int randomYear = getRandomNumberInRange(1855, Year.now().getValue());
        return fodselsnummerFromYear(randomYear);
    }

    public String fodselsnummer() {
        int randomYear = getRandomNumberInRange(1855, Year.now().getValue());
        return fodselsnummerFromYear(randomYear);
    }

    public List<String> listOfFodselsnummers(final int count) {

        List<String> fodselsnummers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int randomYear = getRandomNumberInRange(1855, Year.now().getValue());
            String fnr = generateValidFodselsnummer(randomYear, false, false);
            fodselsnummers.add(fnr);
        }
        return fodselsnummers;

    }

    public List<String> listOfDnummers(final int count) {

        List<String> fodselsnummers = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            int randomYear = getRandomNumberInRange(1855, Year.now().getValue());
            String fnr = generateValidFodselsnummer(randomYear, true, false);
            fodselsnummers.add(fnr);
        }
        return fodselsnummers;

    }

    private String generateValidFodselsnummer(final int year, final boolean isDNummer, final boolean isExpanded) {
        try {
            return generateFodselsnummer(year, isDNummer, isExpanded);
        } catch (RuntimeException e) {
            log.debug("Try regenerate, but failed: " + e.getMessage());
            return generateValidFodselsnummer(year, isDNummer, isExpanded);
        }
    }

    private int handleFirstChecksum(int[] digits, boolean isExpanded) {
        int checksum1 = FodselsnummerChecksum.getBeregnetRestSiffer1(digits);
        if (isExpanded) {
            // For expanded numbers, we can not generate checksum1, so we just return 0.
            checksum1 += getRandomNumberInRange(1,3);
        } else {
            // For normal numbers, we can generate checksum1.
            if (checksum1 == 11) {
                checksum1 = 0; // If checksum1 is 11, we set it to 0.
            } else if (checksum1 == ILLEGAL_CHECKSUM_VALUE) {
                throw new RuntimeException("Invalid checksum1 " + checksum1 + ", please try different individNumber.");
            }
        }
        return checksum1;
    }

    private String generateFodselsnummer(int year, boolean isDnummer, boolean isExpanded) {
        int[] digits = getFodselsnummerWithEmptyChecksums(year, isDnummer, isExpanded);

        digits[9] = handleFirstChecksum(digits, isExpanded);
        int checksum2 = FodselsnummerChecksum.getChecksum2(digits, digits[9]);
        if (checksum2 == ILLEGAL_CHECKSUM_VALUE) {
            throw new RuntimeException("Invalid checksum2: " + checksum2 + ", please try different individNumber.");
        }
        digits[10] = checksum2;

        String synteticPid = Arrays
                .stream(digits)
                .mapToObj(String::valueOf)
                .reduce((a, b) -> a.concat("").concat(b))
                .get();

        return synteticPid;
    }

    private int[] getFodselsnummerWithEmptyChecksums(int randomYear, boolean isDnummer, boolean isExpanded) {
        final String ssnDate = generateRandomSsnDate(randomYear, isDnummer);
        String individString = padding0sToStart(getIndividNumber(randomYear, isExpanded), 3);
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
    private int getIndividNumber(final int birthYear, final boolean isExpanded) {
        if (isExpanded) {
            return getRandomNumberInRange(0, 999);
        }
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


    private String generateRandomSsnDate(int year, boolean isDnummer) {
        int randomMonth = getRandomNumberInRange(1, 12);

        YearMonth yearMonthObject = YearMonth.of(year, randomMonth);
        int daysInMonth = yearMonthObject.lengthOfMonth();
        int randomDay = getRandomNumberInRange(1, daysInMonth);

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy");
        String randomDate = LocalDate.of(year, randomMonth, randomDay).format(dateTimeFormatter);

        // if D-number, then add 40 to the first digit of the day
        if (isDnummer) {
            int day = Integer.parseInt(randomDate.substring(0, 2));
            day += 40; // D-number has first digit of day between 4 and 7
            randomDate = padding0sToStart(day, 2) + randomDate.substring(2);
        }

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
