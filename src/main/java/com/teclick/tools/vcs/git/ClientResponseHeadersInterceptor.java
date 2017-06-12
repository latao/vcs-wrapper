package com.teclick.tools.vcs.git;

import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;

import java.util.List;

/**
 * Created by Nelson on 2017-06-09 21:43.
 * ClientResponseHeadersInterceptor
 */
public class ClientResponseHeadersInterceptor extends AbstractPhaseInterceptor<Message> {

    public ClientResponseHeadersInterceptor() {
        super(Phase.POST_INVOKE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        Message outMessage = message.getExchange().getOutMessage();
        List objectList = outMessage.get(List.class);
        if (null != objectList) {
            for (Object obj : objectList) {
                if ((null != obj) && (obj.getClass().isAssignableFrom(ClientResponseHeaders.class))) {
                    ((ClientResponseHeaders) obj).setHeaders(message);
                }
            }
        }
    }
}
