package com.tmser.selenium.cmd;

public enum BrowserType {
    CHROME("webdriver.chrome.driver"),
    MSEDGE("webdriver.edge.driver"),
    FIREFOX("webdriver.gecko.driver");

    public String getEnvName() {
        return envName;
    }

    private String envName;

    BrowserType(String envName){
        this.envName = envName;
    }
    public static BrowserType from(String type){
        BrowserType browserType = null;
        try {
            browserType = BrowserType.valueOf(type.toUpperCase());
        } catch (Exception e) {
            browserType = CHROME;
        }
        return browserType;
    }
}
