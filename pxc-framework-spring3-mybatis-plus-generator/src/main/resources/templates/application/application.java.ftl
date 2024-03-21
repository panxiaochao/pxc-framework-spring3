package ${parentPackage};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.net.InetAddress;

/**
* 应用启动类
*
* @author ${author}
* @since ${date}
*/
@SpringBootApplication
@ComponentScan(value = {
// 必须：扫描框架配置
"io.github.panxiaochao",
})
public class Application {

private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

/**
* 启动项
*
* @param args 参数
*/
public static void main(String[] args) throws Exception{
ConfigurableApplicationContext application = SpringApplication.run(Application.class, args);
Environment env = application.getEnvironment();
String ip = InetAddress.getLocalHost().getHostAddress();
String applicationName = env.getProperty("spring.application.name");
String port = env.getProperty("server.port");
String path = env.getProperty("server.servlet.context-path");
if (!StringUtils.hasText(path) || "/".equals(path)) {
path = "";
}

LOGGER.info("\n----------------------------------------------------------\n\t{}{}{}{}",
applicationName + " is running! Access URLs:",
"\n\tLocal    访问网址: \thttp://localhost:" + port + path,
"\n\tExternal 访问网址: \thttp://" + ip + ":" + port + path,
"\n----------------------------------------------------------\n");
}
}
