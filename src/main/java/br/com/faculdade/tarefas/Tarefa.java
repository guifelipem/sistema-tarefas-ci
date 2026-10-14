package br.com.faculdade.tarefas;
import jakarta.persistence.*;
import java.time.LocalDateTime;
@Entity
public class Tarefa {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,length=120) private String titulo;
 @Column(length=1000) private String descricao;
 @Enumerated(EnumType.STRING) @Column(nullable=false) private Status status=Status.PENDENTE;
 @Column(nullable=false,updatable=false) private LocalDateTime dataCriacao=LocalDateTime.now();
 public enum Status { PENDENTE, CONCLUIDA }
 public Long getId() { return id; }
 public String getTitulo() { return titulo; }
 public void setTitulo(String titulo) { this.titulo=titulo; }
 public String getDescricao() { return descricao; }
 public void setDescricao(String descricao) { this.descricao=descricao; }
 public Status getStatus() { return status; }
 public void setStatus(Status status) { this.status=status; }
 public LocalDateTime getDataCriacao() { return dataCriacao; }
}
