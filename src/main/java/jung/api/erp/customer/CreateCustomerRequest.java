package jung.api.erp.customer;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CreateCustomerRequest(
        @Schema(description = "고객명")
        @NotBlank
        String customerName
) {
}
