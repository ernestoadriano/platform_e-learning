package dev.elearning.certificate.controller;

import dev.elearning.certificate.dto.request.CertificateRequest;
import dev.elearning.certificate.dto.response.CertificateResponse;
import dev.elearning.certificate.dto.response.VerificationResponse;
import dev.elearning.certificate.service.CertificateService;
import dev.elearning.certificate.service.VerificationService;
import org.springframework.data.domain.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/certicates")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private VerificationService verificationService;

    @PostMapping
    public ResponseEntity<CertificateResponse> generateCertificate(@RequestBody CertificateRequest request) {
        CertificateResponse response = certificateService.generateCertificate(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CertificateResponse> getCertificateById(@PathVariable("id") Long id) {
        CertificateResponse response = certificateService.getCertificateById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/code/{code}")
    public ResponseEntity<CertificateResponse> getCertificateByCode(@PathVariable("code") String code) {
        CertificateResponse response = certificateService.getCertificateByCode(code);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/paginated")
    public ResponseEntity<Page<CertificateResponse>> getCertificatesByUserIdPaginated(
            @PathVariable("userId") Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10" ) int size,
            @RequestParam(defaultValue = "issuedAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction
    ) {
        Sort.Direction sortDirection = direction.equalsIgnoreCase("esc") ? Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        Page<CertificateResponse> responses = certificateService.getCertificateByUserIdPaginated(userId, pageable);

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/users/{userId}/courses/{courseId}/exists")
    public ResponseEntity<Boolean> hasCertificate(
            @PathVariable("userId") Long userId,
            @PathVariable("courseId") Long courseId
    ) {
        boolean hasCert = certificateService.hasCertificate(userId, courseId);
        return ResponseEntity.ok(hasCert);
    }

    @GetMapping("/users/{userId}/courses/{courseId}")
    public ResponseEntity<CertificateResponse> getUserCertificateForCourse(@PathVariable("userId") Long userId,
                                                                           @PathVariable("courseId") Long courseId) {
        CertificateResponse response = certificateService.getUserCertificateForCourse(userId, courseId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/users/{userId}/count")
    public ResponseEntity<Long> countCertificatesByUserId(@PathVariable("userId") Long userId) {
        long count = certificateService.countCertificatesByUserId(userId);
        return ResponseEntity.ok(count);
    }

    @PostMapping("/{id}/download")
    public ResponseEntity<Void> incrementDownloadCount(@PathVariable("id") Long id) {
        certificateService.incrementDownloadCount(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/verify/{code}")
    public ResponseEntity<VerificationResponse> verifyCertificate(@PathVariable("code") String code) {
        VerificationResponse response = verificationService.verifyCertificate(code);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Long id) {
        certificateService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteCertificatesByUserId(@PathVariable("userId") Long userId) {
        certificateService.deleteCertificatesByUserId(userId);
        return ResponseEntity.noContent().build();
    }
}
