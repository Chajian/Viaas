import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.SecureRandom;
import java.util.Arrays;

public class AESECBModeTest {
    public static void main(String[] args) {
        // 生成随机AES密钥（128位，16字节）
        byte[] key = generateRandomKey(128);

        // 打印生成的密钥
        System.out.println("生成的AES密钥: " + bytesToHex(key));

        // 生成随机明文块（128位，16字节）
        byte[] plainText = generateRandomBlock(7915); // 32字节的明文块

        // 打印原始明文
        System.out.println("原始明文: " + bytesToHex(plainText));

        // 加密
        byte[] encryptedText = AESECB.git (plainText, key);

        // 打印加密后的密文
        System.out.println("加密后的密文: " + bytesToHex(encryptedText));

        // 解密
        byte[] decryptedText = AESECB.decrypt(encryptedText, key);

        // 打印解密后的明文
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
