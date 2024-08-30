package unit;

import java.util.Arrays;

public class AESUtils {

    private static final int BLOCK_SIZE = 16; // AES块大小为16字节

    /**
     * 加密入口，支持不同的模式
     *
     * @param plainText 明文
     * @param key       密钥
     * @param iv        初始化向量
     * @param mode      加密模式（"ECB", "CBC", "CTR"）
     * @return 加密后的密文
     */
    public static byte[] encrypt(byte[] plainText, byte[] key, byte[] iv, String mode) {
        switch (mode.toUpperCase()) {
            case "ECB":
                return encryptECB(plainText, key);
            case "CBC":
                return encryptCBC(plainText, key, iv);
            case "CTR":
                return encryptCTR(plainText, key, iv);
            default:
                throw new IllegalArgumentException("不支持的加密模式: " + mode);
        }
    }

    /**
     * 解密入口，支持不同的模式
     *
     * @param cipherText 密文
     * @param key        密钥
     * @param iv         初始化向量
     * @param mode       解密模式（"ECB", "CBC", "CTR"）
     * @return 解密后的明文
     */
    public static byte[] decrypt(byte[] cipherText, byte[] key, byte[] iv, String mode) {
        switch (mode.toUpperCase()) {
            case "ECB":
                return decryptECB(cipherText, key);
            case "CBC":
                return decryptCBC(cipherText, key, iv);
            case "CTR":
                return decryptCTR(cipherText, key, iv);
            default:
                throw new IllegalArgumentException("不支持的解密模式: " + mode);
        }
    }

    // ECB模式加密
    private static byte[] encryptECB(byte[] plainText, byte[] key) {
        int numBlocks = (plainText.length + BLOCK_SIZE - 1) / BLOCK_SIZE;
        byte[] encryptedText = new byte[numBlocks * BLOCK_SIZE];

        for (int i = 0; i < numBlocks; i++) {
            byte[] plainBlock = Arrays.copyOfRange(plainText, i * BLOCK_SIZE, Math.min((i + 1) * BLOCK_SIZE, plainText.length));
            byte[] paddedBlock = padBlock(plainBlock, BLOCK_SIZE);
            byte[] encryptedBlock = AESEncryption.encrypt(paddedBlock, key);
            System.arraycopy(encryptedBlock, 0, encryptedText, i * BLOCK_SIZE, BLOCK_SIZE);
        }

        return encryptedText;
    }

    // ECB模式解密
    private static byte[] decryptECB(byte[] cipherText, byte[] key) {
        int numBlocks = cipherText.length / BLOCK_SIZE;
        byte[] decryptedText = new byte[cipherText.length];

        for (int i = 0; i < numBlocks; i++) {
            byte[] cipherBlock = Arrays.copyOfRange(cipherText, i * BLOCK_SIZE, (i + 1) * BLOCK_SIZE);
            byte[] decryptedBlock = AESDecryption.decrypt(cipherBlock, key);
            System.arraycopy(decryptedBlock, 0, decryptedText, i * BLOCK_SIZE, BLOCK_SIZE);
        }

        return unpadData(decryptedText);
    }

    // CBC模式加密
    private static byte[] encryptCBC(byte[] plainText, byte[] key, byte[] iv) {
        int numBlocks = (plainText.length + BLOCK_SIZE - 1) / BLOCK_SIZE;
        byte[] encryptedText = new byte[numBlocks * BLOCK_SIZE];
        byte[] previousBlock = iv.clone();

        for (int i = 0; i < numBlocks; i++) {
            byte[] plainBlock = Arrays.copyOfRange(plainText, i * BLOCK_SIZE, Math.min((i + 1) * BLOCK_SIZE, plainText.length));
            byte[] paddedBlock = padBlock(plainBlock, BLOCK_SIZE);

            // CBC模式：与前一个密文块或IV异或
            for (int j = 0; j < BLOCK_SIZE; j++) {
                paddedBlock[j] ^= previousBlock[j];
            }

            byte[] encryptedBlock = AESEncryption.encrypt(paddedBlock, key);
            System.arraycopy(encryptedBlock, 0, encryptedText, i * BLOCK_SIZE, BLOCK_SIZE);
            previousBlock = encryptedBlock;
        }

        return encryptedText;
    }

    // CBC模式解密
    private static byte[] decryptCBC(byte[] cipherText, byte[] key, byte[] iv) {
        int numBlocks = cipherText.length / BLOCK_SIZE;
        byte[] decryptedText = new byte[cipherText.length];
        byte[] previousBlock = iv.clone();

        for (int i = 0; i < numBlocks; i++) {
            byte[] cipherBlock = Arrays.copyOfRange(cipherText, i * BLOCK_SIZE, (i + 1) * BLOCK_SIZE);
            byte[] decryptedBlock = AESDecryption.decrypt(cipherBlock, key);

            for (int j = 0; j < BLOCK_SIZE; j++) {
                decryptedBlock[j] ^= previousBlock[j];
            }

            System.arraycopy(decryptedBlock, 0, decryptedText, i * BLOCK_SIZE, BLOCK_SIZE);
            previousBlock = cipherBlock;
        }

        return unpadData(decryptedText);
    }

    // CTR模式加密和解密（相同逻辑）
    private static byte[] encryptCTR(byte[] plainText, byte[] key, byte[] iv) {
        int numBlocks = (plainText.length + BLOCK_SIZE - 1) / BLOCK_SIZE;
        byte[] encryptedText = new byte[plainText.length];
        byte[] counter = iv.clone();

        for (int i = 0; i < numBlocks; i++) {
            byte[] encryptedCounter = AESEncryption.encrypt(counter, key);

            int start = i * BLOCK_SIZE;
            int end = Math.min((i + 1) * BLOCK_SIZE, plainText.length);
            byte[] inputBlock = Arrays.copyOfRange(plainText, start, end);

            for (int j = 0; j < inputBlock.length; j++) {
                encryptedText[start + j] = (byte) (inputBlock[j] ^ encryptedCounter[j]);
            }

            incrementCounter(counter);
        }

        return encryptedText;
    }

    private static byte[] decryptCTR(byte[] cipherText, byte[] key, byte[] iv) {
        // CTR模式的解密逻辑与加密相同
        return encryptCTR(cipherText, key, iv);
    }

    // 增加计数器
    private static void incrementCounter(byte[] counter) {
        for (int i = counter.length - 1; i >= 0; i--) {
            counter[i]++;
            if (counter[i] != 0) {
                break;
            }
        }
    }

    // PKCS7填充函数
    private static byte[] padBlock(byte[] block, int blockSize) {
        int paddingLength = blockSize - (block.length % blockSize);
        byte[] paddedBlock = Arrays.copyOf(block, block.length + paddingLength);
        Arrays.fill(paddedBlock, block.length, paddedBlock.length, (byte) paddingLength);
        return paddedBlock;
    }

    // 去除PKCS7填充
    private static byte[] unpadData(byte[] paddedData) {
        int paddingLength = paddedData[paddedData.length - 1] & 0xFF;

        if (paddingLength <= 0 || paddingLength > BLOCK_SIZE) {
            throw new IllegalArgumentException("填充长度不正确");
        }

        for (int i = paddedData.length - paddingLength; i < paddedData.length; i++) {
            if (paddedData[i] != (byte) paddingLength) {
                throw new IllegalArgumentException("填充格式不正确");
            }
        }

        return Arrays.copyOf(paddedData, paddedData.length - paddingLength);
    }
}
