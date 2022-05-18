package io.mosip.idrepo.service;

import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.mosip.idrepo.util.RestUtil;

@Service
public class MigrationService {

	private static final Logger logger = LoggerFactory.getLogger(MigrationService.class);
	@Autowired
	RestUtil restUtil;

	@Autowired
	private Environment environment;
	
	@Autowired
	private ObjectMapper mapper;

	public void initialize() {
		try {
			createDraft("987974586605995");
			getDraft("987974586605995");
			updateDraft("987974586605995");
			publishDraft("987974586605995");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void getData() {

	}

	@SuppressWarnings("unchecked")
	public void createDraft(String regId) {
		List<String> pathsegments = List.of(regId);
		logger.info("createDraft :: creating draft for registration id : " + regId);
		Map<String, Object> response = null;
		ArrayList<String> errors = new ArrayList<>();
		try {
			response = restUtil.postApi(environment.getProperty("draft.create.rest.uri"), pathsegments, "", "",
					MediaType.APPLICATION_JSON, null, Map.class);
			if (response.containsKey("errors")) {
				errors = (ArrayList<String>) response.get("errors");
				if (errors.size() > 0) {
					logger.error("Error :: createDraft :: registration id :: " + regId + " " + response.get("errors"));
				} else {
					Map<String, String> res=mapper.readValue(mapper.writeValueAsString(response.get("response")),
							Map.class);					
					logger.info("createDraft :: created draft for registration id : " + regId + ":: status: " + res.get("status"));
				}
			}
		} catch (Exception e) {
			logger.error("Error :: createDraft :: registration id :: " + regId + " " + e.getMessage());
			e.printStackTrace();
		}		
	}

	@SuppressWarnings("unchecked")
	public void getDraft(String regId) {
		List<String> pathsegments = List.of(regId);
		logger.info("getDraft :: gettting draft for registration id : " + regId);		
		Map<String, Object> getApiResponse = null;
		try {
			getApiResponse = restUtil.getApi(environment.getProperty("draft.get.rest.uri"), pathsegments, "", "",
					Map.class);
			if (getApiResponse.containsKey("errors")) {
				ArrayList<String> errors = (ArrayList<String>) getApiResponse.get("errors");
				if (errors.size() > 0) {
					logger.error(
							"Error :: getDraft :: registration id :: " + regId + " " + getApiResponse.get("errors"));
				} else {
					Map<String, String> res=mapper.readValue(mapper.writeValueAsString(getApiResponse.get("response")),
							Map.class);
					Map<String, String> identity = mapper.readValue(mapper.writeValueAsString(res.get("identity")),
							Map.class);
					logger.info("getDraft :: gettting draft for registration id : " + regId + "::" + " uin "+identity.get("UIN"));
				}
			}
		} catch (Exception e) {
			logger.error("Error :: getDraft :: registration id :: " + regId + " " + e.getMessage());
			e.printStackTrace();
		}

		

	}

	@SuppressWarnings("unchecked")
	public void updateDraft(String regId) {
		byte[] decodedBytes = Base64.getDecoder().decode(encryptedRequest);
		String decodedString = new String(decodedBytes);
		List<String> pathsegments = List.of(regId);
		logger.info("updateDraft :: updating draft for registration id : " + regId);
		Map<String, Object> response = null;
		try {
			response = restUtil.patchApi(environment.getProperty("draft.update.rest.uri"), pathsegments, "", "",
					MediaType.APPLICATION_JSON, decodedString, Map.class);
			if (response.containsKey("errors")) {				
				ArrayList<String> errors = (ArrayList<String>) response.get("errors");
				if (errors.size() > 0) {
					logger.error("Error :: createDraft :: registration id :: " + regId + " " + response.get("errors"));
				} else {
					Map<String, String> res=mapper.readValue(mapper.writeValueAsString(response.get("response")),
							Map.class);					
					logger.info("updateDraft :: updated draft for registration id : " + regId + ":: status " + res.get("status"));
				}				
			}
		} catch (Exception e) {
			logger.error("Error :: getDraft :: registration id :: " + regId + " " + e.getMessage());
			e.printStackTrace();
		}		
	}

	@SuppressWarnings("unchecked")
	public void publishDraft(String regId) {
		List<String> pathsegments = List.of(regId);
		logger.info("publishDraft :: publishing draft for registration id : " + regId);
		Map<String, Object> response = null;
		try {
			response = restUtil.getApi(environment.getProperty("draft.publish.rest.uri"), pathsegments, "", "",
					Map.class);
			if (response.containsKey("errors")) {				
				ArrayList<String> errors = (ArrayList<String>) response.get("errors");
				if (errors.size() > 0) {
					logger.error("Error :: createDraft :: registration id :: " + regId + " " + response.get("errors"));
				} else {
					Map<String, String> res=mapper.readValue(mapper.writeValueAsString(response.get("response")),
							Map.class);
					logger.info("updateDraft :: updated draft for registration id : " + regId + ":: status " + res.get("status"));
				}				
			} 
		} catch (Exception e) {
			logger.error("Error :: publishDraft :: registration id :: " + regId + " " + e.getMessage());
			e.printStackTrace();
		}		
	}
}