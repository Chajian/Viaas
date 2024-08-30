import java.util.Arrays;

public class AESECB {
    // AES加密函数，支持ECB模式
    public static byte[] encrypt(byte[] plainText, byte[] key) {
        int blockSize = 16; // AES块大小为16字节
        int numBlocks = (plainText.length + blockSize - 1) / blockSize; // 计算需要加密的块数
        byte[] encryptedText = new byte[numBlocks * blockSize]; // 密文数组

        for (int i = 0; i < numBlocks; i++) {
            // 取明文块
            byte[] plainBlock = Arrays.copyOfRange(plainText, i * blockSize, Math.min((i + 1) * blockSize, plainText.length));
            // 使用PKCS7填充块
            byte[] paddedBlock = padBlock(plainBlock, blockSize);

            // 加密当前块
            byte[] encryptedBlock = AESEncryption.encrypt(paddedBlock, key);

            // 将加密后的块存储到密文中
            System.arraycopy(encryptedBlock, 0, encryptedText, i * blockSize, blockSize);
        }

        return encryptedText;
    }

    // AES解密函数，支持ECB模式
    public static byte[] decrypt(byte[] encryptedText, byte[] key) {
        int blockSize = 16; // AES块大小为16字节
        int numBlocks = encryptedText.length / blockSize; // 计算密文块数
        byte[] decryptedText = new byte[encryptedText.length]; // 明文数组

        for (int i = 0; i < numBlocks; i++) {
            // 取密文块
            byte[] encryptedBlock = Arrays.copyOfRange(encryptedText, i * blockSize, (i + 1) * blockSize);

            // 解密当前块
            byte[] decryptedBlock = AESDecryption.decrypt(encryptedBlock, key);

            // 将解密后的块存储到明文中
            System.arraycopy(decryptedBlock, 0, decryptedText, i * blockSize, blockSize);
        }

        // 去除填充
        return unpadBlock(decryptedText);
    }

    // PKCS7填充函数
    private static byte[] padBlock(byte[] block, int blockSize) {
        int paddingLength = blockSize - (block.length % blockSize); // 计算需要填充的字节数
        byte[] paddedBlock = Arrays.copyOf(block, block.length + paddingLength);
        Arrays.fill(paddedBlock, block.length, paddedBlock.length, (byte) paddingLength);
        return paddedBlock;
    }

    // 去除PKCS7填充
    private static byte[] unpadBlock(byte[] paddedBlock) {
        if (paddedBlock == null || paddedBlock.length == 0) {
            throw new IllegalArgumentException("输入的块不能为空或空");
        }

        int paddingLength = paddedBlock[paddedBlock.length - 1] & 0xFF; // 确保为无符号整数
        // 去除填充，返回解密后的原始数据
        return Arrays.copyOf(paddedBlock, paddedBlock.length - paddingLength);
    }
}
