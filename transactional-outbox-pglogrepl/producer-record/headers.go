package producer_record

import (
	"encoding/base64"
	"strings"
)

// HEADER_DELIMITER é o mesmo delimitador usado em Java
const HEADER_DELIMITER = ";"

// decodeBase64 decodifica um header no formato "chave:valorBase64"
func decodeBase64(encodedHeader string) (key string, value []byte) {
	tokens := strings.SplitN(encodedHeader, ":", 2)
	if len(tokens) != 2 {
		return "", nil
	}
	decoded, err := base64.StdEncoding.DecodeString(tokens[1])
	if err != nil {
		return tokens[0], nil
	}
	return tokens[0], decoded
}

// DecodeHeadersFromBase64 converte o texto em um mapa de headers
// Exemplo de entrada: "foo:Zm9v;bar:YmFy"
func DecodeHeadersFromBase64(text string) map[string][]byte {
	headers := make(map[string][]byte)
	if strings.TrimSpace(text) == "" {
		return headers
	}

	tokens := strings.Split(text, HEADER_DELIMITER)
	for _, token := range tokens {
		token = strings.TrimSpace(token)
		if token == "" {
			continue
		}
		key, value := decodeBase64(token)
		if key != "" && value != nil {
			headers[key] = value
		}
	}

	return headers
}
