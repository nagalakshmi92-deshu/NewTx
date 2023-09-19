package jirautil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.thed.zephyr.cloud.rest.ZFJCloudRestClient;
import com.thed.zephyr.cloud.rest.client.JwtGenerator;

import utilities.LogUtil;

@SuppressWarnings("deprecation")
public class CreateCycleAndAddTests {

	public static void main(String[] args) throws Exception {
		// Replace zephyr baseurl <ZAPI_Cloud_URL> shared with the user for ZAPI Cloud
		String zephyrBaseUrl = "https://prod-api.zephyr4jiracloud.com/connect";
		// zephyr accessKey , we can get from Addons >> zapi section
		String accessKey = "Njc1NTJjZTItMzVkZC0zYmEwLWI3N2UtZmQ3YWNkZDAwNTM2IGFkbWluIGJpbmR1";
		// zephyr secretKey , we can get from Addons >> zapi section
		String secretKey = "0GYaR3cNz-kw8tuQDKNBpirb-u6FnTIqtJDX-YBxCUw";
		// Jira UserName
		String userName = "bindu.poreddy@testingxperts.com";

		ZFJCloudRestClient client = ZFJCloudRestClient.restBuilder(zephyrBaseUrl, accessKey, secretKey, userName)
				.build();

		/** Declare the Variables here **/
		Long projectId = 10000l;
		String cycleName = "Test Cycle -- API DEMO";
		String cycleDescription = "Created by ZAPI CLOUD API";

		String createCycleUri = zephyrBaseUrl + "/public/rest/api/1.0/cycle";

		/** Cycle Object created - DO NOT EDIT **/
		JSONObject createCycleObj = new JSONObject();
		createCycleObj.put("name", cycleName);
		createCycleObj.put("description", cycleDescription);
		createCycleObj.put("startDate", System.currentTimeMillis());
		createCycleObj.put("projectId", projectId);

		StringEntity cycleJSON = null;
		try {
			cycleJSON = new StringEntity(createCycleObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		CreateCycleAndAddTests cc = new CreateCycleAndAddTests();
		String cycleID = cc.createCycle(createCycleUri, client, accessKey, cycleJSON);
		LogUtil.infoLog(CreateCycleAndAddTests.class, "Cycle Created with Cycle Id : " + cycleID);

		/**
		 * Add tests to Cycle IssueId's
		 * 
		 */

		String addTestsUri = zephyrBaseUrl + "/public/rest/api/1.0/executions/add/cycle/" + cycleID;
		String[] issueIds = { "TES01-6" }; // Issue Id's to be added to Test Cycle, add more issueKeys separated by
											// comma

		JSONObject addTestsObj = new JSONObject();
		addTestsObj.put("issues", issueIds);
		addTestsObj.put("method", "1");
		addTestsObj.put("projectId", projectId);
		addTestsObj.put("versionId", "-1");

		StringEntity addTestsJSON = null;
		try {
			addTestsJSON = new StringEntity(addTestsObj.toString());
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		String ID = cc.addTestsToCycle(addTestsUri, client, accessKey, addTestsJSON);
		LogUtil.infoLog(CreateCycleAndAddTests.class, "ID is " + ID);
	}

	@SuppressWarnings("resource")
	public String createCycle(String uriStr, ZFJCloudRestClient client, String accessKey, StringEntity cycleJSON)
			throws URISyntaxException, JSONException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		LogUtil.infoLog(CreateCycleAndAddTests.class, "URI is " + uri.toString());
		LogUtil.infoLog(CreateCycleAndAddTests.class, "JWT is " + jwt);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();

		HttpPost createCycleReq = new HttpPost(uri);
		createCycleReq.addHeader("Content-Type", "application/json");
		createCycleReq.addHeader("Authorization", jwt);
		createCycleReq.addHeader("zapiAccessKey", accessKey);
		createCycleReq.setEntity(cycleJSON);

		try {
			response = restClient.execute(createCycleReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		LogUtil.infoLog(CreateCycleAndAddTests.class, "Status Code is " + statusCode);
		String cycleId = "-1";
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();
			String string = null;
			try {
				string = EntityUtils.toString(entity);
				JSONObject cycleObj = new JSONObject(string);
				cycleId = cycleObj.getString("id");
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return cycleId;
	}

	@SuppressWarnings({ "resource", "unused" })
	public String addTestsToCycle(String uriStr, ZFJCloudRestClient client, String accessKey, StringEntity addTestsJSON)
			throws URISyntaxException, JSONException, IllegalStateException, IOException {

		URI uri = new URI(uriStr);
		int expirationInSec = 360;
		JwtGenerator jwtGenerator = client.getJwtGenerator();
		String jwt = jwtGenerator.generateJWT("POST", uri, expirationInSec);
		LogUtil.infoLog(CreateCycleAndAddTests.class, "URI is " + uri.toString());
		LogUtil.infoLog(CreateCycleAndAddTests.class, "JWT is " + jwt);

		HttpResponse response = null;
		HttpClient restClient = new DefaultHttpClient();

		HttpPost addTestsReq = new HttpPost(uri);
		addTestsReq.addHeader("Content-Type", "application/json");
		addTestsReq.addHeader("Authorization", jwt);
		addTestsReq.addHeader("zapiAccessKey", accessKey);
		addTestsReq.setEntity(addTestsJSON);

		try {
			response = restClient.execute(addTestsReq);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		int statusCode = response.getStatusLine().getStatusCode();
		LogUtil.infoLog(CreateCycleAndAddTests.class, "Status Code is " + statusCode);
		LogUtil.infoLog(CreateCycleAndAddTests.class, "Response is " + response.toString());
		String string = null;
		if (statusCode >= 200 && statusCode < 300) {
			HttpEntity entity = response.getEntity();
			try {
				string = EntityUtils.toString(entity);
				JSONObject cycleObj = new JSONObject(entity);
			} catch (ParseException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			try {
				throw new ClientProtocolException("Unexpected response status: " + statusCode);
			} catch (ClientProtocolException e) {
				e.printStackTrace();
			}
		}
		return string;
	}

}
