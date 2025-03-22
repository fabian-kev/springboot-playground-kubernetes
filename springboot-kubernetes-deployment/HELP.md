# Deploy a Spring Boot App in Kubernetes

This sample covers the creation of deployment.yaml, service.yaml, secret, and config-map.yaml.
It also covers the methods on how to access the pods from the host machine.


```bash
#Build the image:
docker build -t springboot-kubernetes-deployment:0.0.2 .

#Optional - Tag and push to a registry (e.g., Docker Hub): 
docker tag springboot-kubernetes-deployment:0.0.2 fbiankevin/springboot-kubernetes-deployment:0.0.2
docker push fbiankevin/springboot-kubernetes-deployment:0.0.2

#Deploy the app:
kubectl apply -f deployment.yaml

#Expose it internally:
kubectl apply -f service.yaml

#Check Status
#List all deployments:
kubectl get deployments

#List pods:
kubectl get pods

#List services:
kubectl get services

#Debugging
#Describe a pod (replace with actual pod name):
#Execute get pods first to get a specific pod name
kubectl get pods 
kubectl describe pod <pod_name>

#View logs:
kubectl logs spring-boot-app-abcde-12345

#Exec into a pod:
kubectl exec -it spring-boot-app-abcde-12345 -- bash

#Scaling
kubectl scale deployment springboot-kubernetes-deployment --replicas=3
kubectl get deployments

#Cleanup
kubectl delete -f deployment.yaml
kubectl delete -f service.yaml
kubectl delete -f config-map.yaml

#Accessing the App
kubectl port-forward service/springboot-kubernetes-deployment-service 8080:80
```

# ConfigMap
```bash


#Validate the application.properties file inside a pod
kubectl exec -it springboot-kubernetes-deployment-587d54b459-mxdmm -- env | grep SPRING_CONFIG_LOCATION
kubectl exec -it app-spring-boot-5fbfdfd6-bbmt6 -- cat /app/config/application.properties
kubectl exec -it springboot-kubernetes-deployment-b6d67cc76-sdb89 -- ls /app/config

kubectl exec -it springboot-kubernetes-deployment-d565ccc8c-5dn8k -- echo $SPRING_DATASOURCE_PASSWORD


#Apply the change from configmap if there is no changes in deploymeny.yaml
kubectl rollout restart deployment springboot-kubernetes-deployment
```

# Secret
```bash

#Encode a password into base64
echo -n "mysecretpassword" | base64

#Verify 
kubectl get secret spring-boot-secret

#Decode to Confirm:
kubectl get secret spring-boot-secret -o jsonpath='{.data.SPRING_DATASOURCE_PASSWORD}' | base64 --decode

#Verify in Pod:
kubectl exec -it springboot-kubernetes-deployment-5f59c9f564-4qr7w -- env | grep DB_PASSWORD
```

# Helm
```bash
#Generate a Helm chart scaffold:
helm create springboot-kubernetes-deployment

#Dry run
helm install app . --dry-run --debug

#Install:
helm install app .

#Check Resources:
kubectl get deployment app-spring-boot
kubectl describe deployment app-spring-boot

#Upgrading
helm upgrade app .

To restart pods to reflect the changes from a configMap
helm upgrade app . --recreate-pods

# Rollback
#History
helm history app

helm rollback app 1

# Cleaning
helm uninstall app

#verify
kubectl get deployment,configmap,secret,service -l app=app-spring-boot
```