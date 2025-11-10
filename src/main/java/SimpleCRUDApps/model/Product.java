package SimpleCRUDApps.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record Product(
        String _id,
        String name,
        Integer quantity,
        Integer price,
        String image,
        String createdAt,
        String updatedAt,
        Integer __v
) {
    // Constructor for creating new product (without id)
    public Product(String name, Integer quantity, Integer price) {
        this(null, name, quantity, price, null, null, null, null);
    }
}