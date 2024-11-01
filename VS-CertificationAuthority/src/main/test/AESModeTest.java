import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Arrays;

public class AESModeTest {

    public static void main(String[] args) {
        // 生成随机AES密钥（128位，16字节）
        byte[] key = generateRandomKey(128);

        // 生成随机初始化向量（IV）（128位，16字节）
        byte[] iv = generateRandomBlock(16);

        // 打印生成的密钥和IV
        System.out.println("生成的AES密钥: " + bytesToHex(key));
        System.out.println("生成的初始化向量IV: " + bytesToHex(iv));

        // 生成随机明文块（任意长度）
        byte[] plainText = generateRandomBlock(32); // 生成长度为37字节的明文块

        // 测试不同的模式
        testAESMode(plainText, key, iv, "ECB");
        testAESMode(plainText, key, iv, "CBC");
        testAESMode(plainText, key, iv, "CTR");
    }

    // 测试指定模式的AES加密和解密
    private static void testAESMode(byte[] plainText, byte[] key, byte[] iv, String mode) {
        System.out.println("测试模式: " + mode);

        // 加密
        byte[] encryptedText = AESUtils.encrypt(plainText, key, iv, mode);
        System.out.println("加密后的密文: " + bytesToHex(encryptedText));

        // 解密
        byte[] decryptedText = AESUtils.decrypt(encryptedText, key, iv, mode);
        System.out.println("解密后的明文: " + bytesToHex(decryptedText));

        // 验证结果
        if (Arrays.equals(plainText, decryptedText)) {
            System.out.println("测试成功: 解密后的明文与原始明文一致！");
        } else {
            System.out.println("测试失败: 解密后的明文与原始明文不一致。");
        }
    }

    // 生成随机AES密钥
    private static byte[] generateRandomKey(int keySize) {
        try {
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            keyGen.init(keySize, new SecureRandom());
            SecretKey secretKey = keyGen.generateKey();
            return secretKey.getEncoded();
        } catch (Exception e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }

    // 生成随机数据块
    private static byte[] generateRandomBlock(int size) {
        byte[] block = new byte[size];
        new SecureRandom().nextBytes(block);
        return block;
    }

    // 将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString().trim();
    }
}
