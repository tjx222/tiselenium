package com.tmser.selenium.tools;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

import java.io.Serializable;


@Component
@ConfigurationProperties(prefix = "mitm")
public class MitmProxyConfig implements Serializable {

    private String proxyHubIp = "127.0.0.1";

    private String bindIp = "127.0.0.1";

    private Integer proxyHubPort = 60051;

    private Integer bindPort = 8866;

    public String getProxyHubIp() {
        return proxyHubIp;
    }

    public void setProxyHubIp(String proxyHubIp) {
        this.proxyHubIp = proxyHubIp;
    }

    public String getBindIp() {
        return bindIp;
    }

    public void setBindIp(String bindIp) {
        this.bindIp = bindIp;
    }

    public Integer getProxyHubPort() {
        return proxyHubPort;
    }

    public void setProxyHubPort(Integer proxyHubPort) {
        this.proxyHubPort = proxyHubPort;
    }

    public Integer getBindPort() {
        return bindPort;
    }

    public void setBindPort(Integer bindPort) {
        this.bindPort = bindPort;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("proxyHubIp", proxyHubIp)
                .append("proxyHubPort", proxyHubPort)
                .append("bindIp", bindIp)
                .append("bindPort", bindPort)
                .toString();
    }
}