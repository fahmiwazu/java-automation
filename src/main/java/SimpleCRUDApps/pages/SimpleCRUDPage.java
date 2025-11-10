package SimpleCRUDApps.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import utils.SafeAction;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SimpleCRUDPage {
    private final WebDriver driver;
    private final SafeAction safeAction;

    //Locator
    public static final By productName = By.cssSelector("#productName");
    public static final By productPrice = By.cssSelector("#productPrice");
    public static final By productQuantity = By.cssSelector("#productQuantity");
    public static final By addProduct = By.cssSelector("button.btn.btn-primary");
    public static final By productNotification = By.cssSelector(".notification.success");
    public static final By productList = By.cssSelector(".product-item");

    public static final By updateName = By.cssSelector("#updateName");
    public static final By updatePrice = By.cssSelector("#updatePrice");
    public static final By updateQuantity = By.cssSelector("#updateQuantity");

    public static final By confirmUpdate = By.cssSelector("div[class='modal-buttons'] button[type='submit']");
    public static final By cancelUpdate = By.cssSelector("button.btn.btn-cancel");

    public static final By confirmDelete = By.cssSelector("button[onclick='confirmDelete()']");
    public static final By cancelDelete = By.cssSelector("button[onclick='closeDeleteModal()']");

    public static final By loadingProduct = By.cssSelector(".loading");

    public SimpleCRUDPage(WebDriver driver) {
        this.driver = driver;
        this.safeAction = new SafeAction(driver);
    }

    public void waitUpdateName(){
        safeAction.waitForElementToBeClickable(updateName);
    }

    public void clickCancelDelete(){
        safeAction.safeClick(cancelDelete);
    }

    public void clickConfirmDelete(){
        safeAction.safeClick(confirmDelete);
    }

    public void clickCancelUpdate(){
        safeAction.safeClick(cancelUpdate);
    }

    public void clickConfirmUpdate(){
        safeAction.safeClick(confirmUpdate);
    }

    public void setUpdateQuantity(String quantity){
        safeAction.safeInput(updateQuantity, quantity);
    }

    public void setUpdatePrice(String price){
        safeAction.safeInput(updatePrice, price);
    }

    public void setUpdateName(String product){
        safeAction.safeInput(updateName, product);
    }

    public void setProductName(String product){
        safeAction.safeInput(productName, product);
    }

    public void setProductPrice(String price){
        safeAction.safeInput(productPrice, price);
    }

    public void setProductQuantity(String quantity){
        safeAction.safeInput(productQuantity, quantity);
    }

    public void clickAddProduct(){
        safeAction.safeClick(addProduct);
    }

    public void waitLoadProduct(){
        safeAction.waitForElementToDisappear(loadingProduct);
    }

    public String addProductAndGetId(String name, String price, String quantity) {
        // Fill in the form
        setProductName(name);
        setProductPrice(price);
        setProductQuantity(quantity);

        // Submit the form
        clickAddProduct();

        // Wait for and extract product ID from notification
        return extractProductIdFromNotification();
    }

    public String extractProductIdFromNotification() {
        try {
            // Wait for notification to appear
            WebElement notification = safeAction.waitForElementToBeVisible(productNotification);

            // Get notification text
            String notificationText = notification.getText();
            System.out.println("Notification: " + notificationText);

            // Extract product ID
            Pattern pattern = Pattern.compile("Product ID: ([a-f0-9]{24}) ");
            Matcher matcher = pattern.matcher(notificationText);

            if (matcher.find()) {
                String productId = matcher.group(1);
                System.out.println("Extracted Product ID: " + productId);
                return productId;
            }

            throw new RuntimeException("Could not extract Product ID from notification");

        } catch (Exception e) {
            System.err.println("Error extracting product ID: " + e.getMessage());
            throw e;
        }
    }

    public void clickUpdateButtonByProductId(String productId) {
        // XPath that looks for button with exact product ID in onclick attribute
        String xpath = String.format(
                "//button[@class='btn btn-update' and contains(@onclick, 'openUpdateModal(\"%s\"')]",
                productId
        );

        safeAction.waitForElementToBeClickable(By.xpath(xpath));

        System.out.println("Clicking update button for Product ID: " + productId);
        safeAction.safeClick(By.xpath(xpath));
    }

    public void clickDeleteButtonByProductId(String productId) {
        String xpath = String.format(
                "//button[contains(@onclick, 'openDeleteModal(\"%s\")')]",
                productId
        );

        safeAction.waitForElementToBeClickable(By.xpath(xpath));

        System.out.println("Clicking delete button for Product ID: " + productId);

        safeAction.safeClick(By.xpath(xpath));
    }

    public void waitForNotificationToDisappear() {
        safeAction.waitForElementToDisappear(productNotification);
    }

    public String productItemXpathLocator(String product){
        return String.format(
                "//div[@class='product-item'][.//span[@class='product-id' and text()='%s']]",
                product
        );
    }

    /**
     * Extract clean product name from a specific product ID
     * @param productId The product ID to search for
     * @return Clean product name (e.g., "Kopi")
     */
    public String extractProductNameById(String productId) {
        String xpath = String.format(
                "//span[@class='product-id' and text()='%s']/parent::div/span[@class='product-name']",
                productId
        );

        WebElement nameElement = safeAction.waitForElementToBeVisible(By.xpath(xpath));
        String name = nameElement.getText().trim();

        System.out.println("Extracted Product Name: " + name);
        return name;
    }

    /**
     * Extract clean price value from a specific product ID
     * Converts "IDR 28.000,00" to "28000"
     * @param productId The product ID to search for
     * @return Clean price value as string (e.g., "28000")
     */
    public String extractProductPriceById(String productId) {
        String xpath = String.format(
                "//span[@class='product-id' and text()='%s']/parent::div/span[@class='product-price']",
                productId
        );

        WebElement priceElement = safeAction.waitForElementToBeVisible(By.xpath(xpath));
        String priceText = priceElement.getText(); // e.g., "IDR 28.000,00"

        // Remove "IDR ", thousand separators (.), and decimal part
        String cleanPrice = priceText
                .replace("IDR", "")
                .replace(".", "")
                .replaceAll(",\\d+$", "") // Remove decimal part (,00)
                .trim();

        System.out.println("Extracted Price: " + priceText + " -> " + cleanPrice);
        return cleanPrice;
    }

    /**
     * Extract clean quantity value from a specific product ID
     * Converts "Qty: 2" to "2"
     * @param productId The product ID to search for
     * @return Clean quantity value as string (e.g., "2")
     */
    public String extractProductQuantityById(String productId) {
        String xpath = String.format(
                "//span[@class='product-id' and text()='%s']/parent::div/span[@class='product-quantity']",
                productId
        );

        WebElement quantityElement = safeAction.waitForElementToBeVisible(By.xpath(xpath));
        String quantityText = quantityElement.getText(); // e.g., "Qty: 2"

        // Remove "Qty: " prefix
        String cleanQuantity = quantityText
                .replace("Qty:", "")
                .trim();

        System.out.println("Extracted Quantity: " + quantityText + " -> " + cleanQuantity);
        return cleanQuantity;
    }

    /**
     * Extract all product data (name, price, quantity) for a specific product ID
     * Returns clean values ready for comparison or reuse
     * @param productId The product ID to search for
     * @return Map containing "name", "price", and "quantity" keys with clean values
     */
    public Map<String, String> extractProductDataById(String productId) {
        Map<String, String> productData = new HashMap<>();

        productData.put("name", extractProductNameById(productId));
        productData.put("price", extractProductPriceById(productId));
        productData.put("quantity", extractProductQuantityById(productId));

        System.out.println("Extracted Product Data for ID " + productId + ": " + productData);
        return productData;
    }

    /**
     * Extract clean price as double value
     * @param productId The product ID to search for
     * @return Price as double (e.g., 28000.0)
     */
    public double extractProductPriceAsDouble(String productId) {
        String priceStr = extractProductPriceById(productId);
        return Double.parseDouble(priceStr);
    }

    /**
     * Extract clean quantity as integer value
     * @param productId The product ID to search for
     * @return Quantity as integer (e.g., 2)
     */
    public int extractProductQuantityAsInt(String productId) {
        String quantityStr = extractProductQuantityById(productId);
        return Integer.parseInt(quantityStr);
    }

    /**
     * Verify if product exists in the list
     * @param productId The product ID to check
     * @return true if product exists, false otherwise
     */
    public boolean isProductExists(String productId) {
        try {
            String xpath = String.format("//span[@class='product-id' and text()='%s']", productId);
            driver.findElement(By.xpath(xpath));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Verify if product is deleted (no longer in the list)
     * @param productId The product ID to check
     * @return true if product is deleted, false if still exists
     */
    public boolean isProductDeleted(String productId) {
        return !isProductExists(productId);
    }

}