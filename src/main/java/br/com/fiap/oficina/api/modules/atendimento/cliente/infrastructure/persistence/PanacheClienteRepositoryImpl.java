package br.com.fiap.oficina.api.modules.atendimento.cliente.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.atendimento.cliente.application.repository.ClienteRepository;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.entity.Cliente;
import br.com.fiap.oficina.api.modules.atendimento.cliente.domain.valueobject.Endereco;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class PanacheClienteRepositoryImpl implements ClienteRepository, PanacheRepositoryBase<ClienteJpaEntity, UUID> {

    @Override
    @Transactional
    public void salvar(Cliente cliente) {
        ClienteJpaEntity entity = new ClienteJpaEntity();
        entity.setId(cliente.getId());
        entity.setUsuarioId(cliente.getUsuarioId().orElse(null));
        entity.setNome(cliente.getNome());
        entity.setEmail(cliente.getEmail());
        entity.setCpfCnpj(cliente.getCpfCnpj());
        entity.setTelefone(cliente.getTelefone());
        entity.setEndereco(converterParaEmbeddable(cliente.getEndereco()));

        persist(entity);
    }

    @Override
    @Transactional
    public void atualizar(Cliente cliente) {
        ClienteJpaEntity entity = findByIdOptional(cliente.getId())
                .orElseThrow(() -> new NotFoundException("Cliente não encontrado"));

        entity.setUsuarioId(cliente.getUsuarioId().orElse(null));
        entity.setNome(cliente.getNome());
        entity.setEmail(cliente.getEmail());
        entity.setCpfCnpj(cliente.getCpfCnpj());
        entity.setTelefone(cliente.getTelefone());
        entity.setEndereco(converterParaEmbeddable(cliente.getEndereco()));
        entity.setDeletadoEm(cliente.getDeletadoEm().orElse(null));
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        deleteById(id);
    }

    @Override
    public Optional<Cliente> buscarPorId(UUID id) {
        return findByIdOptional(id).map(this::converterParaDominio);
    }

    @Override
    public Optional<Cliente> buscarPorUsuarioId(UUID usuarioId) {
        return find("usuarioId", usuarioId).firstResultOptional().map(this::converterParaDominio);
    }

    @Override
    public Optional<Cliente> buscarPorCpfCnpj(String cpfCnpj) {
        return find("cpfCnpj", cpfCnpj).firstResultOptional().map(this::converterParaDominio);
    }

    @Override
    public List<Cliente> buscarPorIds(List<UUID> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return find("id in ?1", ids)
                .stream()
                .map(this::converterParaDominio)
                .toList();
    }

    @Override
    public Pagina<Cliente> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos) {
        var query = incluirInativos ? findAll() : find("deletadoEm is null");
        query.page(Page.of(pagina, tamanhoLista));
        List<Cliente> itens = query
                .stream()
                .map(this::converterParaDominio)
                .collect(Collectors.toList());
        return new Pagina<>(itens, pagina, tamanhoLista, (int) query.pageCount(), query.count());
    }

    private Cliente converterParaDominio(ClienteJpaEntity entity) {
        Endereco endereco = entity.getEndereco() != null ? new Endereco(
                entity.getEndereco().getCep(),
                entity.getEndereco().getLogradouro(),
                entity.getEndereco().getNumero(),
                entity.getEndereco().getComplemento(),
                entity.getEndereco().getBairro(),
                entity.getEndereco().getCidade(),
                entity.getEndereco().getEstado()
        ) : null;

        return Cliente.reconstituir(
                entity.getId(),
                entity.getUsuarioId(),
                entity.getNome(),
                entity.getEmail(),
                entity.getCpfCnpj(),
                entity.getTelefone(),
                endereco,
                Optional.ofNullable(entity.getDeletadoEm())
        );
    }

    private EnderecoEmbeddable converterParaEmbeddable(Endereco endereco) {
        if (endereco == null) return null;
        EnderecoEmbeddable embeddable = new EnderecoEmbeddable();
        embeddable.setCep(endereco.getCep());
        embeddable.setLogradouro(endereco.getLogradouro());
        embeddable.setNumero(endereco.getNumero());
        embeddable.setComplemento(endereco.getComplemento());
        embeddable.setBairro(endereco.getBairro());
        embeddable.setCidade(endereco.getCidade());
        embeddable.setEstado(endereco.getEstado());
        return embeddable;
    }
}






