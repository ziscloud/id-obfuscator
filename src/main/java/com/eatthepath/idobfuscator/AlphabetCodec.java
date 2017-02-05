package com.eatthepath.idobfuscator;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AlphabetCodec implements IntegerCodec {

    private final char[] alphabet;
    private final Map<Character, Integer> charactersToValues = new HashMap<>();

    public AlphabetCodec(final char... alphabet) {
        Objects.requireNonNull(alphabet, "Alphabet must not be null.");

        if (alphabet.length < 2) {
            throw new IllegalArgumentException("Alphabet must contain at least two characters");
        }

        this.alphabet = alphabet;

        for (int i = 0; i < alphabet.length; i++) {
            this.charactersToValues.put(alphabet[i], i);
        }
    }

    @Override
    public String encodeIntegerAsString(final int i) {
        final String encodedString;

        if (i == 0) {
            encodedString = new String(new char[] { this.alphabet[0] });
        } else {
            long workingCopy = i & 0xffffffffL;
            final StringBuilder builder = new StringBuilder();

            while (workingCopy != 0) {
                builder.insert(0, this.alphabet[(int) (workingCopy % this.alphabet.length)]);
                workingCopy /= this.alphabet.length;
            }

            encodedString = builder.toString();
        }

        return encodedString;
    }

    @Override
    public int decodeStringAsInteger(final String string) {
        final char[] chars = string.toCharArray();
        long decoded = 0;
        int exponent = chars.length - 1;

        for (final char c : chars) {
            final int x = this.charactersToValues.get(c);
            decoded += x * exponentiate(this.alphabet.length, exponent--);
        }

        return (int) decoded;
    }

    static long exponentiate(final int x, final int exponent) {
        long exponentiated = 1;

        for (int i = 0; i < exponent; i++) {
            exponentiated *= x;
        }

        return exponentiated;
    }
}