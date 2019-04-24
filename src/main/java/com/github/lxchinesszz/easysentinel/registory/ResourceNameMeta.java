package com.github.lxchinesszz.easysentinel.registory;


import lombok.AllArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author liuxin
 * @version Id: ResourceNameMeta.java, v 0.1 2019-04-23 10:59
 */
@AllArgsConstructor
@ToString
public class ResourceNameMeta implements Serializable {
    public Integer Qps;
    public String resourceName;
    public String resourceClassPath;
}
