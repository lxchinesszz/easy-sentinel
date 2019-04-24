package com.github.lxchinesszz.easysentinel.registory;

import com.github.lxchinesszz.easysentinel.util.MD5;
import lombok.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author liuxin
 * @version Id: EasySentinelInstance.java, v 0.1 2019-04-22 17:34
 */
@Setter
@Getter
@ToString
public class EasySentinelInstance implements Serializable {

    private String id;

    private String appName;

    private String host;

    private boolean cover;

    private Set<ResourceNameMeta> resourceNameMetas;


    public EasySentinelInstance(@NonNull String appName, @NonNull String host) {
        this.appName = appName;
        this.host = host;
        this.id = createId(appName, host);
    }

    public String createId(String appName, String host) {
        byte[] binaryData = (appName + host).getBytes();
        return MD5.encodeMd5(binaryData);
    }

    public boolean addResourceName(ResourceNameMeta resourceNameMeta) {
        if (null == resourceNameMetas) {
            resourceNameMetas = new HashSet();
        }
        return resourceNameMetas.add(resourceNameMeta);
    }


}
