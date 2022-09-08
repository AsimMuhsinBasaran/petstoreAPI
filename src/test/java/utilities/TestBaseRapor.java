package utilities;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class TestBaseRapor {
    protected static ExtentReports extentReports;
    //First assignment
    protected static ExtentTest extentTest;
    // Records information about tests
    protected static ExtentHtmlReporter extentHtmlReporter;
    // Creates an HTML report

    @BeforeTest(alwaysRun = true)
    public void setUpTest() {
        extentReports = new ExtentReports();
        String date = new SimpleDateFormat("yyyy.MM.dd__hh.mm.ss").format(new Date());
        String filePath = System.getProperty("user.dir") + "/target/APIReport/report"+date+".html";
        extentHtmlReporter = new ExtentHtmlReporter(filePath);
        extentReports.attachReporter(extentHtmlReporter);

        // Additional information
        extentReports.setSystemInfo("Environment","Test");
        //extentReports.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
        extentReports.setSystemInfo("Automation Engineer", "Asim");
        extentHtmlReporter.config().setDocumentTitle("Report");
        extentHtmlReporter.config().setReportName("TestNG API Report");
    }
    @AfterTest(alwaysRun = true)
    public void tearDownTest() {
        extentReports.flush();
    }
}


