package com.auxime.contract.proxy;

import java.util.Optional;
import java.util.UUID;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.auxime.contract.model.ProfileInfo;

/**
 * @author Nicolas
 * @version 1.0.0
 * @since 1.0.0
 *
 */
@FeignClient(value = "microserviceprofiles", url = "http://localhost:8500")
public interface AccountFeign {

	@GetMapping(value = "/api/accountManagement/accounts/doExist", produces = MediaType.APPLICATION_JSON_VALUE)
	boolean getAccountsyExist(@RequestParam("accountId") UUID id);

	@GetMapping(value = "/api/accountManagement/accounts/detailsFromAccount", produces = MediaType.APPLICATION_JSON_VALUE)
	Optional<ProfileInfo> getProfilesFromAccountId(@RequestParam("accountId") UUID id);

}
