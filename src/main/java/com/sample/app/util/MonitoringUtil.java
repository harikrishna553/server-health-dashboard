package com.sample.app.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import com.sample.app.dto.*;

public class MonitoringUtil {

	public static SystemsToMonitorRootModel monitor(SystemsToMonitorRootModel payload) {

		if (payload == null) {
			return new SystemsToMonitorRootModel();

		}
		SystemsToMonitorRootModel response = new SystemsToMonitorRootModel();

		List<SystemsToMonitor> systemsToMonitorResponses = new ArrayList<>();

		List<SystemsToMonitor> systemsToMonitor = payload.getSystemsToMonitor();

		for (SystemsToMonitor systems : systemsToMonitor) {

			SystemsToMonitor systemsToMonitorResult = new SystemsToMonitor();
			systemsToMonitorResult.setEnvType(systems.getEnvType());

			List<SystemDetails> systemDetails = systems.getSystemsDetails();
			List<SystemDetails> systemDetailsResponses = new ArrayList<>();

			for (SystemDetails systemDetail : systemDetails) {
				long startTime = System.currentTimeMillis();
				int respCode = getResponseCode(systemDetail.getHealthEndpoint());
				long endTime = System.currentTimeMillis();

				SystemDetails temp = new SystemDetails();

				if (respCode == 200)
					temp.setHealth(true);
				else
					temp.setHealth(false);

				temp.setHealthEndpoint(systemDetail.getHealthEndpoint());
				temp.setResponseCode(respCode);
				temp.setServiceName(systemDetail.getServiceName());
				temp.setResponseTimeInMillis((endTime-startTime));

				systemDetailsResponses.add(temp);

			}

			systemsToMonitorResult.setSystemsDetails(systemDetailsResponses);

			systemsToMonitorResponses.add(systemsToMonitorResult);

		}

		response.setSystemsToMonitor(systemsToMonitorResponses);

		return response;

	}

	public static int getResponseCode(String urlTemp) {

		try {
			URL url = new URL(urlTemp);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();

			return connection.getResponseCode();
		} catch (Exception e) {
			e.printStackTrace();
			return 500;
		}

	}

	public static String postRequest(String urlTmp, String body) {

		try {
			URL url = new URL(urlTmp);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setDoOutput(true);
			connection.setRequestProperty("Accept", "application/json");
			connection.setRequestProperty("Content-Type", "application/json; utf-8");
			try (OutputStream os = connection.getOutputStream()) {
				byte[] input = body.getBytes("utf-8");
				os.write(input, 0, input.length);
			}

			connection.connect();
			try (BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"))) {
				StringBuilder response = new StringBuilder();
				String responseLine = null;
				while ((responseLine = br.readLine()) != null) {
					response.append(responseLine.trim());
				}
				return response.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Could not send mail";
		}

	}

	static {
		init();
	}

	private static void init() {
		TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub

			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				// TODO Auto-generated method stub

			}

		} };

		SSLContext sc = null;
		try {
			sc = SSLContext.getInstance("SSL");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		try {
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
		HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		// Create all-trusting host name verifier
		HostnameVerifier validHosts = new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				return true;
			}
		};
		// All hosts will be valid
		HttpsURLConnection.setDefaultHostnameVerifier(validHosts);
	}
}
