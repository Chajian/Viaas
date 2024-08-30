import java.util.Arrays;

public class AESCTR {

    // AES加密和解密函数，支持CTR模式
    public static byte[] process(byte[] inputText, byte[] key, byte[] iv) {
        int blockSize = 16; // AES块大小为16字节
        int numBlocks = (inputText.length + blockSize - 1) / blockSize; // 计算需要处理的块数
        byte[] outputText = new byte[inputText.length]; // 输出数组，与输入长度相同

        // 初始化计数器（Counter）为IV
        byte[] counter = iv.clone();

        for (int i = 0; i < numBlocks; i++) {
            // 对计数器进行AES加密
            byte[] encryptedCounter = AESEncryption.encrypt(counter, key);

            // 取输入文本块
            int start = i * blockSize;
            int end = Math.min((i + 1) * blockSize, inputText.length);
            byte[] inputBlock = Arrays.copyOfRange(inputText, start, end);

            // 对当前块进行CTR模式的XOR操作
            for (int j = 0; j < inputBlock.length; j++) {
                outputText[start + j] = (byte) (inputBlock[j] ^ encryptedCounter[j]);
            }

            // 增加计数器
            incrementCounter(counter);
        }

        return outputText;
    }

    // 增加计数器
    private static void incrementCounter(byte[] counter) {
        for (int i = counter.length - 1; i >= 0; i--) {
            counter[i]++;
            if (counter[i] != 0) { // 当发生溢出时，向前进位
                break;
            }
        }
    }
}
