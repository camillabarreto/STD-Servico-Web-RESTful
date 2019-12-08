# Serviço Web RESTful em Java

## Preparar simulação

Para iniciar a simulação é necessário clonar esse repositório na sua máquina, compilar e executar o projeto. Abra um terminal e execute os comandos listados:

#### Clonar

```shell
  git clone https://github.com/STD29006-classroom/2019-02-projeto-pratico-02-camillabarreto
```

#### Compilar

```shell
  cd 2019-02-projeto-pratico-02-camillabarreto

  ./gradlew build
```

#### Executar

```shell
  ./gradlew appRun
```

#### Simular

Agora abra um cliente HTTP e utilize os recursos. Acesse [API REST](https://stdprojetopratico02.docs.apiary.io) ou [apiary.apib](https://github.com/STD29006-classroom/2019-02-projeto-pratico-02-camillabarreto/blob/master/apiary.apib) para ter acesso a documentação.

## Funcionalidades implementadas

| Recurso | Verbo | Implementado |
| :--------------: | :--------------: | :--------------: |
| accounts | GET | OK |
| replicas | GET | OK |
| replicas | POST | OK |
| replicas | DELETE | OK |
| operation | POST | OK |
| decision/{id_operacao} | PUT | OK |
| decision/{id_operacao} | DELETE | OK |
| historic | GET | OK |
| seed | POST | OK |
