package com.ztplatform.service;

import com.ztplatform.dto.AdminStatsDTO;
import com.ztplatform.dto.AuditLogDTO;
import com.ztplatform.model.AuditLog;
import com.ztplatform.model.Credential;
import com.ztplatform.model.Document;
import com.ztplatform.model.Request;
import com.ztplatform.repository.AuditLogRepository;
import com.ztplatform.repository.CredentialRepository;
import com.ztplatform.repository.DocumentRepository;
import com.ztplatform.repository.RequestRepository;
import com.ztplatform.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final StudentRepository studentRepository;
    private final DocumentRepository documentRepository;
    private final RequestRepository requestRepository;
    private final CredentialRepository credentialRepository;
    private final AuditLogRepository auditLogRepository;

    public AdminStatsDTO getStats() {
        return new AdminStatsDTO(
                studentRepository.count(),
                documentRepository.countByStatus(Document.DocumentStatus.ISSUED),
                requestRepository.countByStatus(Request.RequestStatus.PENDING),
                credentialRepository.countByStatus(Credential.CredentialStatus.REVOKED)
        );
    }

    public List<AuditLogDTO> getAuditLogs(int limit) {
        int pageSize = Math.min(Math.max(limit, 1), 100);

        return auditLogRepository
                .findAllByOrderByTimestampDesc(PageRequest.of(0, pageSize))
                .stream()
                .map(this::toDto)
                .toList();
    }

    private AuditLogDTO toDto(AuditLog log) {
        String description = log.getDescription();
        if (description == null || description.isBlank()) {
            description = log.getDetails();
        }

        return new AuditLogDTO(
                log.getId(),
                log.getAction(),
                description,
                log.getIpAddress(),
                log.getTimestamp()
        );
    }
}
