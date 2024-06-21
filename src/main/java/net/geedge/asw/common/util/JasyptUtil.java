package net.geedge.asw.common.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.iv.RandomIvGenerator;

import cn.hutool.core.util.HexUtil;

/**
 * Jasypt 密钥加密工具类
 * 默认使用 版本默认加密算法 PBEWithHMACSHA512AndAES_256
 *
 * @Link https://github.com/ulisesbocchio/jasypt-spring-boot
 */
public class JasyptUtil {

    /**
     * encrypt
     *
     * @param content
     * @return
     */
    public static String encrypt(String content) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        // 加密算法
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());
        String password = HexUtil.encodeHexStr(Constants.AES_KEY);
        encryptor.setPassword(password);
        String encrypt = encryptor.encrypt(content);
        return encrypt;
    }


    /**
     * decrypt
     *
     * @param content
     * @return
     */
    public static String decrypt(String content) {
        StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
        // 加密算法
        encryptor.setAlgorithm("PBEWithHMACSHA512AndAES_256");
        encryptor.setIvGenerator(new RandomIvGenerator());
        String password = HexUtil.encodeHexStr(Constants.AES_KEY);
        encryptor.setPassword(password);
        String decrypt = encryptor.decrypt(content);
        return decrypt;
    }

}
