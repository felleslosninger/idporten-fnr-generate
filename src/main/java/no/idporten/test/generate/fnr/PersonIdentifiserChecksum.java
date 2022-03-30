package no.idporten.test.generate.fnr;

public class PersonIdentifiserChecksum {

    public static int getChecksum1(int[] n) {
        int checksum1 = 11
                - ((3 * n[0] + 7 * n[1] + 6 * n[2] + 1 * n[3] + 8 * n[4] + 9 * n[5] + 4 * n[6] + 5 * n[7] + 2 * n[8]) % 11);
        if (checksum1 == 11) {
            checksum1 = 0;
        }
        return checksum1;
    }

    public static int getChecksum2(int[] n, int checksum1) {
        int checksum2 = 11
                - ((5 * n[0] + 4 * n[1] + 3 * n[2] + 2 * n[3] + 7 * n[4] + 6 * n[5] + 5 * n[6] + 4 * n[7] + 3 * n[8] + 2 * checksum1) % 11);
        if (checksum2 == 11) {
            checksum2 = 0;
        }
        return checksum2;
    }


}
