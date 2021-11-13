package com.tmser.selenium.tools;

import com.deep007.mitmproxyjava.filter.FlowFilter;
import com.deep007.mitmproxyjava.mitmproxy.RemoteMitmproxy;
import com.deep007.mitmproxyjava.modle.FlowRequest;
import com.deep007.mitmproxyjava.modle.FlowResponse;
import io.grpc.internal.JsonParser;
import io.grpc.internal.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;

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
            public void filterRequest(FlowRequest flowRequest) throws InterruptedException {
            }

            private boolean needFilter(FlowRequest flowRequest) {
                String url = flowRequest.getUrl();
                String method = flowRequest.getMethod();
                if("post".equals(method.toLowerCase()) && url.contains("/cart/inventory")){
                    String uri = url.substring(8);
                    if(!uri.substring(uri.indexOf('/')).contains(".")){
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void filterResponse(FlowResponse flowResponse) throws IOException {
                FlowRequest flowRequest = flowResponse.getRequest();
                if (needFilter(flowRequest)) {
                    int statusCode = flowResponse.getStatusCode();
                    if(statusCode == 200){
                        String oldContent = flowResponse.getContentAsString();
                        //String newContent = oldContent.replaceAll("\\\\x24","\\\\x24\\\\x5F");

                        Object result = JsonParser.parse(oldContent);
                        List<?> opnList = JsonUtil.getList((Map<String, ?>) result, "opn_list");
                        if(opnList != null && opnList.size() > 0){
                            Integer quantity = JsonUtil.getNumberAsInteger((Map<String, ?>) opnList.get(0), "quantity");
                            String name = JsonUtil.getString((Map<String, ?>) opnList.get(0),"orderable_number");
                            logger.info("success code: {}, count:{}", name, quantity );
                            Semaphore semaphore = TiSeleniumTools.semaphoreMap.get(name);
                            if(semaphore != null){
                                TiSeleniumTools.resultMap.put(name, quantity);
                                semaphore.release();
                            }
                        }

                        flowResponse.setContentAsString(oldContent);
                    }
                }
            }

        });
        remoteMitmproxy.start();
        logger.info("proxy started");
        return true;
    }

    @PreDestroy
    public void stop(){
        if(remoteMitmproxy != null){
            remoteMitmproxy.stop();
        }
    }
}
