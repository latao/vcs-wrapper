package com.teclick.tools.vcs.git;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.List;

/**
 * Created by Nelson on 2017-06-09 21:43.
 * ResponseHttpHeadersInterceptor
 */
public class ResponseHttpHeadersInterceptor extends AbstractPhaseInterceptor<Message> {

    public ResponseHttpHeadersInterceptor() {
        super(Phase.POST_INVOKE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Message outMessage = message.getExchange().getOutMessage();
        List objectList = outMessage.get(List.class);
        for (Object obj : objectList) {
            if (obj.getClass().isAssignableFrom(ResponseHttpHeaders.class)) {
                ((ResponseHttpHeaders)obj).setHeaders(message);
            }
        }
    }
}
