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
   k3d cluster create -p "8000:30000@loadbalancer" --agents 2
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

- Create a namespace for grafana
```bash
   kubectl create namespace grafana
```

- Create a namespace for pyroscope
```bash
   kubectl create namespace pyroscope
```
### Install Grafana and Pyroscope 
- Add grafana helm repository
```bash
  helm repo add grafana https://grafana.github.io/helm-charts && helm repo update
```

- Install pyroscope
```bash
   helm -n pyroscope-test install pyroscope grafana/pyroscope
```
- Install grafana
```bash
   helm upgrade -n grafana --install grafana grafana/grafana \
  --set image.repository=grafana/grafana \
  --set image.tag=main \
  --set env.GF_INSTALL_PLUGINS=grafana-pyroscope-app \
  --set env.GF_AUTH_ANONYMOUS_ENABLED=true \
  --set env.GF_AUTH_ANONYMOUS_ORG_ROLE=Admin \
  --set env.GF_DIAGNOSTICS_PROFILING_ENABLED=true \
  --set env.GF_DIAGNOSTICS_PROFILING_ADDR=0.0.0.0 \
  --set env.GF_DIAGNOSTICS_PROFILING_PORT=9094 \
  --set-string 'podAnnotations.profiles\.grafana\.com/cpu\.scrape=true' \
  --set-string 'podAnnotations.profiles\.grafana\.com/cpu\.port=9094' \
  --set-string 'podAnnotations.profiles\.grafana\.com/memory\.scrape=true' \
  --set-string 'podAnnotations.profiles\.grafana\.com/memory\.port=9094' \
  --set-string 'podAnnotations.profiles\.grafana\.com/goroutine\.scrape=true' \
  --set-string 'podAnnotations.profiles\.grafana\.com/goroutine\.port=9094'
```

- Add datasource to grafana
```bash
 helm upgrade -n grafana --reuse-values grafana grafana/grafana \                                                                                                      1 ↵ andrelucas@MacBookAir
     --values monitoring/grafana/datasources.yaml
  
```

- Add postgresql helm repository
```bash
   helm repo add bitnami https://charts.bitnami.com/bitnami && helm repo update
```
- Install postgresql
```bash
   helm install postgresql bitnami/postgresql --version 15.5.28 --namespace postgres --set auth.database=toilet --set auth.password=toilet --set auth.username=toilet
```

- Install the application
```bash
    kubectl -n toilet apply -f infraascode
```