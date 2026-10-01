import { useQuery, useMutation, useQueryClient } from "react-query";
import api from "../services/api";

function mapRequest(r) {
  if (!r || typeof r !== "object") return r;
  return {
    ...r,
    doc_type: r.doc_type ?? (r.documentType ? String(r.documentType).toLowerCase() : undefined),
    purpose: r.purpose ?? r.reason,
    status: r.status ? String(r.status).toLowerCase() : r.status,
    created_at: r.created_at ?? r.requestedAt,
    rejection_reason: r.rejection_reason ?? r.rejectionReason,
  };
}

function mapRequests(data) {
  if (Array.isArray(data)) return data.map(mapRequest);
  if (Array.isArray(data?.data)) return data.data.map(mapRequest);
  if (Array.isArray(data?.content)) return data.content.map(mapRequest);
  return data;
}

export function useRequests() {
  return useQuery(
    "requests",
    () => api.get("/requests/my").then((r) => mapRequests(r.data)),
    { staleTime: 1000 * 60 }
  );
}

export function useSubmitRequest() {
  const queryClient = useQueryClient();
  return useMutation(
    (payload) => api.post("/requests", payload).then((r) => r.data),
    { onSuccess: () => queryClient.invalidateQueries("requests") }
  );
}