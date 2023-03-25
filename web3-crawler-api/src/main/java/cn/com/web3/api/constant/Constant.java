package cn.com.web3.api.constant;

public class Constant {

    public static final String BASE_TEMP_DIR;

    static {
        BASE_TEMP_DIR = System.getProperty("os.name").toLowerCase().contains("windows") ? "C:\\data\\web3" : "/home/admin/tmp/web3";
    }

}
