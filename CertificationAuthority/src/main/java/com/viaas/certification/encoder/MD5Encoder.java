package com.viaas.certification.encoder;

import io.micrometer.common.util.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.util.Arrays;

public class MD5Encoder implements PasswordEncoder {


    @Override
    public String encode(CharSequence rawPassword) {
        return Arrays.toString(DigestUtils.md5Digest(rawPassword.toString().getBytes()));
    }

    @Override
    public boolean matches(CharSequence input, String hash) {
        if(ObjectUtils.isEmpty(input)||StringUtils.isEmpty(hash)){

        }
         return hash.equals(Arrays.toString(DigestUtils.md5Digest(input.toString().getBytes())));
    }
}
