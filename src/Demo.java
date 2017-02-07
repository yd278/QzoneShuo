/**
 * Created by Anchor on 2017/2/2.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class Demo {

    public static void main(String args[]) throws IOException, InterruptedException {
        //若浏览器driver不在环境变量里 就放到第三个arg
        if(args.length==3){
            System.setProperty("webdriver.edge.driver", args[2]);
        }
        //
        Demo demo = new Demo();
        List<String> result = demo.edgeDrive(args[0],args[1]);

        File file = new File("result.txt");
        FileWriter fileWriter = new FileWriter(file);
        for( String s : result){
            fileWriter.write(s + "\n");
        }
        fileWriter.close();
    }

    public List<String> edgeDrive(String account, String password) throws IOException, InterruptedException {
        //因为ff版本太高不兼容所以用了edge浏览器
        List<String> result = new ArrayList<>();
        WebDriver driver = new EdgeDriver();
        //打开
        driver.get("http://qzone.qq.com");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //不造为什么edge存不了cookies……那就手动登录一下
        driver.switchTo().frame("login_frame");
        driver.findElement(By.id("switcher_plogin")).click();
        driver.findElement(By.id("u")).clear();
        driver.findElement(By.id("u")).sendKeys(account);
        driver.findElement(By.id("p")).clear();
        driver.findElement(By.id("p")).sendKeys(password);
        driver.findElement(By.id("login_button")).click();
        //登录完成 等页面加载，视网速调时间

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.get("http://user.qzone.qq.com/"+ account + "/311");

        try {
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.switchTo().frame("app_canvas_frame");
        boolean notEnd = true;
        while (notEnd) {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            List<WebElement> shuos = driver.findElements(By.cssSelector(".feed"));
            for(WebElement shuo : shuos){
                String shuoResult = "";
                String content = shuo.findElement(By.cssSelector(".content")).getText();
                String time = shuo.findElement(By.cssSelector(".c_tx.c_tx3.goDetail")).getAttribute("title");
                shuoResult += time + "\n" + content;
                try{
                    WebElement pre = shuo.findElement(By.cssSelector(".quote.bor2")).findElement(By.cssSelector(".bd"));
                    shuoResult += " 【转自：】 " + pre.getText();
                }catch(NoSuchElementException ignored){
                }
                result.add(shuoResult);
            }
            try{
                WebElement nextPage = driver.findElement(By.linkText("下一页"));
                nextPage.click();
                notEnd = true;
            }catch(NoSuchElementException e){
                notEnd = false;
            }
        }
        driver.close();
        return result;
    }

}
