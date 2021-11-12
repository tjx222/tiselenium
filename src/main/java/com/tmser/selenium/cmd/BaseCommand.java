package com.tmser.selenium.cmd;

public class BaseCommand {

    public static final String SUCCESS = "success";

    public static final String DEFAULT_PAGE;

    public static final String HOME_PAGE = "https://www.ti.com.cn/";

    static {
        DEFAULT_PAGE = System.getProperty("default_page_url",
                "https://www.ti.com.cn/zh-cn/ordering-resources/buying-tools/quick-add-to-cart.html");
    }


    public static final String DEFAULT_PRODUCT_PAGE = "https://www.ti.com/store/ti/zh/p/product/?p=";


}
