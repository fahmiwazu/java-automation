package SimpleCRUDApps.api;

import config.ConfigLoader;

public class productProperties {
    public static final String DEV = ConfigLoader.get("BASE_URL_SIMPLE_CRUD_DEV");
    public static final String PROD = ConfigLoader.get("BASE_URL_SIMPLE_CRUD_PROD");

    public static final String productAPI = String.format("%s%s/",PROD, baseEndPoint.productService);
}
