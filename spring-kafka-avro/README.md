O Serializer da confluent coloca 5  bytes no inicio da mensagem avro,
então postar uma mensagem avro para um consumidor da confluent nao 
vai funcionar

```
confluent-user.avro
00000000: 00 00 00 00 01 08 4a 6f 61 6f 00 08 00 0c 4f 72 61 6e 67 65                                ......Joao....Orange


confluent-user-v2.avro
00000000: 00 00 00 00 02 08 4a 6f 61 6f 00 08 00 0c 4f 72 61 6e 67 65 00 2e                          ......Joao....Orange..


vanilla-user.avro
00000000: 08 4a 6f 61 6f 00 08 00 0c 4f 72 61 6e 67 65                                               .Joao....Orange


vanilla-user-v2.avro
00000000: 08 4a 6f 61 6f 00 08 00 0c 4f 72 61 6e 67 65 00 2e                                         .Joao....Orange..

 
```
