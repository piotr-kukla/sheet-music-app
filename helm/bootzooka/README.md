# sheetmusic

![Version: 0.3.0](https://img.shields.io/badge/Version-0.3.0-informational?style=flat-square) ![Type: application](https://img.shields.io/badge/Type-application-informational?style=flat-square) ![AppVersion: 1.0](https://img.shields.io/badge/AppVersion-1.0-informational?style=flat-square)

A Helm chart for Sheetmusic

**Homepage:** <https://softwaremill.github.io/sheetmusic/>

## Installation

### Add Helm repository

```
helm repo add softwaremill https://charts.softwaremill.com/
helm repo update
```

## Fetch and Customize Sheetmusic chart
```
helm fetch softwaremill/sheetmusic --untar
```

## Install Sheetmusic chart

```
helm install --generate-name sheetmusic
```

## Configuration

The following table lists the configurable parameters of the chart and the default values.

## Values

| Key | Type | Default | Description |
|-----|------|---------|-------------|
| sheetmusic.affinity | object | `{}` |  |
| sheetmusic.fullnameOverride | string | `""` |  |
| sheetmusic.image.pullPolicy | string | `"Always"` |  |
| sheetmusic.image.repository | string | `"softwaremill/sheetmusic"` |  |
| sheetmusic.image.tag | string | `"latest"` |  |
| sheetmusic.ingress.annotations."kubernetes.io/ingress.class" | string | `"nginx"` |  |
| sheetmusic.ingress.annotations."kubernetes.io/tls-acme" | string | `"true"` |  |
| sheetmusic.ingress.enabled | bool | `true` |  |
| sheetmusic.ingress.hosts[0].host.domain | string | `"sheetmusic.example.com"` |  |
| sheetmusic.ingress.hosts[0].host.path | string | `"/"` |  |
| sheetmusic.ingress.hosts[0].host.pathType | string | `"ImplementationSpecific"` |  |
| sheetmusic.ingress.hosts[0].host.port | string | `"http"` |  |
| sheetmusic.ingress.tls[0].hosts[0] | string | `"sheetmusic.example.com"` |  |
| sheetmusic.ingress.tls[0].secretName | string | `"sheetmusic-tls"` |  |
| sheetmusic.ingress.tls_enabled | bool | `false` |  |
| sheetmusic.java_opts | string | `"-XX:MaxRAMPercentage=60"` |  |
| sheetmusic.liveness_initial_delay | int | `60` |  |
| sheetmusic.logback_json_encode | bool | `false` |  |
| sheetmusic.nameOverride | string | `""` |  |
| sheetmusic.nodeSelector | object | `{}` |  |
| sheetmusic.otel.enabled | bool | `false` |  |
| sheetmusic.otel.endpoint | string | `""` |  |
| sheetmusic.otel.metric_export_interval | string | `"60s"` |  |
| sheetmusic.otel.protocol | string | `""` |  |
| sheetmusic.otel.service_name | string | `"sheetmusic"` |  |
| sheetmusic.readiness_initial_delay | int | `60` |  |
| sheetmusic.replicaCount | int | `1` |  |
| sheetmusic.reset_password_url | string | `"https://sheetmusic.example.com/password-reset?code=%s"` |  |
| sheetmusic.resources | object | `{}` |  |
| sheetmusic.service.port | int | `8080` |  |
| sheetmusic.service.type | string | `"ClusterIP"` |  |
| sheetmusic.smtp.enabled | bool | `true` |  |
| sheetmusic.smtp.from | string | `"hello@sheetmusic.example.com"` |  |
| sheetmusic.smtp.host | string | `"server.example.com"` |  |
| sheetmusic.smtp.password | string | `"sheetmusic"` |  |
| sheetmusic.smtp.port | int | `465` |  |
| sheetmusic.smtp.ssl | string | `"true"` |  |
| sheetmusic.smtp.ssl_ver | string | `"false"` |  |
| sheetmusic.smtp.username | string | `"server.example.com"` |  |
| sheetmusic.sql.host | string | `"{{ .Values.postgresql.fullnameOverride }}"` | Value will be taken from 'postgresql.fullnameOverride' setting |
| sheetmusic.sql.name | string | `"{{ .Values.postgresql.auth.database }}"` | Value will be taken from 'postgresql.postgresqlDatabase' setting |
| sheetmusic.sql.password | string | `"{{ .Values.postgresql.auth.password }}"` | Value will be taken from 'postgresql.postgresqlPassword' setting |
| sheetmusic.sql.port | string | `"{{ .Values.postgresql.service.port }}"` | Value will be taken from 'postgresql.service.port' setting |
| sheetmusic.sql.username | string | `"{{ .Values.postgresql.auth.username }}"` | Value will be taken from 'postgresql.postgresqlUsername' setting |
| sheetmusic.tolerations | list | `[]` |  |
| postgresql.auth.database | string | `"sheetmusic"` | Database name for Sheetmusic |
| postgresql.auth.password | string | `"sheetmusic"` | Password for PostgreSQL user |
| postgresql.auth.username | string | `"postgres"` | Username for PostgreSQL user |
| postgresql.connectionTest.image.pullPolicy | string | `"IfNotPresent"` |  |
| postgresql.connectionTest.image.repository | string | `"bitnami/postgresql"` |  |
| postgresql.connectionTest.image.tag | int | `11` |  |
| postgresql.enabled | bool | `true` | Disable if you already have PostgreSQL running in cluster where Sheetmusic chart is being deployed |
| postgresql.fullnameOverride | string | `"sheetmusic-pgsql-postgresql"` |  |
| postgresql.service.port | int | `5432` |  |
