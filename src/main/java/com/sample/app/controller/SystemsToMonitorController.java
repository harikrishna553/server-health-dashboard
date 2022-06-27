package com.sample.app.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sample.app.dto.SystemDetails;
import com.sample.app.dto.SystemsToMonitor;
import com.sample.app.dto.SystemsToMonitorRootModel;
import com.sample.app.util.FileUtil;
import com.sample.app.util.MonitoringUtil;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("api/v1/systems/monitor")
@Tag(name = "Dashboard v1", description = "This section contains dashboard related APIs")
public class SystemsToMonitorController {

	private static final Map<String, SystemsToMonitorRootModel> cache = new HashMap<>();

	@Operation(summary = "Register new application to monitor", description = "This API Register new application to monitor. User can onboard all the services endpoints by environment and provide unique key to the environment")
	@PostMapping()
	public ResponseEntity<Response> create(@RequestBody SystemsToMonitorRootModel payload) {
		String dashboardId = payload.getId();

		if (cache.containsKey(dashboardId)) {
			throw new ConstraintViolationException("Dashboard with " + dashboardId + " is aleady exists", null);
		}

		cache.put(dashboardId, payload);

		Response resp = new Response();

		resp.respId = dashboardId;

		return ResponseEntity.status(HttpStatus.CREATED).body(resp);

	}

	@RequestMapping(value = "/html/{id}", method = RequestMethod.GET)
	public ResponseEntity<String> monitorByIdHtml(@PathVariable(value = "id") String id,
			@RequestParam(name = "refreshSeconds", defaultValue = "60") int refreshSeconds) throws IOException {

		SystemsToMonitorRootModel resp = MonitoringUtil.monitor(cache.get(id));
		String htmlD = getHtml(resp, refreshSeconds);

		return ResponseEntity.status(HttpStatus.OK).body(htmlD);

	}

	public static class Response implements Serializable {
		private String respId;

		public String getRespId() {
			return respId;
		}

		public void setRespId(String respId) {
			this.respId = respId;
		}

	}

	private static String getHtml(SystemsToMonitorRootModel resp, int refreshSeconds) throws IOException {
		StringBuilder builder = new StringBuilder();

		builder.append("<meta http-equiv=\"refresh\" content=\"" + refreshSeconds + "\">");

		String css = FileUtil.resourceAsString("styles.css");

		builder.append("<style>").append(css).append("</style>");

		List<SystemsToMonitor> systemsToMonitor = resp.getSystemsToMonitor();

		builder.append("<header>Monitoring Dashboard</header>");
		builder.append("<div id= \"mainDiv\">");

		for (SystemsToMonitor sys : systemsToMonitor) {
			String envType = sys.getEnvType();

			builder.append("<h1>").append(envType).append("</h1>");

			List<SystemDetails> sysDetails = sys.getSystemsDetails();

			builder.append("<table id = \"reportsTable\">");

			builder.append("<tr>");

			builder.append("<th>");
			builder.append("ServiceName");
			builder.append("</th>");

			builder.append("<th>");
			builder.append("HealthEndpoint");
			builder.append("</th>");

			builder.append("<th>");
			builder.append("ResponseCode");
			builder.append("</th>");
			
			builder.append("<th>");
			builder.append("ResponseTime(in millis)");
			builder.append("</th>");

			builder.append("<th>");
			builder.append("Status");
			builder.append("</th>");

			builder.append("</tr>");

			for (SystemDetails sysDetail : sysDetails) {
				String healthEndpoint = sysDetail.getHealthEndpoint();
				int respCode = sysDetail.getResponseCode();
				String serviceName = sysDetail.getServiceName();
				long responseTime = sysDetail.getResponseTimeInMillis();

				// builder.append("healthEndPoint : ").append(healthEndpoint);

				builder.append("<tr>");
				
				builder.append("<td>");
				builder.append(serviceName);
				builder.append("</td>");

				builder.append("<td>");
				builder.append(healthEndpoint);
				builder.append("</td>");

				builder.append("<td>");
				builder.append(respCode);
				builder.append("</td>");
				
				builder.append("<td>");
				builder.append(responseTime);
				builder.append("</td>");

				if (respCode == 200) {
					builder.append("<td>");
					builder.append("<span class=\"greenDot\"></span>");
					builder.append("</td>");
				} else {
					builder.append("<td>");
					builder.append("<span class=\"redDot\"></span>");
					builder.append("</td>");
				}

				builder.append("</tr>");

			}
			builder.append("</table>");

			builder.append("<div>");
		}
		return builder.toString();
	}

}
