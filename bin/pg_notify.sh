#!/bin/bash

while [ ! -f 0.txt ]; do
    echo -n ''
done

for N in $(seq 1 1000); do
#    psql --echo-all \
#         --dbname template1 \
#         --user postgres \
#         --command "notify lista, '{ "Terminal": "$$", "Mensagem": "$N" }';"
    psql --echo-all \
         --dbname stat \
         --user stat \
         --command "notify lista, '{ "Terminal": "$$", "Mensagem": "$N" }';"
done

