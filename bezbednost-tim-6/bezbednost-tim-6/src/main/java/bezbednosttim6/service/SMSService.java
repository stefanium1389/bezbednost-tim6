package bezbednosttim6.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import ClickSend.ApiClient;

@Service
public class SMSService {
	
	@Value("${clickSend-username}")
	private String clickSendUsername;
	@Value("${clickSend-apiKey}")
	private String clickSendApiKey;
	
	@Bean
	@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
	public ApiClient clickSendConfig(){
	ApiClient clickSendApiClient = new ApiClient();
	clickSendApiClient.setUsername(clickSendUsername);
	clickSendApiClient.setPassword(clickSendApiKey);
	return clickSendApiClient;
	}

}
