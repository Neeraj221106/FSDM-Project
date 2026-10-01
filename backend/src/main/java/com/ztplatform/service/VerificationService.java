package com.ztplatform.service;

import com.ztplatform.dto.VerificationResponseDTO;
import com.ztplatform.model.Credential;
import com.ztplatform.repository.CredentialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class VerificationService {

    private final CredentialRepository credentialRepository;
    private final DigitalSignatureService digitalSignatureService;

    public VerificationResponseDTO verifyCredential(String credentialId) {

        Credential credential = credentialRepository
                .findByCredentialId(credentialId)
                .orElse(null);

        // Credential not found
        if (credential == null) {
            throw new RuntimeException("Credential not found");
        }

        // Return the actual credential status from database
        return buildResponse(credential, credential.getStatus().name());
    }

    private VerificationResponseDTO buildResponse(
            Credential credential,
            String status
    ) {

        String studentName = credential.getDocument().getStudent().getUser() != null
                ? credential.getDocument().getStudent().getUser().getName()
                : "Unknown";

        return new VerificationResponseDTO(
                credential.getCredentialId(),
                credential.getDocument()
                        .getStudent()
                        .getStudentId(),
                studentName,
                credential.getDocument()
                        .getDocumentType()
                        .name(),
                credential.getDocument().getTitle(),
                status,
                credential.getIssuedAt(),
                credential.getExpiresAt(),
                credential.getDocument().getFileUrl(),
                "Zero-Trust Platform"
        );
    }
}