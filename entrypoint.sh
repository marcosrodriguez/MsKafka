#!/bin/bash

source /opt/stratio/stratio-kms/kms_utils.sh
export PORT0=${PORT0:-"8080"}
###export KAFKA_WRITER=alstomkafka-sec
export INSTANCE_NAME=${INSTANCE_NAME:-mskafka}
export KAFKA_WRITER_NORM="${KAFKA_WRITER^^}"

echo PORT0=${PORT0}

declare -a VAULT_HOSTS
IFS_OLD=$IFS
IFS=',' read -r -a VAULT_HOSTS <<< "$VAULT_HOST"

# Approle login from role_id, secret_id
if [ "xxx$VAULT_TOKEN" == "xxx" ];
then
    login \
    && echo "OK: Login in Vault" \
    ||  echo "Error: Login in Vault"
fi

#2--- GET SECRETS WITH APP TOKEN
getCert "userland" \
       ${KAFKA_WRITER} \
       ${KAFKA_WRITER} \
        "JKS" \
        "/etc/stratio" \
&& echo "OK: Getting ${KAFKA_WRITER} certificate"   \
||  echo "Error: Getting ${KAFKA_WRITER} certificate"

getCAbundle "/etc/stratio" "JKS" \
&& echo "OK: Getting ca-bundle"   \
||  echo "Error: Getting ca-bundle"

echo listado $(ls /etc/stratio)

JKS_PASSWORD=${KAFKA_WRITER_NORM}_KEYSTORE_PASS

export KAFKA_SSL_TRUSTSTORE_LOCATION=/etc/stratio/ca-bundle.jks
export KAFKA_SSL_TRUSTSTORE_PASSWORD=${DEFAULT_KEYSTORE_PASS}

export KAFKA_SSL_KEYSTORE_LOCATION=/etc/stratio/${KAFKA_WRITER}.jks
##export KAFKA_SSL_KEYSTORE_PASSWORD=${KAFKA_WRITER_KEYSTORE_PASS}
export KAFKA_SSL_KEYSTORE_PASSWORD=${!JKS_PASSWORD}

##export KAFKA_SSL_KEYPASSWORD=${KAFKA_WRITER_KEYSTORE_PASS}
export KAFKA_SSL_KEYPASSWORD=${!JKS_PASSWORD}

##echo ${KAFKA_SSL_TRUSTSTORE_LOCATION}
##echo ${KAFKA_SSL_TRUSTSTORE_PASSWORD}
##echo ${KAFKA_SSL_KEYSTORE_LOCATION}
##echo ${KAFKA_SSL_KEYSTORE_PASSWORD}
##echo ${KAFKA_SSL_KEYPASSWORD}



export JAVA_OPTS="--server.port=${PORT0} --spring.profiles.active=prod"

java -Xmx256m -jar /data/app.jar ${JAVA_OPTS}
