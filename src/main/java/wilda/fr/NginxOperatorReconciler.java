package wilda.fr;

import io.fabric8.kubernetes.client.KubernetesClient;
import io.javaoperatorsdk.operator.api.reconciler.Context;
import io.javaoperatorsdk.operator.api.reconciler.DeleteControl;
import io.javaoperatorsdk.operator.api.reconciler.Reconciler;
import io.javaoperatorsdk.operator.api.reconciler.UpdateControl;

public class NginxOperatorReconciler implements Reconciler<NginxOperator> { 
  private final KubernetesClient client;

  public NginxOperatorReconciler(KubernetesClient client) {
    this.client = client;
  }

  @Override
  public UpdateControl<NginxOperator> reconcile(NginxOperator resource, Context context) {

    System.out.println(String.format("Hello %s 🎉🎉 !!", resource.getSpec().getName()));

    return UpdateControl.noUpdate();
  }

  @Override
  public DeleteControl cleanup(NginxOperator resource, Context context) {
    System.out.println(String.format("Goodbye %s 😢", resource.getSpec().getName()));

    return Reconciler.super.cleanup(resource, context);
  }
}

