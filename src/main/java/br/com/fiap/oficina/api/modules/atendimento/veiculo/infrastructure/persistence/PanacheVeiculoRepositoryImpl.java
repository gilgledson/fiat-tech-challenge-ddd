package br.com.fiap.oficina.api.modules.atendimento.veiculo.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.atendimento.veiculo.application.repository.VeiculoRepository;
import br.com.fiap.oficina.api.modules.atendimento.veiculo.domain.entity.Veiculo;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import br.com.fiap.oficina.api.shared.domain.entity.Pagina;

@ApplicationScoped
public class PanacheVeiculoRepositoryImpl implements VeiculoRepository, PanacheRepositoryBase<VeiculoJpaEntity, UUID> {

    @Override
    @Transactional
    public void salvar(Veiculo veiculo) {
        VeiculoJpaEntity entity = new VeiculoJpaEntity();
        entity.setId(veiculo.getId());
        entity.setClienteId(veiculo.getClienteId());
        entity.setPlaca(veiculo.getPlaca());
        entity.setMarca(veiculo.getMarca());
        entity.setModelo(veiculo.getModelo());
        entity.setAno(veiculo.getAno());

        persist(entity);
    }

    @Override
    @Transactional
    public void atualizar(Veiculo veiculo) {
        VeiculoJpaEntity entity = findByIdOptional(veiculo.getId())
                .orElseThrow(() -> new NotFoundException("Veículo não encontrado"));

        entity.setClienteId(veiculo.getClienteId());
        entity.setPlaca(veiculo.getPlaca());
        entity.setMarca(veiculo.getMarca());
        entity.setModelo(veiculo.getModelo());
        entity.setAno(veiculo.getAno());
        entity.setDeletadoEm(veiculo.getDeletadoEm().orElse(null));
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        deleteById(id);
    }

    @Override
    public Optional<Veiculo> buscarPorId(UUID id) {
        return findByIdOptional(id).map(this::converterParaDominio);
    }

    @Override
    public Optional<Veiculo> buscarPorPlaca(String placa) {
        return find("placa", placa).firstResultOptional().map(this::converterParaDominio);
    }

    @Override
    public List<Veiculo> buscarPorClienteId(UUID clienteId) {
        return find("clienteId", clienteId).stream().map(this::converterParaDominio).toList();
    }

    @Override
    public Pagina<Veiculo> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos) {
        var query = incluirInativos ? findAll() : find("deletadoEm is null");
        query.page(Page.of(pagina, tamanhoLista));
        List<Veiculo> itens = query
                .stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
        return new Pagina<>(itens, pagina, tamanhoLista, (int) query.pageCount(), query.count());
    }

    private Veiculo converterParaDominio(VeiculoJpaEntity entity) {
        return Veiculo.reconstituir(
                entity.getId(),
                entity.getClienteId(),
                entity.getPlaca(),
                entity.getMarca(),
                entity.getModelo(),
                entity.getAno(),
                Optional.ofNullable(entity.getDeletadoEm())
        );
    }
}






