package wilda.fr;

import io.fabric8.kubernetes.api.model.Namespaced;
import io.fabric8.kubernetes.client.CustomResource;
import io.fabric8.kubernetes.model.annotation.Group;
import io.fabric8.kubernetes.model.annotation.Version;

@Version("v1")
@Group("fr.wilda")
public class NginxOperator extends CustomResource<NginxOperatorSpec, NginxOperatorStatus> implements Namespaced {}

