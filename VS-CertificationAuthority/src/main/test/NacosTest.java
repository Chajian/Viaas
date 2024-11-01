import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.exception.NacosException;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Properties;

@SpringBootTest
public class NacosTest {

    @Test
    void contextLoads() {
        try {
            Properties properties = new Properties();
            properties.put("serverAddr", "8.138.15.0:8848");
            properties.put("namespace", "3a20549b-6856-4fe0-ac2d-fd20ae0dd9d4");
            ConfigService configService = NacosFactory.createConfigService(properties);
            String contentInfo = configService.getConfig("VS-CertificationAuthority.yml",
                    "DEFAULT_GROUP", 1000L);
            System.out.println("contentInfo:" + contentInfo);
        } catch (NacosException e) {
            throw new RuntimeException(e);
        }
    }
}
