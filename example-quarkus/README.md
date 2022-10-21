
```
podman system service --time=0 &

export DOCKER_HOST="unix://$XDG_RUNTIME_DIR/podman/podman.sock"

mvn docker:remove clean package docker:build docker:start

```
