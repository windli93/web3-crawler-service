package cn.com.web3.logic.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @Author hongjian.li
 * @Description
 * @Date 2022/10/12 17:24
 **/
@Data
@Component
public class ValueConfig {

    @Value("${webdriver.remote.driver.url}")
    private String remoteUrl;

    @Value("${http.shein.proxy.enable}")
    private Boolean httpSheinProxyEnable;

    @Value("${http.shein.proxy.hostname}")
    private String httpSheinProxyHostName;

    @Value("${http.shein.proxy.port}")
    private Integer httpSheinProxyPort;

    @Value("${http.shein.proxy.username}")
    private String httpSheinProxyUserName;

    @Value("${http.shein.proxy.userpwd}")
    private String httpSheinProxyUserPwd;
}
