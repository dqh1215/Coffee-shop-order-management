package com.architecture_solution.coffeeshop_order_management.config;

import com.architecture_solution.coffeeshop_order_management.dto.common.ErrorResponse;
import com.architecture_solution.coffeeshop_order_management.exception.ErrorCode;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.LinkedHashMap;
import java.util.Map;

/*
 * Swagger UI: /swagger-ui.html, OpenAPI JSON: /v3/api-docs.
 * Mọi endpoint mặc định yêu cầu JWT; endpoint public khai báo @SecurityRequirements() để bỏ yêu cầu này.
 * Response lỗi dùng chung khai báo ở components; controller gắn bằng annotation trong config.openapi
 * (@NotFoundResponse, @ConflictResponse, ...). Response 401 được tự thêm cho mọi endpoint cần JWT.
 */
@Configuration
public class OpenApiConfig {
    private static final String BEARER_AUTH = "bearerAuth";

    @Bean
    public OpenAPI openAPI() {
        Components components = new Components()
                .addSecuritySchemes(BEARER_AUTH, new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .description("Access token lấy từ POST /api/auth/login"));

        ModelConverters.getInstance().read(ErrorResponse.class).forEach(components::addSchemas);
        components
                .addResponses("BadRequest", errorResponse("Dữ liệu không hợp lệ",
                        ErrorCode.VALIDATION_FAILED, "items: must not be empty"))
                .addResponses("Unauthorized", errorResponse("Chưa đăng nhập hoặc token không hợp lệ",
                        ErrorCode.UNAUTHENTICATED, ErrorCode.UNAUTHENTICATED.getMessage()))
                .addResponses("Forbidden", errorResponse("Không có quyền thực hiện",
                        ErrorCode.ACCESS_DENIED, ErrorCode.ACCESS_DENIED.getMessage()))
                .addResponses("NotFound", errorResponse("Không tìm thấy tài nguyên",
                        ErrorCode.ORDER_NOT_FOUND, ErrorCode.ORDER_NOT_FOUND.getMessage()))
                .addResponses("Conflict", errorResponse("Xung đột dữ liệu hoặc chuyển trạng thái không hợp lệ",
                        ErrorCode.INVALID_ORDER_STATUS, ErrorCode.INVALID_ORDER_STATUS.getMessage()));

        return new OpenAPI()
                .info(new Info()
                        .title("Coffee Shop Order Management API")
                        .version("v1")
                        .description("REST API đặt món cho quán cà phê. Mọi response thành công được bọc trong "
                                + "ApiResponse (status_code, message, data); lỗi trả về ErrorResponse."))
                .components(components)
                .addSecurityItem(new SecurityRequirement().addList(BEARER_AUTH));
    }

    @Bean
    public OpenApiCustomizer unauthorizedResponseCustomizer() {
        // security == null nghĩa là operation dùng yêu cầu JWT mặc định; endpoint public có security rỗng.
        return openApi -> openApi.getPaths().values().forEach(path -> path.readOperations().forEach(operation -> {
            if (operation.getSecurity() == null) {
                operation.getResponses().addApiResponse("401",
                        new ApiResponse().$ref("#/components/responses/Unauthorized"));
            }
        }));
    }

    // Ví dụ lấy từ ErrorCode để khớp với body mà GlobalExceptionHandler trả về thật.
    private ApiResponse errorResponse(String description, ErrorCode errorCode, String message) {
        Schema<?> schema = new Schema<>().$ref("#/components/schemas/ErrorResponse");
        Map<String, Object> example = new LinkedHashMap<>();
        example.put("status_code", errorCode.getStatus().value());
        example.put("code", errorCode.getCode());
        example.put("error", errorCode.getStatus().getReasonPhrase());
        example.put("message", message);
        example.put("path", "/api/orders/3f2a1c9e-8b7d-4e6f-9a1b-2c3d4e5f6a7b");
        example.put("timestamp", "2026-01-01T08:00:00Z");
        return new ApiResponse()
                .description(description)
                .content(new Content().addMediaType("application/json",
                        new MediaType().schema(schema).example(example)));
    }
}
