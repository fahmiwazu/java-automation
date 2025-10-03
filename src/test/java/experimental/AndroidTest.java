package experimental;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

public class AndroidTest {

    static AndroidDriver driver;

    public static void main(String[] args) throws MalformedURLException {
        openMobileApp();
    }

    public static void openMobileApp() throws MalformedURLException {

        DesiredCapabilities cap = getCapabilities();

        // Appium server URL
        URL url = new URL("http://127.0.0.1:4723");

        try {
            driver = new AndroidDriver(url, cap);

            // Set implicit wait
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

            System.out.println("✅ Application Started Successfully");
            System.out.println("📱 Device: " + driver.getCapabilities().getCapability("deviceName"));
            System.out.println("🤖 Platform: " + driver.getCapabilities().getCapability("platformName"));
            System.out.println("📦 App Package: " + driver.getCurrentPackage());
//            System.out.println("🎯 Current Activity: " + driver.getCurrentActivity());

        } catch (Exception e) {
            System.err.println("❌ Failed to start application: " + e.getMessage());
            System.err.println("\n🔧 Troubleshooting steps:");
            System.err.println("1. Make sure Grab app is installed on your device");
            System.err.println("2. Ensure the app can be launched manually");
            System.err.println("3. Check if the package name is correct: com.grabtaxi.passenger");
            System.err.println("4. Try launching the app manually first, then run the test");

            // Alternative: Find launcher activity manually
            System.err.println("\n🔍 To find the correct launcher activity, run:");
            System.err.println("adb shell dumpsys package com.grabtaxi.passenger | grep -A 5 -B 5 \"android.intent.action.MAIN\"");

            e.printStackTrace();
        }
    }

    private static DesiredCapabilities getCapabilities() {
        DesiredCapabilities cap = new DesiredCapabilities();

        // Device info
        cap.setCapability("platformName", "Android");
        cap.setCapability("appium:deviceName", "Saal");
        cap.setCapability("appium:udid", "zpt8jfo7fykf4dt8");
        cap.setCapability("appium:automationName", "uiAutomator2");
        cap.setCapability("appium:platformVersion", "10");

        // App info - Let Appium auto-detect the launcher activity
//        cap.setCapability("appium:appPackage", "com.grabtaxi.passenger");
        cap.setCapability("appium:appPackage", "com.grabtaxi.passenger");
        cap.setCapability("appium:appActivity", "com.grab.pax.newface.common.alias.DefaultLauncherAlias");
// replace with what adb shows you

        // DO NOT specify appActivity - let Appium find it automatically

        // FIX: Skip problematic device initialization
        cap.setCapability("appium:skipDeviceInitialization", true);
        cap.setCapability("appium:ignoreHiddenApiPolicyError", true);

        // Additional stability settings
        cap.setCapability("appium:newCommandTimeout", 300);
        cap.setCapability("appium:systemPort", 8201);
        cap.setCapability("appium:noReset", true); // Don't reset app state
        cap.setCapability("appium:fullReset", false); // Don't uninstall app

        // Auto-launch settings
        cap.setCapability("appium:autoLaunch", true); // Ensure app launches
        cap.setCapability("appium:autoGrantPermissions", true); // Auto-grant permissions
        return cap;
    }

    // Helper method to find launcher activity
    public static void findLauncherActivity() {
        System.out.println("🔍 Run these commands to find the correct launcher activity:");
        System.out.println("adb shell pm dump com.grabtaxi.passenger | grep -A 5 -B 5 MAIN");
        System.out.println("adb shell cmd package resolve-activity --brief com.grabtaxi.passenger | tail -n 1");
    }

    // Add cleanup method
    public static void closeApp() {
        if (driver != null) {
            driver.quit();
            System.out.println("🔚 Application Closed");
        }
    }
}