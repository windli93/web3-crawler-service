package cn.com.web3.logic.logic;

import cn.com.web3.logic.config.ValueConfig;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;
import java.net.URL;
import java.time.Duration;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author hongjian.li
 * @Description
 * @Date 2023/3/25 19:21
 **/
@Slf4j
@Component
public class ArbitrumCrawlerLogic {

    @Resource
    private ValueConfig valueConfig;

    /**
     * @Author hongjian.li
     * @Description 获取Cookie和Token
     **/
    public Pair<Boolean, ConcurrentHashMap<String, Object>> getCookieAndToken(String baseUrl, String baseUri) {
        WebDriver chromeDriver = null;
        long startTime = DateUtil.currentSeconds();
        ConcurrentHashMap<String, Object> hashMap = new ConcurrentHashMap();
        try {
            ChromeOptions options = new ChromeOptions();
            String[] args = new String[]{
                    "--disable-blink-features",
                    "--disable-blink-features=AutomationControlled",
                    "--blink-settings=imagesEnabled=false",
                    "--disable-gpu",
                    "--no-sandbox",
                    "--start-maximized",
                    "--single-process",
                    "--user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36 Edg/109.0.1518.55"
            };
            //加载参数
            options.addArguments(args);
//            org.springframework.core.io.Resource resource = new DefaultResourceLoader().getResource("classpath:/extension/extension_matemask.crx");
//            File file = FileUtil.createTempFile(new UUIDGenerator().next(), ".crx", new File(Constant.BASE_TEMP_DIR), true);
//            IoUtil.copy(resource.getInputStream(), new FileOutputStream(file));
            //加载matemask钱包
            options.addExtensions(new File("D:\\git\\web3-crawler-service\\extension\\metamask-chrome-10.22.0.zip"));
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
//            options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
            options.setPageLoadTimeout(Duration.ofSeconds(120));
            options.setImplicitWaitTimeout(Duration.ofSeconds(120));
            options.setScriptTimeout(Duration.ofSeconds(120));
            chromeDriver = new RemoteWebDriver(new URL(valueConfig.getRemoteUrl()), options);
//            chromeDriver = new ChromeDriver(options);
//            chromeDriver.manage().window().maximize();
            chromeDriver.get(baseUrl + baseUri);
//            Set<String> windowHandles = chromeDriver.getWindowHandles();
//            log.info("Windows:{}", JSON.toJSONString(windowHandles));
//            String currentWindows = chromeDriver.getWindowHandle();
//            log.info("currentWindows:{}", currentWindows);
            //先进入新建小狐狸钱包
            WebElement importEle = chromeDriver.findElement(By.xpath("//div[@class='onboarding-welcome']/div/ul/li[2]/button"));
            log.info("importEle:{}", importEle.getText());
            importEle.click();
            //同意协议
            WebElement agreementEle = chromeDriver.findElement(By.xpath("//div[@class='onboarding-metametrics__buttons']/button[@class='onboarding-metametrics__buttons']"));
            log.info("importEle:{}", agreementEle.getText());
            agreementEle.click();
            //定位到Twitter
            WebElement buttonEle = chromeDriver.findElement(By.xpath("//nav[@class='l-Header__connect']/ul/li[1]/a/button"));
            log.info("buttonEleText:{}", buttonEle.getText());
            //定位到Twitter
            WebElement twitterEle = chromeDriver.findElement(By.xpath("//nav[@class='l-Header__connect']/ul/li[2]/a"));
            String twitterEleText = twitterEle.getAttribute("href");
            log.info("twitterEleText:{}", twitterEleText);
            //定位到Discord
            WebElement discordEle = chromeDriver.findElement(By.xpath("//nav[@class='l-Header__connect']/ul/li[3]/a"));
            String discordEleText = discordEle.getAttribute("href");
            log.info("discordEleText:{}", discordEleText);
            //定位到Telagram
            WebElement telegramEle = chromeDriver.findElement(By.xpath("//nav[@class='l-Header__connect']/ul/li[4]/a"));
            String telegramEleText = telegramEle.getAttribute("href");
            log.info("telegramEleText:{}", telegramEleText);
            //点击钱包连接
            buttonEle.click();
            log.info("telegramEleText:{}", telegramEleText);
        } catch (Exception e) {
            log.info("获取Cookie异常：{}", e.getMessage());
            return new Pair<>(false, null);
        } finally {
            long endTime = DateUtil.currentSeconds();
            if (chromeDriver != null) {
                chromeDriver.quit();
            }
            log.info("Get cookie and Token time :{}", endTime - startTime);
        }
        return new Pair<>(true, hashMap);
    }
}
