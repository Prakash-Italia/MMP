package org.iitwf.selenium.mmpequinox;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.HashMap;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import com.github.javafaker.Faker;

public class patientRegistration {
	
	static WebDriver driver=new ChromeDriver();
	static Faker faker = new Faker();
	static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	@SuppressWarnings("deprecation")
	static String ssn=RandomStringUtils.randomNumeric(9);
	static String fname=faker.address().firstName();
	static String lname=faker.address().lastName();
	static String pwd=faker.internet().password(10, 15, true);
	static String username=fname+"."+lname;
	
	static HashMap <String, String> expected = new HashMap<>();
	static HashMap <String, String> actual = new HashMap<>();
	
	public static void main(String[] args) throws InterruptedException {
		
		System.out.println("SSN::"+ssn);
		System.out.println("USER NAME::"+username);
		System.out.println("PASSWORD::"+pwd);
		registration();
		patientAccepted(ssn);
		login(username, pwd);
		expected.put("First Name", fname);
		expected.put("Last Name", lname);
		expected.put("SSN", ssn);
		
		if(actual.equals(expected))
		{
			System.out.println("::User created succesfully and aproved by admin::");
		}
		driver.quit();
	}
	
	@SuppressWarnings("deprecation")
	public static void registration() throws InterruptedException

	{
		try {
	driver.get("http://85.209.95.122/MMP-Release2-Integrated-Build.6.8.000/portal/login.php");
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
	driver.findElement(By.xpath("//input[@value='Register']")).click();
	
	driver.findElement(By.xpath("//input[@id='firstname']")).sendKeys(fname);
	driver.findElement(By.xpath("//input[@id='lastname']")).sendKeys(lname);
	driver.findElement(By.xpath("//input[@id='datepicker']")).sendKeys(sdf.format(faker.date().birthday()));
	driver.findElement(By.xpath("//input[@id='license']")).sendKeys(RandomStringUtils.randomNumeric(8));
	driver.findElement(By.xpath("//input[@id='ssn']")).sendKeys(ssn);
	driver.findElement(By.xpath("//input[@id='state']")).sendKeys(faker.address().state());
	driver.findElement(By.xpath("//input[@id='city']")).sendKeys(faker.address().cityName());
	driver.findElement(By.xpath("//input[@id='address']")).sendKeys(faker.address().streetAddress());
	driver.findElement(By.xpath("//input[@id='zipcode']")).sendKeys(RandomStringUtils.randomNumeric(5));
	driver.findElement(By.xpath("//input[@id='age']")).sendKeys(RandomStringUtils.randomNumeric(2));
	driver.findElement(By.xpath("//input[@id='height']")).sendKeys(Integer.toString(faker.number().numberBetween(120, 200)));
	driver.findElement(By.xpath("//input[@id='weight']")).sendKeys(Integer.toString(faker.number().numberBetween(50, 100)));
	driver.findElement(By.xpath("//input[@id='pharmacy']")).sendKeys(faker.company().name());
	driver.findElement(By.xpath("//input[@id='pharma_adress']")).sendKeys(faker.address().streetAddress());
	driver.findElement(By.xpath("//input[@id='email']")).sendKeys(faker.internet().emailAddress());
	driver.findElement(By.xpath("//input[@id='password']")).sendKeys(pwd);
	driver.findElement(By.xpath("//input[@id='username']")).sendKeys(fname+"."+lname);
	driver.findElement(By.xpath("//input[@id='confirmpassword']")).sendKeys(pwd);
	WebElement question=driver.findElement(By.xpath("//select[@id='security']"));
	question.click(); Select s=new Select(question);
	s.selectByContainsVisibleText("pet");
	driver.findElement(By.xpath("//input[@id='answer']")).sendKeys(faker.animal().name());
	driver.findElement(By.xpath("//input[@name='register']")).click();
	Thread.sleep(3000);
	handlealert();
	
		}
		catch(Exception e)
		{
			registration();
		}
	}

	public static String handlealert() {
		Alert alert=driver.switchTo().alert();
		String alertText=alert.getText();
		alert.accept();
		System.out.println(alertText);
		return alertText;
	}
	
	public static void patientAccepted(String ssn) throws InterruptedException
	{
		driver.get("http://85.209.95.122/MMP-Release2-Admin-Build.2.1.000/login.php");
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
		driver.findElement(By.xpath("//input[@id='username']")).sendKeys("Ben@123");
		driver.findElement(By.xpath("//input[@id='password']")).sendKeys("Frank@123");
		driver.findElement(By.xpath("//input[@name='admin']")).click();

		driver.findElement(By.xpath("//span[normalize-space()='Users']")).click();
		
		driver.findElement(By.xpath("//td[normalize-space()='"+ssn+"']/preceding-sibling::*//a")).click();
		
		WebElement approval=driver.findElement(By.xpath("//select[@id='sapproval']"));
		approval.click();
		Select list=new Select(approval);
		list.selectByVisibleText("Accepted");
		driver.findElement(By.xpath("//input[@value='Submit']")).click();
		Thread.sleep(1000);
		handlealert();
		//driver.switchTo().alert().accept();
	}
	public static void login(String username, String pwd) throws InterruptedException
	{
	
	driver.get("http://85.209.95.122/MMP-Release2-Integrated-Build.6.8.000/portal/login.php");
	driver.manage().window().maximize();
	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			
	driver.findElement(By.id("username")).sendKeys(username);
	driver.findElement(By.id("password")).sendKeys(pwd);
	driver.findElement(By.name("submit")).click();
	
	driver.findElement(By.xpath("//span[normalize-space()='Profile']")).click();
	String actfname=driver.findElement(By.xpath("//input[@id='fname']")).getDomAttribute("value");
	String actlname=driver.findElement(By.xpath("//input[@id='lname']")).getDomAttribute("value");
	String actssn=driver.findElement(By.xpath("//input[@id='ssn']")).getDomAttribute("value");

	actual.put("First Name", actfname);
	actual.put("Last Name", actlname);
	actual.put("SSN", actssn);
	
	}
}

