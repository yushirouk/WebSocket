package com.polling.longpolling;


import org.springframework.web.context.request.async.DeferredResult;

public class LongPollingSession {

	private final String sessionId;
    private final DeferredResult<String> deferredResult;

    public LongPollingSession(final String sessionId, final DeferredResult<String> deferredResult) {
        this.sessionId = sessionId;
        this.deferredResult = deferredResult;
    }

    public String getSessionId() {
        return sessionId;
    }

    public DeferredResult<String> getDeferredResult() {
        return deferredResult;
    }
}
