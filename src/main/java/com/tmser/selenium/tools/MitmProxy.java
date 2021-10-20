package com.tmser.selenium.tools;

import com.deep007.mitmproxyjava.filter.FlowFilter;
import com.deep007.mitmproxyjava.mitmproxy.RemoteMitmproxy;
import com.deep007.mitmproxyjava.modle.FlowRequest;
import com.deep007.mitmproxyjava.modle.FlowResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Service
public class MitmProxy {
    private static final Logger logger = LoggerFactory.getLogger(MitmProxy.class);

    @Autowired
    private MitmProxyConfig mitmProxyConfig;

    private RemoteMitmproxy remoteMitmproxy;


    public boolean start(){
        if(remoteMitmproxy != null){
            logger.info("proxy was started");
            return true;
        }
        logger.info("proxy starting ...");
        remoteMitmproxy = new RemoteMitmproxy(mitmProxyConfig.getProxyHubIp(), mitmProxyConfig.getProxyHubPort(),
                mitmProxyConfig.getBindIp(),mitmProxyConfig.getBindPort());

        remoteMitmproxy.addFlowFilter(new FlowFilter() {

            @Override
            public void filterRequest(FlowRequest flowRequest) {
            }

            private boolean needFilter(FlowRequest flowRequest) {
                String url = flowRequest.getUrl();
                logger.info("filter url: {}", url);
                String method = flowRequest.getMethod();
                if("get".equals(method.toLowerCase()) && url.contains("ti.com")){
                    String uri = url.substring(8);
                    if(!uri.substring(uri.indexOf('/')).contains(".")){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void filterResponse(FlowResponse flowResponse) {
                FlowRequest flowRequest = flowResponse.getRequest();
                if (needFilter(flowRequest)) {
                   String oldContent = flowResponse.getContentAsString();
                   //String newContent = oldContent.replaceAll("\\\\x24","\\\\x24\\\\x5F");
                   logger.info("replace success");
                   //flowResponse.setContentAsString(newContent);
                }
            }

        });
        remoteMitmproxy.start();
        logger.info("proxy started");
        return true;
    }

    @PreDestroy
    private void stop(){
        if(remoteMitmproxy != null){
            remoteMitmproxy.stop();
        }
    }
}
