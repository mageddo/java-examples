@XmlSchema(
	namespace = "http://www.cip-bancos.org.br/ARQ/ASLC027ERR.xsd",
	xmlns = {
		@XmlNs(prefix = "slc", namespaceURI = "http://www.cip-bancos.org.br/ARQ/ASLC027ERR.xsd")
	},
	elementFormDefault = XmlNsForm.QUALIFIED
)
/*
 * Classe para definir o namespace geral do pacote. Namespace padrão não funciona nessa implementação da JAXB
 */
package com.mageddo.jaxb;

import javax.xml.bind.annotation.XmlNs;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlSchema;
