package com.tmser.selenium.cmd;

import org.openqa.selenium.WebDriver;

/**
 * 任务
 */
public interface Task {

    void start(WebDriver driver);

    void stop();
}
