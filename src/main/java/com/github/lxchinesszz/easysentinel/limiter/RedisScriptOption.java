package com.github.lxchinesszz.easysentinel.limiter;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

/**
 * 封装redis操作
 *
 * @author liuxin
 * @version Id: RedisScriptOption.java, v 0.1 2019-04-21 22:04
 */
@RequiredArgsConstructor
public class RedisScriptOption {
    @Getter(AccessLevel.PROTECTED)
    private final List<String> keys;

    @Getter(AccessLevel.PROTECTED)
    private final List<String> argv;

    public int keyCount() {
        return keys.size();
    }

    public String[] argvs() {
        return argv.toArray(new String[]{});
    }
}
