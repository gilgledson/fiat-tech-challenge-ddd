package br.com.fiap.oficina.api.modules.atendimento.cliente.domain;

import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Endereço - Testes de Domínio")
class EnderecoTest {

    @Test
    @DisplayName("Deve criar um endereço com todos os campos")
    void deveCriarEndereco() {
        Endereco endereco = new Endereco(
                "01001-000",
                "Praça da Sé",
                "s/n",
                "Lado ímpar",
                "Sé",
                "São Paulo",
                "SP"
        );

        assertEquals("01001-000", endereco.getCep());
        assertEquals("Praça da Sé", endereco.getLogradouro());
        assertEquals("s/n", endereco.getNumero());
        assertEquals("Lado ímpar", endereco.getComplemento());
        assertEquals("Sé", endereco.getBairro());
        assertEquals("São Paulo", endereco.getCidade());
        assertEquals("SP", endereco.getEstado());
    }

    @Test
    @DisplayName("Deve permitir atualizar campos do endereço")
    void devePermitirAtualizarCampos() {
        Endereco endereco = new Endereco(null, null, null, null, null, null, null);
        
        endereco.setCep("12345-678");
        endereco.setLogradouro("Rua Teste");
        
        assertEquals("12345-678", endereco.getCep());
        assertEquals("Rua Teste", endereco.getLogradouro());
    }
}









