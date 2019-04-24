package com.github.lxchinesszz.easysentinel.registory;

import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author liuxin
 * @version Id: EasySentinelModelDO.java, v 0.1 2019-04-22 17:31
 */
@Builder
@ToString
public class ResourceMetaInfo implements Serializable {


    @Setter
    private List<EasySentinelInstance> easySentinelInstances;


    public void add(EasySentinelInstance easySentinelInstance) {
        if (easySentinelInstances == null) {
            easySentinelInstances = new ArrayList<>();
        }
        easySentinelInstances.add(easySentinelInstance);
    }

    public List<EasySentinelInstance> getEasySentinelInstances() {
        if (easySentinelInstances == null) {
            easySentinelInstances = new ArrayList<>();
        }
        return easySentinelInstances;
    }

}
