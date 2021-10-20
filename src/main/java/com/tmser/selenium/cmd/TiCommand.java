package com.tmser.selenium.cmd;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.tmser.selenium.tools.MitmProxy;
import com.tmser.selenium.tools.NoticeService;
import com.tmser.selenium.tools.TiSeleniumTools;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.io.*;
import java.time.Duration;
import java.util.*;

@ShellComponent
public class TiCommand extends BaseCommand {

    private static final Logger logger = LoggerFactory.getLogger(TiCommand.class);
    private WebDriver driver;

    private BrowserType browserType;

    private String[] productIds;

    @Autowired
    private AddressInfo addressInfo;

    @Autowired
    private StorageTask storageTask;

    @Autowired
    private InvoiceInfo invoiceInfo;



    @Value("${sys.fileSaveLocation}")
    private String fileSaveLocation;

    @Value("${sys.username}")
    private String userName;

    @Value("${sys.password}")
    private String password;

    @Autowired
    private NoticeService noticeService;

    @Autowired
    private MitmProxy mitmProxy;


    @PostConstruct
    public  void init(){
        TiSeleniumTools.setAddressInfo(addressInfo);
        TiSeleniumTools.setInvoiceInfo(invoiceInfo);
    }
    @ShellMethod("start webdriver.")
    public String start(
            @ShellOption(value = "-t", defaultValue = "") String type,
            @ShellOption(value = "-p", defaultValue = "") String driverPath,
            @ShellOption(value = "-u", defaultValue = "") String url,
            @ShellOption(value = "-a", defaultValue = "") String account,
            @ShellOption(value = "-p", defaultValue = "") String password) {
        if (driver != null) {
            return "driver was aready started!";
        }

        String msg = SUCCESS;
        browserType = BrowserType.from(type);
        if (StringUtils.hasText(account)) {
            this.userName = account;
        }

        if (StringUtils.hasText(password)) {
            this.password = password;
        }

        if (StringUtils.hasText(driverPath)) {
            System.setProperty(browserType.getEnvName(), driverPath);
        } else {
            String property = System.getProperty(browserType.getEnvName(), null);
            if (Objects.isNull(property)) {
                System.setProperty(browserType.getEnvName(), "C:/webdriver/" + browserType.name().toLowerCase() + "driver.exe");
            }
        }


        switch (browserType) {
            case MSEDGE:
                var options = new EdgeOptions();
                options.addArguments("user-agent=Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/86.0.4240.198 Safari/537.36");
                driver = new EdgeDriver();
                break;
            case CHROME:
                ChromeOptions opes = new ChromeOptions();
                opes.addArguments("start-maximized");
                opes.setExperimentalOption("useAutomationExtension", Boolean.FALSE);
                opes.setExperimentalOption("excludeSwitches", Lists.newArrayList("enable-automation"));
                opes.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/94.0.4606.71 Safari/537.36");
                driver = new ChromeDriver(opes);
                break;
            case FIREFOX:
                driver = new FirefoxDriver();
                break;
            default:
                break;
        }

        //driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(50));
        if (StringUtils.hasText(url)) {
            return open(url, 0);
        }
        return SUCCESS;
    }

    boolean checkNotStarted() {
        return driver == null;
    }

    @ShellMethod("open url.")
    public String open(@ShellOption(value = "-u", defaultValue = "") String url, @ShellOption(value = "-f", defaultValue = "1") int force) {
        if (checkNotStarted()) {
            if (force == 0) {
                return "请先执行start";
            }

            start("", "", "","","");
        }
        if (StringUtils.isEmpty(url)) {
            url = BaseCommand.DEFAULT_PAGE;
        }

        String msg = SUCCESS;
        if (StringUtils.hasText(url) && url.startsWith("http")) {
            if (!TiSeleniumTools.open(driver, url)) {
                msg = String.format("open %s failed", url);
                driver.close();
                driver = null;
            }
        }

        return msg;
    }


    @ShellMethod("shutdown driver.")
    public String shutdown() {
        try {
            if (driver != null) {
                driver.close();
                driver = null;
            }
        } catch (Exception e) {
            logger.info("close failed!", e);
            driver = null;
        }

        return SUCCESS;
    }

    @ShellMethod(value = "set default product ids.", key = "set-product")
    public String setProduct(String[] pids) {
        if (pids != null) {
            this.productIds = pids;
        }

        logger.info("default product : {}", Arrays.toString(pids));
        return SUCCESS;
    }

    @ShellMethod(value = "refresh current page.", key = "re")
    public String refresh() {
        driver.navigate().refresh();
        return SUCCESS;
    }

    @ShellMethod(value = "screen shot page.", key = "shot")
    public String shot(@ShellOption(value = "-o", defaultValue = "") String out) throws IOException {
        String root = StringUtils.hasText(out) ? out : fileSaveLocation;
        String fileName = System.currentTimeMillis()+".png";
        FileUtils.writeByteArrayToFile(new File(root, fileName), TiSeleniumTools.getScreenShot(driver));
        return SUCCESS;
    }

    @ShellMethod(value = "send key", key = "send-key")
    public String sendKey(String keys) {
        if (keys == null) {
            return "no key to send";
        }
        logger.info("send keys : {}", keys);
        TiSeleniumTools.sendKeys(keys);
        return SUCCESS;
    }

    @ShellMethod(value = "add cart", key = "add-cart")
    public String addCart() {
        if (checkNotStarted()) {
            return "not start";
        }
        boolean addResult = TiSeleniumTools.addCart(driver);
        return addResult ? SUCCESS : "添加失败，检查库存是否查询到或足够";
    }

    @ShellMethod(value = "to order", key = "to-order")
    public String toOrder() {
        if (checkNotStarted()) {
            return "not start";
        }
        TiSeleniumTools.choseCountry(driver);
        boolean addResult = TiSeleniumTools.toOrder(driver);
        return addResult ? SUCCESS : "下单失败";
    }

    @ShellMethod(value = "set address", key = "set-address")
    public String address() {
        if (checkNotStarted()) {
            return "not start";
        }
        boolean addResult = TiSeleniumTools.address(driver);
        return addResult ? SUCCESS : "没有到达设置地址页";
    }

    @ShellMethod(value = "login", key = "login")
    public String login() {
        if (checkNotStarted()) {
            return "not start";
        }
        boolean addResult = TiSeleniumTools.login(driver, userName, password);
        return addResult ? SUCCESS : "不是登陆页";
    }

    @ShellMethod(value = "set invoice", key = "set-invoice")
    public String invoice() {
        if (checkNotStarted()) {
            return "not start";
        }
        boolean addResult = TiSeleniumTools.invoice(driver);
        return addResult ? SUCCESS : "没有到达设置发票";
    }

    @ShellMethod(value = "set safe", key = "set-safe")
    public String safe() {
        if (checkNotStarted()) {
            return "not start";
        }
        boolean addResult = TiSeleniumTools.safe(driver);
        return addResult ? SUCCESS : "没有到达设置合规";
    }


    @ShellMethod(value = "start mitm proxy", key = "start-proxy")
    public String startProxy() {
         mitmProxy.start();
         return SUCCESS;
    }

    @ShellMethod(value = "set distribution", key = "set-distribution")
    public String distribution() {
        if (checkNotStarted()) {
            return "not start";
        }
        boolean addResult = TiSeleniumTools.distribution(driver);
        return addResult ? SUCCESS : "没有到达设置配送方式";
    }

    @ShellMethod(value = "set pay", key = "set-pay")
    public String pay() {
        if (checkNotStarted()) {
            return "not start";
        }
        boolean addResult = TiSeleniumTools.pay(driver);
        return addResult ? SUCCESS : "没有到达设置支付方式";
    }

    @ShellMethod(value = "focus element", key = "to-ele")
    public String toEle(@ShellOption(defaultValue = "ID") String key, @ShellOption(defaultValue = "") String val) throws InterruptedException {
        logger.info("focus element : {}", val);
        Thread.sleep(5000);
        TiSeleniumTools.scrollWindow(driver,600);
        WebElement element = TiSeleniumTools.findElement(driver, key.toUpperCase(), val);
        if (element == null) {
            return "not found!";
        }
        logger.info("isDisplay:{}, location:{}, size:{}", element.isDisplayed(), element.getLocation(), element.getSize());
        TiSeleniumTools.moveAndClick(element.getRect(), 600);
        TiSeleniumTools.sendKeys("Asfds123");
        return SUCCESS;
    }

    @ShellMethod(value = "mouse pos", key = "pos")
    public String pos() throws InterruptedException {
        Thread.sleep(5000);
        TiSeleniumTools.printMousePos();
        return SUCCESS;
    }



    @ShellMethod(value = "show properties", key = "show")
    public String show(@ShellOption(defaultValue = "addr") String key) {
        logger.info("show proprties for  {}", key);
        if("addr".equals(key)){
            return TiSeleniumTools.getAddressInfo().toString();
        }

        if ("invoice".equals(key)) {
            return TiSeleniumTools.getInvoiceInfo().toString();
        }

        return "no such key";
    }
    @ShellMethod(value = "mouse move", key = "mv")
    public String mourseMove(@ShellOption(defaultValue = "0") Integer x, @ShellOption(defaultValue = "0") Integer y) {
        logger.info("mouse move {}", x, y);
        TiSeleniumTools.mouseMove(x, y);
        return SUCCESS;
    }


    @ShellMethod(value = "query-storage with pids.", key = "query-storage")
    public String queryStorage(@ShellOption(defaultValue = "") String pids) throws InterruptedException {
        Thread.sleep( 5000);
        logger.info("product ids : {}", pids);
        String[] pidarr = pids.split(" ");
        Set<String> pidSet = Sets.newHashSet();
        if (pidarr.length > 0) {
            pidSet.addAll(Arrays.asList(pidarr));
        }

        if (pidSet.isEmpty()) {
            if (this.productIds == null || this.productIds.length == 0) {
                return "no productId to query.";
            }
            pidSet.addAll(Arrays.asList(this.productIds));
        }

        logger.info("query product : {}", pidSet);

        Map<String, Integer> storageMap = TiSeleniumTools.queryStorage(driver, pidSet);

        return storageMap.toString();
    }


    @ShellMethod(value = "start storage task.", key = "start-storage-task")
    public String startStorageTask() throws InterruptedException {
        Thread.sleep( 5000);
        open("", 1);
        storageTask.start(driver);
        return SUCCESS;
    }

    @ShellMethod(value = "stop storage task.", key = "stop-storage-task")
    public String stopStorageTask() {
        storageTask.stop();
        return SUCCESS;
    }

    @ShellMethod(value = "set y offset.", key = "set-y")
    public String setYoffset(@ShellOption(defaultValue = "0") int yoffset) {
        TiSeleniumTools.baseOffsetY = yoffset;
        return SUCCESS;
    }


    @ShellMethod(value = "auto-to-order with pids.", key = "auto-to-order")
    public String autoToOrder(@ShellOption(defaultValue = "") String[] pids) {
        open("", 1);
        logger.info("product ids : {}", Arrays.toString(pids));
        Set<String> pidSet = Sets.newHashSet();
        if (pids != null && pids.length > 0) {
            pidSet.addAll(Arrays.asList(pids));
        }

        if (pidSet.isEmpty()) {
            if (this.productIds == null || this.productIds.length == 0) {
                return "no productId to query.";
            }
            pidSet.addAll(Arrays.asList(this.productIds));
        }

        logger.info("query product : {}", pidSet);

        Map<String, Integer> storageMap = TiSeleniumTools.queryStorage(driver, pidSet);
        for (Map.Entry<String, Integer> entry : storageMap.entrySet()) {
            if (entry.getValue() > 0) {
                boolean addCart = TiSeleniumTools.addCart(driver);
                if(addCart){
                    TiSeleniumTools.login(driver, userName, password);
                    boolean addResult = TiSeleniumTools.toOrder(driver);
                }
                break;
            }
        }

        return storageMap.toString();
    }


    @ShellMethod(value = "auto-to-pay.", key = "auto-to-pay")
    public String autoToPay() {
        if(checkNotStarted()){
            return "not start";
        }
        Step.buildStep(TiSeleniumTools::address, TiSeleniumTools::invoice,
                TiSeleniumTools::safe, TiSeleniumTools::distribution, TiSeleniumTools::pay,
                d->{
                    try {
                        String root = fileSaveLocation;
                        String fileName = System.currentTimeMillis()+".png";
                        FileUtils.writeByteArrayToFile(new File(root, fileName), TiSeleniumTools.getScreenShot(driver));
                        noticeService.sendSuccessMsg(fileName);
                        return  true;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return false;
                });
        return SUCCESS;
    }



}
