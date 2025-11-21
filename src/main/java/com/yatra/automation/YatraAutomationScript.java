package com.yatra.automation;

import java.time.Duration;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.Test;

public class YatraAutomationScript {

	@Test
	public void launchBrowser() throws InterruptedException{
		//Disabling the notification popup
		ChromeOptions options =new ChromeOptions();
		options.addArguments("--disable-notifications");
		//Launching the browser
		WebDriver driver=new ChromeDriver(options);
		driver.get("https://www.yatra.com/");
		driver.manage().window().maximize();
		WebDriverWait wait=new WebDriverWait(driver, Duration.ofSeconds(20));
		//handling the intermittent popup
		handlePopUp(wait);
		//Selecting depature date
		clickOnDepatureDate(wait);
		//Selecting current month calendar
		WebElement currentmonth=selectTheMonthFromTheCalendar(wait,0); 
		//Selecting next month calendar
		WebElement nextmonth=selectTheMonthFromTheCalendar(wait,1); 
		
		Thread.sleep(2000);
		
		String currentmonthprice=getMeLowestPrice(currentmonth);
		String nextmonthprice=getMeLowestPrice(nextmonth);
		System.out.println(currentmonthprice);
		System.out.println(nextmonthprice);
		
		int lowestpriceoftwo=lowestOfTwoMonths(currentmonthprice, nextmonthprice);
		
		System.out.println("Lowest Price out of current and next month is : Rs "+lowestpriceoftwo);
		
		driver.quit();
		
	}

	public void clickOnDepatureDate(WebDriverWait wait) {
		By calendarLocator=By.xpath("//div[@aria-label='Departure Date inputbox' and @role='button']");
		
		wait.until(ExpectedConditions.elementToBeClickable(calendarLocator)).click();
	}

	public void handlePopUp(WebDriverWait wait) {
		By popupLocator=By.xpath("(//div[contains(@class,'style_popup')])[1]");
		
		try {
			WebElement popup=wait.until(ExpectedConditions.visibilityOfElementLocated(popupLocator));
			WebElement popupcross=popup.findElement(By.xpath("(.//img[@alt='cross'])[1]"));
			popupcross.click();
			
		} catch (TimeoutException e) {
			System.out.println("Popup did not appear");
		}
	}
	
	public int lowestOfTwoMonths(String currentmonthprice, String nextmonthprice) {
		//Choose Thursday, November 27th, 2025 is : 5648
		//Choose Monday, December 8th, 2025 is : 4980
		int lowestpriceint = 0;
		
		int indexofcharcurrent=currentmonthprice.indexOf("Rs");
		String currentpricestring=currentmonthprice.substring(indexofcharcurrent+2);
		int indexofcharnext=nextmonthprice.indexOf("Rs");
		String nextpricestring=nextmonthprice.substring(indexofcharnext+2);
		
		int currentmonthpriceint = Integer.parseInt(currentpricestring);
		int nextmonthpriceint = Integer.parseInt(nextpricestring);
		if(currentmonthpriceint<nextmonthpriceint) {
			lowestpriceint+=currentmonthpriceint;
		}
		else if(currentmonthpriceint==nextmonthpriceint)
		{
			lowestpriceint+=currentmonthpriceint;
		}
		else
		{
			lowestpriceint+=nextmonthpriceint;
		}
		
		return lowestpriceint;
		
	}

	
	private void elseif(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public String getMeLowestPrice(WebElement currentmonth) {
		By priceLocator=By.xpath(".//span[contains(@class,'custom-day-content ')]");
		
		List<WebElement> pricelist=currentmonth.findElements(priceLocator);
		WebElement priceElement=null;
		int lowestint=Integer.MAX_VALUE;
		for(WebElement prices:pricelist) {
			String price=prices.getText();
			if(price.length()>0) {
			price=price.replace("₹", "").replace(",", "");
			int priceint=Integer.parseInt(price);
			if(priceint<lowestint) {
				lowestint=priceint;
				priceElement=prices;
			}
			}
		}
		WebElement pricedate=priceElement.findElement(By.xpath(".//..//.."));
		String result=pricedate.getAttribute("aria-label")+ " price is Rs"+lowestint;
		return result;
	}

	
	private WebElement selectTheMonthFromTheCalendar(WebDriverWait wait, int index) {
		
		By datePickerLocator=By.xpath("//div[@class='react-datepicker__month-container']");
		
		List<WebElement> dataPickerList=wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(datePickerLocator));
		
		WebElement month=dataPickerList.get(index);
		
		return month;
		
		
	}
	
}
