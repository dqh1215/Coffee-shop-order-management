package com.architecture_solution.coffeeshop_order_management.controller;

import com.architecture_solution.coffeeshop_order_management.config.openapi.BadRequestResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.ConflictResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.CreatedResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.ForbiddenResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.NotFoundResponse;
import com.architecture_solution.coffeeshop_order_management.config.openapi.OkResponse;
import com.architecture_solution.coffeeshop_order_management.dto.common.ApiResponse;
import com.architecture_solution.coffeeshop_order_management.dto.common.PageResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CancelOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.request.CreateOrderRequest;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.BillResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderResponse;
import com.architecture_solution.coffeeshop_order_management.dto.order.response.OrderSummaryResponse;
import com.architecture_solution.coffeeshop_order_management.enums.OrderStatus;
import com.architecture_solution.coffeeshop_order_management.security.CustomUserDetails;
import com.architecture_solution.coffeeshop_order_management.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "Order", description = "Đặt món, xem đơn, huỷ và xác nhận đơn")
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@Slf4j
public class OrderController {
    private static final int MAX_PAGE_SIZE = 50;
    private final OrderService orderService;

    @Operation(summary = "Tạo đơn hàng",
            description = "Cần đăng nhập. Đơn mới có trạng thái PENDING; giá món và tổng tiền được lưu lại tại thời điểm đặt. "
                    + "400 nếu món tạm hết hoặc voucher hết hạn, 404 nếu món hoặc voucher không tồn tại.")
    @CreatedResponse
    @BadRequestResponse
    @NotFoundResponse
    @PostMapping
    public ResponseEntity<ApiResponse<OrderResponse>> create(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Valid @RequestBody CreateOrderRequest request) {
        String userId = principal.getUser().getId();
        log.info("order creates from : {}",  userId);
        OrderResponse response = orderService.createOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.created(response, "Order created"));
    }

    @Operation(summary = "Xem chi tiết đơn hàng",
            description = "Chủ đơn hoặc ADMIN. Đơn của người khác trả về 404.")
    @OkResponse
    @NotFoundResponse
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<OrderResponse>> getOrder(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getOrder(id, principal.getUser().getId(), principal.isAdmin())));
    }

    @Operation(summary = "Xem hoá đơn",
            description = "Chủ đơn hoặc ADMIN. Trả về các khoản tiền đã lưu khi tạo đơn, không tính lại.")
    @OkResponse
    @NotFoundResponse
    @GetMapping("/{id}/bill")
    public ResponseEntity<ApiResponse<BillResponse>> getBill(
            @PathVariable String id,
            @AuthenticationPrincipal CustomUserDetails principal) {
        return ResponseEntity.ok(ApiResponse.success(
                orderService.getBill(id, principal.getUser().getId(), principal.isAdmin())));
    }

    @Operation(summary = "Danh sách đơn hàng",
            description = "CUSTOMER chỉ thấy đơn của mình; ADMIN thấy tất cả và có thể lọc theo user_id. Sắp xếp mới nhất trước.")
    @OkResponse
    @BadRequestResponse
    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<OrderSummaryResponse>>>> getOrders(
            @AuthenticationPrincipal CustomUserDetails principal,
            @Parameter(description = "Lọc theo trạng thái")
            @RequestParam(required = false) OrderStatus status,
            @Parameter(description = "Lọc theo người đặt (chỉ có tác dụng với ADMIN)")
            @RequestParam(name = "user_id", required = false) String userId,
            @Parameter(description = "Từ ngày (yyyy-MM-dd)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @Parameter(description = "Đến ngày (yyyy-MM-dd), không được trước from")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @Parameter(description = "Số trang, bắt đầu từ 0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Số phần tử mỗi trang (1-50)")
            @RequestParam(defaultValue = "10") int size) {

        int safePage = Math.max(page, 0);
        int safeSize = Math.max(1, Math.min(size, MAX_PAGE_SIZE));
        Pageable pageable = PageRequest.of(safePage, safeSize, Sort.by(Sort.Direction.DESC, "createdAt"));

        return ResponseEntity.ok(ApiResponse.success(orderService.getOrders(
                principal.getUser().getId(), principal.isAdmin(), status, userId, from, to, pageable)));
    }

    @Operation(summary = "Huỷ đơn hàng",
            description = "Chủ đơn hoặc ADMIN. PENDING hoặc CONFIRMED -> CANCELLED; body (cancel_reason) không bắt buộc. "
                    + "409 nếu đơn đã bị huỷ.")
    @OkResponse
    @BadRequestResponse
    @NotFoundResponse
    @ConflictResponse
    @PostMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<OrderResponse>> cancelOrder(
            @PathVariable String id,
            @Valid @RequestBody(required = false) CancelOrderRequest request,
            @AuthenticationPrincipal CustomUserDetails principal) {
        OrderResponse response = orderService.cancelOrder(id, request,
                principal.getUser().getId(), principal.isAdmin());
        log.info("order cancelled : {}",  id);
        return ResponseEntity.ok(ApiResponse.success(response,"Order " + id + " cancelled"));
    }

    @Operation(summary = "Xác nhận đơn hàng", description = "ADMIN. PENDING -> CONFIRMED; 409 nếu đơn không ở trạng thái PENDING.")
    @OkResponse
    @ForbiddenResponse
    @NotFoundResponse
    @ConflictResponse
    @PostMapping("/{id}/confirm")
    public ResponseEntity<ApiResponse<OrderResponse>> confirmOrder(@PathVariable String id) {
        log.info("Confirming order: {}", id);
        return ResponseEntity.ok(ApiResponse.success(orderService.confirmOrder(id)));
    }
}