import { useState } from "react";
import { Link } from "react-router-dom";
import { useQuery } from "react-query";
import { FileText, Clock, Loader2, ChevronRight } from "lucide-react";
import StatusBadge from "../components/StatusBadge";
import { useAuth } from "../hooks/useAuth";
import { getAllRequests } from "../services/requests";

function normalizeStatus(status) {
  return String(status ?? "pending").toLowerCase();
}

function formatDocumentType(documentType) {
  if (!documentType) return "Document Request";
  return String(documentType)
    .replace(/_/g, " ")
    .replace(/\b\w/g, (char) => char.toUpperCase());
}

function formatDate(dateValue) {
  if (!dateValue) return "Date unavailable";
  const date = new Date(dateValue);
  if (Number.isNaN(date.getTime())) return "Date unavailable";
  return date.toLocaleDateString("en-IN", {
    day: "numeric",
    month: "short",
    year: "numeric",
  });
}

function StaffRequestRow({ req }) {
  const documentType = req?.documentType ?? req?.document_type ?? req?.doc_type;
  const status = normalizeStatus(req?.status);
  const createdAt = req?.createdAt ?? req?.created_at ?? req?.requestedAt;
  const displayId = String(req?.id ?? "").slice(0, 8).toUpperCase();

  return (
    <Link
      to={`/staff/review/${req.id}`}
      className="flex items-center gap-4 px-5 py-4 bg-white border border-gray-200 rounded-xl hover:bg-gray-50 transition-colors mb-3"
    >
      <div className="w-9 h-9 rounded-lg bg-blue-50 flex items-center justify-center flex-shrink-0">
        <FileText size={16} strokeWidth={1.5} className="text-blue-500" />
      </div>
      <div className="flex-1 min-w-0">
        <p className="text-sm font-semibold text-gray-900 truncate">
          {formatDocumentType(documentType)}
        </p>
        <p className="text-xs text-gray-400 mt-0.5">
          #{displayId || "N/A"} · {req?.student_name || "Student"} · {formatDate(createdAt)}
        </p>
      </div>
      <StatusBadge status={status} />
      <ChevronRight size={16} className="text-gray-300" />
    </Link>
  );
}

export default function StaffDashboard() {
  const { role } = useAuth();
  const { data: requests, isLoading, isError } = useQuery(
    "staff-requests",
    () => getAllRequests(),
    { staleTime: 30000 }
  );

  const [filter, setFilter] = useState("all");
  const requestList = Array.isArray(requests) ? requests : [];
  const filters = ["all", "pending", "approved", "rejected"];

  const filtered =
    filter === "all"
      ? requestList
      : requestList.filter((request) => normalizeStatus(request?.status) === filter);

  return (
    <div className="min-h-screen bg-gray-50">
      <header className="bg-white border-b border-gray-200 px-7 py-3.5 flex items-center justify-between sticky top-0 z-10">
        <h1 className="text-sm font-semibold text-gray-900">Staff Dashboard</h1>
        {role === "ADMIN" && (
          <Link to="/admin" className="text-xs text-blue-500 font-medium hover:underline">
            Admin panel
          </Link>
        )}
      </header>

      <div className="max-w-4xl mx-auto w-full px-7 py-8">
        <div className="flex gap-2 flex-wrap mb-6">
          {filters.map((filterName) => {
            const count =
              filterName === "all"
                ? requestList.length
                : requestList.filter((request) => normalizeStatus(request?.status) === filterName).length;

            return (
              <button
                type="button"
                key={filterName}
                onClick={() => setFilter(filterName)}
                className={`px-4 py-1.5 rounded-full text-sm font-medium border transition-colors ${
                  filter === filterName
                    ? "bg-blue-500 border-blue-500 text-white"
                    : "bg-white border-gray-200 text-gray-500 hover:border-blue-300 hover:text-blue-500"
                }`}
              >
                {filterName === "all" ? "All requests" : filterName}
                {filterName !== "all" && (
                  <span className="ml-1.5 text-xs opacity-70">({count})</span>
                )}
              </button>
            );
          })}
        </div>

        {isLoading && (
          <div className="flex items-center justify-center py-20 text-gray-400">
            <Loader2 size={24} className="spinning mr-2" /> Loading requests…
          </div>
        )}

        {isError && (
          <div className="text-center py-20 text-red-400">
            Could not load requests. Please refresh.
          </div>
        )}

        {!isLoading && !isError && filtered.length === 0 && (
          <div className="text-center py-20 text-gray-400">
            <Clock size={36} strokeWidth={1.2} className="mx-auto mb-3 text-gray-300" />
            <p className="font-medium text-gray-500">
              No {filter !== "all" ? filter : ""} requests found
            </p>
          </div>
        )}

        {!isLoading &&
          !isError &&
          filtered.map((req) => (
            <StaffRequestRow key={String(req?.id)} req={req} />
          ))}
      </div>
    </div>
  );
}
