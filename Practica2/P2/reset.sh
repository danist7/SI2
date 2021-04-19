#!/bin/bash
cd P1-base
ant replegar
ant delete-pool-local
ant delete-db
ant limpiar-todo
ant todo
cd ..
