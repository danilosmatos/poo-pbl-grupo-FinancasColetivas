package domain.entities;

import java.util.ArrayList;

public class Grupo {
    private final String nome;
    private boolean aberto;
    private ArrayList<Individuo> membros;

    public Grupo(String nome) {
        this.nome = nome;
        this.aberto = true;
        this.membros = new ArrayList<>();
    }

    public void abrirGrupo() {
        this.aberto = true;
    }

    public void fecharGrupo() {
        this.aberto = false;
    }

    public void adicionarMembro(Individuo pessoa) {
        if (!this.aberto) {
            System.out.println("Erro: Grupo fechado.");
            return;
        }
        membros.add(pessoa);
    }

    public void removerMembro(int idBusca) {
        if (!this.aberto) {
            System.out.println("Erro: Grupo fechado.");
            return;
        }

        Individuo pessoaParaRemover = null;
        for (Individuo membro : membros) {
            if (membro.getId() == idBusca) {
                pessoaParaRemover = membro;
                break;
            }
        }

        if (pessoaParaRemover != null) {
            membros.remove(pessoaParaRemover);
        } else {
            System.out.println("Membro não encontrado.");
        }
    }

    public void listarMembros() {
        for (Individuo membro : membros) {
            membro.exibirInfo();
        }
    }
}