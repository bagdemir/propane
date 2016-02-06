package io.moo.propane.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

/**
 * Created by bagdemir on 30/01/16.
 */
public class ContextInfo {

  public enum Regions {
    UE1, UW1, UW2, EW1, EC1, APS1, APN1, APS2, APN2, SAE1, US, EU, AP, SA
  }

  public enum Environments {
    DEV, QE, QA, STAGE, PROD
  }

  public enum Stacks {
    A, B, BLUE, GREEN
  }

  private final List<String> contextIds = Collections.synchronizedList(Lists.newArrayList());


  public void put(final String... contexts) {
    Preconditions.checkNotNull(contexts, "contexts may not be null.");
    contextIds.addAll(Arrays.asList(contexts));
  }


  public void put(final String context) {
    Preconditions.checkNotNull(context, "context may not be null.");
    contextIds.add(context);
  }


  public List<String> getContexts() {
    return contextIds;
  }


  public static ContextInfo of(String... id) {
    final ContextInfo contextInfo = new ContextInfo();
    contextInfo.put(id);
    return contextInfo;
  }
}
