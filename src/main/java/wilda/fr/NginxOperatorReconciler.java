package wilda.fr;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

public class NginxOperatorReconciler implements Reconciler<NginxOperator> { 
  private final KubernetesClient client;

  public NginxOperatorReconciler(KubernetesClient client) {
    this.client = client;
  }

  // TODO Fill in the rest of the reconciler

  @Override
  public UpdateControl<NginxOperator> reconcile(NginxOperator resource, Context context) {
    // TODO: fill in logic

    return UpdateControl.noUpdate();
  }
}

