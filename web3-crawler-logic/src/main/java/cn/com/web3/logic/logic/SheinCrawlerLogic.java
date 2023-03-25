package cn.com.web3.logic.logic;

import cn.com.web3.logic.config.ValueConfig;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Pair;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.gargoylesoftware.htmlunit.*;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.net.URL;
import java.nio.charset.Charset;
import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author hongjian.li
 * @Description Shein爬虫逻辑
 * @Date 2023/3/25 14:48
 **/
@Component
@Slf4j
public class SheinCrawlerLogic {

    @Resource
    private ValueConfig valueConfig;

    //新品和常规商品
    public final static int[] SORT_TYPE = new int[]{8, 9};
    //匹配参数
    public final static Pattern PATTERN_REGEX = Pattern.compile("var\\s+_constants\\s+=\\s+(.*)");

    //同一个线程的Cookie信息共享
    protected final static ThreadLocal<ConcurrentHashMap<String, String>> THREAD_COOKIE_LOCAL = new ThreadLocal<>();


    /**
     * @Author hongjian.li
     * @Description 获取Shein商品列表
     **/
    public Pair<Boolean, String> getSheinProductListJson(String baseUrl, String requestUrl, Integer page, Integer sortType) {
        String baseUri = "/ListJsonService?_ver=1.1.8&_lang=en";
        baseUri = baseUrl.endsWith("/") ? baseUri.replaceFirst("/", "") : baseUri;
        String pathUrl = sortType == SORT_TYPE[0] ? requestUrl + "&page=" + page : requestUrl + "&sort=" + sortType + "&page=" + page;
        baseUri = baseUri + pathUrl;
        ConcurrentHashMap<String, String> threadMap = getCurrentThreadCookieInfo(baseUrl, "");
        if (threadMap == null) {
            return new Pair<>(false, "获取不到有效的Cookie信息");
        }
        long startTime = DateUtil.currentSeconds();
        WebClient webClient = getWebClient();
        String requestCookie = threadMap.get("x-site-cookie");
        String requestCsrfToken = threadMap.get("x-site-csrfToken");
        try {
            URL link2 = new URL(baseUrl + baseUri);
            log.info("URL link:{}", link2.getPath());
            WebRequest webRequest = new WebRequest(link2, HttpMethod.GET);
            webRequest.setAdditionalHeader("Accept", "*/*");
            webRequest.setAdditionalHeader("Content-Type", "application/json");
            webRequest.setAdditionalHeader("Accept-Language", "en-US,en;q=0.8");
            webRequest.setAdditionalHeader("Accept-Encoding", "gzip,deflate,sdch");
            webRequest.setAdditionalHeader("Accept-Charset", "ISO-8859-1,utf-8;q=0.7,*;q=0.3");
            webRequest.setAdditionalHeader("X-Requested-With", "XMLHttpRequest");
            webRequest.setAdditionalHeader("Cache-Control", "no-cache");
            webRequest.setAdditionalHeader("x-csrf-token", requestCsrfToken);
            webRequest.setAdditionalHeader("cookie", requestCookie);
            webRequest.setAdditionalHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/106.0.0.0 Safari/537.36 Edg/106.0.1370.37");
            webRequest.setCharset(Charset.defaultCharset());
            Thread.sleep(1000);
            WebResponse response1 = webClient.loadWebResponse(webRequest);
            String result = response1.getContentAsString();
            log.info("response:{}", result);
            long endTime = DateUtil.currentSeconds();
            log.info("SheinProductListJson获取Url:【{}】花费时间为:【{}s】", baseUrl + baseUri, endTime - startTime);
            return new Pair<>(Boolean.TRUE, result);
        } catch (Exception e) {
            log.info("SheinProductListJson获取数据有异常问题...........{}", e.getMessage());
            return new Pair<>(false, e.getMessage());
        } finally {
            if (webClient != null) {
                webClient.close();
            }
        }
    }

    /**
     * @Author hongjian.li
     * @Description 获取当前线程cookie信息
     **/
    protected ConcurrentHashMap<String, String> getCurrentThreadCookieInfo(String baseUrl, String baseUri) {
        ConcurrentHashMap<String, String> threadMap = THREAD_COOKIE_LOCAL.get();
        if (threadMap == null || threadMap.isEmpty()) {
            log.info("当前线程名称【{}】Thread local信息为空，重新获取Cookie和CsrfToken", Thread.currentThread().getName());
            threadMap = refreshCookie(baseUrl, baseUri);
            if (threadMap == null) {
                return null;
            }
        } else {
            log.info("当前线程名称【{}】Thread local信息不为空，直接获取Cookie和CsrfToken", Thread.currentThread().getName());
        }
        return threadMap;
    }

    /**
     * @Author hongjian.li
     * @Description 获取Cookie和Token
     **/
    protected Pair<Boolean, ConcurrentHashMap<String, Object>> getCookieAndToken(String baseUrl, String baseUri) {
        RemoteWebDriver chromeDriver = null;
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
            options.addArguments(args);
            options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
            options.setPageLoadTimeout(Duration.ofSeconds(30));
            options.setImplicitWaitTimeout(Duration.ofSeconds(30));
            options.setScriptTimeout(Duration.ofSeconds(30));
            chromeDriver = new RemoteWebDriver(new URL(valueConfig.getRemoteUrl()), options);
            chromeDriver.manage().window().maximize();
            chromeDriver.get(baseUrl + baseUri);
            Thread.sleep(2000L);
            String csrfToken = "";
            String page = chromeDriver.getPageSource();
            Matcher m = PATTERN_REGEX.matcher(page);
            while (m.find()) {
                String matchValue = m.group();
                matchValue = matchValue.replaceFirst("var\\s+_constants\\s+=\\s+", "");
                JSONObject object = JSONUtil.parseObj(matchValue.trim());
                csrfToken = object.getStr("csrf_token");
                log.info("token value:{}", csrfToken);
            }
            Set<Cookie> cookies1 = chromeDriver.manage().getCookies();
            hashMap.put("cookie", cookies1);
            hashMap.put("csrfToken", csrfToken);
            hashMap.put("pageSource", page);
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

    protected ConcurrentHashMap<String, String> refreshCookie(String baseUrl, String baseUri) {
        Pair<Boolean, ConcurrentHashMap<String, Object>> result = getCookieAndToken(baseUrl, baseUri);
        if (!result.getKey()) {
            log.info("获取不到Cookie或者Token，无法进行接口请求");
            return null;
        }
        ConcurrentHashMap<String, Object> hashMap = result.getValue();
        Set<Cookie> cookies1 = (Set<Cookie>) hashMap.get("cookie");
        String csrfToken = (String) hashMap.get("csrfToken");
        String cookieInfo = "";
        for (Cookie cookie : cookies1) {
            log.info("name:{}", cookie.getName());
            cookieInfo += cookie.getName() + "=" + cookie.getValue() + "; ";
        }
        cookieInfo = cookieInfo.trim();
        if (cookieInfo.length() <= 0) {
            log.info("获取到的Cookie数量为空，无法进行接口请求");
            return null;
        }
//        cookieInfo = cookieInfo.substring(0, cookieInfo.length() - 1);
        log.info("cookieInfo:{}", cookieInfo);
        ConcurrentHashMap<String, String> threadMap = new ConcurrentHashMap<>();
        threadMap.put("x-site-cookie", cookieInfo);
        threadMap.put("x-site-csrfToken", csrfToken);
        THREAD_COOKIE_LOCAL.set(threadMap);
        return threadMap;
    }


    /**
     * @Author hongjian.li
     * @Description 封装WebClient客户端
     **/
    protected WebClient getWebClient() {
        //设置日志级别
        java.util.logging.Logger.getLogger("com.gargoylesoftware.htmlunit").setLevel(Level.INFO);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");
        //设置Web客户端信息
        WebClient webClient = new WebClient(BrowserVersion.CHROME);
        //设置代理
        if (valueConfig.getHttpSheinProxyEnable()) {
            ProxyConfig proxyConfig = webClient.getOptions().getProxyConfig();
            proxyConfig.setProxyHost(valueConfig.getHttpSheinProxyHostName());
            proxyConfig.setProxyPort(valueConfig.getHttpSheinProxyPort());
            DefaultCredentialsProvider credentialsProvider = (DefaultCredentialsProvider) webClient.getCredentialsProvider();
            credentialsProvider.addCredentials(valueConfig.getHttpSheinProxyUserName(), valueConfig.getHttpSheinProxyUserPwd());
        }
        //启用支持HTTPS的协议
        String[] sslClientProtocols = {"TLSv1", "TLSv1.1", "TLSv1.2"};
        webClient.getOptions().setSSLClientProtocols(sslClientProtocols);
        webClient.getOptions().isUseInsecureSSL();
        // 默认设置忽略项
        webClient.getOptions().setJavaScriptEnabled(false);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setDownloadImages(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setRedirectEnabled(true);
        webClient.getCache().setMaxSize(0);
        webClient.waitForBackgroundJavaScript(10000);
        webClient.setJavaScriptTimeout(10000);
        webClient.waitForBackgroundJavaScriptStartingBefore(10000);
        webClient.getCookieManager().setCookiesEnabled(true);// 默认开启cookie
        // 默认超时时间60*4秒
        webClient.getOptions().setTimeout(240000);
        return webClient;
    }

}
