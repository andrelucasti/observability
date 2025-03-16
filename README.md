# observability

### Prerequisites
- [Docker](https://www.docker.com/)
- [Docker Compose](https://docs.docker.com/compose/)
- Helm 3 (https://helm.sh/docs/intro/install/)
- [Kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/)
- k3d (https://k3d.io/#installation)
- [K9s](https://k9scli.io/topics/install/)

### Setup
- Create a k3d cluster
```bash
    k3d cluster create otel-demo --agents 3 --api-port 6550 -p "8080:80@loadbalancer"
```

#### Install cert-manager (required by OpenTelemetry Operator)

```bash
    kubectl apply -f https://github.com/cert-manager/cert-manager/releases/download/v1.13.1/cert-manager.yaml
    kubectl wait --for=condition=Available deployment --all -n cert-manager --timeout=2m
```
### Install OpenTelemetry Operator
```bash
    kubectl apply -f https://github.com/open-telemetry/opentelemetry-operator/releases/latest/download/opentelemetry-operator.yaml
```

### Install SigNoz
```bash
    kubectl create namespace signoz && \
    helm repo add signoz https://signoz.io/helm-charts && \ 
    helm --namespace signoz install signoz signoz/signoz && \   
    helm --namespace signoz install -f monitoring/k8s-cluster.yaml signoz-k8s-infra signoz/k8s-infra 
```
### Namespaces
- Create a namespace for the application
```bash
  kubectl create namespace toilet
```
- Create a namespace for the postgresql
```bash
   kubectl create namespace postgres   
```
- Add postgresql helm repository
```bash
   helm repo add bitnami https://charts.bitnami.com/bitnami && \
   helm repo update
```
- Install postgresql
```bash
    helm install postgresql bitnami/postgresql \ 
      --version 15.5.28 \ 
      --namespace postgres \ 
      --set auth.database=toilet \ 
      --set auth.password=toilet \ 
      --set auth.username=toilet
```


#### Adding otel-collector to the application
```bash
    kubectl -n toilet apply -f monitoring/otel
```

- Install the application
```bash
    kubectl -n toilet apply -f infraascode/app
```