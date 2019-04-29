package com.github.lxchinesszz.easysentinel;

/**
 * @author liuxin
 * @version Id: BaseSyntaxTest.java, v 0.1 2019-04-26 16:02
 */
public class BaseSyntaxTest {


  public static void main(String[] args) {
    String gangstar_ = doService("Gangstar ");
    System.out.println(gangstar_);
  }

  public static String doService(String name) {
    BaseSyntaxTest bst = new BaseSyntaxTest();
    bst.preProcessor();
    try {
      return bst.invoker(name);
    } catch (Exception e) {
      return "Mock::" + name;
    } finally {
      bst.afterProcessor();
    }
  }

  public String invoker(String name) {
    return "Invoker::" + name;
  }

  public void preProcessor() {
    System.out.println("业务处理前");
  }

  public void afterProcessor() {
    System.out.println("业务处理后");
  }
}
