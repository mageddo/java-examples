## Comportamento Ordered
O CPC trabalha com a seguinte lógica quando usando Ordered: 

Vou paralelizar mandando registros de chaves diferentes para a mesma thread 
ou até para threads diferentes se entender que vale a pena, 
mas nunca dois ou mais registros de mesma chave para a mesma thread.

No entendimento do algoritmo não faz sentido mandar um lote de registros da mesma chave para uma thread 
pois isso poderia gerar concorrência, portanto faz mais sentido guardar o segundo registro e mandar para outra 
ou mesma thread quando a primeira thread retornar. Isso é verdade, mas não faz sentido quando quero fazer reduce
