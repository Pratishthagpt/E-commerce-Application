package dev.pratishtha.project.paymentService.controllers;

import dev.pratishtha.project.paymentService.dtos.PaymentLinkRequestDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentLinkResponseDTO;
import dev.pratishtha.project.paymentService.dtos.PaymentStatusDto;
import dev.pratishtha.project.paymentService.exceptions.ExceptionDTO;
import dev.pratishtha.project.paymentService.services.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Payment processing API.")
@RestController
@RequestMapping("/api/payments")
public class PaymentController {

    private PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @Operation(summary = "API for generating payment link from payment gateway for order with input order-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Generate payment link from payment gateway for order with input order-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentLinkResponseDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @PostMapping
//    first initiating payment, which will return us the payment link
    public ResponseEntity<PaymentLinkResponseDTO> generatingPaymentLink (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @RequestBody PaymentLinkRequestDTO paymentLinkRequestDTO
    ) {
        PaymentLinkResponseDTO paymentLinkResponseDTO = paymentService.generatingPaymentLink(token, paymentLinkRequestDTO);

        return new ResponseEntity<>(paymentLinkResponseDTO, HttpStatus.CREATED);
    }

    @Operation(summary = "API for get payment status from payment gateway for order using payment link-id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Get payment status from payment gateway for order using payment link-id.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = PaymentLinkResponseDTO.class))
                    }),
            @ApiResponse(responseCode = "401", description = "Invalid token input. Unauthorized user access.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "404", description = "Payment link not found.",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    }),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class))
                    })
    })
    @GetMapping("/{id}")
//    first initiating payment, which will return us the payment link
    public ResponseEntity<PaymentStatusDto> getPaymentStatus (
            @RequestHeader (HttpHeaders.AUTHORIZATION) String token,
            @PathVariable("id") String paymentId,
            @RequestBody PaymentLinkRequestDTO paymentLinkRequestDTO
    ) {
        PaymentStatusDto paymentStatusDto = paymentService.getPaymentStatus(token, paymentLinkRequestDTO, paymentId);

        return new ResponseEntity<>(paymentStatusDto, HttpStatus.OK);
    }
}
