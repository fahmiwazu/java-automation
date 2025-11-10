package SimpleCRUDApps.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

    // Get Product
    @JsonProperty("_id")
    private String id;
    private String name;
    private Integer quantity;
    private Double price;
    private String createdAt;
    private String updatedAt;
    @JsonProperty("__v")
    private Integer version;

    // Delete Product
    private String message;

}