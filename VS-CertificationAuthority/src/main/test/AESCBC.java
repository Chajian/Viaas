import java.util.Arrays;

public class AESCBC {
    // AES加密函数，支持CBC模式
    public static byte[] encrypt(byte[] plainText, byte[] key, byte[] iv) {
        int blockSize = 16; // AES块大小为16字节
        int numBlocks = (plainText.length + blockSize - 1) / blockSize; // 计算需要加密的块数
        byte[] encryptedText = new byte[numBlocks * blockSize]; // 密文数组
        byte[] previousBlock = iv.clone(); // 初始化IV

        for (int i = 0; i < numBlocks; i++) {
            // 取明文块
            byte[] plainBlock = Arrays.copyOfRange(plainText, i * blockSize, Math.min((i + 1) * blockSize, plainText.length));
            // 使用PKCS7填充块
            byte[] paddedBlock = padBlock(plainBlock, blockSize);

            // CBC模式：与前一个密文块或IV异或
            for (int j = 0; j < blockSize; j++) {
                paddedBlock[j] ^= previousBlock[j];
            }

            // 加密当前块
            byte[] encryptedBlock = AESEncryption.encrypt(paddedBlock, key);

            // 将加密后的块存储到密文中
            System.arraycopy(encryptedBlock, 0, encryptedText, i * blockSize, blockSize);

            // 更新previousBlock为当前的密文块
            previousBlock = encryptedBlock;
        }

        return encryptedText;
    }

    // AES解密函数，支持CBC模式
    public static byte[] decrypt(byte[] encryptedText, byte[] key, byte[] iv) {
        int blockSize = 16; // AES块大小为16字节
        int numBlocks = encryptedText.length / blockSize; // 计算密文块数
        byte[] decryptedText = new byte[encryptedText.length]; // 明文数组
        byte[] previousBlock = iv.clone(); // 初始化IV

        for (int i = 0; i < numBlocks; i++) {
            // 取密文块
            byte[] encryptedBlock = Arrays.copyOfRange(encryptedText, i * blockSize, (i + 1) * blockSize);

            // 解密当前块
            byte[] decryptedBlock = AESDecryption.decrypt(encryptedBlock, key);

            // CBC模式：与前一个密文块或IV异或
            for (int j = 0; j < blockSize; j++) {
                decryptedBlock[j] ^= previousBlock[j];
            }

            // 将解密后的块存储到明文中
            System.arraycopy(decryptedBlock, 0, decryptedText, i * blockSize, blockSize);

            // 更新previousBlock为当前的密文块
            previousBlock = encryptedBlock;
        }

        // 去除填充
        return unpadBlock(decryptedText);
    }

    // PKCS7填充函数
    private static byte[] padBlock(byte[] block, int blockSize) {
        int paddingLength = blockSize - block.length;
        byte[] paddedBlock = Arrays.copyOf(block, blockSize);
        Arrays.fill(paddedBlock, block.length, blockSize, (byte) paddingLength);
        return paddedBlock;
    }

    // 去除PKCS7填充
    private static byte[] unpadBlock(byte[] paddedBlock) {
        int paddingLength = paddedBlock[paddedBlock.length - 1];
        return Arrays.copyOf(paddedBlock, paddedBlock.length - paddingLength);
    }
}
