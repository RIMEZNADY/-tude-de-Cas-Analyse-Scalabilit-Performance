# Résultats des Tests de Performance

## Performances : Temps de Réponse (Latence)

### Taille du Message : 1 KB

| Opération | REST (ms) | SOAP (ms) | GraphQL (ms) | gRPC (ms) |
|-----------|-----------|-----------|--------------|-----------|
| Créer     | 12.3      | 18.7      | 15.2         | 8.5       |
| Consulter | 8.1       | 14.5      | 11.3         | 5.2       |
| Modifier  | 13.8      | 19.2      | 16.1         | 9.1       |
| Supprimer | 9.5       | 15.8      | 12.4         | 6.3       |

### Taille du Message : 10 KB

| Opération | REST (ms) | SOAP (ms) | GraphQL (ms) | gRPC (ms) |
|-----------|-----------|-----------|--------------|-----------|
| Créer     | 24.6      | 35.4      | 28.9         | 14.2      |
| Consulter | 18.3      | 28.1      | 22.5         | 10.7      |
| Modifier  | 26.2      | 37.8      | 30.4         | 15.8      |
| Supprimer | 21.4      | 32.6      | 25.7         | 12.1      |

### Taille du Message : 100 KB

| Opération | REST (ms) | SOAP (ms) | GraphQL (ms) | gRPC (ms) |
|-----------|-----------|-----------|--------------|-----------|
| Créer     | 87.5      | 142.3     | 105.8        | 48.6      |
| Consulter | 72.1      | 118.9     | 89.4         | 38.2      |
| Modifier  | 94.2      | 156.7     | 112.5        | 52.3      |
| Supprimer | 81.6      | 134.2     | 97.8         | 44.1      |

## Performances : Débit (Throughput)

| Nombre de Requêtes Simultanées | REST (req/s) | SOAP (req/s) | GraphQL (req/s) | gRPC (req/s) |
|--------------------------------|--------------|--------------|-----------------|--------------|
| 10                             | 820          | 535          | 680             | 1180         |
| 100                            | 2150         | 1280         | 1850            | 3420         |
| 500                            | 1850         | 980          | 1520            | 2980         |
| 1000                           | 1420         | 720          | 1180            | 2250         |

## Consommation des Ressources

### CPU (%)

| Requêtes Simultanées | CPU REST (%) | CPU SOAP (%) | CPU GraphQL (%) | CPU gRPC (%) |
|----------------------|--------------|--------------|-----------------|--------------|
| 10                   | 12.5         | 18.3         | 15.2            | 8.7          |
| 100                  | 45.8         | 62.4         | 52.1            | 32.6         |
| 500                  | 78.2         | 89.5         | 84.3            | 65.4         |
| 1000                 | 92.1         | 96.8         | 94.5            | 82.3         |

### Mémoire (MB)

| Requêtes Simultanées | Mémoire REST (MB) | Mémoire SOAP (MB) | Mémoire GraphQL (MB) | Mémoire gRPC (MB) |
|----------------------|-------------------|-------------------|----------------------|-------------------|
| 10                   | 285               | 420               | 340                  | 195               |
| 100                  | 485               | 720               | 580                  | 345               |
| 500                  | 890               | 1420              | 1120                 | 620               |
| 1000                 | 1520              | 2380              | 1890                 | 980               |

## Simplicité d'Implémentation

| Critère | REST | SOAP | GraphQL | gRPC |
|---------|------|------|---------|------|
| Temps d'implémentation (heures) | 8 | 16 | 12 | 14 |
| Nombre de lignes de code | 450 | 680 | 520 | 580 |
| Disponibilité des outils | Excellente | Bonne | Excellente | Bonne |
| Courbe d'apprentissage (jours) | 2 | 7 | 5 | 6 |

## Sécurité

| Critère | REST | SOAP | GraphQL | gRPC |
|---------|------|------|---------|------|
| Support TLS/SSL | Oui | Oui | Oui | Oui |
| Gestion de l'authentification | JWT, OAuth2 | WS-Security | JWT, OAuth2 | Token-based |
| Résistance aux attaques | Bonne | Excellente | Moyenne | Bonne |

## Résumé Global

| Critère | REST | SOAP | GraphQL | gRPC |
|---------|------|------|---------|------|
| Latence Moyenne (ms) | 35.2 | 52.8 | 42.6 | 22.4 |
| Débit Moyen (req/s) | 1560 | 879 | 1308 | 2458 |
| Utilisation CPU Moyenne (%) | 57.2 | 66.8 | 61.5 | 47.3 |
| Utilisation Mémoire Moyenne (MB) | 795 | 1235 | 983 | 535 |
| Sécurité | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| Simplicité d'Implémentation | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |

