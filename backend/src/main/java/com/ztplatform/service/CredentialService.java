package com.ztplatform.service;

import com.ztplatform.dto.CredentialDTO;
import com.ztplatform.model.Credential;
import com.ztplatform.model.Document;
import com.ztplatform.repository.CredentialRepository;
import com.ztplatform.repository.DocumentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CredentialService {

        private final CredentialRepository credentialRepository;
        private final DocumentRepository documentRepository;
        private final DigitalSignatureService digitalSignatureService;
        private final QRCodeService qrCodeService;

        public Credential getCredentialEntityByCredentialId(
                        String credentialId) {

                return credentialRepository
                                .findByCredentialId(credentialId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Credential not found"));
        }

        public CredentialDTO createCredential(Long documentId) {

                Document document = documentRepository.findById(documentId)
                                .orElseThrow(() -> new RuntimeException("Document not found"));

                if (credentialRepository.findByDocumentId(documentId).isPresent()) {
                        throw new RuntimeException(
                                        "Credential already exists for this document");
                }

                Credential credential = new Credential();

                String credentialId = generateCredentialId();

                credential.setCredentialId(credentialId);
                credential.setCredentialName(document.getTitle());
                credential.setDocument(document);

                /*
                 * Data that represents this credential.
                 * This exact data will be digitally signed.
                 */
                String dataToSign = credentialId + "|" +
                                document.getId() + "|" +
                                document.getStudent().getStudentId() + "|" +
                                document.getDocumentType().name() + "|" +
                                document.getTitle();

                // Generate real digital signature
                String digitalSignature = digitalSignatureService.sign(dataToSign);

                credential.setDigitalSignature(digitalSignature);

                /*
                 * QR contains the verification URL.
                 */
                String qrData = "http://192.168.0.143:3000/verify/" + credential.getCredentialId();

                credential.setQrData(qrData);

                credential.setStatus(
                                Credential.CredentialStatus.VALID);

                Credential savedCredential = credentialRepository.save(credential);

                return convertToDTO(savedCredential);
        }

        public CredentialDTO getCredentialById(Long id) {

                Credential credential = credentialRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Credential not found"));

                return convertToDTO(credential);
        }

        public CredentialDTO getCredentialByCredentialId(
                        String credentialId) {

                Credential credential = credentialRepository.findByCredentialId(credentialId)
                                .orElseThrow(() -> new RuntimeException(
                                                "Credential not found"));

                return convertToDTO(credential);
        }

        public List<CredentialDTO> getAllCredentials() {

                return credentialRepository.findAll()
                                .stream()
                                .map(this::convertToDTO)
                                .toList();
        }

        public List<CredentialDTO> getMyCredentials(String userEmail) {
                List<Credential> credentials = credentialRepository.findByUserEmail(userEmail);
                return credentials.stream()
                                .map(this::convertToDTO)
                                .toList();
        }

        public CredentialDTO revokeCredential(Long id) {

                Credential credential = credentialRepository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Credential not found"));

                credential.setStatus(
                                Credential.CredentialStatus.REVOKED);

                credential.setRevokedAt(
                                java.time.LocalDateTime.now());

                Credential updatedCredential = credentialRepository.save(credential);

                return convertToDTO(updatedCredential);
        }

        private String generateCredentialId() {

                return "ZTC-" +
                                UUID.randomUUID()
                                                .toString()
                                                .substring(0, 8)
                                                .toUpperCase();
        }

        private CredentialDTO convertToDTO(
                        Credential credential) {

                CredentialDTO dto = new CredentialDTO(
                                credential.getId(),
                                credential.getCredentialId(),
                                credential.getDocument().getId(),
                                credential.getDigitalSignature(),
                                credential.getQrData(),
                                credential.getStatus(),
                                credential.getIssuedAt(),
                                credential.getExpiresAt(),
                                credential.getRevokedAt());

                // Add document details for frontend
                dto.setDocumentType(credential.getDocument().getDocumentType());
                dto.setDocumentTitle(credential.getDocument().getTitle());
                dto.setFileUrl(credential.getDocument().getFileUrl());

                // Add student name for admin
                if (credential.getDocument().getStudent() != null &&
                    credential.getDocument().getStudent().getUser() != null) {
                        dto.setStudentName(credential.getDocument().getStudent().getUser().getName());
                }

                return dto;
        }
}