/**
 * Created by Anchor on 2017/2/2.
 */

import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        driver.get("http://user.qzone.qq.com/"+ account + "/311");
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
            Thread.sleep(6000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        driver.switchTo().frame("app_canvas_frame");
        boolean notEnd = true;
        while (notEnd) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            List<WebElement> contents = driver.findElements(By.cssSelector(".content"));
            List<WebElement> times = driver.findElements(By.cssSelector(".c_tx.c_tx3.goDetail"));
            for (int i = 0; i < contents.size(); i++) {
                String s = times.get(i).getText() + " " + contents.get(i).getText().replace("\n", "\\n");
                result.add(s);
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
