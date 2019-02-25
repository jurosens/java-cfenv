/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.pivotal.cfenv.spring.boot;

import java.util.Map;

import io.pivotal.cfenv.core.CfCredentials;
import io.pivotal.cfenv.core.CfEnv;
import io.pivotal.cfenv.core.CfService;

/**
 * @author Mark Pollack
 * @author Scott Frederick
 */
public class CfSingleSignOnProcessor implements CfEnvProcessor {

	private static final String PIVOTAL_SSO_LABEL = "p-identity";

	@Override
	public CfService findService(CfEnv cfEnv) {
		return cfEnv.findServiceByLabel(PIVOTAL_SSO_LABEL);
	}

	@Override
	public String getPropertySourceName() {
		return "cfEnvSingleSignOn";
	}

	@Override
	public void process(CfCredentials cfCredentials, Map<String, Object> properties) {
		String clientId = cfCredentials.getString("client_id");
		String clientSecret = cfCredentials.getString("client_secret");
		String authDomain = cfCredentials.getString("auth_domain");

		properties.put("security.oauth2.client.clientId", clientId);
		properties.put("security.oauth2.client.clientSecret", clientSecret);
		properties.put("security.oauth2.client.accessTokenUri",
				authDomain + "/oauth/token");
		properties.put("security.oauth2.client.userAuthorizationUri",
				authDomain + "/oauth/authorize");
		properties.put("ssoServiceUrl", authDomain);
		properties.put("security.oauth2.resource.userInfoUri",
				authDomain + "/userinfo");
		properties.put("security.oauth2.resource.tokenInfoUri",
				authDomain + "/check_token");
		properties.put("security.oauth2.resource.jwk.key-set-uri",
				authDomain + "/token_keys");
	}
}
