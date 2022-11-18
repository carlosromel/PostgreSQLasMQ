#!/bin/bash

echo "Aguardando a existência do arquiuvo waiting.txt.$$ para iniciar os testes."

while [ ! -f waiting.txt.$$ ]; do
    echo -n ''
done

for N in $(seq 1 1000); do
    psql --echo-all \
         --dbname stat \
         --user stat \
         --command "notify lista, '{ "Processo": "$$", "Mensagem": "$N" }';"
done

if [ -f waiting.txt.$$ ]; then
    rm waiting.txt.$$
fi
