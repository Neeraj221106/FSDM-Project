package com.ztplatform.service;

import com.ztplatform.dto.RequestDTO;
import com.ztplatform.model.Document;
import com.ztplatform.model.Request;
import com.ztplatform.model.Student;
import com.ztplatform.model.User;
import com.ztplatform.repository.DocumentRepository;
import com.ztplatform.repository.RequestRepository;
import com.ztplatform.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RequestService {

    private final RequestRepository requestRepository;
    private final StudentRepository studentRepository;
    private final DocumentRepository documentRepository;
    private final CredentialService credentialService;

    public RequestDTO createRequest(RequestDTO dto, User authenticatedUser) {

        Student student = studentRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() ->
                        new RuntimeException("Student profile not found"));

        Request request = new Request();

        request.setStudent(student);
        request.setDocumentType(resolveDocumentType(dto));
        request.setReason(resolveReason(dto));
        request.setStatus(Request.RequestStatus.PENDING);

        Request savedRequest = requestRepository.save(request);

        return convertToDTO(savedRequest);
    }

    public List<RequestDTO> getMyRequests(User authenticatedUser) {

        Student student = studentRepository.findByUserId(authenticatedUser.getId())
                .orElseThrow(() ->
                        new RuntimeException("Student profile not found"));

        return getRequestsByStudent(student.getId());
    }

    public List<RequestDTO> getAllRequests() {

        return requestRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<RequestDTO> getRequestsByStudent(Long studentId) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() ->
                        new RuntimeException("Student not found"));

        return requestRepository.findByStudent(student)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public RequestDTO getRequestById(Long id) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found"));

        return convertToDTO(request);
    }

    @Transactional
    public RequestDTO approveRequest(Long id, User admin) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found"));

        // Only pending requests can be approved
        if (request.getStatus() != Request.RequestStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending requests can be approved"
            );
        }

        // Mark request as approved
        request.setStatus(Request.RequestStatus.APPROVED);
        request.setReviewedBy(admin);
        request.setReviewedAt(LocalDateTime.now());
        request.setRejectionReason(null);

        Request updatedRequest = requestRepository.save(request);

        // Create document for the approved request
        Document document = new Document();

        document.setStudent(request.getStudent());
        document.setDocumentType(request.getDocumentType());
        document.setTitle(generateDocumentTitle(
                request.getDocumentType()
        ));
        document.setStatus(Document.DocumentStatus.ISSUED);
        document.setIssuedAt(LocalDateTime.now());

        Document savedDocument = documentRepository.save(document);

        // Generate credential automatically
        credentialService.createCredential(savedDocument.getId());

        return convertToDTO(updatedRequest);
    }

    public RequestDTO rejectRequest(
            Long id,
            User admin,
            String rejectionReason
    ) {

        Request request = requestRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Request not found"));

        // Only pending requests can be rejected
        if (request.getStatus() != Request.RequestStatus.PENDING) {
            throw new RuntimeException(
                    "Only pending requests can be rejected"
            );
        }

        request.setStatus(Request.RequestStatus.REJECTED);
        request.setReviewedBy(admin);
        request.setReviewedAt(LocalDateTime.now());
        request.setRejectionReason(rejectionReason);

        Request updatedRequest = requestRepository.save(request);

        return convertToDTO(updatedRequest);
    }

    private Document.DocumentType resolveDocumentType(RequestDTO dto) {

        if (dto.getDocumentType() != null) {
            return dto.getDocumentType();
        }

        String raw = dto.getDocType();

        if (raw == null || raw.isBlank()) {
            throw new RuntimeException("Document type is required");
        }

        String normalized = raw.trim().toUpperCase().replace('-', '_');

        try {
            return Document.DocumentType.valueOf(normalized);
        } catch (IllegalArgumentException ex) {
            return switch (normalized) {
                case "TRANSCRIPT" -> Document.DocumentType.MARKSHEET;
                case "PASSING_CERTIFICATE", "PROVISIONAL_DEGREE" ->
                        Document.DocumentType.DEGREE_CERTIFICATE;
                case "MIGRATION_CERTIFICATE" ->
                        Document.DocumentType.TRANSFER_CERTIFICATE;
                case "INTERNSHIP_COMPLETION" ->
                        Document.DocumentType.INTERNSHIP_CERTIFICATE;
                case "CHARACTER_CERTIFICATE",
                     "CONDUCT_CERTIFICATE",
                     "NO_DUES_CERTIFICATE",
                     "FEE_RECEIPT",
                     "SCHOLARSHIP_LETTER",
                     "ENROLLMENT_CONFIRMATION",
                     "MEDIUM_OF_INSTRUCTION",
                     "GAP_CERTIFICATE",
                     "BANK_LOAN_LETTER",
                     "VISA_SUPPORT_LETTER" ->
                        Document.DocumentType.BONAFIDE_CERTIFICATE;
                default -> throw new RuntimeException(
                        "Unsupported document type: " + raw
                );
            };
        }
    }

    private String resolveReason(RequestDTO dto) {

        String reason = dto.getReason();

        if ((reason == null || reason.isBlank()) && dto.getPurpose() != null) {
            reason = dto.getPurpose();
        }

        if (reason == null) {
            reason = "";
        }

        if (dto.getRemarks() != null && !dto.getRemarks().isBlank()) {
            if (reason.isBlank()) {
                return dto.getRemarks();
            }
            return reason + " | " + dto.getRemarks();
        }

        return reason;
    }

    private String generateDocumentTitle(
            Document.DocumentType documentType
    ) {

        return switch (documentType) {

            case BONAFIDE_CERTIFICATE ->
                    "Bonafide Certificate";

            case MARKSHEET ->
                    "Marksheet";

            case DEGREE_CERTIFICATE ->
                    "Degree Certificate";

            case INTERNSHIP_CERTIFICATE ->
                    "Internship Certificate";

            case TRANSFER_CERTIFICATE ->
                    "Transfer Certificate";
        };
    }

    private RequestDTO convertToDTO(Request request) {

        Long reviewedById = null;

        if (request.getReviewedBy() != null) {
            reviewedById = request.getReviewedBy().getId();
        }

        RequestDTO dto = new RequestDTO(
                request.getId(),
                request.getStudent().getId(),
                request.getDocumentType(),
                request.getReason(),
                request.getStatus().name(),
                request.getRejectionReason(),
                reviewedById
        );

        dto.setDocType(request.getDocumentType().name().toLowerCase());
        dto.setPurpose(request.getReason());
        dto.setRequestedAt(request.getRequestedAt());
        dto.setCreatedAt(request.getRequestedAt());

        if (request.getStudent() != null) {
            Student student = request.getStudent();
            dto.setRollNo(student.getStudentId());
            dto.setCourse(student.getCourse());
            dto.setYear(student.getYear());
            if (student.getUser() != null) {
                dto.setStudentName(student.getUser().getName());
            }
        }

        return dto;
    }
}