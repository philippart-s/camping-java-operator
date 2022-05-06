# Déroulé de la démo
## 🎉 Init project
 - la branche `01-init-project` contient le résultat de cette étape
 - [installer / mettre](https://sdk.operatorframework.io/docs/installation/) à jour la dernière version du [Operator SDK](https://sdk.operatorframework.io/) (v1.20.0 au moment de l'écriture du readme)
 - créer le répertoire `camping-java-operator`
 - dans le répertoire `camping-java-operator`, scaffolding du projet avec Quarkus : `operator-sdk init --plugins quarkus --domain fr.wilda --project-name camping-java-operator`
 - l'arborescence générée est la suivante:
    ```bash
    .
    ├── LICENSE
    ├── Makefile
    ├── PROJECT
    ├── README.md
    ├── pom.xml
    ├── src
    │   └── main
    │       ├── java
    │       └── resources
    │           └── application.properties
    ```
 - supprimer le `-operator` dans le nom du fichier `application.properties`:
    ```yaml
        quarkus.container-image.build=true
        #quarkus.container-image.group=
        quarkus.container-image.name=camping-java-operator
        # set to true to automatically apply CRDs to the cluster when they get regenerated
        quarkus.operator-sdk.crd.apply=false
        # set to true to automatically generate CSV from your code
        quarkus.operator-sdk.generate-csv=false
    ```
 - vérification que cela compile : `mvn clean compile`
 - tester le lancement: `mvn quarkus:dev`:
    ```bash
        __  ____  __  _____   ___  __ ____  ______ 
        --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
        -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
        --\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
        2022-05-03 11:43:19,423 WARN  [io.fab.kub.cli.Config] (Quarkus Main Thread) Found multiple Kubernetes config files [[/Users/sphilipp/dev/ovh/k8s/kubeconfig-example2.yml, /Users/sphilipp/dev/ovh/k8s/kubeconfig.yml, /Users/sphilipp/dev/ovh/k8s/k8s-colima.yml, /Users/sphilipp/dev/ovh/k8s/kubeconfig-trillio.yml]], using the first one: [/Users/sphilipp/dev/ovh/k8s/kubeconfig-example2.yml]. If not desired file, please change it by doing `export KUBECONFIG=/path/to/kubeconfig` on Unix systems or `$Env:KUBECONFIG=/path/to/kubeconfig` on Windows.

        2022-05-03 11:43:19,478 WARN  [io.fab.kub.cli.Config] (Quarkus Main Thread) Found multiple Kubernetes config files [[/Users/sphilipp/dev/ovh/k8s/kubeconfig-example2.yml, /Users/sphilipp/dev/ovh/k8s/kubeconfig.yml, /Users/sphilipp/dev/ovh/k8s/k8s-colima.yml, /Users/sphilipp/dev/ovh/k8s/kubeconfig-trillio.yml]], using the first one: [/Users/sphilipp/dev/ovh/k8s/kubeconfig-example2.yml]. If not desired file, please change it by doing `export KUBECONFIG=/path/to/kubeconfig` on Unix systems or `$Env:KUBECONFIG=/path/to/kubeconfig` on Windows.

        2022-05-03 11:43:19,482 WARN  [io.fab.kub.cli.Config] (Quarkus Main Thread) Found multiple Kubernetes config files [[/Users/sphilipp/dev/ovh/k8s/kubeconfig-example2.yml, /Users/sphilipp/dev/ovh/k8s/kubeconfig.yml, /Users/sphilipp/dev/ovh/k8s/k8s-colima.yml, /Users/sphilipp/dev/ovh/k8s/kubeconfig-trillio.yml]], using the first one: [/Users/sphilipp/dev/ovh/k8s/kubeconfig-example2.yml]. If not desired file, please change it by doing `export KUBECONFIG=/path/to/kubeconfig` on Unix systems or `$Env:KUBECONFIG=/path/to/kubeconfig` on Windows.

        2022-05-03 11:43:19,656 INFO  [io.qua.ope.run.AppEventListener] (Quarkus Main Thread) Quarkus Java Operator SDK extension 3.0.4 (commit: da80246 on branch: da80246dd6b953c245fcad5a01487db81d55a1bc) built on Wed Mar 02 23:29:51 CET 2022
        2022-05-03 11:43:19,656 WARN  [io.qua.ope.run.AppEventListener] (Quarkus Main Thread) No Reconciler implementation was found so the Operator was not started.
        2022-05-03 11:43:19,706 INFO  [io.quarkus] (Quarkus Main Thread) camping-java-operator 0.0.1-SNAPSHOT on JVM (powered by Quarkus 2.7.3.Final) started in 2.253s. Listening on: http://localhost:8080
        2022-05-03 11:43:19,707 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
        2022-05-03 11:43:19,707 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [cdi, kubernetes, kubernetes-client, micrometer, openshift-client, operator-sdk, smallrye-context-propagation, smallrye-health, vertx]

        --
        Tests paused
        Press [r] to resume testing, [o] Toggle test output, [:] for the terminal, [h] for more options>
    ```

## 📄 CRD generation
 - la branche `02-crd-generation` contient le résultat de cette étape
 - création de l'API : `operator-sdk create api --version v1 --kind NginxOperator`
 - cette commande a créé les 4 classes nécessaires pour créer l'opérateur:
    ```bash
    src
    └── main
        ├── java
        │   └── wilda
        │       └── fr
        │           ├── NginxOperator.java
        │           ├── NginxOperatorReconciler.java
        │           ├── NginxOperatorSpec.java
        │           └── NginxOperatorStatus.java
    ```
  - tester que tout compile que la CRD se génère bien: `mvn clean package` (ou restez en mode `mvn quarkus:dev` pour voir la magie opérer en direct :wink:)
  - une exception apparaît, cela vient du fait que la CRD n'est pas générée côté Kubernetes, cela va être corrigée dans les étapes suivantes:
  ```bash
  2022-03-28 15:42:02,261 ERROR [io.qua.run.Application] (Quarkus Main Thread) Failed to start application (with profile dev): io.javaoperatorsdk.operator.MissingCRDException: 'nginxoperators.fr.wilda' v1 CRD was not found on the cluster, controller 'nginxoperatorreconciler' cannot be registered
  ```
  - la CRD doit être générée dans le target, `target/kubernetes/nginxoperators.fr.wilda-v1.yml`:
      ```yaml
      # Generated by Fabric8 CRDGenerator, manual edits might get overwritten!
      apiVersion: apiextensions.k8s.io/v1
      kind: CustomResourceDefinition
      metadata:
        name: nginxoperators.fr.wilda
      spec:
        group: fr.wilda
        names:
          kind: NginxOperator
          plural: nginxoperators
          singular: nginxoperator
        scope: Namespaced
        versions:
        - name: v1
          schema:
            openAPIV3Schema:
              properties:
                spec:
                  type: object
                status:
                  type: object
              type: object
          served: true
          storage: true
          subresources:
            status: {}
      ```

## 📝 CRD auto apply
 - la branche `03-auto-apply-crd` contient le résultat de cette étape
 - changer le paramétrage permettant la création / automatique de la CRD dans le `application.properties` (cela va permettre de ne plus avoir l'exception):
      ```properties
      # set to true to automatically apply CRDs to the cluster when they get regenerated
      quarkus.operator-sdk.crd.apply=true
      ```
 - arrêter et relancer l'opérateur en mode `dev` : `mvn quarkus:dev`:
      ```bash
      2022-03-08 13:46:48,219 WARN  [io.qua.ope.dep.OperatorSDKProcessor] (build-20) 'nginxoperatorreconciler' controller is configured to watch all namespaces, this requires a ClusterRoleBinding for which we MUST specify the namespace of the operator ServiceAccount. This can be specified by setting the 'quarkus.kubernetes.namespace' property. However, as this property is not set, we are leaving the namespace blank to be provided by the user by editing the 'nginxoperatorreconciler-cluster-role-binding' ClusterRoleBinding to provide the namespace in which the operator will be deployed.
      2022-03-08 13:46:48,221 WARN  [io.qua.ope.dep.OperatorSDKProcessor] (build-20) 'nginxoperatorreconciler' controller is configured to validate CRDs, this requires a ClusterRoleBinding for which we MUST specify the namespace of the operator ServiceAccount. This can be specified by setting the 'quarkus.kubernetes.namespace' property. However, as this property is not set, we are leaving the namespace blank to be provided by the user by editing the 'nginxoperatorreconciler-crd-validating-role-binding' ClusterRoleBinding to provide the namespace in which the operator will be deployed.
      __  ____  __  _____   ___  __ ____  ______ 
      --/ __ \/ / / / _ | / _ \/ //_/ / / / __/ 
      -/ /_/ / /_/ / __ |/ , _/ ,< / /_/ /\ \   
      --\___\_\____/_/ |_/_/|_/_/|_|\____/___/   
      ```
  - vérifier que la CRD a bien été créée : 
      ```bash
      kubectl get crds nginxoperators.fr.wilda
      NAME                      CREATED AT
      nginxoperators.fr.wilda   2022-03-08T12:46:49Z
      ```

## 👋  Hello World with Quarkus
 - la branche `04-hello-world` contient le résultat de cette étape
 - ajouter un champ `name` dans `NginxOperatorSpec.java`:
      ```java
      public class NginxOperatorSpec {
          private String name;

          public void setName(String name) {
              this.name = name;
          }

          public String getName() {
              return name;
          }
      }
      ```
  - vérifier que la CRD a bien été mise à jour:
      ```bash
      $ kubectl get crds nginxoperators.fr.wilda -o yaml
      apiVersion: apiextensions.k8s.io/v1
      kind: CustomResourceDefinition
      metadata:
        creationTimestamp: "2022-03-08T12:46:49Z"
        generation: 2
        name: nginxoperators.fr.wilda
        resourceVersion: "28080830902"
        uid: acbc5340-292c-4a26-9003-d2d0b9da1683
      spec:
        conversion:
          strategy: None
        group: fr.wilda
        names:
          kind: NginxOperator
          listKind: NginxOperatorList
          plural: nginxoperators
          singular: nginxoperator
        scope: Namespaced
        versions:
        - name: v1
          schema:
            openAPIV3Schema:
              properties:
                spec:
                  properties:
                    name:
                      type: string
                  type: object
      ```
 - modifier le reconciler `NginxOperatorReconciler.java`:
    ```java
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
    ```
  - créer le namespace `test-helloworld-operator`: `kubectl create ns test-helloworld-operator`
  - créer la CR `src/test/resources/cr-test-hello-world.yaml` pour tester:
      ```yaml
      apiVersion: "fr.wilda/v1"
      kind: NginxOperator
      metadata:
        name: hello-world
      spec:
        name: Camping des Speakers 2022
      ```
  - créer la CR dans Kubernetes : `kubectl apply -f ./src/test/resources/cr-test-hello-world.yaml -n test-helloworld-operator`
  - la sortie de l'opérateur devrait afficher le message `Camping des Speakers 2022 🎉🎉 !!`
  - supprimer la CR : `kubectl delete nginxOperator/hello-world -n test-helloworld-operator`
  - la sortie de l'opérateur devrait ressembler à cela:
      ```bash
      Camping des Speakers 2022 🎉🎉 !!
      Camping des Speakers 2022 😢 
      ```

## 🤖 Nginx operator
 - la branche `05-nginx-operator` contient le résultat de cette étape
 - modifier la classe `NginxOperatorSpec.java`:
      ```java
      public class NginxOperatorSpec {

        private Integer replicaCount;
        private Integer port;

        public void setPort(Integer port) {
            this.port = port;
        }

        public Integer getPort() {
            return port;
        }

        public void setReplicaCount(Integer replicaCount) {
            this.replicaCount = replicaCount;
        }

        public Integer getReplicaCount() {
            return replicaCount;
        }
     }
      ```
 - pour simplifier la création du Pod et du Service pour Nginx on passe par des manifests en YAML.
    `src/main/resources/k8s/nginx-deployment.yaml`:
      ```yaml
      apiVersion: apps/v1
      kind: Deployment
      metadata:
        name: nginx-deployment
        labels:
          app: nginx
      spec:
        replicas: 1
        selector:
          matchLabels:
            app: nginx
        template:
          metadata:
            labels:
              app: nginx
          spec:
            containers:
            - name: nginx
              image: ovhplatform/hello:1.0
              ports:
              - containerPort: 80
      ```
      `src/main/resources/k8s/nginx-service.yaml`:
      ```yaml
      apiVersion: v1
      kind: Service
      metadata:
        name: nginx-service
      spec:
        type: NodePort
        selector:
          app: nginx
        ports:
          - port: 80
            targetPort: 80
            nodePort: 30080
       ```
 - modifier le reconciler `NginxOperatorReconciler.java`:
    ```java
    public class NginxOperatorReconciler implements Reconciler<NginxOperator> {
      private final KubernetesClient client;

      public NginxOperatorReconciler(KubernetesClient client) {
        this.client = client;
      }

      @Override
      public UpdateControl<NginxOperator> reconcile(NginxOperator resource, Context context) {

        System.out.println("🛠️  Create / update Nginx resource operator ! 🛠️");

        String namespace = resource.getMetadata().getNamespace();

        // Load the Nginx deployment
        Deployment deployment = loadYaml(Deployment.class, "/k8s/nginx-deployment.yaml");
        // Apply the number of replicas and namespace
        deployment.getSpec().setReplicas(resource.getSpec().getReplicaCount());
        deployment.getMetadata().setNamespace(namespace);

        // Create or update Nginx server
        client.apps().deployments().inNamespace(namespace).createOrReplace(deployment);

        // Create service
        Service service = loadYaml(Service.class, "/k8s/nginx-service.yaml");
        Service existingService = client.services().inNamespace(resource.getMetadata().getNamespace()).withName("nginx-service").get();
        if (existingService == null || !existingService.getSpec().getPorts().get(0).getNodePort().equals(resource.getSpec().getPort())) {
            service.getSpec().getPorts().get(0).setNodePort(resource.getSpec().getPort());
            client.services().inNamespace(namespace).createOrReplace(service);
        }        

        return UpdateControl.noUpdate();
      }

      @Override
      public DeleteControl cleanup(NginxOperator resource, Context context) {
        System.out.println("💀 Delete Nginx resource operator ! 💀");

        client.apps().deployments().inNamespace(resource.getMetadata().getNamespace()).delete();
        client.services().inNamespace(resource.getMetadata().getNamespace()).withName("nginx-service").delete();

        return Reconciler.super.cleanup(resource, context);
      }

      /**
      * Load a YAML file and transform it to a Java class.
      * 
      * @param clazz The java class to create
      * @param yamlPath The yaml file path in the classpath
      */
      private <T> T loadYaml(Class<T> clazz, String yamlPath) {
        try (InputStream is = getClass().getResourceAsStream(yamlPath)) {
          return Serialization.unmarshal(is, clazz);
        } catch (IOException ex) {
          throw new IllegalStateException("Cannot find yaml on classpath: " + yamlPath);
        }
      }
    }
    ```
 - créer le namespace `test-nginx-operator`: `kubectl create ns test-nginx-operator`
 - créer la CR: `src/test/resources/cr-test-nginx-operator.yaml`:
      ```yaml
        apiVersion: "fr.wilda/v1"
        kind: NginxOperator
        metadata:
          name: nginx-camping-operator
        spec:
          replicaCount: 1
          port: 30080
      ```
 - puis l'appliquer sur Kubernetes: `kubectl apply -f ./src/test/resources/cr-test-nginx-operator.yaml -n test-nginx-operator`
 - l'opérateur devrait créer le pod Nginx et son service:
      Dans le terminal du quarkus:
      ```bash
      🛠️  Create / update Nginx resource operator ! 🛠️
      ```
      Dans Kubernetes:
      ```bash
      $ kubectl get pod,svc,nginxoperator  -n test-nginx-operator
        NAME                                    READY   STATUS    RESTARTS   AGE
        pod/nginx-deployment-84c7b56775-khq4g   1/1     Running   0          14s

        NAME                    TYPE       CLUSTER-IP     EXTERNAL-IP   PORT(S)        AGE
        service/nginx-service   NodePort   10.3.109.153   <none>        80:30080/TCP   7s

        NAME                                            AGE
        nginxoperator.fr.wilda/nginx-template-operator   5s
      ```
 - tester dans un navigateur ou par un curl l'accès à `http://<node external ip>:30080`, pour récupérer l'IP externe du node : `kubectl get nodes -o wide`