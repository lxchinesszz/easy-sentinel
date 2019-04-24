package com.github.lxchinesszz.easysentinel.util;

import com.github.lxchinesszz.easysentinel.exception.BlockException;

/**
 * @author liuxin
 * @version Id: DefaultLimiterBlockHandler.java, v 0.1 2019-04-22 10:25
 */
public class DefaultLimiterBlockHandler {

  public Object fallBack(BlockException block, String userName) {
    return "DefaultLimiterBlockHandler::" + userName + block.getMessage();
  }


}
