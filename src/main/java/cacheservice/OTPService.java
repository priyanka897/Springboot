package cacheservice;

import java.sql.SQLException;

public interface OTPService 
{
	public String OTPCallCashing(String policyNo,String sessionId, String identifier) throws SQLException , Exception;

}
