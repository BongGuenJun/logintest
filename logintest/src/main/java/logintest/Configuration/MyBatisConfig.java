package logintest.Configuration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan(basePackages = "logintest.Mapper")
public class MyBatisConfig {
}