---
layout: default
title:  "Production deployment"
---

## Docker

To build a docker image, run `docker/Docker/publishLocal`. This will create the `docker:latest` image.

You can test the image by using the provided `docker-compose.yml` file.

## Kubernetes

Use [Helm](https://helm.sh/) to easily deploy Sheetmusic into [Kubernetes](https://kubernetes.io/) cluster.

### Add SoftwareMill Helm repository

```
helm repo add softwaremill https://charts.softwaremill.com/
helm repo update
```

### Fetch and Customize Sheetmusic chart

```
helm fetch softwaremill/sheetmusic --untar
```

### Install Sheetmusic chart

```
helm install --generate-name sheetmusic
```

Please see [Sheetmusic Helm Chart
documentation](https://github.com/softwaremill/sheetmusic/blob/master/helm/sheetmusic/README.md) for more information,
including configuration options.
