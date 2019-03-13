package br.senai.sp.agendaandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.Serializable;

import br.senai.sp.dao.ContatoDAO;
import br.senai.sp.modelo.Contato;

public class CadastroContatoActivity extends AppCompatActivity  {
    private String acao = "";
    private Contato contatoAtualizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_contato);
        ContatoHelper helper = new ContatoHelper(this);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if(extras == null){
            acao = "salvar";
        } else {
            acao = "atualizar";
            contatoAtualizar = (Contato)extras.getSerializable("contato");
            helper.setContato(contatoAtualizar);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_cadastro_contato, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ContatoHelper helper = new ContatoHelper(this);
        ContatoDAO dao = new ContatoDAO(CadastroContatoActivity.this);

        boolean temErros = helper.getErros();

        int id = item.getItemId();

        if(id == R.id.mi_cc_salvar){
            if(temErros){
                Toast.makeText(CadastroContatoActivity.this, "Verifique os erros!!! e seu nome é "+ helper.nome(), Toast.LENGTH_LONG).show();
            }

            if(acao.equals("salvar") && !temErros){
                Contato contato =  new Contato();
                contato = helper.getContato();
                dao.salvar(contato);
                Toast.makeText(CadastroContatoActivity.this, contato.getNome()+" foi salvo com sucesso!!!", Toast.LENGTH_LONG).show();
                dao.close();
                finish();
            } else if (acao.equals("atualizar")){
                int id_cont_atualizar = contatoAtualizar.getId();
                contatoAtualizar = helper.getContato();
                contatoAtualizar.setId(id_cont_atualizar);
                dao.atualizar(contatoAtualizar);
                Toast.makeText(CadastroContatoActivity.this, contatoAtualizar.getNome()+" foi atualizado com sucesso!!!", Toast.LENGTH_LONG).show();
                dao.close();
                finish();
            }


        }

        return super.onOptionsItemSelected(item);
    }
}