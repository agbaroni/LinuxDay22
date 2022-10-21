#!/bin/bash

# Aspetto che Keycloak sale
echo -n "Waiting "
STOP=no
while [ "$STOP" != "yes" ]; do
    echo -n "."
    sleep 1s
    curl -s http://test-keycloak:8080/ 2>&1 > /dev/null
    if [ $? -eq 0 ]; then
	STOP=yes
	echo ""
    fi
done

echo "Recupero access token dell'utente admin"
TOKEN=$(curl -s -d 'client_id=admin-cli' -d 'username=admin' -d 'password=admin' -d 'grant_type=password' http://test-keycloak:8080/auth/realms/master/protocol/openid-connect/token | jq -r .access_token)

echo "Creazione del Realm"
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"enabled":true,"realm":"bank"}' http://test-keycloak:8080/auth/admin/realms

echo "Creazione del client "
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"enabled":true,"clientId":"agency","redirectUris":["http://localhost:28080/*","http://test-service:8080/*"],"secret":"4931f7a6-2cf4-4bf4-a48b-495756557fcc"}' http://test-keycloak:8080/auth/admin/realms/bank/clients

echo "Salvataggio id del client"
ID_CLIENT=$(curl -s -H "Authorization: Bearer $TOKEN" http://test-keycloak:8080/auth/admin/realms/bank/clients?clientId=agency | jq -r .[0].id)

echo "Crezione gruppi"
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"name":"operators"}' http://test-keycloak:8080/auth/admin/realms/bank/groups
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"name":"users"}' http://test-keycloak:8080/auth/admin/realms/bank/groups

echo "Creazione utenti"
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"credentials":[{"type":"password","value":"password","temporary":false}],"email":"wamozart@linuxdaymilano.org","emailVerified":true,"username":"wamozart","enabled":true,"groups":["users"]}' http://test-keycloak:8080/auth/admin/realms/bank/users

curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"credentials":[{"type":"password","value":"password","temporary":false}],"email":"gverdi@linuxdaymilano.org","emailVerified":true,"username":"gverdi","enabled":true,"groups":["users"]}' http://test-keycloak:8080/auth/admin/realms/bank/users

curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"credentials":[{"type":"password","value":"password","temporary":false}],"email":"agbaroni@linuxdaymilano.org","emailVerified":true,"username":"agbaroni","enabled":true,"groups":["operators"]}' http://test-keycloak:8080/auth/admin/realms/bank/users

echo "Salvataggio id dell'utente operatore"
ID_USER=$(curl -s -H "Authorization: Bearer $TOKEN" http://test-keycloak:8080/auth/admin/realms/bank/users?username=agbaroni | jq -r .[0].id)

#echo "Elenco utenti"
#curl -s -H "Authorization: Bearer $TOKEN" http://test-keycloak:8080/auth/admin/realms/bank/users

echo "Creazione Role"
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '{"name":"operator"}' http://test-keycloak:8080/auth/admin/realms/bank/clients/$ID_CLIENT/roles

echo "Salvataggio ID del Role"
ID_ROLE=$(curl -s -H "Authorization: Bearer $TOKEN" http://test-keycloak:8080/auth/admin/realms/bank/clients/$ID_CLIENT/roles/operator | jq -r .id)

echo "Assegnazione Role (Client) all'utente operator"
curl -s -X POST -H "Authorization: Bearer $TOKEN" -H 'Content-Type: application/json' -d '[{"id":"'$ID_ROLE'","name":"operator"}]' http://test-keycloak:8080/auth/admin/realms/bank/users/$ID_USER/role-mappings/clients/$ID_CLIENT
