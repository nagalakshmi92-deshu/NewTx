package utilities;

/**
 * This CommonSetting class help in generate results
 *
 */
public class CommonSettings {

	private String appType;
	private String appEnviornment;
	private String projectName;
	private String emailOutput;
	private String emailId;
	private String htmlReport;
	private String xlsReport;
	private String testLogs;
	private String executionEnv;
	private String cloudProvider;
	private String hostName;
	private String key;
	private String remoteOS;
	private String BuildNumber;
	private String Browser;
	private String Url;
	private String ManageToolName;
	private String testlinkTool;
	private String testLinkHostName;
	private String testlinkAPIKey;
	private String testlinkProjectName;
	private String testlinkPlanName;
	private String jiraTestManagement;
	private String jiraCycleID;
	private String jiraProjectID;
	private String bugToolName;
	private String bugTool;
	private String bugToolHostName;
	private String bugToolUserName;
	private String bugToolPassword;
	private String bugToolProjectName;

	/**
	 * @param projectName
	 * @param appType
	 * @param appEnviornment
	 * @param emailOutput
	 * @param emailId
	 * @param htmlReport
	 * @param xlsReport
	 * @param testLogs
	 * @param defectMgmt
	 * @param testMgmt
	 */
	public CommonSettings(String projectName, String appType, String appEnviornment, String emailOutput, String emailId,
			String htmlReport, String xlsReport, String testLogs, String defectMgmt, String testMgmt) {
		super();
		this.projectName = projectName;
		this.appType = appType;
		this.appEnviornment = appEnviornment;
		this.emailOutput = emailOutput;
		this.emailId = emailId;
		this.htmlReport = htmlReport;
		this.xlsReport = xlsReport;
		this.testLogs = testLogs;
	}

	/**
	 * 
	 */
	public CommonSettings() {
		super();
	}

	/**
	 * @param projectName
	 */
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	/**
	 * @return
	 */
	public String getProjectName() {
		LogUtil.infoLog(getClass(), "Project Name: " + projectName);
		return projectName;
	}

	/**
	 * @return
	 */
	public String getAppType() {
		LogUtil.infoLog(getClass(), appType);
		return appType;
	}

	/**
	 * @param appType
	 */
	public void setAppType(String appType) {
		this.appType = appType;
	}

	/**
	 * @return
	 */
	public String getAppEnviornment() {
		LogUtil.infoLog(getClass(), "App Environment: " + appEnviornment);
		return appEnviornment;
	}

	/**
	 * @param appEnviornment
	 */
	public void setAppEnviornment(String appEnviornment) {
		this.appEnviornment = appEnviornment;
	}

	/**
	 * @return
	 */
	public String getEmailOutput() {
		LogUtil.infoLog(getClass(), "Email Output: " + emailOutput);
		return emailOutput;
	}

	/**
	 * @param emailOutput
	 */
	public void setEmailOutput(String emailOutput) {
		this.emailOutput = emailOutput;
	}

	/**
	 * @return
	 */
	public String getEmailIds() {
		LogUtil.infoLog(getClass(), "Email id: " + emailId);
		return emailId;
	}

	/**
	 * @param emailId
	 */
	public void setEmailIds(String emailId) {
		this.emailId = emailId;
	}

	/**
	 * @return
	 */
	public String getHtmlReport() {
		LogUtil.infoLog(getClass(), "HTML Report: " + htmlReport);
		return htmlReport;
	}

	/**
	 * @param htmlReport
	 */
	public void setHtmlReport(String htmlReport) {
		this.htmlReport = htmlReport;
	}

	/**
	 * @return
	 */
	public String getXlsReport() {
		LogUtil.infoLog(getClass(), "XLS Report: " + xlsReport);
		return xlsReport;
	}

	/**
	 * @param xlsReport
	 */
	public void setXlsReport(String xlsReport) {
		this.xlsReport = xlsReport;
	}

	/**
	 * @return
	 */
	public String getTestLogs() {
		LogUtil.infoLog(getClass(), "Test Logs: " + testLogs);
		return testLogs;
	}

	/**
	 * @param testLogs
	 */
	public void setTestLogs(String testLogs) {
		this.testLogs = testLogs;
	}

	public String getExecutionEnv() {
		LogUtil.infoLog(getClass(), "Execution environment: " + executionEnv);
		return executionEnv;
	}

	public void setExecutionEnv(String executionEnv) {
		this.executionEnv = executionEnv;
	}

	public String getCloudProvider() {
		LogUtil.infoLog(getClass(), "Cloud Provider: " + cloudProvider);
		return cloudProvider;
	}

	public void setCloudProvider(String cloudProvider) {
		this.cloudProvider = cloudProvider;
	}

	public String getHostName() {
		LogUtil.infoLog(getClass(), "Host Name: " + hostName);
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getRemoteOS() {
		LogUtil.infoLog(getClass(), "Remote OS: " + remoteOS);
		return remoteOS;
	}

	public void setRemoteOS(String remoteOS) {
		this.remoteOS = remoteOS;
	}

	public String getBuildNumber() {
		LogUtil.infoLog(getClass(), "Build Number: " + BuildNumber);
		return BuildNumber;
	}

	public void setBuildNumber(String buildNumber) {
		BuildNumber = buildNumber;
	}

	public String getBrowser() {
		LogUtil.infoLog(getClass(), "Browser: " + Browser);
		return Browser;
	}

	public void setBrowser(String browser) {
		Browser = browser;
	}

	public String getUrl() {
		LogUtil.infoLog(getClass(), "URL: " + Url);
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getTestlinkTool() {
		LogUtil.infoLog(getClass(), "Test Link Tool: " + testlinkTool);
		return testlinkTool;
	}

	public void setTestlinkTool(String testlinkTool) {
		this.testlinkTool = testlinkTool;
	}

	public String getTestLinkHostName() {
		LogUtil.infoLog(getClass(), "Test Link Host Name: " + testLinkHostName);
		return testLinkHostName;
	}

	public void setTestLinkHostName(String testLinkHostName) {
		this.testLinkHostName = testLinkHostName;
	}

	public String getTestlinkAPIKey() {
		LogUtil.infoLog(getClass(), "Test Link API Key: " + testlinkAPIKey);
		return testlinkAPIKey;
	}

	public void setTestlinkAPIKey(String testlinkAPIKey) {
		this.testlinkAPIKey = testlinkAPIKey;
	}

	public String getTestlinkProjectName() {
		LogUtil.infoLog(getClass(), "Test Link Project Name: " + testlinkProjectName);
		return testlinkProjectName;
	}

	public void setTestlinkProjectName(String testlinkProjectName) {
		this.testlinkProjectName = testlinkProjectName;
	}

	public String getTestlinkPlanName() {
		LogUtil.infoLog(getClass(), "Test Link Plan Name: " + testlinkPlanName);
		return testlinkPlanName;
	}

	public void setTestlinkPlanName(String testlinkPlanName) {
		this.testlinkPlanName = testlinkPlanName;
	}

	public String getBugToolName() {
		LogUtil.infoLog(getClass(), "Bug Tool Name: " + bugToolName);
		return bugToolName;
	}

	public void setBugToolName(String bugToolName) {
		this.bugToolName = bugToolName;
	}

	public String getbugTool() {
		LogUtil.infoLog(getClass(), "Bug Tool: " + bugTool);
		return bugTool;
	}

	public void setbugTool(String bugTool) {
		this.bugTool = bugTool;
	}

	public String getbugToolHostName() {
		LogUtil.infoLog(getClass(), "Bug Tool Host Name: " + bugToolHostName);
		return bugToolHostName;
	}

	public void setbugToolHostName(String bugToolHostName) {
		this.bugToolHostName = bugToolHostName;
	}

	public String getbugToolUserName() {
		LogUtil.infoLog(getClass(), "Bug Tool User Name: " + bugToolUserName);
		return bugToolUserName;
	}

	public void setbugToolUserName(String bugToolUserName) {
		this.bugToolUserName = bugToolUserName;
	}

	public String getbugToolPassword() {
		return bugToolPassword;
	}

	public void setbugToolPassword(String bugToolPassword) {
		this.bugToolPassword = bugToolPassword;
	}

	public String getbugToolProjectName() {
		LogUtil.infoLog(getClass(), "Bug Tool Project Name: " + bugToolProjectName);
		return bugToolProjectName;
	}

	public void setbugToolProjectName(String bugToolProjectName) {
		this.bugToolProjectName = bugToolProjectName;
	}

	public String getJiraCycleID() {
		LogUtil.infoLog(getClass(), "JIRA Cycle ID: " + jiraCycleID);
		return jiraCycleID;
	}

	public void setJiraCycleID(String jiraCycleID) {
		this.jiraCycleID = jiraCycleID;
	}

	public String getJiraProjectID() {
		LogUtil.infoLog(getClass(), "JIRA Projct ID: " + jiraProjectID);
		return jiraProjectID;
	}

	public void setJiraProjectID(String jiraProjectID) {
		this.jiraProjectID = jiraProjectID;
	}

	public String getJiraTestManagement() {
		LogUtil.infoLog(getClass(), "JIRA Test Management: " + jiraTestManagement);
		return jiraTestManagement;
	}

	public void setJiraTestManagement(String jiraTestManagement) {
		this.jiraTestManagement = jiraTestManagement;
	}

	public String getManageToolName() {
		LogUtil.infoLog(getClass(), "Manage Tool Name: " + ManageToolName);
		return ManageToolName;
	}

	public void setManageToolName(String manageToolName) {
		this.ManageToolName = manageToolName;
	}

}
