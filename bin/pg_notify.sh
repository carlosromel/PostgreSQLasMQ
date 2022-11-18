#!/bin/bash

while [ ! -f 0.txt ]; do
    echo -n ''
done

for N in $(seq 1 1000); do
    psql -d stat -U stat -c "notify lista, 'Terminal $$, Mensagem: $N';"
done

