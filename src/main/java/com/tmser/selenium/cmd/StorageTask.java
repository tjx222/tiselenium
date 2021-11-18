package com.tmser.selenium.cmd;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tmser.selenium.tools.NoticeService;
import com.tmser.selenium.tools.TiSeleniumTools;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * 库存查询任务
 */
@Service
public class StorageTask implements Task {
    private static final Logger logger = LoggerFactory.getLogger(StorageTask.class);
    private static Cache<String, Object> localCache = CacheBuilder.newBuilder().softValues()
            .expireAfterWrite(300, TimeUnit.SECONDS).build();

    private volatile boolean running = false;

    @Autowired
    private StorageInfo storageInfo;

    @Autowired
    private NoticeService noticeService;

    @Async
    @Override
    public void start(WebDriver driver) {
        if(running){
            logger.info("already running");
            return ;
        }
        running = true;
        logger.info("start to storage task, ids:{}",storageInfo);
        do {
            Set<String> pidSet = Sets.newHashSet();
            if (storageInfo.getIds() != null) {
                for (String id : storageInfo.getIds()) {
                    if(Objects.isNull(localCache.getIfPresent(id))){
                        pidSet.add(id);
                        if(!running){
                            return;
                        }
                       if(pidSet.size() >= 60){ //最多30条 执行一次
                            doQueryStorage(driver, pidSet);
                            pidSet.clear();
                        }
                    }
                }
                if (pidSet.isEmpty()) {
                    logger.info("no id for query");
                    return;
                }
            }
            if(running){
                try {
                    doQueryStorage(driver, pidSet);
                } catch (Exception e) {
                    logger.error("failed query storage: ", e);
                    running = false;
                    break;
                }
               // driver.manage().deleteAllCookies();
               // driver.navigate().refresh();
            }
        }while (running);
    }

    private void doQueryStorage(WebDriver driver, Set<String> pidSet) {

        Map<String, Integer> storageMap = TiSeleniumTools.queryStorage(driver, pidSet);
        List<Map.Entry<String, Integer>> hasStorageEntries = Lists.newArrayList();
        for (Map.Entry<String, Integer> entry : storageMap.entrySet()) {
            if(entry.getValue() > 0){
                hasStorageEntries.add(entry);
                localCache.put(entry.getKey(),entry.getValue());
            }
        }

        if(hasStorageEntries.size() > 0){
            StringBuilder result = new StringBuilder("[");
            for (Map.Entry<String, Integer> e : hasStorageEntries) {
                result.append(e.getKey()).append(": ").append(e.getValue()).append(",");
            }
            result.append("]");
            noticeService.sendStorageMsg(result.toString());
            logger.info("has storage info: {}", result);
        }

       // driver.manage().deleteAllCookies();
       // driver.navigate().refresh();
    }

    @Override
    public void stop() {
        this.running = false;
    }
}
