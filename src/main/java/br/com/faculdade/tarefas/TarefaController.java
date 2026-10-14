package br.com.faculdade.tarefas;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.data.domain.Sort;
import java.util.List;
@RestController
@RequestMapping("/api/tarefas")
public class TarefaController {
 private final TarefaRepository repository;
 public TarefaController(TarefaRepository repository) { this.repository=repository; }
 public record Entrada(@NotBlank @Size(max=120) String titulo, @Size(max=1000) String descricao, @NotNull Tarefa.Status status) {}
 @GetMapping public List<Tarefa> listar() { return repository.findAll(Sort.by("id").descending()); }
 @GetMapping("/{id}") public Tarefa buscar(@PathVariable Long id) { return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,"Tarefa não encontrada")); }
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public Tarefa criar(@Valid @RequestBody Entrada entrada) { return salvar(new Tarefa(),entrada); }
 @PutMapping("/{id}") public Tarefa editar(@PathVariable Long id,@Valid @RequestBody Entrada entrada) { return salvar(buscar(id),entrada); }
 @PatchMapping("/{id}/concluir") public Tarefa concluir(@PathVariable Long id) { Tarefa t=buscar(id);t.setStatus(Tarefa.Status.CONCLUIDA);return repository.save(t); }
 @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void excluir(@PathVariable Long id) { repository.delete(buscar(id)); }
 private Tarefa salvar(Tarefa t,Entrada e) { t.setTitulo(e.titulo().trim());t.setDescricao(e.descricao());t.setStatus(e.status());return repository.save(t); }
}
