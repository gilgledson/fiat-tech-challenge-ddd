package br.com.fiap.oficina.api.modules.operacional.funcionario.infrastructure.persistence;

import br.com.fiap.oficina.api.modules.operacional.funcionario.application.repository.FuncionarioRepository;
import br.com.fiap.oficina.api.modules.operacional.funcionario.domain.entity.Funcionario;
import br.com.fiap.oficina.api.shared.domain.entity.Pagina;
import io.quarkus.hibernate.orm.panache.PanacheRepositoryBase;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

@ApplicationScoped
public class FuncionarioRepositoryImpl
        implements FuncionarioRepository, PanacheRepositoryBase<FuncionarioJpaEntity, UUID> {

    @Override
    public Optional<Funcionario> buscarPorId(UUID id) {
        return findByIdOptional(id).map(FuncionarioJpaEntity::toDomain);
    }

    @Override
    public Optional<Funcionario> buscarPorCpf(String cpf) {
        return find("cpf", cpf).firstResultOptional().map(FuncionarioJpaEntity::toDomain);
    }

    @Override
    public Optional<Funcionario> buscarPorUsuarioId(UUID usuarioId) {
        return find("usuarioId", usuarioId).firstResultOptional().map(FuncionarioJpaEntity::toDomain);
    }

    @Override
    @Transactional
    public Funcionario salvar(Funcionario funcionario) {
        FuncionarioJpaEntity entity = FuncionarioJpaEntity.fromDomain(funcionario);
        persist(entity);
        return entity.toDomain();
    }

    @Override
    @Transactional
    public Funcionario atualizar(Funcionario funcionario) {
        FuncionarioJpaEntity entity = findByIdOptional(funcionario.getId())
                .orElseThrow(() -> new NotFoundException("Funcionario não encontrado"));

        entity.setNome(funcionario.getNome());
        entity.setSobrenome(funcionario.getSobrenome());
        entity.setCpf(funcionario.getCpf());
        entity.setTelefone(funcionario.getTelefone());
        entity.setCargo(funcionario.getCargo());
        entity.setUsuarioId(funcionario.getUsuarioId());
        entity.setAtivo(funcionario.isAtivo());
        entity.setDeletadoEm(funcionario.getDeletadoEm());
        return entity.toDomain();
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        deleteById(id);
    }

    @Override
    public Pagina<Funcionario> listarTodos(int pagina, int tamanhoLista, boolean incluirInativos) {
        var query = incluirInativos ? findAll() : find("ativo", true);
        query.page(Page.of(pagina, tamanhoLista));
        List<Funcionario> itens = query.stream()
                .map(FuncionarioJpaEntity::toDomain)
                .toList();
        return new Pagina<>(itens, pagina, tamanhoLista, (int) query.pageCount(), query.count());
    }
}






