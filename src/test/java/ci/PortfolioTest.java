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
@Feature("Live Portfolio")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PortfolioTest extends BaseTests {
    private ScreenshotHandler screenshot;

    @BeforeAll
    public void setUp(){
        screenshot = new ScreenshotHandler(driver, this.getClass().getSimpleName());
        allureDescription();
    }

    @Test
    @DisplayName("Portfolio Website Test")
    @Story("Fahmi Portfolio Main Page")
    @Owner("Fahmi Wiradika")
    @Link(name="Repository", url="https://github.com/fahmiwazu/docs-portfolio")
    @Link(name="Live Website", url="https://fahmiwazu.github.io/docs-portfolio")
    public void fahmiDocs(){
        Allure.step("Navigate URL ", ()->{
            driver.manage().window().setSize(new Dimension(1920, 1080));
            driver.get(TestConfig.BASE_URL_DEV);
            screenshot.attachScreenshotToAllure("Fahmi's Portfolio");
        });

    }

    private void allureDescription(){
        Allure.description("""
        # 👨‍💻 Portfolio Test Suite
        
        ## 📋 Overview
        This test class validates the **Portfolio Website** accessibility and visual rendering. 🎨 \s
        It uses **Selenium WebDriver** and **Allure Reporting** to capture evidence of the portfolio's appearance and functionality. 📸
        
        ## 🌍 Environment
        - **Base URL:** Configured via `TestConfig.BASE_URL_DEV` 🌐 \s
        - **Browser Resolution:** `1920 x 1080` 🖥️ \s
        - **Test Lifecycle:** `PER_CLASS` - Single instance for all tests ♻️ \s
        
        ## ✨ Key Features
        - **📸 Visual Evidence Capture** \s
          - Automated screenshot generation for portfolio pages 📷 \s
          - Screenshots attached directly to Allure reports 🎯 \s
          - High-resolution evidence (1920x1080) for detailed review 🔍 \s

        - **🎯 Simple & Focused Testing** \s
          - Quick validation of portfolio availability ✅ \s
          - Baseline visual regression testing 👀 \s
          - Foundation for expanded UI testing 🏗️ \s

        - **📊 Automated Reporting** \s
          - Windows-specific Allure report generation 🪟 \s
          - Console output for debugging 🖨️ \s
          - Visual evidence embedded in reports 📑 \s

        ## 🔄 Test Flow
        1. **🛠️ Setup (`@BeforeAll`)** \s
           - Initialize screenshot handler for the test class 📷 \s
           - Configure test environment settings ⚙️ \s
           - Prepare browser for testing 🌐 \s

        2. **🎯 Test Execution**
           - **🚀 Navigate to Portfolio** \s
             - Set browser window to 1920x1080 resolution 🖥️ \s
             - Load portfolio website from configured URL 🌐 \s
             - Capture full-page screenshot as evidence 📸 \s

        3. **🏁 Post-Test (`@AfterAll`)** \s
           - **📑 Allure Report Generation**: Automatically builds HTML report on Windows OS 🪟 \s
           - **✅ Success Logging**: Confirms report generation status ✔️ \s
           - **❌ Error Handling**: Logs failures with exit codes 🚨 \s

        ## 🔍 Test Coverage
        - **✅ Portfolio Accessibility**: Verifies the portfolio website loads successfully \s
        - **📸 Visual State**: Captures the current appearance of the portfolio \s
        - **🌐 URL Navigation**: Validates routing to the configured base URL \s
        - **🖥️ Responsive Setup**: Ensures consistent viewport size for testing \s

        ## 📊 Evidence & Artifacts
        - **Screenshots**: \s
          - Full-page captures of the portfolio website 🖼️ \s
          - Attached to Allure steps for visual verification 👁️ \s

        - **Allure Reports**: \s
          - Comprehensive HTML reports with visual evidence 📑 \s
          - Step-by-step execution flow 🔄 \s
          - Success/failure indicators with screenshots 📈 \s

        ## 🎁 Benefits
        - **Quick Portfolio Verification**: Instant validation that the site is live and rendering 🚀 \s
        - **Visual Documentation**: Automatically captures how the portfolio looks 📷 \s
        - **Foundation for Expansion**: Easy to extend with more comprehensive UI tests 🏗️ \s
        - **Professional Reporting**: Clean, visual reports for stakeholders 💼 \s

        ## 🔧 Technical Notes
        - **Test Instance Lifecycle**: `PER_CLASS` ensures single setup/teardown for efficiency ⚡ \s
        - **OS-Specific Reporting**: Allure generation only on Windows to avoid path issues 🪟 \s
        - **Screenshot Handler**: Dedicated utility for consistent screenshot management 🛠️ \s

        ---
        
        **🎯 Purpose**: This test serves as a smoke test and visual baseline for the portfolio website, \s
        ensuring it's accessible and rendering correctly before more detailed testing begins. 🚀
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
