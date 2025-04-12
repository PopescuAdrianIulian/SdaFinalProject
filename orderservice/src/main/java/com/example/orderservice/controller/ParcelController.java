package com.example.orderservice.controller;

import com.example.orderservice.request.parcel.ParcelRequest;
import com.example.orderservice.response.parcel.ParcelResponse;
import com.example.orderservice.service.ParcelLabelService;
import com.example.orderservice.service.ParcelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/parcel")
@RequiredArgsConstructor
public class ParcelController {

    private final ParcelService parcelService;
    private final ParcelLabelService parcelLabelService;

    @PostMapping("/createParcel")
    public ResponseEntity<ParcelResponse> createParcel(@Valid @RequestBody ParcelRequest parcelRequest) {
        return ResponseEntity.ok(parcelService.createParcel(parcelRequest));
    }

    @GetMapping("/{awb}")
    public ResponseEntity<ParcelResponse> getParcelStatusByAwb(@Valid @PathVariable String awb) {
        return ResponseEntity.ok(parcelService.getParcelByAwb(awb));
    }

    @GetMapping("/allParcels/{email}")
    public ResponseEntity<List<ParcelResponse>> getAllParcelsByUser(@Valid @PathVariable String email) {
        return ResponseEntity.ok(parcelService.getAllParcelsByUser(email));
    }

    @GetMapping("/label/{awb}")
    public ResponseEntity<byte[]> getParcelLabel(@PathVariable String awb) throws Exception {
        ParcelResponse parcel = parcelService.getParcelByAwb(awb);
        byte[] pdfBytes = parcelLabelService.generateLabel(parcel);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment()
                .filename("parcel-label-" + awb + ".pdf")
                .build());

        return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
    }


}
