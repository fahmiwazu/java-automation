package experimental;

import base.BaseTests;
import config.TestConfig;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.openqa.selenium.Dimension;
import utils.ScreenshotHandler;

import java.io.File;
import java.io.IOException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PortfolioTest extends BaseTests {
    private ScreenshotHandler screenshot;

    @BeforeAll
    public void setUp(){
        screenshot = new ScreenshotHandler(driver, this.getClass().getSimpleName());
    }

    @Test
    public void fahmiDocs(){
        Allure.step("Navigate URL ", ()->{
            driver.manage().window().setSize(new Dimension(1920, 1080));
            driver.get(TestConfig.BASE_URL_DEV);
            screenshot.attachScreenshotToAllure("Fahmi's Portfolio");
        });

    }

    @AfterAll
    public void reporting(){
        String os = System.getProperty("os.name").toLowerCase();

        if (!os.contains("win")) {
            System.out.println("Skipping Allure report generation on non-Windows OS.");
        } else {
            generateAllureReport();
        }
    }

    static void generateAllureReport() {
        try {
            // Create ProcessBuilder for PowerShell / CMD
            int exitCode = getCode();

            if (exitCode == 0) {
                System.out.println("✅ Allure report generated successfully.");
            } else {
                System.err.println("❌ Failed to generate Allure report. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static int getCode() throws IOException, InterruptedException {
        ProcessBuilder processBuilder = new ProcessBuilder(
                "cmd.exe", "/c",
                TestConfig.GENERATE_ALLURE_HTML_REPORT
        );

        // ✅ Set working directory to your project path
        processBuilder.directory(new File(TestConfig.PROJECT_DIR));

        processBuilder.inheritIO(); // so logs appear in console
        Process process = processBuilder.start();
        return process.waitFor();
    }

}
