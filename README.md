# Student Management System with Kubernetes

Ce projet est une application de gestion d'étudiants déployée sur Kubernetes.

## Prérequis

- Docker
- kubectl
- Minikube ou un cluster Kubernetes
- Maven

## Construction de l'image Docker

```bash
docker build -t student-management:latest .
```

## Déploiement sur Kubernetes

1. Démarrer Minikube (si vous utilisez Minikube) :
   ```bash
   minikube start --driver=docker --cpus=4 --memory=4096 --disk-size=20g
   minikube addons enable ingress
   ```

2. Se déplacer dans le répertoire k8s :
   ```bash
   cd k8s
   ```

3. Rendre le script exécutable (Linux/Mac) :
   ```bash
   chmod +x deploy.sh
   ```

4. Exécuter le script de déploiement :
   ```bash
   ./deploy.sh
   ```

5. Accéder à l'application :
   - Via le port NodePort : http://localhost:30080

## Architecture

- **Backend** : Spring Boot avec Spring Data JPA
- **Base de données** : MySQL 8.0
- **Orchestration** : Kubernetes

## Développement

Pour le développement local, vous pouvez utiliser :

```bash
mvn spring-boot:run
```

## Tests

```bash
mvn test
```

## Nettoyage

Pour supprimer toutes les ressources Kubernetes :

```bash
kubectl delete namespace student-ns
```

## Licence

MIT
