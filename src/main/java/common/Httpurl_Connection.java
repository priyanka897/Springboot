package commons;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ResourceBundle;

import javax.net.ssl.HttpsURLConnection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.qc.api.service.impl.OTPServiceimpl;

public class Httpurl_Connection 
{
	private static Logger logger = LogManager.getLogger(Httpurl_Connection.class);
	public String httpConnection_response(String policyNo, String methodidentifier)
	{
		logger.info("Inside Method:: httpConnection_response ");
		StringBuilder result = new StringBuilder();
		String output = new String();
		ResourceBundle res = ResourceBundle.getBundle("errorMessages");
		String soaCorrelationId = "CorelationId"+System.currentTimeMillis();
		HttpURLConnection conn = null;
		String soaMsgVersion=""; String soaAppID=""; String soaUserID=""; String soaUserPswd="";
		String applicationurl=""; String docID = "PRM23";	String SendTo = "C"; String docDispatchMode = "E";
		String fromDate = "04/01/2016";	String toDate = "03/31/2017";

		if("OTP".equalsIgnoreCase(methodidentifier))
		{
			logger.info("Method Identifier :-  " +methodidentifier );
			logger.info("");
			soaMsgVersion=res.getString("soaMsgVersion");
			soaAppID=res.getString("soaAppID");
			soaUserID=res.getString("soaUserID");
			soaUserPswd=res.getString("soaUserPswd");
			applicationurl=res.getString("Soa_url_OTP");

		}else if("PolicyInfo".equalsIgnoreCase(methodidentifier))
		{
			logger.info("Method Identifier :-  " +methodidentifier );
			soaMsgVersion=res.getString("soaMsgVersion");
			soaAppID=res.getString("soaAppID");
			soaUserID=res.getString("soaUserIDProd");
			soaUserPswd=res.getString("soaUserPasswordProd");
			applicationurl=res.getString("Soa_url_policy360");
		}else if("PolicyDetail".equalsIgnoreCase(methodidentifier))
		{
			logger.info("Method Identifier :-  " +methodidentifier );
			soaMsgVersion=res.getString("soaMsgVersion");
			soaAppID=res.getString("soaAppID");
			soaUserID=res.getString("soaUserIDProd");
			soaUserPswd=res.getString("soaUserPasswordProd");
			applicationurl=res.getString("Soa_url_cushsurrender");
		}
		else
		{
			logger.info("Method Identifier :-  " +methodidentifier );
			soaMsgVersion=res.getString("soaMsgVersion");
			soaAppID=res.getString("soaAppID");
			soaUserID=res.getString("soaUserID");
			soaUserPswd=res.getString("soaUserPswd");
			applicationurl=res.getString("Soa_url_mlidocservice");
		}
		try {
			XTrustProvider trustProvider = new XTrustProvider();
			trustProvider.install();
			URL url = new URL(applicationurl);
			conn = (HttpURLConnection) url.openConnection();
			HttpsURLConnection.setFollowRedirects(true);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			StringBuilder requestdata = new StringBuilder();
			if(!"MLIDOC".equalsIgnoreCase(methodidentifier))
			{
				requestdata.append(" 	{	 ");
				requestdata.append(" 	   \"request\": {	 ");
				requestdata.append(" 	      \"header\": {	 ");
				requestdata.append(" 	         \"soaCorrelationId\": \"").append(soaCorrelationId).append("\",	 ");
				requestdata.append(" 	         \"soaMsgVersion\": \"").append(soaMsgVersion).append("\",	 ");
				requestdata.append(" 	         \"soaAppId\": \"").append(soaAppID).append("\",	 ");
				requestdata.append(" 	         \"soaUserId\": \"").append(soaUserID).append("\",	 ");
				requestdata.append(" 	         \"soaPassword\": \"").append(soaUserPswd).append("\"	 ");
				requestdata.append(" 	      },	 ");
				requestdata.append(" 	      \"requestData\": {	 ");
				requestdata.append(" 	         \"policyNumber\": \"").append(policyNo).append("\"	 ");
				requestdata.append(" 	      }	 ");
				requestdata.append(" 	   }	 ");
				requestdata.append(" 	}	 ");
				logger.info("Request Data For Hitting API : - "+requestdata.toString());
			}
			else
			{
				requestdata.append("{\"request\":{\"header\":{\"soaCorrelationId\":\"");
				requestdata.append(soaCorrelationId);
				requestdata.append("\",\"soaMsgVersion\":\"1.0\",\"soaAppId\":\"");
				requestdata.append(soaAppID);
				requestdata.append("\",\"soaUserId\":\"");
				requestdata.append(soaUserID);
				requestdata.append("\",\"soaPassword\":\"");
				requestdata.append(soaUserPswd);
				requestdata.append("\"},\"requestData\":{\"dispatchDocuments\":{\"policyNumber\":\"");
				requestdata.append(policyNo);
				requestdata.append("\",\"docId\":\"");
				requestdata.append(docID);
				requestdata.append("\",\"sendTo\":\"");
				requestdata.append(SendTo);
				requestdata.append("\",\"emailIdC\":\"\",\"emailIdA\":\"\",\"docDispatchMode\":\"");
				requestdata.append(docDispatchMode);
				requestdata.append("\",\"fromDate\":\"");
				requestdata.append(fromDate);
				requestdata.append("\",\"toDate\":\"");
				requestdata.append(toDate);
				requestdata.append(
						"\",\"fromYear\":\"\",\"toYear\":\"\",\"source\":\"\",\"machineIP\":\"\",\"uniqueTransId\":\"\",\"userId\":\"\",\"requestedBy\":\"\"}}}}");
				logger.info("Request Data For Hitting API : - "+requestdata.toString());

			}
			OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
			writer.write(requestdata.toString());
			writer.flush();
			try {
				writer.close();
			} catch (Exception e1) {
			}

			int apiResponseCode = conn.getResponseCode();
			logger.info("API Response Code : - " + apiResponseCode);
			if (apiResponseCode == 200) {
				BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
				while ((output = br.readLine()) != null) {
					result.append(output);
				}
				conn.disconnect();
				br.close();
			}
			else{
				return "InvalidResponse";
			}
		}
		catch(Exception e)
		{
			logger.info("Exception Occoured While Calling API's " + e);
		}
		logger.info("OutSide Method:: httpConnection_response ");
		return result.toString();
	}
}