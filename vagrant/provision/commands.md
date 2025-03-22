Command Used

# Vagrant and Bash Script Reference
https://github.com/justmeandopensource/kubernetes/tree/master/vagrant-provisioning



### Make the shell scripts executable
```bash
[springboot-kubernetes-deployment](..%2F..%2Fspringboot-kubernetes-deployment)
## Provision the nodes
chmod +x bootstrap_kmaster.sh bootstrap_kworker.sh

# Provisioning or turning on the VMs
vagrant up
# Up the VM without running the bash scripts
vagrant up --no-provision

# Stopping the VMs
vagrant halt

# Check k8s status
sudo systemctl status kubelet

## Copy the kubeconfig to vagrant user and make it accessible using vagrant user
vagrant ssh kmaster
sudo cp /etc/kubernetes/admin.conf ~/.kube/config
sudo chown vagrant:vagrant ~/.kube/config  

# Copy the kubeconfig from VM to local machine to be able to access the nodes
vagrant ssh kmaster -c "cat ~/.kube/config" > ~/.kube/config

#verify: It should list down the nodes such as kmaster, kworker1, and kworker2
kubectl get nodes
```


