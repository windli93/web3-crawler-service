package cn.com.web3.test;

import cn.com.web3.logic.logic.SheinCrawlerLogic;
import cn.hutool.core.lang.Pair;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author hongjian.li
 * @Description
 * @Date 2023/3/25 16:11
 **/
@Slf4j
@Component
public class SheinTest extends BaseTest {

    @Resource
    private SheinCrawlerLogic crawlerLogic;

    @Test
    public void testSheinProductList() {
        String baseURL = "https://us.shein.com/";
        String baserUri = "&type=daily_new&child_cat_id=2031&requestType=pageChange&daily=2023-03-25";
        Pair<Boolean, String> result = crawlerLogic.getSheinProductListJson(baseURL, baserUri, 1, 8);
        log.info("Result key:{}  result value:{}", result.getKey(), result.getValue());
    }

}
