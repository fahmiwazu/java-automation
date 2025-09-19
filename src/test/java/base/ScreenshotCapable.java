package base;

import utils.ScreenshotHandler;

/**
 * Interface for test classes that support screenshot capture
 * Provides a clean contract for screenshot functionality
 */
public interface ScreenshotCapable {

    /**
     * Get the screenshot handler for this test instance
     * @return ScreenshotHandler instance or null if not available
     */
    ScreenshotHandler getScreenshotHandler();

    /**
     * Check if screenshot capability is currently available
     * @return true if screenshots can be taken
     */
    default boolean isScreenshotAvailable() {
        return getScreenshotHandler() != null;
    }

    /**
     * Take a screenshot with a custom name
     * @param name screenshot name
     * @return path to saved screenshot
     */
    default String takeScreenshot(String name) {
        ScreenshotHandler handler = getScreenshotHandler();
        if (handler != null) {
            try {
                return handler.takeFullPageScreenshot(name);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Screenshot interrupted", e);
            }
        }
        throw new IllegalStateException("Screenshot handler not available");
    }
}