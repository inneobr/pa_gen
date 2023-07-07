#!/bin/bash
echo "Instalando Drive Oracle na dependência Maven local..."
  mvn install:install-file -Dfile=ojdbc8.jar  -DgroupId=com.oracle -DartifactId=ojdbc8 -Dversion=21.6.0.0.0 -Dpackaging=jar
echo "Instalação do Drive Oracle concluída..."
