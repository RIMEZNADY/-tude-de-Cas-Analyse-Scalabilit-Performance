# Étude Comparative : REST vs SOAP vs GraphQL vs gRPC

## Objectif
Comparer les performances, la scalabilité, la simplicité d'implémentation et la sécurité de REST, SOAP, GraphQL et gRPC pour une plateforme de réservation d'hôtels.

## Architecture

### Backend
- **REST** : Spring Boot REST API
- **SOAP** : Spring Boot SOAP Web Service
- **GraphQL** : Apollo Server (Node.js)
- **gRPC** : Java gRPC Server

### Base de Données
- PostgreSQL (pour toutes les implémentations)

### Frontend
- Client React.js simple pour tester les fonctionnalités CRUD

### Monitoring
- Prometheus : Collecte de métriques
- Grafana : Visualisation des métriques
- Jaeger : Traçage distribué
- Elastic Stack : Analyse des logs

### Outils de Test
- Apache JMeter
- k6
- Locust
- Gatling

## Structure du Projet

```
.
├── rest-api/          # API REST Spring Boot
├── soap-api/          # API SOAP Spring Boot
├── graphql-api/       # API GraphQL Apollo Server
├── grpc-api/          # API gRPC Java
├── client/            # Client React.js
├── tests/             # Scripts de test de performance
├── monitoring/        # Configuration Prometheus, Grafana, Jaeger
├── docker-compose.yml # Orchestration des services
└── docs/              # Documentation et rapports
```

## Démarrage Rapide

### 1. Démarrer tous les services
```bash
docker-compose up -d
```

### 2. Exécuter les tests de performance
```bash
cd tests
./run-all-tests.sh
```

### 3. Visualiser les résultats
- Grafana : http://localhost:3000
- Jaeger UI : http://localhost:16686
- Kibana : http://localhost:5601

## Métriques Évaluées

### Performances
- Latence (temps de réponse moyen)
- Débit (requêtes par seconde)
- Consommation CPU
- Consommation Mémoire

### Scalabilité
- Tests avec 10, 100, 500, 1000 requêtes simultanées
- Messages de taille variable (1 KB, 10 KB, 100 KB)

### Simplicité
- Temps d'implémentation
- Nombre de lignes de code
- Courbe d'apprentissage

### Sécurité
- Support TLS/SSL
- Authentification
- Résistance aux attaques

## Résultats
Les résultats des tests seront disponibles dans le dossier `docs/results/` après l'exécution des tests.

## Documentation

- **[QUICK_START.md](QUICK_START.md)** : Démarrage rapide en 5 minutes
- **[GUIDE.md](GUIDE.md)** : Guide complet pour réaliser l'étude
- **[INSTALLATION.md](INSTALLATION.md)** : Guide d'installation détaillé
- **[SUMMARY.md](SUMMARY.md)** : Résumé du projet et prochaines étapes
- **[PROJECT_STRUCTURE.md](PROJECT_STRUCTURE.md)** : Structure détaillée du projet

