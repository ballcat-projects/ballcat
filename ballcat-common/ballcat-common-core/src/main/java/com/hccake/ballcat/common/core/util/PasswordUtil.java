package com.hccake.ballcat.common.core.util;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.AES;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

/**
 * @author Hccake
 * @version 1.0
 * @date 2019/9/25 15:14
 */
public class PasswordUtil {

    public static final PasswordEncoder ENCODER = new BCryptPasswordEncoder();

    public static String decodeAesAndEncodeBCrypt(String pass, String secretKey){
        return encodeBCrypt(decodeAES(pass, secretKey));
    }


    public static String decodeAES(String aesPass, String secretKey){

        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(secretKey.getBytes(), "AES"),
                new IvParameterSpec(secretKey.getBytes()));
        byte[] result = aes.decrypt(Base64.decode(aesPass.getBytes(StandardCharsets.UTF_8)));
        // 删除byte数组中补位产生的\u0000, 否则密码校验时会有问题
        return new String(result, StandardCharsets.UTF_8).replaceAll("[\u0000]", "");
    }


    public static String encodeAESBase64(String pass, String secretKey){
        AES aes = new AES(Mode.CBC, Padding.NoPadding,
                new SecretKeySpec(secretKey.getBytes(), "AES"),
                new IvParameterSpec(secretKey.getBytes()));
        return aes.encryptBase64(pass, StandardCharsets.UTF_8);
    }


    public static String encodeBCrypt(String pass){
        return ENCODER.encode(pass);
    }


    public static void main(String[] args) {

        System.out.println(decodeAES("4Yj0Jfy+MjEW/RGafIoEJA==","==BallCat-Auth=="));;


        String pass = "a123456";
        String password = ENCODER.encode(pass);

        System.out.println(password);

        System.out.println(ENCODER.matches(pass,password));


        System.out.println(ENCODER.matches(pass,"$2a$10$YJDXeAsk7FjQQVTdutIat.rPR3p3uUPWmZyhtnRDOrIjPujOAUrla"));

    }

}
