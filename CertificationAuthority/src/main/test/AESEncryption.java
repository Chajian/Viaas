public class AESEncryption {
    // AES的S-Box
    private static final int[] S_BOX = { /* S-Box Values */ };

    public static byte[] decrypt(byte[] state, byte[] key) {
        byte[] expandedKey = AESKeyExpansion.keyExpansion(key); // 执行密钥扩展

        // 初始轮
        addRoundKey(state, expandedKey, 10);

        // 主轮（第N-1轮到第1轮）
        for (int round = 9; round > 0; round--) {
            invShiftRows(state);
            invSubBytes(state);
            addRoundKey(state, expandedKey, round);
            invMixColumns(state);
        }

        // 最终轮
        invShiftRows(state);
        invSubBytes(state);
        addRoundKey(state, expandedKey, 0);

        return state;
    }

    // 轮密钥加法
    public static void addRoundKey(byte[] state, byte[] expandedKey, int round) {
        for (int i = 0; i < 16; i++) {
            state[i] ^= expandedKey[round * 16 + i]; // 计算一维数组的索引
        }
    }

    // 逆S盒代换
    public static void invSubBytes(byte[] state) {
        for (int i = 0; i < 16; i++) {
            state[i] = (byte) S_BOX[state[i] & 0xFF];
        }
    }

    // 逆行移位
    public static void invShiftRows(byte[] state) {
        byte[] temp = new byte[16];

        // 第一行不变
        temp[0] = state[0];
        temp[1] = state[13];
        temp[2] = state[10];
        temp[3] = state[7];

        // 第二行右移1字节
        temp[4] = state[4];
        temp[5] = state[1];
        temp[6] = state[14];
        temp[7] = state[11];

        // 第三行右移2字节
        temp[8] = state[8];
        temp[9] = state[5];
        temp[10] = state[2];
        temp[11] = state[15];

        // 第四行右移3字节
        temp[12] = state[12];
        temp[13] = state[9];
        temp[14] = state[6];
        temp[15] = state[3];

        System.arraycopy(temp, 0, state, 0, 16); // 将临时数组拷贝回状态数组
    }

    // 逆列混合
    public static void invMixColumns(byte[] state) {
        byte[] temp = new byte[16];
        for (int i = 0; i < 4; i++) {
            int colIndex = i * 4;
            temp[colIndex] = (byte) (mul(0x0E, state[colIndex]) ^ mul(0x0B, state[colIndex + 1]) ^ mul(0x0D, state[colIndex + 2]) ^ mul(0x09, state[colIndex + 3]));
            temp[colIndex + 1] = (byte) (mul(0x09, state[colIndex]) ^ mul(0x0E, state[colIndex + 1]) ^ mul(0x0B, state[colIndex + 2]) ^ mul(0x0D, state[colIndex + 3]));
            temp[colIndex + 2] = (byte) (mul(0x0D, state[colIndex]) ^ mul(0x09, state[colIndex + 1]) ^ mul(0x0E, state[colIndex + 2]) ^ mul(0x0B, state[colIndex + 3]));
            temp[colIndex + 3] = (byte) (mul(0x0B, state[colIndex]) ^ mul(0x0D, state[colIndex + 1]) ^ mul(0x09, state[colIndex + 2]) ^ mul(0x0E, state[colIndex + 3]));
        }
        System.arraycopy(temp, 0, state, 0, 16); // 将混合后的列数据拷贝回状态数组
    }

    // 有限域GF(2^8)上的乘法
    private static byte mul(int a, byte b) {
        byte result = 0;
        byte temp = b;

        for (int i = 0; i < 8; i++) {
            if ((a & 1) != 0) {
                result ^= temp;
            }
            boolean highBitSet = (temp & 0x80) != 0;
            temp <<= 1;
            if (highBitSet) {
                temp ^= 0x1B;
            }
            a >>= 1;
        }

        return result;
    }
}
