package com.viaas.certification.encoder;

public interface HashEncoder {
    String hash(String input);

    boolean verify(String input,String hash);
}
