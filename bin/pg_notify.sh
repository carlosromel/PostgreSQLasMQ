#!/bin/bash

if [ ! -f waiting.txt ]; then
	cat <<-HERE
	Aguardando a existência do arquivo waiting.txt para o início dos testes.
	Abra uma quantidade suficiente de terminais com esse mesmo comando.

	Ex:
	for N in 0 1 2 3 4 5 6 7 8 9; do
	    xterm -e 'bin/pg_notify.sh'
	done

	Quando o arquivo waiting.txt passar a existir (touch waiting.txt), os
	roteiros começarão sua execução em paralelo.
	HERE
fi

while [ ! -f waiting.txt ]; do
    echo -n ''
done

for N in $(seq 1 1000); do
    psql --echo-all \
         --dbname stat \
         --user stat \
         --command "notify lista, '{ "Processo": "$$", "Mensagem": "$N" }';"
done

if [ -f waiting.txt ]; then
    rm waiting.txt
fi

