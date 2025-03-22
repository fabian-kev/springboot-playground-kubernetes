# Deploy Spring Boot App to Kubernetes with Vagrant

This project sets up a Kubernetes cluster using Vagrant. It provisions a master node (`kmaster`) and worker nodes (`kworker1`, `kworker2`, etc.) with bash scripts. This README is the main guide for the project and its child modules.

Inspired by: [justmeandopensource/kubernetes](https://github.com/justmeandopensource/kubernetes/tree/master/vagrant-provisioning)

## What You Need

- [Vagrant](https://www.vagrantup.com/downloads) (Installed)
- [VirtualBox](https://www.virtualbox.org/wiki/Downloads) (Or another provider Vagrant supports)
- A terminal or command line tool
- Basic knowledge of Kubernetes (optional)

## Project Structure

- `Vagrantfile`: Configures the VMs for Kubernetes.
- `bootstrap.sh`: Configures the common tools that needs to be installed.
- `bootstrap_kmaster.sh`: Sets up the master node.
- `bootstrap_kworker.sh`: Sets up the worker nodes.

## How to Set It Up

### 1. Go to Vagrant Folder
Download or clone this project to your computer:

```bash
cd /vagrant/provision
```

### 2. Make Scripts Executable
Run this to allow the bash scripts to work:

```bash
chmod +x bootstrap_kmaster.sh bootstrap_kworker.sh
```

### 3. Start the VMs
Provision and start the Kubernetes cluster:

```bash
vagrant up
```

- **Tip**: To start VMs without running the scripts:
  ```bash
  vagrant up --no-provision
  ```

### 4. Log In to the Master Node
Connect to the `kmaster` VM:

```bash
vagrant ssh kmaster
```

### 5. Set Up Kubeconfig (Inside `kmaster`)
Make the Kubernetes config accessible to the `vagrant` user:

```bash
sudo cp /etc/kubernetes/admin.conf ~/.kube/config
sudo chown vagrant:vagrant ~/.kube/config
```

Exit the VM with `exit`.

### 6. Copy Kubeconfig to Your Local Machine
Choose one of these methods to get the config file locally:

- **Method 1: Using SSH**
  ```bash
  vagrant ssh kmaster -c "cat ~/.kube/config" > ~/.kube/config
  ```

### 7. Verify the Cluster
Check that the nodes are running:

```bash
kubectl get nodes
```

For more details:
```bash
kubectl get nodes -o wide
```

You should see `kmaster`, `kworker1`, `kworker2`, etc.

## Managing the VMs

- **Stop the VMs**:
  ```bash
  vagrant halt
  ```

- **Delete the VMs** (if you don’t need them anymore):
  ```bash
  vagrant destroy
  ```

## Troubleshooting

- **VMs won’t start?**  
  Check that Vagrant and VirtualBox are installed. Look at the terminal output for errors.

- **Kubernetes not running?**  
  SSH into `kmaster` and check:
  ```bash
  sudo systemctl status kubelet
  ```

- **Nodes not showing?**  
  Verify the `~/.kube/config` file is correctly copied to your local machine.

## Customize It

- Edit the `Vagrantfile` to change VM settings (e.g., memory, CPU, or number of workers).
- Modify `bootstrap_kmaster.sh` or `bootstrap_kworker.sh` for custom Kubernetes setups.
