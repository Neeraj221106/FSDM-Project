import { useQuery, useMutation, useQueryClient } from "react-query";
import { ShieldCheck, FileText, Users, Activity, Loader2, XCircle } from "lucide-react";
import { Link } from "react-router-dom";
import api from "../services/api";

export default function AdminPanel() {
  const queryClient = useQueryClient();

  const { data: stats, isLoading: statsLoading, isError: statsError } = useQuery(
    "admin-stats",
    () => api.get("/admin/stats").then((r) => r.data),
    { staleTime: 60000 }
  );

  const { data: logs, isLoading: logsLoading, isError: logsError } = useQuery(
    "audit-logs",
    () => api.get("/admin/audit-logs?limit=20").then((r) => r.data),
    { staleTime: 30000 }
  );

  const { data: credentials, isLoading: credentialsLoading, isError: credentialsError } = useQuery(
    "admin-credentials",
    () => api.get("/credentials").then((r) => r.data),
    { staleTime: 30000 }
  );

  const revokeMutation = useMutation(
    (credentialId) => api.put(`/credentials/${credentialId}/revoke`),
    {
      onSuccess: () => {
        queryClient.invalidateQueries("admin-credentials");
        queryClient.invalidateQueries("admin-stats");
      },
    }
  );

  const logList = Array.isArray(logs) ? logs : [];
  const credentialList = Array.isArray(credentials) ? credentials : [];

  const statCards = [
    { label: "Total students",   value: stats?.total_students   ?? "—", icon: Users,     color: "bg-blue-50 text-blue-500"  },
    { label: "Documents issued", value: stats?.total_documents  ?? "—", icon: FileText,  color: "bg-green-50 text-green-500"},
    { label: "Pending requests", value: stats?.pending_requests ?? "—", icon: Activity,  color: "bg-amber-50 text-amber-500"},
    { label: "Revoked creds",    value: stats?.revoked          ?? "—", icon: ShieldCheck,color:"bg-red-50 text-red-500"    },
  ];

  const actionColor = {
    LOGIN: "text-blue-500", DOCUMENT_ISSUED: "text-green-500",
    REQUEST_CREATED: "text-gray-500", REQUEST_REJECTED: "text-red-500",
    CREDENTIAL_REVOKED: "text-purple-500", QR_SCANNED: "text-amber-500",
  };

  const statusColor = {
    VALID: "bg-green-100 text-green-700",
    REVOKED: "bg-red-100 text-red-700",
    EXPIRED: "bg-gray-100 text-gray-700",
  };

  const handleRevoke = (credentialId) => {
    if (window.confirm("Are you sure you want to revoke this credential?")) {
      revokeMutation.mutate(credentialId);
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 px-7 py-3.5 flex items-center justify-between sticky top-0 z-10">
        <div className="flex items-center gap-2">
          <ShieldCheck size={18} strokeWidth={1.5} className="text-blue-500" />
          <h1 className="text-sm font-semibold text-gray-900">Admin Panel</h1>
        </div>
        <Link to="/staff" className="text-xs text-blue-500 font-medium hover:underline">
          ← Back to requests
        </Link>
      </header>

      <div className="max-w-5xl mx-auto px-7 py-7">
        {/* Stats */}
        <div className="grid grid-cols-4 gap-4 mb-8">
          {statCards.map(({ label, value, icon: Icon, color }) => (
            <div key={label} className="bg-white border border-gray-200 rounded-xl p-4 flex items-center gap-3">
              <div className={`w-9 h-9 rounded-lg flex items-center justify-center ${color}`}>
                <Icon size={17} strokeWidth={1.5} />
              </div>
              <div>
                <p className="text-xl font-bold text-gray-900 leading-none">
                  {statsLoading ? <Loader2 size={16} className="spinning text-gray-300" /> : statsError ? "Error" : value}
                </p>
                <p className="text-xs text-gray-400 mt-0.5">{label}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Credential Management */}
        <div className="bg-white border border-gray-200 rounded-2xl overflow-hidden mb-8">
          <div className="px-6 py-4 border-b border-gray-100">
            <h2 className="text-sm font-semibold text-gray-900">Credential Management</h2>
            <p className="text-xs text-gray-400 mt-0.5">View and revoke issued credentials</p>
          </div>

          {credentialsLoading ? (
            <div className="flex items-center justify-center py-12 text-gray-400">
              <Loader2 size={20} className="spinning mr-2" /> Loading credentials…
            </div>
          ) : credentialsError ? (
            <div className="px-6 py-12 text-center text-sm text-red-500">
              Could not load credentials. Please refresh.
            </div>
          ) : credentialList.length === 0 ? (
            <div className="px-6 py-12 text-center text-sm text-gray-400">
              No credentials issued yet.
            </div>
          ) : (
            <div className="overflow-x-auto">
              <table className="w-full">
                <thead className="bg-gray-50 border-b border-gray-100">
                  <tr>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Credential ID</th>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Student Name</th>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Document Type</th>
                    <th className="px-6 py-3 text-left text-xs font-semibold text-gray-500 uppercase tracking-wider">Status</th>
                    <th className="px-6 py-3 text-right text-xs font-semibold text-gray-500 uppercase tracking-wider">Actions</th>
                  </tr>
                </thead>
                <tbody className="divide-y divide-gray-50">
                  {credentialList.map((cred) => (
                    <tr key={cred.id} className="hover:bg-gray-50 transition-colors">
                      <td className="px-6 py-3.5 text-sm font-mono text-gray-700">{cred.credentialId}</td>
                      <td className="px-6 py-3.5 text-sm text-gray-700">{cred.studentName || "—"}</td>
                      <td className="px-6 py-3.5 text-sm text-gray-700">
                        {cred.documentType?.replace(/_/g, " ") || cred.documentTitle || "—"}
                      </td>
                      <td className="px-6 py-3.5">
                        <span className={`inline-flex px-2 py-1 text-xs font-medium rounded-full ${statusColor[cred.status] || "bg-gray-100 text-gray-700"}`}>
                          {cred.status}
                        </span>
                      </td>
                      <td className="px-6 py-3.5 text-right">
                        {cred.status === "VALID" && (
                          <button
                            onClick={() => handleRevoke(cred.id)}
                            disabled={revokeMutation.isLoading}
                            className="inline-flex items-center gap-1.5 px-3 py-1.5 text-xs font-medium text-red-600 bg-red-50 hover:bg-red-100 rounded-lg transition-colors disabled:opacity-50 disabled:cursor-not-allowed"
                          >
                            <XCircle size={14} />
                            Revoke
                          </button>
                        )}
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          )}
        </div>

        {/* Audit log */}
        <div className="bg-white border border-gray-200 rounded-2xl overflow-hidden">
          <div className="px-6 py-4 border-b border-gray-100">
            <h2 className="text-sm font-semibold text-gray-900">Audit Log</h2>
            <p className="text-xs text-gray-400 mt-0.5">Immutable record of every action on the platform</p>
          </div>

          {logsLoading ? (
            <div className="flex items-center justify-center py-12 text-gray-400">
              <Loader2 size={20} className="spinning mr-2" /> Loading logs…
            </div>
          ) : logsError ? (
            <div className="px-6 py-12 text-center text-sm text-red-500">
              Could not load audit logs. Please refresh.
            </div>
          ) : logList.length === 0 ? (
            <div className="px-6 py-12 text-center text-sm text-gray-400">
              No audit records yet.
            </div>
          ) : (
            <div className="divide-y divide-gray-50">
              {logList.map((log) => (
                <div key={log.id} className="flex items-start gap-4 px-6 py-3.5 hover:bg-gray-50 transition-colors">
                  <div className={`text-[11px] font-bold mt-0.5 w-36 flex-shrink-0 ${actionColor[log.action] ?? "text-gray-500"}`}>
                    {log.action?.replace(/_/g, " ")}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="text-sm text-gray-700 font-medium truncate">{log.description}</p>
                    <p className="text-xs text-gray-400 mt-0.5 font-mono">{log.ip_address}</p>
                  </div>
                  <div className="text-xs text-gray-400 whitespace-nowrap flex-shrink-0">
                    {new Date(log.timestamp).toLocaleString("en-IN", {
                      day: "numeric", month: "short",
                      hour: "2-digit", minute: "2-digit",
                    })}
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      </div>
    </div>
  );
}