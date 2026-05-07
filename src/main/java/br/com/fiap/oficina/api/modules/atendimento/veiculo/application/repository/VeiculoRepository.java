package br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface VeiculoRepository {

    public void salvar(Veiculo veiculo);

    public void atualizar(Veiculo veiculo);

    public void deletar(UUID id);

    public Optional<Veiculo> buscarPorId(UUID id);

    public Optional<Veiculo> buscarPorPlaca(String placa);

    public List<Veiculo> buscarPorClienteId(UUID clienteId);

    public Pagina<Veiculo> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos);

}






