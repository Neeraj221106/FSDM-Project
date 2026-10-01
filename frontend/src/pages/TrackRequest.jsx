import { useState } from "react";
import {
  FileText,
  ChevronDown,
  ChevronUp,
  Clock,
  CheckCircle,
  XCircle,
  Download,
  Loader2,
} from "lucide-react";

import Sidebar from "../components/Sidebar";
import Navbar from "../components/Navbar";
import StatusBadge from "../components/StatusBadge";
import { useRequests } from "../hooks/useRequests";

/*
 * Request status timeline
 */
const statusSteps = {
  pending: ["Submitted", "Under Review", "Awaiting Approval"],
  approved: ["Submitted", "Under Review", "Approved"],
  rejected: ["Submitted", "Under Review", "Rejected"],
  issued: ["Submitted", "Under Review", "Approved", "Issued"],
};

/*
 * Convert any status value safely to lowercase.
 */
function normalizeStatus(status) {
  return String(status ?? "pending").toLowerCase();
}

/*
 * Convert backend document type into readable text.
 *
 * Example:
 * BONAFIDE_CERTIFICATE
 * ->
 * Bonafide Certificate
 */
function formatDocumentType(documentType) {
  if (!documentType) {
    return "Document Request";
  }

  return String(documentType)
    .replace(/_/g, " ")
    .replace(/\b\w/g, (char) => char.toUpperCase());
}

/*
 * Format request date safely.
 */
function formatDate(dateValue) {
  if (!dateValue) {
    return "Date unavailable";
  }

  const date = new Date(dateValue);

  if (Number.isNaN(date.getTime())) {
    return "Date unavailable";
  }

  return date.toLocaleDateString("en-IN", {
    day: "numeric",
    month: "short",
    year: "numeric",
  });
}

/*
 * Timeline component
 */
function Timeline({ status }) {
  const normalizedStatus = normalizeStatus(status);

  const steps =
    statusSteps[normalizedStatus] ?? statusSteps.pending;

  const activeIdx = steps.length - 1;

  return (
    <div className="flex items-start gap-0 mt-4">
      {steps.map((step, i) => {
        const isDone = i < activeIdx;
        const isActive = i === activeIdx;

        const isReject =
          normalizedStatus === "rejected" && isActive;

        return (
          <div
            key={step}
            className="flex items-center flex-1 last:flex-none"
          >
            <div className="flex flex-col items-center">
              <div
                className={`
                  w-7 h-7 rounded-full
                  flex items-center justify-center
                  text-xs font-bold flex-shrink-0

                  ${
                    isReject
                      ? "bg-red-100 text-red-600"
                      : isDone || isActive
                      ? "bg-green-100 text-green-600"
                      : "bg-gray-100 text-gray-400"
                  }
                `}
              >
                {isReject ? (
                  <XCircle size={14} />
                ) : isDone || isActive ? (
                  <CheckCircle size={14} />
                ) : (
                  i + 1
                )}
              </div>

              <p
                className={`
                  text-[11px] mt-1
                  text-center whitespace-nowrap
                  font-medium

                  ${
                    isReject
                      ? "text-red-500"
                      : isActive
                      ? "text-gray-800"
                      : isDone
                      ? "text-green-600"
                      : "text-gray-400"
                  }
                `}
              >
                {step}
              </p>
            </div>

            {i < steps.length - 1 && (
              <div
                className={`
                  flex-1 h-[1.5px]
                  mb-4 mx-1

                  ${
                    i < activeIdx
                      ? "bg-green-400"
                      : "bg-gray-200"
                  }
                `}
              />
            )}
          </div>
        );
      })}
    </div>
  );
}

/*
 * Individual request row
 */
function RequestRow({ req }) {
  const [open, setOpen] = useState(false);

  /*
   * Backend RequestDTO:
   *
   * id
   * studentId
   * documentType
   * reason
   * status
   * rejectionReason
   * reviewedBy
   *
   * We also support the frontend's older field names
   * so existing data does not break.
   */

  const requestId = req?.id;

  const documentType =
    req?.documentType ??
    req?.document_type ??
    req?.doc_type;

  const reason =
    req?.reason ??
    req?.purpose ??
    "";

  const rejectionReason =
    req?.rejectionReason ??
    req?.rejection_reason ??
    "";

  const status = normalizeStatus(req?.status);

  const createdAt =
    req?.createdAt ??
    req?.created_at ??
    req?.requestedAt ??
    req?.requested_at;

  const documentUrl =
    req?.documentUrl ??
    req?.document_url;

  /*
   * IMPORTANT:
   * Backend ID is Long/number.
   *
   * Therefore:
   * req.id.slice(...)
   *
   * would crash.
   *
   * String(req.id) is safe for both
   * numbers and strings.
   */
  const displayId = String(requestId ?? "").slice(0, 8).toUpperCase();

  return (
    <div className="border border-gray-200 rounded-xl overflow-hidden mb-3 bg-white">
      {/* Header row */}
      <button
        type="button"
        onClick={() => setOpen((value) => !value)}
        className="
          w-full flex items-center gap-4
          px-5 py-4 text-left
          hover:bg-gray-50
          transition-colors
        "
      >
        {/* Icon */}
        <div
          className="
            w-9 h-9 rounded-lg
            bg-blue-50
            flex items-center justify-center
            flex-shrink-0
          "
        >
          <FileText
            size={16}
            strokeWidth={1.5}
            className="text-blue-500"
          />
        </div>

        {/* Request information */}
        <div className="flex-1 min-w-0">
          <p className="text-sm font-600 text-gray-900 truncate">
            {formatDocumentType(documentType)}
          </p>

          <p className="text-xs text-gray-400 mt-0.5">
            #{displayId || "N/A"} · {formatDate(createdAt)}
          </p>
        </div>

        {/* Status */}
        <StatusBadge status={status} />

        {/* Expand button */}
        <div className="text-gray-400 ml-2">
          {open ? (
            <ChevronUp size={16} />
          ) : (
            <ChevronDown size={16} />
          )}
        </div>
      </button>

      {/* Expanded details */}
      {open && (
        <div className="px-5 pb-5 border-t border-gray-100">
          {/* Timeline */}
          <Timeline status={status} />

          {/* Details */}
          <div className="mt-5 grid grid-cols-2 gap-4">
            {/* Purpose / Reason */}
            <div>
              <p
                className="
                  text-[11px]
                  text-gray-400
                  uppercase
                  tracking-wide
                  font-semibold
                  mb-1
                "
              >
                Purpose
              </p>

              <p className="text-sm text-gray-700">
                {reason || "Not specified"}
              </p>
            </div>

            {/* Remarks */}
            {(req?.remarks || req?.remark) && (
              <div>
                <p
                  className="
                    text-[11px]
                    text-gray-400
                    uppercase
                    tracking-wide
                    font-semibold
                    mb-1
                  "
                >
                  Remarks
                </p>

                <p className="text-sm text-gray-700">
                  {req?.remarks ?? req?.remark}
                </p>
              </div>
            )}

            {/* Rejection reason */}
            {rejectionReason && (
              <div
                className="
                  col-span-2
                  bg-red-50
                  border border-red-100
                  rounded-lg
                  px-4 py-3
                "
              >
                <p
                  className="
                    text-[11px]
                    text-red-400
                    uppercase
                    tracking-wide
                    font-semibold
                    mb-1
                  "
                >
                  Rejection reason
                </p>

                <p className="text-sm text-red-700">
                  {rejectionReason}
                </p>
              </div>
            )}
          </div>

          {/* Download document */}
          {status === "issued" && documentUrl && (
            <a
              href={documentUrl}
              target="_blank"
              rel="noreferrer"
              className="
                mt-5
                inline-flex
                items-center
                gap-2
                bg-blue-600
                text-white
                text-sm
                font-semibold
                px-5
                py-2.5
                rounded-lg
                hover:bg-blue-700
                transition-colors
              "
            >
              <Download size={14} />
              Download document
            </a>
          )}
        </div>
      )}
    </div>
  );
}

/*
 * Main Track Requests page
 */
export default function TrackRequest() {
  const {
    data: requests,
    isLoading,
    isError,
  } = useRequests();

  const [filter, setFilter] = useState("all");

  /*
   * Make sure requests is always an array.
   * This prevents .filter() / .map() crashes
   * if the API temporarily returns undefined.
   */
  const requestList = Array.isArray(requests)
    ? requests
    : [];

  const filters = [
    "all",
    "pending",
    "approved",
    "issued",
    "rejected",
  ];

  /*
   * Filter requests by status.
   */
  const filtered =
    filter === "all"
      ? requestList
      : requestList.filter(
          (request) =>
            normalizeStatus(request?.status) === filter
        );

  return (
    <div className="flex min-h-screen">
      {/* Sidebar */}
      <Sidebar />

      <div className="flex-1 flex flex-col bg-gray-50">
        {/* Navbar */}
        <Navbar title="Track Requests" />

        <div className="max-w-4xl mx-auto w-full px-7 py-8">
          {/* Filter tabs */}
          <div className="flex gap-2 flex-wrap mb-6">
            {filters.map((filterName) => {
              const count =
                filterName === "all"
                  ? requestList.length
                  : requestList.filter(
                      (request) =>
                        normalizeStatus(
                          request?.status
                        ) === filterName
                    ).length;

              return (
                <button
                  type="button"
                  key={filterName}
                  onClick={() => setFilter(filterName)}
                  className={`
                    px-4 py-1.5
                    rounded-full
                    text-sm
                    font-medium
                    border
                    transition-colors

                    ${
                      filter === filterName
                        ? "bg-brand-500 border-brand-500 text-white"
                        : "bg-white border-gray-200 text-gray-500 hover:border-blue-300 hover:text-blue-500"
                    }
                  `}
                >
                  {filterName === "all"
                    ? "All requests"
                    : filterName}

                  {filterName !== "all" && (
                    <span className="ml-1.5 text-xs opacity-70">
                      ({count})
                    </span>
                  )}
                </button>
              );
            })}
          </div>

          {/* Loading */}
          {isLoading && (
            <div
              className="
                flex
                items-center
                justify-center
                py-20
                text-gray-400
              "
            >
              <Loader2
                size={24}
                className="animate-spin mr-2"
              />

              Loading requests…
            </div>
          )}

          {/* Error */}
          {isError && (
            <div className="text-center py-20 text-red-400">
              Could not load requests. Please refresh.
            </div>
          )}

          {/* Empty */}
          {!isLoading &&
            !isError &&
            filtered.length === 0 && (
              <div
                className="
                  text-center
                  py-20
                  text-gray-400
                "
              >
                <Clock
                  size={36}
                  strokeWidth={1.2}
                  className="
                    mx-auto
                    mb-3
                    text-gray-300
                  "
                />

                <p className="font-medium text-gray-500">
                  No{" "}
                  {filter !== "all"
                    ? filter
                    : ""}{" "}
                  requests found
                </p>
              </div>
            )}

          {/* Request rows */}
          {!isLoading &&
            !isError &&
            filtered.map((req) => (
              <RequestRow
                key={String(req?.id ?? Math.random())}
                req={req}
              />
            ))}
        </div>
      </div>
    </div>
  );
}