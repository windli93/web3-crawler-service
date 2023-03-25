package cn.com.web3.test;

import cn.com.web3.logic.logic.ArbitrumCrawlerLogic;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author hongjian.li
 * @Description
 * @Date 2023/3/25 19:29
 **/
@Slf4j
@Component
public class AttributeTest extends BaseTest {

    @Resource
    private ArbitrumCrawlerLogic arbitrumCrawlerLogic;

    @Test
    public void test1() {
        arbitrumCrawlerLogic.getCookieAndToken("https://arbitrum.foundation/", "");
    }
}
