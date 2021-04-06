#!/bin/bash
for i in P1-base P1-ws P1-ejb-cliente-remoto; do
cd $i
ant replegar; ant delete-pool-local
cd -
done
cd  P1-ejb-servidor-remoto
ant replegar-todo
cd -
cd P1-base
ant delete-db
cd -
for i in P1-base P1-ejb-servidor-remoto P1-ejb-cliente-remoto P1-ws; do
cd $i
ant limpiar-todo
cd -
done

cd P1-base
ant todo
cd -
cd P1-ejb-servidor-remoto
ant p2
cd -
cd P1-ejb-cliente-remoto
ant casitodo
cd -
cd P1-ws
ant p2
cd
