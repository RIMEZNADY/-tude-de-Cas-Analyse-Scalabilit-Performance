#!/bin/bash

# Script pour exécuter tous les tests de performance
# Usage: ./run-all-tests.sh [rest|soap|graphql|grpc]

API_TYPE=${1:-rest}
RESULTS_DIR="results/$(date +%Y%m%d_%H%M%S)"
mkdir -p "$RESULTS_DIR"

echo "=== Exécution des tests de performance pour $API_TYPE ==="
echo "Résultats seront sauvegardés dans: $RESULTS_DIR"

# Tests JMeter
echo "=== Exécution des tests JMeter ==="
if command -v jmeter &> /dev/null; then
    jmeter -n -t jmeter/${API_TYPE}-api-test.jmx -l "$RESULTS_DIR/jmeter-results.jtl" -e -o "$RESULTS_DIR/jmeter-report"
    echo "Rapport JMeter généré dans $RESULTS_DIR/jmeter-report"
else
    echo "JMeter n'est pas installé, saut des tests JMeter"
fi

# Tests k6
echo "=== Exécution des tests k6 ==="
if command -v k6 &> /dev/null; then
    k6 run --out json="$RESULTS_DIR/k6-results.json" k6/${API_TYPE}-api-test.js
    echo "Résultats k6 sauvegardés dans $RESULTS_DIR/k6-results.json"
else
    echo "k6 n'est pas installé, saut des tests k6"
fi

# Tests Locust
echo "=== Exécution des tests Locust ==="
if command -v locust &> /dev/null; then
    locust -f locust/${API_TYPE}_api_test.py --headless -u 100 -r 10 -t 5m --html "$RESULTS_DIR/locust-report.html" --csv "$RESULTS_DIR/locust-results"
    echo "Rapport Locust généré dans $RESULTS_DIR/locust-report.html"
else
    echo "Locust n'est pas installé, saut des tests Locust"
fi

echo "=== Tous les tests sont terminés ==="

