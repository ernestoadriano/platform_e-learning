package dev.elearing.platform.controller;

import dev.elearing.platform.dto.CertificateResponse;
import dev.elearing.platform.model.User;
import dev.elearing.platform.service.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/certificates")
@CrossOrigin(origins = "*")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @GetMapping("/me")
    public ResponseEntity<List<CertificateResponse>> getUserCertificates(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(certificateService.getUserCertificates(user.getId()));
    }

    @GetMapping("/courses/{courseId}/has")
    public ResponseEntity<Boolean> hasCertificate(@PathVariable Long courseId, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(certificateService.hasCertificate(user.getId(), courseId));
    }

    @PostMapping("/courses/{courseId}/generate")
    public ResponseEntity<CertificateResponse> generateCertificate(
            @PathVariable Long courseId,
            @AuthenticationPrincipal User user) {
        CertificateResponse certificate = certificateService.generateCertificate(user.getId(), courseId);
        return ResponseEntity.ok(certificate);
    }
}