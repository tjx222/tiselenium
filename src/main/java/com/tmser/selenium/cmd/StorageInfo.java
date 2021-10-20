package com.tmser.selenium.cmd;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.style.ToStringCreator;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
@ConfigurationProperties(prefix = "storage")
public class StorageInfo implements Serializable {

    //商品id
    private String[] ids;

    public String[] getIds() {
        return ids;
    }

    public void setIds(String[] ids) {
        this.ids = ids;
    }

    @Override
    public String toString() {
        return new ToStringCreator(this)
                .append("type", ids)
                .toString();
    }
}
