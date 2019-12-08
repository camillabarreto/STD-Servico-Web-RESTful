# Serviço Web RESTful em Java

## Preparar simulação

Para iniciar a simulação é necessário clonar esse repositório na sua máquina, compilar e executar o projeto. Abra um terminal e execute os comandos listados:

#### Clonar

```shell
  git clone https://github.com/STD29006-classroom/2019-02-projeto-pratico-02-camillabarreto
```

#### Compilar

```shell
  cd std-projeto-pratico-02

  ./gradlew build
```

#### Executar

```shell
  ./gradlew appRun
```

#### Simular

Agora abra um cliente HTTP e utilize os recursos [API REST](https://github.com/STD29006-classroom/2019-02-projeto-pratico-02-camillabarreto/blob/master/apiary.apib).

## Funcionalidades implementadas

| Recurso | Verbo | Implementado |
| :--------------: | :--------------: | :--------------: |
| accounts | GET | X |
| replicas | GET | X |
| replicas | POST | X |
| replicas | DELETE | X |
| operation | POST | X |
| decision/{id_operacao} | PUT | X |
| decision/{id_operacao} | DELETE | X |
| historic | GET | X |
| seed | POST | X |
