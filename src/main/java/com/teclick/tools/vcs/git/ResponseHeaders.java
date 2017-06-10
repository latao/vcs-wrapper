package com.teclick.tools.vcs.git;

import org.apache.cxf.helpers.CastUtils;
import org.apache.cxf.message.Message;

import java.util.List;
import java.util.Map;

/**
 * Created by Nelson on 2017-06-09 22:42.
 * ResponseHeaders
 */
public class ResponseHeaders {

    private Map<String, List<String>> headers;

    public void setHeaders(Message message) {
        this.headers = CastUtils.cast((Map<?, ?>) message.get(Message.PROTOCOL_HEADERS));
    }

    public Map<String, List<String>> getHeaders() {
        return headers;
    }
}
