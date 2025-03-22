# Deploy a Spring Boot App in Kubernetes

This project shows how to deploy a Spring Boot app in Kubernetes. It includes creating `deployment.yaml`, `service.yaml`, `secret`, and `config-map.yaml`, plus ways to access the app from your local machine.

## What You Need

- A Kubernetes cluster (e.g., from the parent Vagrant project)
- [Docker](https://www.docker.com/get-started) (to build the app image)
- [kubectl](https://kubernetes.io/docs/tasks/tools/) (to manage Kubernetes)
- [Helm](https://helm.sh/docs/intro/install/) (optional, for Helm deployment)

## Build the App Image

1. **Build the Docker Image**  
   Run this in the project folder with your Dockerfile:

   ```bash
   docker build -t springboot-kubernetes-deployment:0.0.2 .
   ```

2. **Push to a Registry**  
   Tag and upload it to Docker Hub (replace `fbiankevin` with your username):

   ```bash
   docker tag springboot-kubernetes-deployment:0.0.2 fbiankevin/springboot-kubernetes-deployment:0.0.2
   docker push fbiankevin/springboot-kubernetes-deployment:0.0.2
   ```

## Deploy the App

1. **Deploy to Kubernetes**  
   Apply the deployment file:

   ```bash
   kubectl apply -f deployment.yaml
   ```

2. **Expose the App Internally**  
   Create a service:

   ```bash
   kubectl apply -f service.yaml
   ```

## Check Status

- **List Deployments**:
  ```bash
  kubectl get deployments
  ```

- **List Pods**:
  ```bash
  kubectl get pods
  ```

- **List Services**:
  ```bash
  kubectl get services
  ```

## Debugging

1. **Describe a Pod**  
   First, get the pod name:
   ```bash
   kubectl get pods
   ```
   Then describe it:
   ```bash
   kubectl describe pod <pod_name>
   ```

2. **View Logs**  
   Replace with your pod name:
   ```bash
   kubectl logs spring-boot-app-abcde-12345
   ```

3. **Exec into a Pod**  
   Access the pod’s shell:
   ```bash
   kubectl exec -it spring-boot-app-abcde-12345 -- bash
   ```

## Scaling

Increase the number of pods:
```bash
kubectl scale deployment app-spring-boot --replicas=3
kubectl get deployments
```

## Access the App

Forward the service to your local machine:
```bash
kubectl port-forward service/app-spring-boot-service 8080:80
```
Then open `http://localhost:8080` in your browser.

## ConfigMap

Apply `config-map.yaml` with `kubectl apply -f config-map.yaml`.

### Validate Config Inside a Pod
- Check environment variable:
  ```bash
  kubectl exec -it app-spring-boot-587d54b459-mxdmm -- env | grep SPRING_CONFIG_LOCATION
  ```

- View the properties file:
  ```bash
  kubectl exec -it app-spring-boot-5fbfdfd6-bbmt6 -- cat /app/config/application.properties
  ```

- List config directory:
  ```bash
  kubectl exec -it app-spring-boot-b6d67cc76-sdb89 -- ls /app/config
  ```

- Check a specific variable:
  ```bash
  kubectl exec -it app-spring-boot-d565ccc8c-5dn8k -- echo $SPRING_DATASOURCE_PASSWORD
  ```

### Reload Config Changes
If `deployment.yaml` hasn’t changed:
```bash
kubectl rollout restart deployment app-spring-boot
```

## Secret

Apply `secret.yaml` with `kubectl apply -f secret.yaml`.

### Create a Secret
Encode a password:
```bash
echo -n "mysecretpassword" | base64
```

### Verify Secret
- Check the secret:
  ```bash
  kubectl get secret spring-boot-secret
  ```

- Decode to confirm:
  ```bash
  kubectl get secret spring-boot-secret -o jsonpath='{.data.SPRING_DATASOURCE_PASSWORD}' | base64 --decode
  ```

- Verify in pod:
  ```bash
  kubectl exec -it app-spring-boot-5f59c9f564-4qr7w -- env | grep DB_PASSWORD
  ```

## Helm (Optional)

1. **Create a Helm Chart**  
   Generate a chart scaffold:
   ```bash
   helm create app-spring-boot
   ```

2. **Test It**  
   Dry run to check:
   ```bash
   helm install app . --dry-run --debug
   ```

3. **Install the Chart**  
   Deploy it:
   ```bash
   helm install app .
   ```

4. **Check Resources**
   ```bash
   kubectl get deployment app-spring-boot
   kubectl describe deployment app-spring-boot
   ```

5. **Upgrade the Chart**  
   Apply changes:
   ```bash
   helm upgrade app .
   ```

   To restart pods for ConfigMap updates:
   ```bash
   helm upgrade app . --recreate-pods
   ```

6. **Rollback**  
   Check history:
   ```bash
   helm history app
   ```

   Roll back to version 1:
   ```bash
   helm rollback app 1
   ```

7. **Clean Up**  
   Remove the Helm release:
   ```bash
   helm uninstall app
   ```

   Verify cleanup:
   ```bash
   kubectl get deployment,configmap,secret,service -l app=app-spring-boot
   ```

## Cleanup

Remove everything:
```bash
kubectl delete -f deployment.yaml
kubectl delete -f service.yaml
kubectl delete -f config-map.yaml
```
