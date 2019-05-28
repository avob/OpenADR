export function isActionReport (report) {
  return (report.start == null && report.duration == null) 
  		&& !isMetadataReport(report)
  		&& report.reportName === "METADATA_TELEMETRY_STATUS";
}

export function isMetadataReport (report) {
	  return (report.start == null && report.duration == null) 
	  	&& report.reportName === "METADATA_TELEMETRY_STATUS" 
	  	&& report.reportSpecifierId === "METADATA";
	}

export function isHistoryReport (report) {
  return (report.start != null || report.duration != null) && report.reportName === "METADATA_HISTORY_USAGE";
}

export function isTelemetryReport (report) {
  return report.reportName === "METADATA_TELEMETRY_USAGE";
}