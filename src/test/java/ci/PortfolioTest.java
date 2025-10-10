package ci;

import base.BaseTests;
import config.TestConfig;
import io.qameta.allure.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.Dimension;
import utils.ScreenshotHandler;

import java.io.File;
import java.io.IOException;

@Epic("Portfolio Test")
//@Feature("Live Portfolio")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PortfolioTest extends BaseTests {
    private ScreenshotHandler screenshot;

    @BeforeAll
    public void setUp(){
        screenshot = new ScreenshotHandler(driver, this.getClass().getSimpleName());
//        allureDescription();
    }

    @Test
    @DisplayName("Portfolio Website Test")
    @Feature("Desktop View")
    @Story("Fahmi Portfolio Main Page")
    @Owner("Fahmi Wiradika")
    @Link(name="Repository", url="https://github.com/fahmiwazu/docs-portfolio")
    @Link(name="Live Website", url="https://fahmiwazu.github.io/docs-portfolio")
    public void fahmiDocsDesktop(){

        allureDescriptionDesktop();

        Allure.step("Navigate URL ", ()->{
            driver.manage().window().setSize(new Dimension(1920, 1080));
            driver.get(TestConfig.BASE_URL_DEV);
            screenshot.attachScreenshotToAllure("Fahmi's Portfolio");
        });

    }

    @Test
    @DisplayName("Portfolio Website Test")
    @Feature("Mobile View")
    @Story("Fahmi Portfolio Main Page")
    @Owner("Fahmi Wiradika")
    @Link(name="Repository", url="https://github.com/fahmiwazu/docs-portfolio")
    @Link(name="Live Website", url="https://fahmiwazu.github.io/docs-portfolio")
    public void fahmiDocsMobile(){

        allureDescriptionMobile();

        Allure.step("Navigate URL ", ()->{
            driver.manage().window().setSize(new Dimension(428, 926));
            driver.get(TestConfig.BASE_URL_DEV);
            screenshot.attachScreenshotToAllure("Fahmi's Portfolio");
        });

    }

    private void allureDescriptionDesktop(){
        Allure.description("""
        # 👨‍💻 Portfolio Test Suite — Desktop View

        ## 📋 Overview
        Validates the portfolio website on a desktop viewport, capturing evidence and basic availability.

        ## 🌍 Environment
        - **Base URL:** `TestConfig.BASE_URL_DEV`  
        - **Viewport:** 1920 x 1080  
        - **Lifecycle:** PER_CLASS

        ## 🔄 Flow
        1) Set window size to 1920x1080  
        2) Navigate to base URL  
        3) Attach full-page screenshot to Allure

        ## 📊 Artifacts
        - Desktop screenshots attached to Allure
        - HTML report generation on Windows
        """
        );
    }

    private void allureDescriptionMobile(){
        Allure.description("""
        # 📱 Portfolio Test Suite — Mobile View

        ## 📋 Overview
        Validates the portfolio website on a mobile-like viewport, focusing on rendering and evidence.

        ## 🌍 Environment
        - **Base URL:** `TestConfig.BASE_URL_DEV`  
        - **Viewport:** 428 x 926  
        - **Lifecycle:** PER_CLASS

        ## 🔄 Flow
        1) Set window size to 428x926  
        2) Navigate to base URL  
        3) Attach viewport screenshot to Allure

        ## 📊 Artifacts
        - Mobile screenshots attached to Allure
        - HTML report generation on Windows
        """
        );
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
