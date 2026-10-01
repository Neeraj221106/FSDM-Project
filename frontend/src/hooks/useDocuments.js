import { useQuery } from "react-query";
import api from "../services/api";

function mapCredential(c) {
  if (!c || typeof c !== "object") return c;
  return {
    id: c.id,
    credential_id: c.credentialId,
    doc_type: c.documentType ? String(c.documentType).toLowerCase() : undefined,
    issue_date: c.issuedAt,
    expiry_date: c.expiresAt,
    file_url: c.fileUrl,
    status: c.status ? String(c.status).toLowerCase() : c.status,
  };
}

function mapCredentials(data) {
  if (Array.isArray(data)) return data.map(mapCredential);
  if (Array.isArray(data?.data)) return data.data.map(mapCredential);
  if (Array.isArray(data?.content)) return data.content.map(mapCredential);
  return data;
}

export function useDocuments() {
  return useQuery(
    "documents",
    () => api.get("/credentials/my").then((r) => mapCredentials(r.data)),
    { staleTime: 1000 * 60 * 2 }
  );
}