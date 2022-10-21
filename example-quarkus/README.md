
```
podman system service --time=0 &

export DOCKER_HOST="unix://$XDG_RUNTIME_DIR/podman/podman.sock"

mvn docker:remove clean package docker:build docker:start

podman logs -f test-setup

echo "127.0.0.1 test-keycloak >> /etc/hosts"

OP_TOKEN=$(curl -s -k -d 'client_id=agency' -d 'client_secret=4931f7a6-2cf4-4bf4-a48b-495756557fcc' -d 'username=agbaroni' -d 'password=password' -d 'grant_type=password' https://test-keycloak:8443/auth/realms/bank/protocol/openid-connect/token | jq -r .access_token)

curl -v -X PUT -H "Authorization: Bearer $OP_TOKEN" -H 'Content-Type: application/json' -d '{"firstName":"Luigi"}' http://localhost:28080/users/gverdi

curl -v -H 'Content-Type: application/json' http://localhost:28080/users/gverdi
```
