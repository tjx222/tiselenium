package com.tmser.selenium.tools;

import com.google.common.collect.Maps;
import com.tmser.selenium.cmd.AddressInfo;
import com.tmser.selenium.cmd.BaseCommand;
import com.tmser.selenium.cmd.InvoiceInfo;
import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.chromium.ChromiumDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.awt.*;
import java.awt.Point;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import static org.openqa.selenium.support.ui.ExpectedConditions.numberOfWindowsToBe;

public class TiSeleniumTools {

    private static final Logger logger = LoggerFactory.getLogger(TiSeleniumTools.class);

    static Robot robot;

    static volatile int errorCount = 0;

    static volatile int refleshCount = 0;

    public static volatile boolean stop = false;

    public static Map<String,Semaphore> semaphoreMap = Maps.newHashMap();

    public static Map<String,Integer> resultMap = Maps.newHashMap();

    static NoticeService noticeService;

    static {
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }

    public static int baseOffsetY = 76;

    public static int baseOffsetX = 140;

    public static final String CONTROL_UP = "K_CRU";
    public static final String CONTROL_DOWN = "K_CRD";
    public static final String ALT_UP = "K_ALTU";
    public static final String ALT_DOWN = "K_ALTD";
    public static final String SHIFT_UP = "K_SHTU";
    public static final String SHIFT_DOWN = "K_SHTD";
    public static final String SPACE = "K_SP";


    public static final String ID = "ID";
    public static final String XPATH = "XPATH";
    public static final String CSS = "CSS";

    private static AddressInfo addressInfo = null;
    private static InvoiceInfo invoiceInfo = null;

    public static void setAddressInfo(AddressInfo addressInfo) {
        TiSeleniumTools.addressInfo = addressInfo;
    }

    public static void setInvoiceInfo(InvoiceInfo invoiceInfo) {
        TiSeleniumTools.invoiceInfo = invoiceInfo;
    }

    public static AddressInfo getAddressInfo() {
        if (addressInfo != null) {
            return addressInfo;
        }
        return new AddressInfo();
    }

    public static InvoiceInfo getInvoiceInfo() {
        if (invoiceInfo != null) {
            return invoiceInfo;
        }
        return new InvoiceInfo();
    }

    public static Map<String, Integer> queryStorage(WebDriver driver, Set<String> codes) {
        errorCount = 0;
        refleshCount = 0;
        stop = false;
        Map<String, Integer> result = Maps.newHashMap();
//        Wait<WebDriver> pageWait = newFluentWait(driver, 60);
//
//        WebElement input = findElementAndDo(driver, pageWait, ID, "tiResponsiveHeader", element -> {
//            if(element == null){
//                return null;
//            }
//            findElementAndDo(driver, pageWait, ID , "sec-overlay", ele->{
//                return ele.isDisplayed() == false;
//            });
//            return element;
//        });
//        if (input == null) {
//            logger.info("to long for load storage page");
//            return result;
//        }
//
//
//        Wait<WebDriver> wait = newFluentWait(driver, 10);

        Wait<Integer> wait=  new FluentWait<Integer>(1)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);
        sendKeys("\0x74");
        pause(5000);
        //int offset = 900;
        scrollWindow(20);
     //   Actions actions = new Actions(driver);
        pause(5 * randKeyTime());
        scrollWindow(-30);
        pause(8 * randKeyTime());
        scrollWindow(9);
        int i = 1;
        for (String code : codes) {
            if(stop){
                return result;
            }
            int storage = loadStorageByRobot(wait, code, i++, 0);
            if (storage > 0) {
                noticeService.sendStorageMsg("【 " + code + " : " + storage +
                        " 】,\n [快速购买](" + BaseCommand.DEFAULT_PRODUCT_PAGE +code + " )");
            }
            result.put(code, storage);
        }
        return result;
    }

    public static boolean toLoginPage(WebDriver driver) {
        logger.info("start----- to login page");
        try {
            String currentUrl = driver.getCurrentUrl();
            Cookie userNameCookie = driver.manage().getCookieNamed("user_pref_givenName");
            if (StringUtils.hasText(currentUrl) && !currentUrl.contains("login.ti.com")) {
                if (userNameCookie != null && StringUtils.hasText(userNameCookie.getValue())) {
                    logger.info("已经登录");
                    return true;
                }

                excuteJs(driver, "document.querySelectorAll(\"ti-login\")[0].shadowRoot.children[0].click();delete window['$cdc_asdjflasutopfhvcZLmcfl_'];delete document['$cdc_asdjflasutopfhvcZLmcfl_'];");
                return true;
            }
        } catch (Exception e) {
            logger.info("can't to login page", e);
        }

        return false;
    }

    public static boolean login(WebDriver driver, String userName, String password) {
        logger.info("start----- login");
        try {
            String currentUrl = driver.getCurrentUrl();
            Cookie userNameCookie = driver.manage().getCookieNamed("user_pref_givenName");
            if (userNameCookie != null && StringUtils.hasText(userNameCookie.getValue())) {
                if (StringUtils.hasText(currentUrl) && !currentUrl.contains("login.ti.com")) {
                    logger.info("已经登录");
                    return true;
                }
            } else if (StringUtils.hasText(currentUrl) && !currentUrl.contains("login.ti.com")) {
                logger.info("不在登录页，当前url: {}", currentUrl);
                return false;
            }

            Actions action = new Actions(driver);
            Wait<WebDriver> wait = newFluentWait(driver);
            WebElement usernameInput = wait.until(d -> d.findElement(By.name("username")));
            action.moveToElement(usernameInput).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            usernameInput.sendKeys(userName);
            WebElement nextbutton = wait.until(d -> d.findElement(By.name("nextbutton")));
            action.moveToElement(nextbutton).pause(randKeyTime()).click().pause(randKeyTime()).perform();

            WebElement passwordInput = wait.until(d -> d.findElement(By.name("password")));
            action.moveToElement(passwordInput).pause(randKeyTime()).click()
                    .pause(randKeyTime()).sendKeys(password).pause(randKeyTime()).perform();
            WebElement loginbutton = wait.until(d -> d.findElement(By.name("loginbutton")));
            action.moveToElement(loginbutton).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            return waitNotInPage(wait, "login.ti.com");
        } catch (Exception e) {
            logger.info("can't login", e);
        }

        return false;
    }

    /**
     * 加入购物车, 如果需要登录则先跳转登录
     *
     * @param driver
     */
    public static boolean addCart(WebDriver driver) {
        logger.info("start----- add cart");
        try {
            Actions action = new Actions(driver);
            Wait<WebDriver> wait = newFluentWait(driver);
            WebElement button = null;
            button = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.ti-quick-add-to-cart-worksheet-actions ti-button")));
            action.pause(3 * randKeyTime()).moveToElement(button).pause(randKeyTime()).perform();
            Rectangle rect = button.getRect();
            moveTo(rect.x, rect.y);
            action.pause(3 * randKeyTime()).moveToElement(button).pause(randKeyTime()).click().perform();//点击添加到购物车，等待10s
            return waitNotInPage(wait, "/quick-add-to-cart");
        } catch (Exception e) {
            logger.info("没有可添加商品");
        }
        return false;
    }

    private static boolean waitNotInPage(Wait<WebDriver> wait, String pageTag) {
        try {
            wait.until(d -> !d.getCurrentUrl().contains(pageTag));
            return true;
        } catch (Exception e) {
            logger.info("url not got to {}", pageTag);
        }
        return false;
    }


    public static void sendKeys(WebDriver driver, String... keys) {
        Actions action = new Actions(driver);
        int c = 0, s = 0, a = 0;
        for (String key : keys) {
            boolean notCtKey = false;
            switch (key.toUpperCase()) {
                case CONTROL_UP:
                    c--;
                    action.keyUp(Keys.CONTROL);
                    break;
                case CONTROL_DOWN:
                    c++;
                    action.keyDown(Keys.CONTROL);
                    break;
                case ALT_DOWN:
                    a++;
                    action.keyDown(Keys.ALT);
                    break;
                case ALT_UP:
                    a--;
                    action.keyUp(Keys.ALT);
                    break;
                case SHIFT_DOWN:
                    s++;
                    action.keyUp(Keys.SHIFT);
                    break;
                case SHIFT_UP:
                    s--;
                    action.keyUp(Keys.SHIFT);
                    break;
                case SPACE:
                    action.sendKeys(Keys.SPACE);
                    break;
                default:
                    notCtKey = true;
                    break;
            }

            action.pause(randKeyTime());
            if (notCtKey) {
                for (int i = 0; i < key.length(); i++) {
                    action.sendKeys(String.valueOf(key.charAt(i)));
                    action.pause(randKeyTime());
                }
            }
        }

        action.perform();
    }

    private static int randKeyTime() {
        return (int) (20 + 40 * Math.random());
    }

    public static WebElement findElement(WebDriver driver, String key, String searchStr) {
        WebElement element = null;
        try {
            switch (key) {
                case ID:
                    element = driver.findElement(By.id(searchStr));
                    break;
                case XPATH:
                    element = driver.findElement(By.xpath(searchStr));
                    break;
                case CSS:
                    element = driver.findElement(By.cssSelector(searchStr));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.info("can't found element, by {} value:{}", key, searchStr);
        }

        return element;
    }

    public static <R> R findElementAndDo(WebDriver driver, String key, String searchStr, Function<WebElement, R> consumer) {
        R r = null;
        try {
            WebElement element = findElement(driver, key, searchStr);
            if (element != null && consumer != null) {
                r = consumer.apply(element);
            }
        } finally {
            clearTag(driver);
        }

        return r;
    }

    public static void doSomething(WebDriver driver, Consumer<WebDriver> consumer) {
        try {
            if (driver != null && consumer != null) {
                consumer.accept(driver);
            }
        } finally {
            clearTag(driver);
        }
    }

    public static boolean findElementAndDo(WebDriver driver, Wait<WebDriver> wait, String key, String searchStr) {
        return findElementAndDo(driver, wait, key, searchStr, null);
    }

    public static <R> R findElementAndDo(WebDriver driver, Wait<WebDriver> wait, String key, String searchStr, Function<WebElement, R> consumer) {
        R r = null;
        try {
            WebElement element = findElement(wait, key, searchStr);
            if (element != null && consumer != null) {
                r = wait.until(driver1 -> {
                    return consumer.apply(element);
                });
            }
        } finally {
            clearTag(driver);
        }
        return r;
    }



    private static void clearTag(WebDriver driver) {
        TiSeleniumTools.excuteJs(driver, "setTimeout(\"delete document['$cdc_asdjflasutopfhvcZLmcfl_']\",20);console.log('delete');");
    }

    public static WebElement findElement(Wait<WebDriver> wait, String key, String searchStr) {
        WebElement element = null;
        try {
            switch (key) {
                case ID:
                    element = wait.until(d -> d.findElement(By.id(searchStr)));
                    break;
                case XPATH:
                    element = wait.until(d -> d.findElement(By.xpath(searchStr)));
                    break;
                case CSS:
                    element = wait.until(d -> d.findElement(By.cssSelector(searchStr)));
                    break;
                default:
                    break;
            }
        } catch (Exception e) {
            logger.info("can't found element, by {} value:{}", key, searchStr);
        }

        return element;
    }

    public static boolean choseCountry(WebDriver driver) {
        logger.info("start----- chose country");
        try {
            if (checkNotInPage(driver, "/cart")) {
                logger.info("not in cart page!");
                return false;
            }
            Actions action = new Actions(driver);
            Wait<WebDriver> wait = newFluentWait(driver);
            selectElementById(driver, wait, action, "CN", "llc-cartpage-ship-to-country");
            WebElement nextbutton = wait.until(d -> d.findElement(By.id("llc-cartpage-ship-to-continue")));
            action.moveToElement(nextbutton).click().pause(10 * randKeyTime()).perform();
            return true;
        } catch (Exception e) {
            logger.info("no found country chose element");
        }
        return false;
    }

    private static void selectElementById(WebDriver driver, Wait<WebDriver> wait, Actions action, String value, String id) {
        if (StringUtils.hasText(value)) {
            WebElement element = waitElemnt(driver, wait, d -> d.findElement(By.id(id)));
            if (element != null && element.isDisplayed() && element.isEnabled()) {
                action.moveToElement(element).pause(randKeyTime()).click().pause(randKeyTime()).perform();
                Select s = new Select(element);
                s.selectByValue(value);
            }
        }
    }


    //随机移动, 移动2秒
    public static void randMove(int x, int y, int endX, int endY) {
        logger.info("mouse start: {}:{} - {}:{}", x, y, endX, endY);
        List<java.awt.Point> points = RandomArc.randomLine(x, y, endX, endY);
        for (java.awt.Point p : points) {
            mouseMove(p.x, p.y);
        }
    }


    public static void moveTo(int x, int y) {
        try {
            logger.info("mouse start: {} - {}", x, y);
            Point location = MouseInfo.getPointerInfo().getLocation();
            randMove(location.x, location.y, x, y);
        } catch (Exception e) {
            logger.info("can't move", e);
        }
    }

    public static void moveAndClick(Rectangle rectangle, int offsetHeight) {
        int xoffset = 0, yoffset = 0;
        if (rectangle != null) {
            xoffset = rectangle.x + rectangle.width / 2;
            yoffset = rectangle.y + rectangle.height / 2 + baseOffsetY - offsetHeight;
        }

        moveAndClick(xoffset, yoffset);
    }


    public static void moveAndClick(int x, int y) {
        try {
            int xoffset = x, yoffset = y;
            logger.info("mouse start: {} - {}", xoffset, yoffset);
            Point location = MouseInfo.getPointerInfo().getLocation();

            randMove(location.x, location.y, xoffset, yoffset);

            if (robot != null) {
                robot.delay(4 * randKeyTime());
                robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                robot.delay(randKeyTime());
                robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
            }
        } catch (Exception e) {
            logger.info("can't move", e);
        }
    }

    public static void moveAndDbclick(int x, int y) {
        try {
            int xoffset = x, yoffset = y;
            logger.info("mouse start: {} - {}", xoffset, yoffset);
            Point location = MouseInfo.getPointerInfo().getLocation();

            randMove(location.x, location.y, xoffset, yoffset);

            if (robot != null) {
                robot.delay(4 * randKeyTime());
                robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                robot.delay(randKeyTime()/2);
                robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
                robot.delay(randKeyTime()/2);
                robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
                robot.delay(randKeyTime()/2);
                robot.mouseRelease(KeyEvent.BUTTON1_DOWN_MASK);
            }
        } catch (Exception e) {
            logger.info("can't move", e);
        }
    }

    public static void sendKeys(String keyStr) {
        try {
            if (robot == null) {
                return;
            }
            List<Integer> keys = KeyParse.parseCode(keyStr);
            boolean isShiftDown = false;
            boolean isCtrlDown = false;
            for (Integer key : keys) {
                robot.keyPress(key);

                if (key == KeyEvent.VK_SHIFT ) {
                    isShiftDown = true;
                    continue;
                }
                if (key == KeyEvent.VK_CONTROL ) {
                    isCtrlDown = true;
                    continue;
                }

                robot.keyRelease(key);
                if (isShiftDown) {
                    robot.keyRelease(KeyEvent.VK_SHIFT);
                    isShiftDown = false;
                }
                if (isCtrlDown) {
                    robot.keyRelease(KeyEvent.VK_CONTROL);
                    isCtrlDown = false;
                }
                robot.delay(6 * randKeyTime());
            }

            robot.delay(10 * randKeyTime());
        } catch (Exception e) {
            logger.info("move failed", e);
        }
    }


    //下单
    public static boolean toOrder(WebDriver driver) {
        logger.info("start----- got to order");
        Actions action = new Actions(driver);
        try {
            choseCountry(driver);
            if (checkNotInPage(driver, "/cart")) {
                logger.info("not in cart page!");
                return false;
            }
            Wait<WebDriver> wait = newFluentWait(driver);
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("tiCartCalculate_Checkout")));
            scrollWindow(driver, "tiCartCalculate_Checkout");
            Rectangle rect = button.getRect();
            moveTo(rect.x, rect.y);
            action.pause(20 * randKeyTime()).moveToElement(button).pause(30 * randKeyTime()).perform();//
            button.click();
            action.pause(10 * randKeyTime());
            return waitNotInPage(wait, "/cart");
        } catch (Exception e) {
            logger.info("not found order button", e);
        }
        return false;
    }


    //地址表单填写
    public static boolean addressForm(WebDriver driver, Wait<WebDriver> wait) {
        try {
            Actions action = new Actions(driver);
        logger.info("start----- fill address form");
            AddressInfo addressInfo = getAddressInfo();
            if (Objects.nonNull(addressInfo.getType())) {
                WebElement addressType = waitElemnt(driver, wait, ExpectedConditions.elementToBeClickable(By.xpath("//input[@value=\"" + addressInfo.getType() + "\"]")));
                if (addressType != null) {
                    action.pause(randKeyTime()).moveToElement(addressType).pause(randKeyTime()).click().pause(randKeyTime()).perform();
                }
            }
            selectElementById(driver, null, action, addressInfo.getOldAddressId(), "paid-address-list-id");

            inputAttrById(driver, null, action, addressInfo.getFirstName(), "firstName");
            inputAttrById(driver, null, action, addressInfo.getLastName(), "lastName");
            inputAttrById(driver, null, action, addressInfo.getAddress1(), "paid-line1");
            inputAttrById(driver, null, action, addressInfo.getAddress2(), "line2");
            inputAttrById(driver, null, action, addressInfo.getTownCity(), "townCity");
            inputAttrById(driver, null, action, addressInfo.getState(), "state-input");
            inputAttrById(driver, null, action, addressInfo.getPostcode(), "postcode");
            inputAttrById(driver, null, action, addressInfo.getCompanyName(), "companyName");
            inputAttrById(driver, null, action, addressInfo.getCompanyUrl(), "shipping_companyUrl");

            if (addressInfo.getPhoneIsMobile()) {
                WebElement element = waitElemnt(driver, null, ExpectedConditions.elementToBeClickable(By.xpath("//input[@value=\"" + addressInfo.getType() + "\"]")));
                if (element != null) {
                    action.pause(randKeyTime()).moveToElement(element).pause(randKeyTime()).click().pause(randKeyTime()).perform();
                }
            }
            selectElementById(driver, null, action, addressInfo.getPhoneCountryPrefix(), "phoneCountryPrefix");
            inputAttrById(driver, null, action, addressInfo.getPhoneNumber(), "phoneNumber");
            inputAttrById(driver, null, action, addressInfo.getPhoneExtension(), "phoneExtension");
            inputAttrById(driver, null, action, addressInfo.getEmail(), "email");

            return true;
        } catch (Exception e) {
            logger.info("not found distribution button", e);
        }
        return false;
    }

    private static void inputAttrById(WebDriver driver, Wait<WebDriver> wait, Actions action, String content, String id) {
        if (StringUtils.hasText(content)) {
            WebElement element = waitElemnt(driver, wait, ExpectedConditions.elementToBeClickable(By.id(id)));
            if (element != null) {
                action.pause(randKeyTime()).moveToElement(element)
                        .pause(randKeyTime()).click()
                        .pause(randKeyTime()).sendKeys(content).perform();
            }
        }
    }

    private static WebElement waitElemnt(WebDriver driver, Wait<WebDriver> wait, Function<WebDriver, WebElement> isTrue) {
        try {
            if (wait != null) {
                return wait.until(isTrue);
            }
            return isTrue.apply(driver);
        } catch (Exception e) {
            logger.info("not found element {}", e.getMessage());
        }
        return null;
    }


    //地址配置
    public static boolean address(WebDriver driver) {
        logger.info("start----- address");
        Actions action = new Actions(driver);
        try {
            if (checkNotInPage(driver, "delivery-address")) {
                logger.info("not in delivery-address page!");
                return false;
            }
            Wait<WebDriver> wait = newFluentWait(driver);
            closeFlag(driver);
            addressForm(driver, wait);
            //Business
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(By.id("paid-shipping-address-select")));
            scrollWindow(driver, "paid-shipping-address-select");
            action.moveToElement(button).pause(randKeyTime()).click().perform();
            waitNotInPage(wait, "delivery-address");
            return true;
        } catch (Exception e) {
            logger.info("set address failed");
        }
        return false;
    }


    public static void mouseMove(int x, int y) {
        try {
            if (robot == null) {
                return;
            }


            robot.mouseMove(x, y);

            robot.delay(randKeyTime() / 3);
            //robot.mousePress(KeyEvent.BUTTON1_DOWN_MASK);
            printMousePos();
        } catch (Exception e) {
            logger.info("move failed", e);
        }
    }

    public static void printMousePos() {
        if (logger.isDebugEnabled()) {
            Point location = MouseInfo.getPointerInfo().getLocation();
            logger.debug("mouser pos: {}, {}", location.x, location.y);
        }
    }

    //关闭购物车确认弹框
    public static boolean closeFlag(WebDriver driver) {
        logger.info("start----- close flag");
        Actions action = new Actions(driver);
        try {
            WebElement flagButton = findElement(driver, "CSS", "div.ab_widget_container_popin-simple button.ab_widget_container_popin-simple_close_button");
            if (flagButton != null && flagButton.isDisplayed()) {
                action.moveToElement(flagButton).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            }
            return true;
        } catch (Exception e) {
            logger.info("set address failed", e);
        }
        return false;
    }


    //发票
    public static boolean invoice(WebDriver driver) {
        logger.info("start --- check invoice");
        try {
            if (checkNotInPage(driver, "cn-tax-invoice")) {
                logger.info("not in cn-tax-invoice page!");
                return false;
            }
            Actions action = new Actions(driver);
            Wait<WebDriver> wait = newFluentWait(driver);
            scrollWindow(driver, "invoiceTitle");
            fillInvoiceForm(driver, wait);

            WebElement checkButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("tax-invoice-submit")));
            scrollWindow(driver, "tax-invoice-submit");
            action.pause(randKeyTime()).moveToElement(checkButton).pause(randKeyTime()).click().pause(randKeyTime()).perform();

            WebElement submit = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector("div.ti_p-button-set button[type='submit']")));
            action.moveToElement(submit).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            return waitNotInPage(wait, "cn-tax-invoice");
        } catch (Exception e) {
            logger.info("not found invoice button", e);
        }
        return false;
    }

    private static boolean fillInvoiceForm(WebDriver driver, Wait<WebDriver> wait) {
        logger.info("start----- fill invoice form");
        try {
            Actions action = new Actions(driver);
            InvoiceInfo invoiceInfo = getInvoiceInfo();
            if (Objects.nonNull(invoiceInfo.getType())) {
                WebElement element = waitElemnt(driver, wait, ExpectedConditions.elementToBeClickable(By.xpath("//input[@value=\"" + invoiceInfo.getType() + "\"]")));
                if (element != null) {
                    action.pause(randKeyTime()).moveToElement(element).pause(randKeyTime()).click().pause(randKeyTime()).perform();
                }
            }

            if (Objects.nonNull(invoiceInfo.getRecipientType())) {
                WebElement element = waitElemnt(driver, wait, ExpectedConditions.elementToBeClickable(By.xpath("//input[@value=\"" + invoiceInfo.getRecipientType() + "\"]")));
                if (element != null) {
                    action.pause(randKeyTime()).moveToElement(element).pause(randKeyTime()).click().pause(randKeyTime()).perform();
                }
            }

            inputAttrById(driver, null, action, invoiceInfo.getInvoiceTitle(), "invoiceTitle");
            inputAttrById(driver, null, action, invoiceInfo.getTaxRegNo(), "taxRegNo");
            inputAttrById(driver, null, action, invoiceInfo.getRegAddress(), "regAddress");
            return true;
        } catch (Exception e) {
            logger.info("fill invoice info failed , {}", e.getMessage());
        }
        return false;
    }

    private static Wait<WebDriver> newFluentWait(WebDriver driver) {
        return new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(20))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);
    }

    private static Wait<WebDriver> newFluentWait(WebDriver driver, int timeout) {
        return new FluentWait<WebDriver>(driver)
                .withTimeout(Duration.ofSeconds(timeout))
                .pollingEvery(Duration.ofMillis(200))
                .ignoring(NoSuchElementException.class);
    }


    //合规
    public static boolean safe(WebDriver driver) {
        logger.info("start----- safe check");
        try {
            Actions action = new Actions(driver);
            Wait<WebDriver> wait = newFluentWait(driver);

            if (checkNotInPage(driver, "regulations-step")) {
                logger.info("not in regulations-step page!");
                return false;
            }

            scrollWindow(driver, "regulations-select");

            WebElement divBox = findElement(driver, "ID", "js-checkout-waiver-textbox");//协议
            if (divBox != null) {
                String scrollJs = "document.getElementById(\"js-checkout-waiver-textbox\").scrollTop = 200;delete window['$cdc_asdjflasutopfhvcZLmcfl_'];delete document['$cdc_asdjflasutopfhvcZLmcfl_'];";
                excuteJs(driver, scrollJs);
            }

            WebElement receiveCheckbox = findElement(driver, "ID", "waiver_acceptwaiver_accept");//接受
            if (receiveCheckbox != null) {
                action.moveToElement(receiveCheckbox).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            }

            WebElement selector = wait.until(d -> d.findElement(By.id("regulations-select")));
            action.moveToElement(selector).pause(randKeyTime()).click().perform(); //合规1
            Select s = new Select(selector);
            s.selectByIndex(0);

            WebElement nextSelector = wait.until(d -> d.findElement(By.id("checkout-regulations-select")));
            action.moveToElement(nextSelector).pause(5 * randKeyTime()).click().pause(2 * randKeyTime()).perform();

            Select ns = new Select(nextSelector);
            ns.selectByIndex(0);

            List<WebElement> checkboxflags = wait.until(d -> d.findElements(By.cssSelector("input[name='militaryFlag']")));
            if (checkboxflags.size() > 1) {//非军方
                action.moveToElement(checkboxflags.get(1)).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            }

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("regulations-submit-btn")));
            scrollWindow(driver, "regulations-submit-btn");
            action.moveToElement(nextButton).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            return waitNotInPage(wait, "regulations-step");
        } catch (Exception e) {
            logger.info("not found safe button", e);
        }
        return false;
    }

    public static boolean checkNotInPage(WebDriver driver, final String uriTag) {
        Wait<WebDriver> wait = newFluentWait(driver);
        try {
            return !wait.until(d -> d.getCurrentUrl().contains(uriTag));
        } catch (Exception e) {
            logger.info("not contain uri: {}", uriTag);
        }
        return true;
    }


    //配送
    public static boolean distribution(WebDriver driver) {
        logger.info("start----- distribution");
        try {
            if (checkNotInPage(driver, "delivery-method")) {
                logger.info("not in delivery-method page!");
                return false;
            }

            Actions action = new Actions(driver);
            Wait<WebDriver> wait = newFluentWait(driver);

            WebElement divScroll = wait.until(d -> d.findElement(By.id("js-checkout-toc-textbox")));
            scrollWindow(driver, "js-checkout-toc-textbox");
            action.moveToElement(divScroll).pause(randKeyTime()).perform();
            Rectangle rect = divScroll.getRect();
            moveTo(rect.x, rect.y);
            String scrollJs = "document.getElementById(\"js-checkout-toc-textbox\").scrollTop = 30;delete window['$cdc_asdjflasutopfhvcZLmcfl_'];delete document['$cdc_asdjflasutopfhvcZLmcfl_'];";
            excuteJs(driver, scrollJs);

            WebElement checkboxflag = wait.until(ExpectedConditions.elementToBeClickable(By.id("terms_accept")));
            action.moveToElement(checkboxflag).pause(randKeyTime()).click().pause(randKeyTime()).perform();

            WebElement nextButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("shipping-method-submit")));
            scrollWindow(driver, "shipping-method-submit");
            action.pause(3 * randKeyTime()).moveToElement(nextButton).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            return waitNotInPage(wait, "delivery-method");
        } catch (Exception e) {
            logger.info("not found distribution button");
        }
        return false;
    }


    //支付
    public static boolean pay(WebDriver driver) {
        logger.info("start ----- pay");
        try {
            if (checkNotInPage(driver, "payment-method")) {
                logger.info("not in payment-method page!");
                return false;
            }
            Actions action = new Actions(driver);
            Wait<WebDriver> wait = newFluentWait(driver);
            WebElement checkboxflag = wait.until(d -> d.findElement(By.id("payment-method-alipay")));
            scrollWindow(driver, "payment-method-alipay");
            action.pause(randKeyTime()).moveToElement(checkboxflag).pause(randKeyTime()).click().pause(randKeyTime()).perform();
            WebElement nextButton = wait.until(d -> d.findElement(By.cssSelector("button.js-apm-paymentbtn")));
            action.moveToElement(nextButton).pause(randKeyTime()).click().pause(2000).perform();
            return true;
        } catch (Exception e) {
            logger.info("not found pay button");
        }
        return false;
    }


    public static byte[] getScreenShot(WebDriver driver) {
        if (driver instanceof RemoteWebDriver) {
            return ((RemoteWebDriver) driver).getScreenshotAs(OutputType.BYTES);
        }
        return null;
    }

    public static void scrollWindow(WebDriver driver, String elementId) {
        String scrollJs = "!function (elementId){" +
                "var destEle = document.getElementById(elementId);" +
                "    var scrolltopTemp = document.documentElement.scrollTop || document.body.scrollTop;" +
                "    var rect = destEle.getBoundingClientRect();" +
                "var dis = rect.top > 0 ? 1 : -1;" +
                "    var top = rect.top < 0? 0 - rect.top + destEle.clientHeight : rect.top - scrolltopTemp ;" +
                "    var currentTop = 0;" +
                "    var requestId;" +
                "    function step () {" +
                "        currentTop += 20;" +
                "        if (currentTop <= top) {" +
                "window.scrollTo(0, scrolltopTemp + currentTop*dis);" +
                "            requestId = window.requestAnimationFrame(step);" +
                "        } else {" +
                "            window.cancelAnimationFrame(requestId);" +
                "        }" +
                "    }" +
                "    window.requestAnimationFrame(step);" +
                "}(\"" + elementId + "\");delete window['$cdc_asdjflasutopfhvcZLmcfl_']; delete document['$cdc_asdjflasutopfhvcZLmcfl_'];";
        excuteJs(driver, scrollJs);
    }

    public static void scrollWindow(WebDriver driver, int heightOffset) {
        String scrollJs = "window.scrollTo(0," + heightOffset + ");delete window['$cdc_asdjflasutopfhvcZLmcfl_']; delete document['$cdc_asdjflasutopfhvcZLmcfl_'];";
        excuteJs(driver, scrollJs);
    }

    public static void scrollWindow(int heightOffset) {
        if (robot != null) {
            robot.mouseWheel(heightOffset);
        }
    }

    public static void excuteJs(WebDriver driver, String scrollJs) {
        if (driver instanceof RemoteWebDriver) {
            ((RemoteWebDriver) driver).executeScript(scrollJs);
        }
    }

    //打开
    public static boolean open(WebDriver driver, String url) {
        logger.info("open  ----- url:{}", url);
        try {

            String currentUrl = driver.getCurrentUrl();
            if (url.equals(currentUrl)) {
                logger.info("open  ----- aready opened");
                return true;
            }

            if (driver instanceof ChromiumDriver) {
                Map<String, Object> params = Maps.newHashMap();
                params.put("source", FileCopyUtils.copyToString(new InputStreamReader(TiSeleniumTools.class
                        .getClassLoader().getResourceAsStream("stealth.min.js"))));
                ((ChromiumDriver) driver).executeCdpCommand("Page.addScriptToEvaluateOnNewDocument", params);
            }

            driver.get(url);
            Wait<WebDriver> wait = newFluentWait(driver);
            wait.until(numberOfWindowsToBe(1));
            return true;
        } catch (Exception e) {
            logger.info("not found pay button");
        }
        return false;
    }

    private static int loadStorage(WebDriver driver, Wait<WebDriver> wait, String code, int line, int offset) {
        try {
            Actions action = new Actions(driver);
            findElementAndDo(driver, wait, XPATH, "//tbody/tr[" + line + "]/td[2]/ti-input", null);
            if (line > 4) {
                Rectangle rectangle = findElementAndDo(driver, CSS, "a.ti-quick-add-to-cart-add-row", add -> {
                    return add.getRect();
                });

                action.pause(2 * randKeyTime()).perform();
                moveAndClick(rectangle, offset);
                //scrollWindow(driver, 50);
                //offset = offset + (line - 4) * 50;
            }
            action.pause(randKeyTime()).perform();

            Rectangle rect = findElementAndDo(driver, XPATH, "//tbody/tr[" + line + "]/td[2]/ti-input", input -> {
                Rectangle r = input.getRect();
                return r;
            });
            action.pause(2 * randKeyTime()).perform();
            moveAndClick(rect, offset);
            sendKeys(code);

            action.pause(randKeyTime()).perform();
            rect = findElementAndDo(driver, XPATH, "//tbody/tr[" + line + "]/td[4]/ti-input", input1 -> {
                Rectangle r = input1.getRect();
                return r;
            });

            action.pause(2 * randKeyTime()).perform();
            moveAndClick(rect, offset);
            sendKeys("1");
            action.pause(2 * randKeyTime()).perform();

            String text = findElementAndDo(driver, wait, XPATH, "//tbody/tr[" + line + "]/td[5]", ele -> {
                try {
                    String elementText = ele.getText();
                    return elementText != null && elementText.length() > 0 ? elementText : null;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            });

            Integer storageCount = Integer.valueOf(text.replaceAll(",", ""));
            logger.info("{} storage is {}", code, text);

  /*          findElementAndDo(driver, XPATH, "//tbody/tr[" + line + "]/td[4]/ti-input", input1 -> {
                action.moveToElement(input1).pause(randKeyTime()).click()
                        .pause(randKeyTime()).sendKeys(String.valueOf(storageCount)).perform();
            });*/

            action.pause(10 * randKeyTime()).perform();
            return storageCount;
        } catch (Exception e) {
            logger.info("query storage for {} failed, message:{}", code, e.getMessage());
        }

        return 0;
    }

    /**
     * 使用按键模拟加载
     *
     * @param driver
     * @param wait
     * @param code
     * @param line
     * @param offset
     * @return
     */
    private static int loadStorageByRobot(WebDriver driver,Wait<WebDriver> pageWait, Wait<WebDriver> wait, String code, int line, int offset) {
        int baseX = baseOffsetX + 156, baseY = baseOffsetY + offset;
        try {
            Actions action = new Actions(driver);
            if (line > 5) {//固定都
                line = 6;
            }
            if (line == 1) {
                findElementAndDo(driver, wait, XPATH, "//tbody/tr[" + line + "]/td[2]/ti-input", null); //等待加载完成
            }

            action.pause(randKeyTime()).perform();
            if (line > 4) {
                action.pause(2 * randKeyTime()).perform();
                moveAndClick(baseX - 70, baseY + 36 + 5 * 50);
                //scrollWindow(driver, 50);
                //offset = offset + (line - 4) * 50;
            }
            action.pause(randKeyTime()).perform();

            moveAndClick(baseX, baseY + line * 50);
            sendKeys(code + "\n");

            action.pause(2 * randKeyTime()).perform();
            moveAndClick(baseX + 290, baseY + line * 50);
            sendKeys("1");
            action.pause(2 * randKeyTime()).perform();

            String text = findElementAndDo(driver, wait, XPATH, "//tbody/tr[" + line + "]/td[5]", ele -> {
                try {
                    pageWait.until(d->{
                        WebElement e =d.findElement(By.id("sec-overlay"));
                        return e.isDisplayed() == false;
                    });

                    String elementText = ele.getText();
                    return elementText != null && elementText.length() > 0 ? elementText : null;
                } catch (StaleElementReferenceException e) {
                    return null;
                }
            });

            Integer storageCount = Integer.valueOf(text.replaceAll(",", ""));
            logger.info("{}: {} storage is {}", line, code, text);

  /*          findElementAndDo(driver, XPATH, "//tbody/tr[" + line + "]/td[4]/ti-input", input1 -> {
                action.moveToElement(input1).pause(randKeyTime()).click()
                        .pause(randKeyTime()).sendKeys(String.valueOf(storageCount)).perform();
            });*/

            action.pause(5 * randKeyTime()).perform();
            if (line > 5) {
                moveAndClick(baseX + 1026, baseY + line * 50);
                action.pause(10 * randKeyTime()).perform();
                moveAndClick(baseX + 1025, baseY + line * 50);
            }
            return storageCount;
        } catch (Exception e) {
            logger.info("query storage for {} failed, line : {}, message:{}, ", code, line, e.getMessage());
            if (errorCount > 2) {
                errorCount = 0;
                Actions actions = new Actions(driver);
                doSomething(driver, d->{
                    driver.manage().deleteAllCookies();
                    driver.navigate().refresh();
                    actions.pause(200 * randKeyTime()).perform();
                });
                scrollWindow(20);
                actions.pause(2 * randKeyTime()).perform();
                scrollWindow(-30);
                actions.pause(2 * randKeyTime()).perform();
                scrollWindow(9);
                if (refleshCount > 3) {
                    refleshCount = -2;
                    actions.pause(60000).perform();
                }
                if (refleshCount == -1) {
                    refleshCount = 0;
                    errorCount = 0;
                    actions.pause(120000).perform();
                }
                refleshCount=refleshCount+1;
                return 0;
            }


            if (line > 5) {
                moveAndClick(baseX + 1026, baseY + line * 50);
            }
            errorCount++;
        }finally {
            clearTag(driver);
        }

        return 0;
    }

    /**
     * 使用按键模拟, mitm 获取
     *
     * @param code
     * @param line
     * @param offset
     * @return
     */
    private static int loadStorageByRobot(Wait<Integer>wait, String code, int line, int offset) {
        int baseX = baseOffsetX + 156, baseY = baseOffsetY + offset;
        try {
            if (line > 5) {//固定都
                line = 6;
            }
            pause(randKeyTime());
            if (line > 4) {
                pause(2 * randKeyTime());
                moveAndClick(baseX - 70, baseY + 36 + 5 * 50);
                //scrollWindow(driver, 50);
                //offset = offset + (line - 4) * 50;
            }
            pause(randKeyTime());

            moveAndClick(baseX, baseY + line * 50);
            sendKeys(code + "\n");

            pause(2 * randKeyTime());
            moveAndClick(baseX + 290, baseY + line * 50);
            sendKeys("1");
            pause(2 * randKeyTime());

            final int l = line;

            Integer storageCount =  wait.until(v ->{
                clearSysClipboardText();
                moveAndDbclick(baseX + 390, baseY + l * 50);
                pause(4 * randKeyTime());
                sendKeys("\021c");
                Integer rs = getSysClipboardText();
                return rs >= 0 ? rs : null;
            });
            if(storageCount >= 0){
                logger.info("{}: {} storage is {}", line, code, storageCount);
            }
            pause(5 * randKeyTime());
            if (line > 5) {
                moveAndClick(baseX + 1026, baseY + line * 50);
                pause(10 * randKeyTime());
                moveAndClick(baseX + 1025, baseY + line * 50);
            }
            return storageCount;
        } catch (Exception e) {
            logger.info("query storage for {} failed, line : {}, message:{}, ", code, line);
            errorCount++;
            checkerror();
            if (line > 5) {
                moveAndClick(baseX + 1026, baseY + line * 50);
            }
            errorCount++;
        }finally {
            clearSysClipboardText();
        }

        return 0;
    }

    private static void checkerror() {
        if (errorCount > 2) {
            errorCount = 0;
            scrollWindow(20);
            pause(2 * randKeyTime());
            scrollWindow(-30);
            pause(2 * randKeyTime());
            scrollWindow(9);
            if (refleshCount > 3) {
                refleshCount = -2;
                pause(60000);
            }
            if (refleshCount == -1) {
                refleshCount = 0;
                errorCount = 0;
                pause(120000);
            }
            refleshCount=refleshCount+1;
        }
    }



    private static void pause(int i) {
        try{
            TimeUnit.MILLISECONDS.sleep(i);
        }catch (Exception e){
            //do nothing
        }
    }


    /**
     *1. 从剪切板获得文字。
     */
    public static Integer getSysClipboardText() {
        Integer ret = -1;
        Clipboard sysClip = Toolkit.getDefaultToolkit().getSystemClipboard();
        // 获取剪切板中的内容
        Transferable clipTf = sysClip.getContents(null);

        if (clipTf != null) {
            // 检查内容是否是文本类型
            if (clipTf.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                try {
                   String retStr = (String) clipTf
                            .getTransferData(DataFlavor.stringFlavor);
                    if(StringUtils.hasText(retStr)){
                        ret = Integer.valueOf(retStr.trim());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        return ret;
    }

    /**
     * 2.将字符串复制到剪切板。
     */
    public static void clearSysClipboardText() {
        Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
        Transferable tText = new StringSelection("");
        clip.setContents(tText, null);

    }


    public static void setNoticeService(NoticeService noticeService) {
        TiSeleniumTools.noticeService = noticeService;
    }
}