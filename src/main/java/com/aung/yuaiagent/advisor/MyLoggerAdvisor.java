package com.aung.yuaiagent.advisor;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.advisor.api.*;
import org.springframework.ai.chat.model.MessageAggregator;
import reactor.core.publisher.Flux;


@Slf4j
public class MyLoggerAdvisor implements CallAroundAdvisor, StreamAroundAdvisor {

	private static final Logger logger = LoggerFactory.getLogger(MyLoggerAdvisor.class);

	@Override
	public String getName() { 
		return this.getClass().getSimpleName();
	}

	@Override
	public int getOrder() { 
		return 0;
	}


	/*
	** interception processing
	* @param advisedRequest
	* @return
	 */
	private AdvisedRequest before(AdvisedRequest advisedRequest) {
		//interception Concept Function,authorization , or violet word etc.
		log.info("AI Request------------------: {}", advisedRequest.userText());
		return advisedRequest;
	}

	private void observeAfter(AdvisedResponse advisedResponse) {
		if (advisedResponse.response() != null) {
			log.info("AI Response----------------: {}", advisedResponse.response().getResult().getOutput().getText()); // allocated the result function
		}
	}
	@Override
	public AdvisedResponse aroundCall(AdvisedRequest advisedRequest, CallAroundAdvisorChain chain) {

		advisedRequest = before(advisedRequest); //specific function

		AdvisedResponse advisedResponse = chain.nextAroundCall(advisedRequest);

		observeAfter(advisedResponse); //specific function

		return advisedResponse;
	}

	@Override
	public Flux<AdvisedResponse> aroundStream(AdvisedRequest advisedRequest, StreamAroundAdvisorChain chain) {

		logger.debug("BEFORE: {}", advisedRequest);

		Flux<AdvisedResponse> advisedResponses = chain.nextAroundStream(advisedRequest);

        return new MessageAggregator().aggregateAdvisedResponse(advisedResponses,
                    advisedResponse -> logger.debug("AFTER: {}", advisedResponse)); 
	}
}