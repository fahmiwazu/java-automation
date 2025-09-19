package config;

public class TestConfig {
    public static final String PROJECT_DIR = ConfigLoader.get("PROJECT_DIR");
    public static final String GENERATE_ALLURE_HTML_REPORT = ConfigLoader.get("GENERATE_ALLURE_HTML_REPORT");
    public static final String BASE_URL_DEV = ConfigLoader.get("BASE_URL_DEV");
    public static final String BASE_URL_LOC = ConfigLoader.get("BASE_URL_LOC");
    public static final String BASE_URL_STG = ConfigLoader.get("BASE_URL_STG");
    public static final String BASE_URL_PRD = ConfigLoader.get("BASE_URL_PRD");
}
