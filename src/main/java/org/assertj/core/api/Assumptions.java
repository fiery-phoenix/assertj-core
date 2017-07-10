package org.assertj.core.api;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.assertj.core.util.CheckReturnValue;

public class Assumptions {

  @CheckReturnValue
  public static AbstractCharSequenceAssert<?, String> assumeThat(String actual) {
    return assumeThat(StringAssert.class, String.class, actual);
  }

  private static <ASSERTION, ACTUAL> ASSERTION assumeThat(Class<ASSERTION> assertionType, Class<ACTUAL> actualType,
                                                          Object actual) {
    Enhancer enhancer = new Enhancer();
    enhancer.setSuperclass(assertionType);
    enhancer.setCallback((MethodInterceptor) (o, method, args, methodProxy) -> {
      try {
        return methodProxy.invokeSuper(o, args);
      } catch (AssertionError e) {
        throw (RuntimeException) Class.forName("org.junit.AssumptionViolatedException")
          .getConstructor(String.class, Throwable.class).newInstance("", e);
      }
    });

    return (ASSERTION) enhancer.create(new Class[] { actualType }, new Object[] { actual });
  }
}
